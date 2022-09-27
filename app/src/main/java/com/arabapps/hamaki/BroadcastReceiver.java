package com.arabapps.hamaki;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            context.sendBroadcast(new Intent("ACTION_UPDATE_FILES"));
        }
    }
}
