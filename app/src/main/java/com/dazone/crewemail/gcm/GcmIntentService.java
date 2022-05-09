package com.dazone.crewemail.gcm;

import static android.support.v4.app.NotificationCompat.PRIORITY_HIGH;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import com.dazone.crewemail.DaZoneApplication;
import com.dazone.crewemail.R;
import com.dazone.crewemail.activities.ActivityMailDetail;
import com.dazone.crewemail.event.ReloadListMail;
import com.dazone.crewemail.utils.Prefs;
import com.dazone.crewemail.utils.Statics;
import com.dazone.crewemail.utils.StaticsBundle;
import com.dazone.crewemail.utils.TimeUtils;
import com.dazone.crewemail.utils.Util;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.greenrobot.eventbus.EventBus;

import java.util.Random;


public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    private String channelId = "Abc 1231";
    private String channelName = "AHUHHS";
    boolean isEnableSound = true, isEnableVibrate = true, isNewMail = true, isTime = true;
    private String strFromTime, strToTime;

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        if (extras != null) {
            if (!extras.isEmpty()) {
                Log.d(Statics.LOG_TAG, extras.toString());

                if (GoogleCloudMessaging.
                        MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    //TODO sendNotification("Send error",extras.toString());
                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_DELETED.equals(messageType)) {
                    //TODO sendNotification("Deleted messages on server ",
                } else if (GoogleCloudMessaging.
                        MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    try {
                        Util.printLogs(extras.toString());
                        String title = extras.getString("Title");
                        String fromName = extras.getString("FromName");
                        String content = extras.getString("Content");
                        String receivedDate = extras.getString("ReceivedDate");
                        String toAddress = extras.getString("ToAddress");
                        String mailNo = extras.getString("MailNo");
                        String mailBoxNo = extras.getString("MailBoxNo");

                        EventBus.getDefault().post(new ReloadListMail());
                        ShowNotification(title, fromName, content, receivedDate, toAddress, Long.parseLong(mailNo), mailBoxNo);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } else {
                Log.d(Statics.LOG_TAG, "empty");
            }
        }
    }


    private void ShowNotification(String title, String fromName, String content, String receivedDate, String toAddress, long mailNo, String mailBoxNo) {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /** PendingIntent */
        Intent intent = new Intent(this, ActivityMailDetail.class);
        intent.putExtra(StaticsBundle.BUNDLE_MAIL_NO, mailNo);
        intent.putExtra(StaticsBundle.BUNDLE_MAIL_FROM_NOTIFICATION, true);
        intent.putExtra(StaticsBundle.BUNDLE_MAIL_FROM_NOTIFICATION_MAILBOX_NO, mailBoxNo);
        intent.putExtra(StaticsBundle.PREFS_KEY_ISREAD, false);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        /** GET PREFERENCES */
        isEnableVibrate = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_VIBRATE, true);
        isEnableSound = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_SOUND, true);
        isNewMail = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_NEW_MAIL, true);
        isTime = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME, true);
        strFromTime = new Prefs().getStringValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME_FROM_TIME, Util.getString(R.string.setting_notification_from_time));
        strToTime = new Prefs().getStringValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME_TO_TIME, Util.getString(R.string.setting_notification_to_time));
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Uri soundUriSilent = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.silent_sound);

        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            mNotificationManager.getNotificationChannel(channelId).setSound(isEnableSound ? soundUri : soundUriSilent, audioAttributes);
            mNotificationManager.createNotificationChannel( mChannel );
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.small_icon_email)
                        .setTicker(getString(R.string.the_new_mail_has_arrived))
                        .setPriority(Notification.PRIORITY_MAX)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.launcher_icon))
                        .setContentTitle(fromName)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        final long[] vibrate = new long[]{1000, 1000, 1000, 1000, 1000};


        if (isEnableVibrate) {
            mBuilder.setVibrate(vibrate);
            Vibrator v = (Vibrator) DaZoneApplication.getInstance().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        } else {
            final long[] noVibrate = new long[]{0, 0, 0, 0, 0};
            mBuilder.setVibrate(noVibrate);
            Vibrator v = (Vibrator) DaZoneApplication.getInstance().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(0);
        }

        // Check notification setting and config notification
        mBuilder.setSound(isEnableSound ? soundUriSilent : soundUriSilent);


        NotificationCompat.BigTextStyle bigTextStyle
                = new NotificationCompat.BigTextStyle();

        /** STYLE BIG TEXT */
        String bigText = "<font color='#878787'>" + title + "</font>";
        if (!TextUtils.isEmpty(content.replaceAll("&nbsp;", "").trim())) {
            bigText = bigText + "<br/>" + content;
        }

        bigTextStyle.bigText(Html.fromHtml(bigText));
        bigTextStyle.setSummaryText(toAddress);
        mBuilder.setStyle(bigTextStyle);

        //consider using setTicker of Notification.Builder
        if (isNewMail) {
            if (isTime) {
                if (TimeUtils.isBetweenTime(strFromTime, strToTime)) {
                    mNotificationManager.notify(new Random().nextInt(), mBuilder.build());
                }
            } else {
                mNotificationManager.notify(new Random().nextInt(), mBuilder.build());
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /** GET PREFERENCES */
        isEnableVibrate = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_VIBRATE, true);
        isEnableSound = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_SOUND, true);
        isNewMail = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_NEW_MAIL, true);
        isTime = new Prefs().getBooleanValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME, true);
        strFromTime = new Prefs().getStringValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME_FROM_TIME, Util.getString(R.string.setting_notification_from_time));
        strToTime = new Prefs().getStringValue(Statics.KEY_PREFERENCES_NOTIFICATION_TIME_TO_TIME, Util.getString(R.string.setting_notification_to_time));

        startForeground(1, getNotification());
    }

    public Notification getNotification() {
        String channel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUriSilent = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.silent_sound);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel).setSmallIcon(android.R.drawable.ic_menu_mylocation).setContentTitle("CrewMail");
        // Check notification setting and config notification
        mBuilder.setSound(isEnableSound ? soundUri : soundUriSilent);
        Notification notification = mBuilder
                .setPriority(PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();

        return notification;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Uri soundUriSilent = Uri.parse("android.resource://"
                + getApplicationContext().getPackageName() + "/" + R.raw.silent_sound);


        NotificationChannel mChannel;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setLightColor(Color.GRAY);
            mChannel.enableLights(true);

            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            mChannel.setSound(isEnableSound ? soundUri : soundUriSilent, audioAttributes);

            if (mNotificationManager != null) {
                mNotificationManager.createNotificationChannel( mChannel );
            } else {
                stopSelf();
            }
        }

        return channelId;
    }
}