package com.example.delicious.API.CloudDB;

import android.app.Application;

import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class CloudDBQuickStartApplication extends Application {

    private static final String CHANNEL_ID = "push";
    @Override
    public void onCreate() {
        super.onCreate();
        CloudDBZoneWrapper.initAGConnectCloudDB(this);
        creatChanel();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    private void creatChanel() {
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Delicious", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Comment");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}