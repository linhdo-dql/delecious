package com.example.delicious;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Opening a Specified Page of an App, and Receive data in the customized Activity class.
 */
public class DeeplinkActivity extends Activity {
    private static final String TAG = "PushDemoLog";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deeplink);
        getIntentData(getIntent());
    }
    private void getIntentData(Intent intent) {
        if (intent != null) {
            // Obtain data set in way 1.
            int iAge1 = 0;
            String name1 = null;
            try {
                Uri uri = intent.getData();
                if (uri == null) {
                    Log.e(TAG, "getData null");
                    return;
                }
                String age1 = uri.getQueryParameter("age");
                name1 = uri.getQueryParameter("name");

                if (age1 != null) {
                    iAge1 = Integer.parseInt(age1);
                }
            } catch (NullPointerException e) {
                Log.e(TAG, "NullPointer," + e);
            } catch (NumberFormatException e) {
                Log.e(TAG, "NumberFormatException," + e);
            } catch (UnsupportedOperationException e) {
                Log.e(TAG, "UnsupportedOperationException," + e);
            } finally {
                Log.i(TAG, "name " + name1 + ",age " + iAge1);
                Toast.makeText(this, "name " + name1 + ",age " + iAge1, Toast.LENGTH_SHORT).show();
            }

            // Obtain data set in way 2.
            // String name2 = intent.getStringExtra("name");
            // int age2 = intent.getIntExtra("age", -1);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getIntentData(intent);
    }
}
