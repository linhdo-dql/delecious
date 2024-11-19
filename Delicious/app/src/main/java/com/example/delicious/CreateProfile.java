package com.example.delicious;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.Adapter.DropdownAdapter;
import com.example.delicious.Adapter.FileUtil;
import com.example.delicious.Model.ItemDropDown;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Continuation;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.example.delicious.MainActivity.idAcc;
import static com.example.delicious.MainActivity.mAGCStorageManagement;

public class CreateProfile extends AppCompatActivity {
    private static final int CHOOSE_AVATAR = 23235;
    EditText editName, edtOld, edtJob;
    TextView tvTittle, tvChildTittle, tvPhone, tvEmail, tvSocial;
    LinearLayout btnPhone, btnEmail, btnSocial;
    ImageView imgBack, imgSuccess, imgAvatar;
    Spinner spnGender;
    Uri uriA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Intent intent = getIntent();
        if(intent!=null) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        getView();
    }
    public void getView() {
        editName = findViewById(R.id.edt_name_create_prf);
        edtJob = findViewById(R.id.edt_job_create_prf);
        edtOld = findViewById(R.id.edt_old_create_prf);
        tvTittle = findViewById(R.id.tv_tt_create_prf);
        tvChildTittle = findViewById(R.id.tv_ctt_create_prf);
        tvPhone = findViewById(R.id.tv_phone_create_prf);
        tvEmail = findViewById(R.id.tv_mail_create_prf);
        tvSocial = findViewById(R.id.tv_social_create_prf);
        btnPhone = findViewById(R.id.btn_phone_create_prf);
        btnEmail = findViewById(R.id.btn_mail_create_prf);
        btnSocial = findViewById(R.id.btn_social_create_prf);
        imgBack = findViewById(R.id.btn_back_prf);
        imgSuccess = findViewById(R.id.btn_success_prf);
        imgAvatar = findViewById(R.id.img_avatar_create_prf);
        spnGender = findViewById(R.id.spinner_gender_user);
        DropdownAdapter dropdownAdapter = new DropdownAdapter(getGender(),this, R.layout.dropdown_item2);
        spnGender.setAdapter(dropdownAdapter);

    }
    public void chooseAvatar(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Title"), CHOOSE_AVATAR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_AVATAR)
        {
            assert data != null;
            Uri uri = data.getData();
            imgAvatar.setImageURI(uri);
            uriA = uri;
        }
    }
    public ArrayList<ItemDropDown> getGender() {
        ArrayList<ItemDropDown> genders = new ArrayList<>();
        genders.add(new ItemDropDown(R.drawable.ic_male,"Nam"));
        genders.add(new ItemDropDown(R.drawable.ic_female,"Nữ"));
        genders.add(new ItemDropDown(R.drawable.ic_gender_diffirent,"Khác"));
        return genders;
    }
    public void contactPhone(View v)
    {
        dialogContact("Số điện thoại", R.string.hintPhone, btnPhone, tvPhone, R.drawable.ic_phone2);
    }
    public void contactMail(View v)
    {
        dialogContact("Địa chỉ Email", R.string.hintMail, btnEmail, tvEmail, R.drawable.ic_mail2);
    }
    public void contactSocial(View v)
    {
        dialogContact("Mạng xã hội", R.string.hintSocial, btnSocial, tvSocial, R.drawable.ic_earth);
    }

    public void dialogContact(String tt, int hinttext, LinearLayout tvbtn, TextView tv,int imageView)
    {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_contact);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView ttDialog = dialog.findViewById(R.id.tv_tt_dialog);
        ttDialog.setText(tt);
        ImageView icContact = dialog.findViewById(R.id.ic_contact);
        icContact.setImageResource(imageView);
        EditText edtContact = dialog.findViewById(R.id.edt_contact_dialog);
        edtContact.setHint(hinttext);
        TextView btnContactDialog = dialog.findViewById(R.id.btn_contact_dialog);
        btnContactDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtContact.getText().toString().isEmpty()) {
                    tvbtn.setBackgroundResource(R.drawable.bg_edt2);
                    tv.setText(edtContact.getText().toString());
                    dialog.dismiss();
                }else {
                    Toast.makeText(CreateProfile.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageView imgBack = dialog.findViewById(R.id.btn_close_dialog_phone);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void createUser(View v)
    {
        try {
            Intent intent = getIntent();
            String idAcc = "d_" + new SimpleDateFormat("hhmmMMssdd").format(Calendar.getInstance().getTime());
            String seAcc = "https://agc-storage-drcn.platform.dbankcloud.cn/v0/delicious-glq1s/userDefault.png?token=cfd628c3-1e7c-4a41-a1bc-8b76e2f7d2e2";
            if(uriA!=null) {
                upsertAcc(idAcc, intent.getStringExtra("mailPhone"));
            }
            else {
                upsertContentAcc(intent.getStringExtra("mailPhone"), seAcc, idAcc);
            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void upsertAcc(String idAcc ,String mailPhone)
    {
        AGCStorageManagement mAGCStorageManagement = AGCStorageManagement.getInstance();
        String storageref = "Account/Images/" + idAcc + "/Avatar/" + new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()) + ".png";
        StorageReference ref = mAGCStorageManagement.getStorageReference(storageref);
        byte[] bytes = null;
        try {
            bytes = FileUtil.readBytes(this, uriA);
        } catch (IOException e) {
            Log.e(TAG, "upload: " + e.getMessage(), e);
        }
        UploadTask uploadTask = ref.putBytes(bytes, null);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.UploadResult, Task<Uri>>() {
            @Override
            public Task<Uri> then(Task<UploadTask.UploadResult> task) throws Exception {
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    upsertContentAcc(mailPhone, downloadUri.toString(), idAcc);
                }
            }
        });
    }
    public void upsertContentAcc(String mpAcc, String uri, String idAcc)
    {
        String nameAcc = editName.getText().toString().trim();
        String jobAcc = edtJob.getText().toString().trim();
        String yearoldAcc = edtOld.getText().toString().trim();
        String gender = getGender().get(spnGender.getSelectedItemPosition()).getText();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();
        String social = tvSocial.getText().toString();
        Accounts accounts = new Accounts(Accounts.class, idAcc, nameAcc, uri, jobAcc, yearoldAcc, mpAcc, phone, email, social, gender);
        if ( mCloudDBZone == null) {
            Log.w("TAG", "CloudDBZone is null, try re-open it");
            return;
        }
        Task<Integer> upsertTask = mCloudDBZone.executeUpsert(accounts);
        upsertTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer cloudDBZoneResult) {
                Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                intent.putExtra("id_acc", idAcc);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                upsertContentAcc(mpAcc, uri, idAcc);
            }
        });
    }
}