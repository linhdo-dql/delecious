package com.example.delicious;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;
import com.google.android.material.textfield.TextInputEditText;
import com.huawei.agconnect.AGCRoutePolicy;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.AGConnectOptionsBuilder;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.PhoneUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.huawei.agconnect.cloud.database.AGConnectCloudDB.initialize;

public class SignUp extends AppCompatActivity {
    EditText edtPhoneMail, edtPass, edtVerify;
    TextView btnSignUp, btnMaXacThuc, tvDN;
    TextInputEditText layoutPhone;
    AGCStorageManagement mAGCStorageManagement;
    CloudDBZoneWrapper cloudDBZoneWrapper = new CloudDBZoneWrapper();
    AGConnectAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        AnhXa();
        AGConnectOptionsBuilder builder = new AGConnectOptionsBuilder();
// Set the storage location. For example, change the default storage location from China to Germany.
        builder.setRoutePolicy(AGCRoutePolicy.CHINA);
// Initialize the AppGallery Connect SDK framework.
        AGConnectInstance.initialize(this, builder);
// At this time, the storage location of the AGConnectAuth instance is GERMANY.
        auth = AGConnectAuth.getInstance();
        initService();
    }

    public void AnhXa() {
        edtPhoneMail = findViewById(R.id.edt_email_phone_su);
        edtPass = findViewById(R.id.edtPass);
        edtVerify = findViewById(R.id.edt_verify);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvDN = findViewById(R.id.tvDN);
       // layoutPhone = findViewById(R.id.layout_edt_phone);
        btnMaXacThuc = findViewById(R.id.btnMaXacThuc);
        btnMaXacThuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerificationCode();
            }
        });

    }
    public void signUp(View v) {
        register();
    }

    private void sendVerificationCode() {
        String inputS = edtPhoneMail.getText().toString();
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
                    Toast.makeText(SignUp.this, "Đã gửi mã xác thực.", Toast.LENGTH_SHORT).show();
                    //You need to get the verification code from your email
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SignUp.this, "Bạn vừa yêu cầu gửi mã gần đây. Vui lòng chờ trong giây lát!" + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // apply for a verification code by phone, indicating that the phone is owned by you.
            String phone = inputS.substring(1);
            Task<VerifyCodeResult> task = AGConnectAuth.getInstance().requestVerifyCode("+84", phone, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    Toast.makeText(SignUp.this, "Đã gửi mã xác thực.", Toast.LENGTH_SHORT).show();
                    //You need to get the verification code from your phone
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(SignUp.this, "Bạn vừa yêu cầu gửi mã gần đây. Vui lòng chờ trong giây lát!" + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public void register() {
        String mailPhone = edtPhoneMail.getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        String vertify = edtVerify.getText().toString().trim();
        if (mailPhone.contains("@")) {
            // apply for a verification code by email, indicating that the email is owned by you.
            EmailUser emailUser = new EmailUser.Builder()
                    .setEmail(mailPhone)
                    .setPassword(pass)//optional,if you set a password, you can log in directly using the password next time.
                    .setVerifyCode(vertify)
                    .build();
            // create email user
            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                        @Override
                        public void onSuccess(SignInResult signInResult) {
                            // After a user is created, the user has logged in by default.
                            intentLogin(mailPhone);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(SignUp.this, "createUser fail:" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String phone = mailPhone.substring(1);
            PhoneUser phoneUser = new PhoneUser.Builder()
                    .setCountryCode("+84")
                    .setPhoneNumber(phone)
                    .setPassword(pass)//optional
                    .setVerifyCode(vertify)
                    .build();
            // create phone user
            AGConnectAuth.getInstance().createUser(phoneUser)
                    .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                        @Override
                        public void onSuccess(SignInResult signInResult) {
                            // After a user is created, the user has logged in by default.
                            intentLogin(mailPhone);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(SignUp.this, "createUser fail:" + e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
    public void intentLogin(String mailPhone)
    {
        Intent intent = new Intent(SignUp.this, LoginActivity.class);
        intent.putExtra("mailphone", mailPhone);
        startActivity(intent);
    }
    public void intentDN(View v) {startActivity(new Intent(SignUp.this, LoginActivity.class));}
    public void initService(){
        //Cloud DB
        initialize(SignUp.this);
        cloudDBZoneWrapper.login();
        cloudDBZoneWrapper.createObjectType();
        //cloudDBZoneWrapper.closeCloudDBZone();
        cloudDBZoneWrapper.openCloudDBZone();
        //Cloud Storage
        mAGCStorageManagement = AGCStorageManagement.getInstance();
    }

    /*public boolean validate(String mailPhone, String pass)
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
    }*/
}