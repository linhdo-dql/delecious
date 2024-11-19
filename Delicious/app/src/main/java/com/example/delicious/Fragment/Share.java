package com.example.delicious.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;
import com.example.delicious.API.CloudDB.FoodComponent;
import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.API.CloudDB.FoodStep;
import com.example.delicious.Adapter.ComponentAdapter;
import com.example.delicious.Adapter.DropdownAdapter;
import com.example.delicious.Adapter.FileUtil;
import com.example.delicious.Adapter.StepAdapter;
import com.example.delicious.Model.ItemDropDown;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneObject;
import com.huawei.agconnect.cloud.database.Text;
import com.huawei.agconnect.cloud.storage.core.FileMetadata;
import com.huawei.agconnect.cloud.storage.core.StorageReference;
import com.huawei.agconnect.cloud.storage.core.UploadTask;
import com.huawei.hmf.tasks.Continuation;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hwid.A;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.example.delicious.MainActivity.mAGCStorageManagement;

public class Share extends Fragment {
    View rootView;
    CloudDBZoneWrapper mCloudDBZoneWrapper;

    Spinner auto_foodType, spinner_amount_people, spinner_time_cook;
    TextView tvAddClick, tta, btn_addStep, btnShareFood;
    RecyclerView rcvComponent, rcvStep;
    FragmentActivity fragmentActivity;
    ComponentAdapter componentAdapter;
    ArrayList<FoodComponent> components = new ArrayList<>();
    ArrayList<FoodStep> steps = new ArrayList<>();
    Uri uriF = null;
    String uris = "";
    ImageView img_avatar_food, imageStepDia;
    EditText edtNameFood, edtProfileFood;
    private final int IMAGE_STEP_DIALOG = 11032;
    SharedPreferences sharedPreferences;
    StepAdapter stepAdapter;
    @Override
    public void onStart() {
        super.onStart();
        Log.e("life", "create");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("life", "create");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_share, container, false);
        initView();
        Log.e("life", "createview");

