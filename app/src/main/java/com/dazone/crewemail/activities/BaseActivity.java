package com.dazone.crewemail.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dazone.crewemail.DaZoneApplication;
import com.dazone.crewemail.R;
import com.dazone.crewemail.customviews.AlertDialogView;
import com.dazone.crewemail.utils.Prefs;
import com.dazone.crewemail.utils.Util;

import java.util.Locale;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    protected String mServerSite;
    private boolean mIsExit;
    public static BaseActivity Instance = null;

    public static boolean isAppWentToBg = true;

    public static boolean isPause = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mServerSite = DaZoneApplication.getInstance().getPrefs().getServerSite();
        Instance = this;
    }

    @Override
    public void onBackPressed() {
        if (!isTaskRoot()) {
            super.onBackPressed();
        } else {
            if (mIsExit) {
                super.onBackPressed();
                System.exit(0);
            } else {
                this.mIsExit = true;
                String Str;
                if (Locale.getDefault().getLanguage().equals("vi")) {
                    Str = "Click thêm lần nữa ứng dụng sẽ được đóng";
                } else if (Locale.getDefault().getLanguage().equals("ko")) {
                    Str = "'뒤로'버튼을 한번 더 누르시면 종료됩니다.";
                } else {
                    Str = "Press back again to quit.";
                }
                Toast.makeText(this, Str, Toast.LENGTH_SHORT).show();
                myHandler.postDelayed(myRunnable, 2000);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        if (!isTaskRoot()) {
            overridePendingTransition(R.anim.finish_activity_show, R.anim.finish_activity_hide);
        }
        super.onDestroy();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        isPause = false;

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPause = false;
    }


    /**
     * For press 2 times to exit app
     */
    private Handler myHandler = new Handler();
    private Runnable myRunnable = new Runnable() {
        public void run() {
            mIsExit = false;
        }
    };

    /**
     * end here
     **/

    @Override
    protected void onStop() {
        myHandler.removeCallbacks(myRunnable);
        super.onStop();
        if (isPause) {
            isAppWentToBg = true;
            isPause = false;
        }
    }


    public void showProgressDialog() {
        if (null == mProgressDialog || !mProgressDialog.isShowing()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setTitle(getString(R.string.loading_title));
            mProgressDialog.setMessage(getString(R.string.loading_content));
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void callActivity(Class cls) {
        Intent newIntent = new Intent(this, cls);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(newIntent);
    }
}
