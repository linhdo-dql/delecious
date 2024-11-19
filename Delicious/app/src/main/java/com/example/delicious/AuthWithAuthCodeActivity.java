/*
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 */

package com.example.delicious;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class AuthWithAuthCodeActivity extends Activity implements View.OnClickListener {
    private final String TAG = "Account";
    private TextView textView;
    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    //login by code
    private static final int REQUEST_SIGN_IN_LOGIN_CODE = 1003;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huawei_authcode);
        textView = findViewById(R.id.hwid_log_text);
        findViewById(R.id.hwid_signInCode).setOnClickListener(this);
        findViewById(R.id.hwid_signout).setOnClickListener(this);
        findViewById(R.id.cancel_authorization).setOnClickListener(this);
    }

    private void signInCode() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setProfile()
                .setAuthorizationCode()
                .createParams();
        mAuthService = AccountAuthManager.getService(AuthWithAuthCodeActivity.this, mAuthParam);
        startActivityForResult(mAuthService.getSignInIntent(), REQUEST_SIGN_IN_LOGIN_CODE);
    }

    private void signOut() {
        Task<Void> signOutTask = mAuthService.signOut();
        signOutTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "signOut Success");
                textView.setText("signOut sucess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "signOut fail");
                textView.setText("signOut fail");
            }
        });
    }

    private void cancelAuthorization() {
        Task<Void> task = mAuthService.cancelAuthorization();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "cancelAuthorization success");
                textView.setText("cancelAuthorization sucess");
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.i(TAG, "cancelAuthorization failure：" + e.getClass().getSimpleName());
                textView.setText("cancelAuthorization failure：" + e.getClass().getSimpleName());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hwid_signInCode:
                signInCode();
                break;
            case R.id.hwid_signout:
                signOut();
                break;
            case R.id.cancel_authorization:
                cancelAuthorization();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Process the sign-in authorization result and obtain an ID token from AuthHuaweiId.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SIGN_IN_LOGIN_CODE) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the user's HUAWEI ID information and ID token are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "Authorization code:" + authAccount.getAuthorizationCode());
                textView.setText("sign in successfully");
            } else {
                // The sign-in failed. No processing is required. Logs are recorded to facilitate fault locating.
                Log.e(TAG, "sign in failed : " +((ApiException)authAccountTask.getException()).getStatusCode());
                textView.setText("sign in failed");
            }
        }
    }
}