        sharedPreferences = getActivity().getSharedPreferences("Tempt", Context.MODE_PRIVATE);
        fragmentActivity = getActivity();
        DropdownAdapter dropdownFT = new DropdownAdapter(getfoodType(),getActivity(), R.layout.dropdown_item);
        auto_foodType.setAdapter(dropdownFT);
//        Toast.makeText(getActivity(), auto_foodType.getSelectedItem().toString(), Toast.LENGTH_LONG);
        DropdownAdapter dropdownAP = new DropdownAdapter(getAmoutP(),getActivity(), R.layout.dropdown_item);
        spinner_amount_people.setAdapter(dropdownAP);
        DropdownAdapter dropdowntimeC = new DropdownAdapter(gettimeCook(),getActivity(), R.layout.dropdown_item);
        spinner_time_cook.setAdapter(dropdowntimeC);
        stepAdapter = new StepAdapter(getActivity());
        return rootView;
    }
    public void initView() {
        auto_foodType = rootView.findViewById(R.id.auto_food_type);
        spinner_amount_people = rootView.findViewById(R.id.spinner_amount_people);
        spinner_time_cook = rootView.findViewById(R.id.spinner_time_cook);
        edtNameFood = rootView.findViewById(R.id.edt_name_food_share);
        edtProfileFood = rootView.findViewById(R.id.edt_profile_food);
        tvAddClick = rootView.findViewById(R.id.btn_addComponent);
        rcvComponent = rootView.findViewById(R.id.rcv_component);
        rcvComponent.setNestedScrollingEnabled(false);
        rcvComponent.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        rcvStep = rootView.findViewById(R.id.rcv_step);
        rcvStep.setNestedScrollingEnabled(false);
        rcvStep.setLayoutManager(new LinearLayoutManager(getActivity(),RecyclerView.VERTICAL, false));
        tvAddClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_addcomponent);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                EditText tv_name_component = dialog.findViewById(R.id.edt_name_com_dia);
                EditText tv_number_component = dialog.findViewById(R.id.edt_number_com_dia);
                Spinner spinner_type_com = dialog.findViewById(R.id.spinner_component_type);
                DropdownAdapter dropdownAdapter = new DropdownAdapter(getComponentType(),getActivity(), R.layout.dropdown_item);
                spinner_type_com.setAdapter(dropdownAdapter);
                TextView btnadd = dialog.findViewById(R.id.btn_com_add_dialog);
                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String com_name = tv_name_component.getText().toString().trim();
                        String com_number = tv_number_component.getText().toString().trim();
                        if(com_number.length()!=0 && com_name.length()!=0) {
                            components.add(new FoodComponent(FoodComponent.class, "c_"+ new SimpleDateFormat("ddMMHHmmss").format(Calendar.getInstance().getTime()), com_name, com_number, getComponentType().get(spinner_type_com.getSelectedItemPosition()).getText(), ""));
                            componentAdapter = new ComponentAdapter(getActivity());
                            rcvComponent.setAdapter(componentAdapter);
                            componentAdapter.setData(components);
                            dialog.dismiss();
                        }
                        else  {
                            Toast.makeText(getActivity(), "Vui lòng nhập đủ yêu cầu", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                ImageView ivClose = dialog.findViewById(R.id.btn_close_com_dialog);
                ivClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        img_avatar_food = rootView.findViewById(R.id.img_avatar_food);
        img_avatar_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Title"), 2);
            }
        });
        tta = rootView.findViewById(R.id.tta);
        btn_addStep = rootView.findViewById(R.id.btn_addStep);
        btn_addStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_addstep);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = dialog.findViewById(R.id.tv_index_step);
                textView.setText("Bước "+ (steps.size()+1));
                EditText editText = dialog.findViewById(R.id.edt_step_dia);
                imageStepDia = dialog.findViewById(R.id.img_step_dialog);
                imageStepDia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Title"), IMAGE_STEP_DIALOG);
                    }
                });
                TextView btnAddStep = dialog.findViewById(R.id.btn_step_add_dialog);
                btnAddStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Text stepCon = new Text(editText.getText().toString().trim());

                        if(stepCon.toString().length()!=0) {
                            steps.add(new FoodStep(FoodStep.class, "s_"+new SimpleDateFormat("ddMMHHmmss").format(Calendar.getInstance().getTime()),uris,stepCon,""));
                            stepAdapter.setData(steps);
                            rcvStep.setAdapter(stepAdapter);
                            dialog.dismiss();
                        }
                        else {
                            Toast.makeText(getActivity(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                ImageView imgClose = dialog.findViewById(R.id.btn_close_step_dialog);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        btnShareFood = rootView.findViewById(R.id.btn_share_food);
        btnShareFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idF = "f_"+new SimpleDateFormat("ddMMHHmmss").format(Calendar.getInstance().getTime());
                if(components.size()!=0 && steps.size()!=0) {
                    if (uriF != null) {
                        String storageref = "Food/Images/" + idF + "/Avatar/" + new SimpleDateFormat("ddMMyyyyHHmmss").format(Calendar.getInstance().getTime()) + ".png";
                        uploadAllFoodInfo(getActivity(), uriF, storageref, idF);
                        uploadComponentF(idF);
                        uploadAllStepF(getActivity(),idF);
                    } else {
                        upsertFoodInfo(idF, "https://img.lovepik.com/photo/50127/2946.jpg_wh860.jpg");
                        uploadComponentF(idF);
                        uploadAllStepF(getActivity(),idF);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2)
        {
            assert data != null;
            Uri uri = data.getData();
            img_avatar_food.setImageURI(uri);
            uriF = uri;
            tta.setText("Đổi ảnh");
            Toast.makeText(fragmentActivity, ""+uri, Toast.LENGTH_SHORT).show();
        }
        if(requestCode==11032)
        {
            Uri uri = data.getData();
            imageStepDia.setImageURI(uri);
            uris = uri.toString();
        }
    }

    public ArrayList<ItemDropDown> getfoodType() {
        ArrayList<ItemDropDown> foodType = new ArrayList<>();
        foodType.add(new ItemDropDown(R.drawable.ic_f_main, "Món chính"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_heath, "Món chay"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_cake, "Món bánh"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_salad, "Món trộn"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_noodle, "Món mì"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_soup, "Món súp"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_crab, "Hải sản"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_fruit, "Hoa quả"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_drink, "Đồ uống"));
        foodType.add(new ItemDropDown(R.drawable.ic_f_cream, "Kem"));
        return foodType;
    }
    public ArrayList<ItemDropDown> getAmoutP() {
        ArrayList<ItemDropDown> amoutP = new ArrayList<>();
        amoutP.add(new ItemDropDown(R.drawable.ic_f_one,"1 người"));
        amoutP.add(new ItemDropDown(R.drawable.ic_f_coupper,"2 người"));
        amoutP.add(new ItemDropDown(R.drawable.ic_f_group,"Gia đình"));
        return amoutP;
    }
    public ArrayList<ItemDropDown> gettimeCook() {
        ArrayList<ItemDropDown> timecook = new ArrayList<>();
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"30 phút"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"1 giờ"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"1.5 giờ"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"2 giờ"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"2.5 giờ"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"3 giờ"));
        timecook.add(new ItemDropDown(R.drawable.ic_clock,"nhiều giờ"));
        return timecook;
    }
    public ArrayList<ItemDropDown> getComponentType() {
        ArrayList<ItemDropDown> comtype = new ArrayList<>();
        comtype.add(new ItemDropDown(R.drawable.ic_spoon,"thìa"));
        comtype.add(new ItemDropDown(R.drawable.ic_ladle,"muỗng canh"));
        comtype.add(new ItemDropDown(R.drawable.ic_gram,"kg"));
        comtype.add(new ItemDropDown(R.drawable.ic_gram2,"gram"));
        comtype.add(new ItemDropDown(R.drawable.ic_lettuce,"mớ"));
        comtype.add(new ItemDropDown(R.drawable.ic_onion,"nhánh"));
        comtype.add(new ItemDropDown(R.drawable.ic_tomato,"quả"));
        comtype.add(new ItemDropDown(R.drawable.ic_potato,"củ"));
        comtype.add(new ItemDropDown(R.drawable.ic_cl,"ml"));
        comtype.add(new ItemDropDown(R.drawable.ic_package,"gói"));
        comtype.add(new ItemDropDown(R.drawable.ic_bowl,"chén"));
        comtype.add(new ItemDropDown(R.drawable.ic_chicken,"con"));
        return comtype;
    }



    private FileMetadata initFileMetadata() {
        FileMetadata metadata = new FileMetadata();
        metadata.setContentType("image/*");
        metadata.setCacheControl("no-cache");
        metadata.setContentEncoding("identity");
        metadata.setContentDisposition("inline");
        metadata.setContentLanguage("en");
        return metadata;
    }

    public void upsertFoodInfo(String idFv, String urlDownload)
    {
        String idFf = idFv;
        String nameF = edtNameFood.getText().toString().trim();
        String avatarFf = urlDownload;
        Text profileF = new Text(edtProfileFood.getText().toString().trim());
        String typeF = getfoodType().get(auto_foodType.getSelectedItemPosition()).getText();
        String amountF = getAmoutP().get(spinner_amount_people.getSelectedItemPosition()).getText();
        String timecookF = gettimeCook().get(spinner_time_cook.getSelectedItemPosition()).getText();
        String authorF = sharedPreferences.getString("id_acc","");
        String amoutviewF = "0";
        String amoutlikeF = "0";
        FoodInfo foodInfo = new FoodInfo(FoodInfo.class, idFf, nameF, avatarFf, profileF, typeF, amountF, timecookF, authorF, amoutviewF, amoutlikeF, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
        upsertObject(foodInfo);
    }
    public void upsertObject(CloudDBZoneObject object) {
        if ( mCloudDBZone == null) {
            Log.w("TAG", "CloudDBZone is null, try re-open it");
            return;
        }
        Task upsertTask = mCloudDBZone.executeUpsert(object);
        upsertTask.addOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer cloudDBZoneResult) {
                Toast.makeText(fragmentActivity, "success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {

            }
        });
    }
    public void uploadAllFoodInfo(Context context, Uri uri, String storageReference, String idF) {
        StorageReference ref = mAGCStorageManagement.getStorageReference(storageReference);
        byte[] bytes = null;
        try {
            bytes = FileUtil.readBytes(context, uri);
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
                    upsertFoodInfo(idF, downloadUri.toString());
                }
            }
        });
    }
    public void uploadComponentF(String idFC)
    {
        for(FoodComponent foodComponent: components)
        {
            foodComponent.setIdF(idFC);
            upsertObject(foodComponent);
        }
    }
    public void uploadAllStepF(Context context, String idF)
    {

        for (FoodStep foodStep: steps)
        {
            foodStep.setIdF(idF);
            String storageref = "Food/Images/" + idF + "/Step/" + new SimpleDateFormat("ddMMyyyyHHmmssSS").format(Calendar.getInstance().getTime()) + ".png";
            StorageReference ref = mAGCStorageManagement.getStorageReference(storageref);
            byte[] bytes = null;
            try {
                bytes = FileUtil.readBytes(context, Uri.parse(foodStep.getImgStep()));
            } catch (IOException e) {
                e.printStackTrace();
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
                        foodStep.setImgStep(downloadUri.toString());
                        upsertObject(foodStep);
                    }
                }
            });
        }

    }
    public void clearAll()
    {
        uris = "";
        uriF = null;
        components = new ArrayList<>();
        steps = new ArrayList<>();
    }
}