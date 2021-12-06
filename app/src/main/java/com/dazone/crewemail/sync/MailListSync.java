package com.dazone.crewemail.sync;

import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dazone.crewemail.DaZoneApplication;
import com.dazone.crewemail.data.ErrorData;
import com.dazone.crewemail.data.MailData;
import com.dazone.crewemail.database.DataManager;
import com.dazone.crewemail.database.WriteMailListTask;
import com.dazone.crewemail.interfaces.OnMailListCallBack;
import com.dazone.crewemail.interfaces.OnWriteLocalDataCallback;
import com.dazone.crewemail.webservices.HttpRequest;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


public class MailListSync implements OnMailListCallBack, OnWriteLocalDataCallback {
    static MailListSync instance;
    final static String LOCK = "";
    private boolean syncing = false;
    private ArrayList<MailData> mailDataArrayList = new ArrayList<>();
    private ArrayList<Integer> syncMailBox = new ArrayList<>();
    private int syncedMailBoxIndex = 0;
    private boolean stopping = false;
    public boolean pausing = false;
    private boolean notifySync = false;
    private int maxCount = 300;
    private int mMailBoxNo = 0;

    public void stop() {
        syncing = false;
        stopping = true;
        mailDataArrayList = new ArrayList<>();
    }

    private MailListSync() {
    }

    public static MailListSync Instance() {
        synchronized (LOCK) {
            if (instance == null) {
                instance = new MailListSync();
            }
            return instance;
        }
    }

    public void sync() {
        try {
            syncedMailBoxIndex = 0;

            syncMailBox = DaZoneApplication.getInstance().getPrefs().getAllMailBox();

            if (syncing || syncMailBox == null || syncMailBox.size() == 0)
                return;

            long lastSync = DaZoneApplication.getInstance().getPrefs().getLastSyncTime();
            if ((Calendar.getInstance().getTimeInMillis() - lastSync) < (3600000 * 8))
                return;

            if (DaZoneApplication.getInstance().getPrefs().getMailBoxNo() == syncMailBox.get(syncedMailBoxIndex)) {
                syncNextBox();
                return;
            }
            sync(syncMailBox.get(syncedMailBoxIndex));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void syncNextBox() {
        syncedMailBoxIndex++;
        if (syncedMailBoxIndex >= syncMailBox.size()) {
            MailDetailSync.Instance().sync();
            syncedMailBoxIndex = 0;
            syncing = false;
            DaZoneApplication.getInstance().getPrefs().putLastSyncTime(Calendar.getInstance().getTimeInMillis());
            return;
        }

        sync(syncMailBox.get(syncedMailBoxIndex));
    }

    private void sync(int mailBoxNo) {
        mMailBoxNo = mailBoxNo;
        syncing = true;
        stopping = false;
        mailDataArrayList = new ArrayList<>();
        HttpRequest.getInstance().getEmailList(mMailBoxNo, 0, 25, true, 0, 0, "",
                1, 4, false, this);

    }

    @Override
    public void onMailListSuccess(List<MailData> mailDataList, int totalEmailCount) {
        if (stopping)
            return;

        try {
            if (notifySync) {
                saveNewMail(mailDataList);
            } else {
                mailDataArrayList.addAll(mailDataList);
                if (mailDataArrayList.size() < totalEmailCount && mailDataArrayList.size() < maxCount) {
                    syncNext();
                } else {
                    writeData();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Crashlytics.logException(e);
        }
    }

    @Override
    public void onMailListFail(ErrorData errorData) {
        syncing = false;
        pausing = true;
    }

    private void syncNext() {
        long anchor = getAnchorMailNo();
        HttpRequest.getInstance().getEmailList(mMailBoxNo, anchor, 25, true, 0, 0, "",
                1, 4, false, this);
    }

    private long getAnchorMailNo() {
        long result = 0;
        if (mailDataArrayList != null)
            result = mailDataArrayList.get(mailDataArrayList.size() - 1).getMailNo();
        return result;
    }

    private void writeData() {
        if (mailDataArrayList != null) {
            new WriteMailListTask(mailDataArrayList, this).execute();
        } else {
            syncNextBox();
        }
    }

    private void writeData(ArrayList<MailData> data) {
        new WriteMailListTask(data, this).execute();
    }


    private void saveNewMail(List<MailData> data) {
        ArrayList<MailData> newMail = new ArrayList<>();
        ArrayList<MailData> savedData = DataManager.Instance().getMailList(mMailBoxNo, "");
        for (MailData mailData : data) {
            boolean found = false;
            for (MailData mailData1 : savedData) {
                if (mailData.getMailNo() == mailData1.getMailNo()) {
                    found = true;
                    break;
                }
            }
            if (!found)
                newMail.add(mailData);
        }

        if (newMail.size() > 0) {
            writeData(newMail);
        }
    }

    @Override
    public void onWriteSuccess() {
        if (notifySync) {
            DaZoneApplication.getInstance().getPrefs().putLastSyncTime(Calendar.getInstance().getTimeInMillis());

            MailDetailSync.Instance().sync();
        } else {
            syncNextBox();
        }
    }
}
