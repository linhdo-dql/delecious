package com.example.delicious;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;
import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.API.CloudDB.ObjectTypeInfoHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.huawei.agconnect.AGCRoutePolicy;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.huawei.agconnect.api.AGConnectApi;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.agconnect.cloud.database.AGConnectCloudDB;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.hwid.I;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;
import com.huawei.hms.utils.HMSBIInitializer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;

public class LoginActivity extends AppCompatActivity {
    TextView tvSignUp, btnSignIn, btnSignInOTP, tvDK;
    HuaweiIdAuthButton btnSignInHuawei;
    AGCStorageManagement mAGCStorageManagement;
    CloudDBZoneWrapper cloudDBZoneWrapper = new CloudDBZoneWrapper();
    EditText edtPhoneMailLogin, edtPassLogin;
    TextInputLayout edtAccLayout, edtPassLayot;
    AccountAuthService mAuth;
    AGConnectAuthCredential credential;
    AGConnectAuth auth;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AnhXa();
        try {
            initService();
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE, 1234123);
            // Create an AGConnectOptionsBuilder instance.
            AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder();
// Set the storage location. For example, change the default storage location from China to Germany.
            builder.setRoutePolicy(AGCRoutePolicy.CHINA);
// Initialize the AppGallery Connect SDK framework.
            AGConnectInstance.initialize(this, builder);

// At this time, the storage location of the AGConnectAuth instance is GERMANY.
            auth = AGConnectAuth.getInstance();
            getToken();
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void AnhXa() {
        btnSignInHuawei = findViewById(R.id.btn_sigin_in_huawei);
        edtPhoneMailLogin = findViewById(R.id.edt_email_phone_li);
        edtPassLogin = findViewById(R.id.edt_pass_li);
        btnSignIn = findViewById(R.id.btn_sign_in);
        edtAccLayout = findViewById(R.id.layout_edt_username);
        edtPassLayot = findViewById(R.id.layout_edt_pass);
        btnSignInOTP = findViewById(R.id.btn_sign_inOTP);
        tvDK = findViewById(R.id.tvDK);
        setControl();
    }
    public void setControl() {
        Intent intent = getIntent();
        if(intent!=null) {
            edtPhoneMailLogin.setText(intent.getStringExtra("mailphone"));
        }
    }
    public void signInwithAccountKit(View v) {
        //Onclick vào btnSignInHuawei
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAccessToken()
                .createParams();
        AccountAuthService service = AccountAuthManager.getService(LoginActivity.this, authParams);
        startActivityForResult(service.getSignInIntent(), 8888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result and obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                /*checkTrung(authAccount.getDisplayName());*/
                Toast.makeText(this, ""+authAccount.getDisplayName(), Toast.LENGTH_SHORT).show();
                AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(authAccount.getAccessToken());
                callBackSignIn(credential);
            } else {
                // The sign-in failed.
                Log.e("error", "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
        AGConnectApi.getInstance().activityLifecycle().onActivityResult(requestCode, resultCode, data);
    }
    public void signIn(View v) {
        AGConnectAuth.getInstance().signOut();
        String mailPhone = edtPhoneMailLogin.getText().toString().trim();
        String pass = edtPassLogin.getText().toString().trim();
        if(validate(mailPhone, pass)) {
            if (mailPhone.contains("@")) {
                AGConnectAuthCredential credential = EmailAuthProvider.credentialWithPassword(mailPhone, pass);
                callBackSignIn(credential);
            }
            else
            {
                // apply for a verification code by phone, indicating that the phone is owned by you.
                String phone = mailPhone.substring(1, mailPhone.length());
                AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithPassword("+84",phone, pass);
                callBackSignIn(credential);
            }
        }
    }

    public boolean validate(String mailPhone, String pass)
    {
        boolean check = true;
        if(Pattern.matches("[0-9]+",mailPhone))
        {
            if(mailPhone.length()<10)
            {
                edtAccLayout.setHelperText("Vui lòng nhập đúng số điện thoại");
                check = false;
            }
            else
            {
                    edtAccLayout.setHelperText("");
            }

        }else {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            if(!Pattern.matches(ePattern,mailPhone))
            {
                edtAccLayout.setHelperText("Vui lòng nhập đúng địa chỉ email");
                check = false;
            }
            else
            {
                edtAccLayout.setHelperText("");
            }
        }
        if(pass.length()<8)
        {
            edtPassLayot.setHelperText("Vui lòng nhập đúng mật khẩu(tối thiểu 8 kí tự).");
            check = false;
        }

        return check;
    }
    public void signInOTP(View v)
    {
        AGConnectAuth.getInstance().signOut();
        String mailPhone = edtPhoneMailLogin.getText().toString().trim();
        if(validateOTP(mailPhone)) {
            sendVerificationCode();
        }
    }
    private void sendVerificationCode() {
        String inputS = edtPhoneMailLogin.getText().toString();
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)// action type
                .sendInterval(30) //shortest send interval ，30-120s
                .locale(Locale.SIMPLIFIED_CHINESE) //optional,must contain country and language eg:zh_CN
                .build();
        if (inputS.contains("@")) {
            // apply for a verification code by email, indicating that the email is owned by you.
            Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode(inputS, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    Toast.makeText(LoginActivity.this, "Đã gửi mã xác thực.", Toast.LENGTH_SHORT).show();
                    //You need to get the verification code from your email
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(LoginActivity.this, "Bạn vừa yêu cầu gửi mã gần đây. Vui lòng chờ trong giây lát!" + e, Toast.LENGTH_SHORT).show();
                }
            });
            dialogVerify();
        } else {
            // apply for a verification code by phone, indicating that the phone is owned by you.
            String phone = inputS.substring(1, inputS.length());
            Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode("+84", phone, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    Toast.makeText(LoginActivity.this, "Đã gửi mã xác thực.", Toast.LENGTH_SHORT).show();
                    //You need to get the verification code from your phone
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(LoginActivity.this, "Bạn vừa yêu cầu gửi mã gần đây. Vui lòng chờ trong giây lát!" + e, Toast.LENGTH_SHORT).show();
                }
            });
            dialogVerify();
        }
    }
    public void dialogVerify()
    {
        String acc = edtPhoneMailLogin.getText().toString().trim();
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_verify);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvAcc = dialog.findViewById(R.id.tv_name_dialog);
        tvAcc.setText(acc);
        EditText edtVerify = dialog.findViewById(R.id.edt_verify_dialog);
        TextView btnVerify = dialog.findViewById(R.id.btn_vertify_dialog);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Verify Login
                String vertifyCode = edtVerify.getText().toString().trim();
                if(vertifyCode.length()!=0)
                {
                    verifyLogin(acc,vertifyCode);
                }
                else {
                    Toast.makeText(LoginActivity.this, "Mã xác thực không được bỏ trống.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ImageView imgClose = dialog.findViewById(R.id.btn_close_dialog);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Close Dialog
                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public void verifyLogin(String inputphoneMail, String vertifyCode)
    {

        if (inputphoneMail.contains("@")) {
            AGConnectAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode(inputphoneMail, null,vertifyCode);
            callBackSignIn(credential);
        }
        else
        {
            // apply for a verification code by phone, indicating that the phone is owned by you.
            String phone = inputphoneMail.substring(1, inputphoneMail.length());
            AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode("+84",phone, null,vertifyCode);
            callBackSignIn(credential);
        }
    }
    public boolean validateOTP(String mailPhone)
    {
        boolean check = true;
        if(Pattern.matches("[0-9]+",mailPhone))
        {
            if(mailPhone.length()<10)
            {
                edtAccLayout.setHelperText("Vui lòng nhập đúng số điện thoại");
                check = false;
            }
            else
            {
                edtAccLayout.setHelperText("");
            }

        }else {
            String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
            if(!Pattern.matches(ePattern,mailPhone))
            {
                edtAccLayout.setHelperText("Vui lòng nhập đúng địa chỉ email");
                check = false;
            }
            else
            {
                edtAccLayout.setHelperText("");
            }
        }
        return check;
    }
    private void callBackSignIn(AGConnectAuthCredential credential) {
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        checkTrung(edtPhoneMailLogin.getText().toString());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(LoginActivity.this, "signIn fail:" + e, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void intentDK(View v)
    {
        startActivity(new Intent(LoginActivity.this, SignUp.class));
    }
    public void initService() throws AGConnectCloudDBException {
        //Cloud DB
        AGConnectCloudDB.initialize(LoginActivity.this);

        cloudDBZoneWrapper.createObjectType();
       // cloudDBZoneWrapper.closeCloudDBZone();
        cloudDBZoneWrapper.openCloudDBZone();
        //Cloud Storage
        mAGCStorageManagement = AGCStorageManagement.getInstance();

    }
    public void intentCreateProfile(String mailPhone)
    {
        Intent intent = new Intent(LoginActivity.this, CreateProfile.class);
        intent.putExtra("mailPhone", mailPhone);
        startActivity(intent);
    }
    private void getToken() {
        // Create a thread.
        new Thread() {
            @Override
            public void run() {
                try {
                    // Obtain the app ID from the agconnect-service.json file.
                    String appId = "104544311";

                    // Set tokenScope to HCM.
                    String tokenScope = "HCM";
                    String token = HmsInstanceId.getInstance(LoginActivity.this).getToken(appId, tokenScope);
                    Log.i(TAG, "get token: " + token);

                    // Check whether the token is empty.
                    if(!TextUtils.isEmpty(token)) {

                    }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }
    public void checkTrung(String mailPhone)
    {
        Task<CloudDBZoneSnapshot<Accounts>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(Accounts.class),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<Accounts>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<Accounts> snapshot) {
                CloudDBZoneObjectList<Accounts> accs = snapshot.getSnapshotObjects();
                ArrayList<Accounts> temp = new ArrayList<>();
                while (accs.hasNext()) {
                    Accounts accounts = null;
                    try {
                        accounts = accs.next();
                        temp.add(accounts);
                    } catch (AGConnectCloudDBException e) {
                        e.printStackTrace();
                    }
                }
                String idacc = checkAcc(temp,mailPhone);
                if(!idacc.trim().isEmpty())
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("id_acc", idacc);
                    startActivity(intent);
                }
                else {
                    intentCreateProfile(mailPhone);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
            }
        });
    }
    public String checkAcc(ArrayList<Accounts> s, String mailPhone)
    {
        String idAcc = "";
        for(Accounts x: s)
        {
            if(x.getMpAcc().equals(mailPhone))
            {
                idAcc = x.getIdAcc();
            }
        }
        return idAcc;
    }
}