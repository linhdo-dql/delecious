package com.example.delicious.API.CloudDB;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZone;
import com.huawei.agconnect.cloud.database.CloudDBZoneConfig;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.ListenerHandler;
import com.huawei.agconnect.cloud.database.ObjectTypeInfo;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class CloudDBZoneWrapper {
    private static final String TAG = "CloudDBZoneWrapper";
    // CloudDB
    public static CloudDBZone mCloudDBZone;
    CloudDBZoneConfig mConfig;
    public static void initAGConnectCloudDB(Context context) {
        AGConnectCloudDB.initialize(context);
    }

    public void login() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            System.out.println("already sign a user");
            return;
        }
        AGConnectAuth.getInstance().signInAnonymously()
                .addOnSuccessListener(signInResult -> {
                    System.out.println("AGConnect OnSuccess");
                }).addOnFailureListener(e -> {
            System.out.println("AGConnect OnFail: " + e.getMessage());
        });
    }

    public void createObjectType() {
        try {
            AGConnectCloudDB.getInstance().createObjectType(ObjectTypeInfoHelper.getObjectTypeInfo());
        } catch (AGConnectCloudDBException e) {
            Log.w("k", "createObjectType: " + e.getMessage());
        }
    }
    public void openCloudDBZone() {
        mConfig = new CloudDBZoneConfig("Delicious",
                CloudDBZoneConfig.CloudDBZoneSyncProperty.CLOUDDBZONE_CLOUD_CACHE,
                CloudDBZoneConfig.CloudDBZoneAccessProperty.CLOUDDBZONE_PUBLIC);
        mConfig.setPersistenceEnabled(true);
        try {
            mCloudDBZone = AGConnectCloudDB.getInstance().openCloudDBZone(mConfig, true);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "openCloudDBZone: " + e.getMessage());
        }
    }
    /**
     * Call AGConnectCloudDB.closeCloudDBZone
     */
    public void closeCloudDBZone() {
        try {
            AGConnectCloudDB.getInstance().closeCloudDBZone(mCloudDBZone);
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "closeCloudDBZone: " + e.getMessage());
        }
    }

    /**
     * Call AGConnectCloudDB.deleteCloudDBZone
     */
    public void deleteCloudDBZone() {
        try {
            AGConnectCloudDB.getInstance().deleteCloudDBZone(mConfig.getCloudDBZoneName());
        } catch (AGConnectCloudDBException e) {
            Log.w(TAG, "deleteCloudDBZone: " + e.getMessage());
        }
    }
}
