package com.example.delicious.API.CloudStorage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.delicious.Adapter.FileUtil;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Continuation;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static com.example.delicious.MainActivity.mAGCStorageManagement;

public class CloudStorageWrapper {
    public static String urlDownload;

    public CloudStorageWrapper() {
    }

}
