package com.example.delicious.Fragment.ChildFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.Model.ItemDropDown;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.example.delicious.DishDetail.idDish;

public class DishProfile extends Fragment {

    private View rootView;
    ImageView avatarD, icTypeD, icAmountD, icTimeCD;
    TextView profileD, authorD, tvTypeD, tvAmountD, tvTimeCD;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dish_profile, container, false);
        initView();
        getProfileDish();
        return rootView;
    }
    public void initView()
    {
        avatarD = rootView.findViewById(R.id.img_avatar_dish);
        icTypeD = rootView.findViewById(R.id.img_type_dish);
        icAmountD = rootView.findViewById(R.id.img_amoutP_dish);
        icTimeCD = rootView.findViewById(R.id.img_timeC_dish);
        profileD = rootView.findViewById(R.id.tv_prf_dish);
        authorD = rootView.findViewById(R.id.tv_author_name_dish);
        tvTypeD = rootView.findViewById(R.id.tv_type_dish);
        tvAmountD = rootView.findViewById(R.id.tv_amountP_dish);
        tvTimeCD = rootView.findViewById(R.id.tv_timeC_dish);
    }
    public void getProfileDish()
    {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }
        Task<CloudDBZoneSnapshot<FoodInfo>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FoodInfo.class).equalTo("idF", idDish),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodInfo> snapshot) {
                Log.e("s", "a" );
                try {
                    FoodInfo foods = snapshot.getSnapshotObjects().get(0);
                    Glide.with(getActivity()).load(foods.getAvatarF()).into(avatarD);
                    setUserName(foods.getAuthorF(),authorD);
                    String typeD = foods.getTypeF();
                    icTypeD.setImageResource(setIcon(typeD, getfoodType()));
                    tvTypeD.setText(typeD);
                    String amountPD = foods.getAmountPF();
                    icAmountD.setImageResource(setIcon(amountPD, getAmoutP()));
                    tvAmountD.setText(amountPD);
                    String timeCD = foods.getTimecookF();
                    icTimeCD.setImageResource(setIcon(timeCD, gettimeCook()));
                    tvTimeCD.setText(timeCD);
                    profileD.setBackgroundResource(R.drawable.bg_edt3);
                    profileD.setText(foods.getProflieF().toString());

                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }
                    snapshot.release();

            }
        });
    }

    public int setIcon(String s, ArrayList<ItemDropDown> x){
        int k = 0;
        for(ItemDropDown m: x)
        {
            if(m.getText().equals(s))
            {
                k = m.getIconsrc();
            }
        }
        return k;

    }
    public void setUserName(String idAcc, TextView tvAuthor)
    {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }
        Task<CloudDBZoneSnapshot<Accounts>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(Accounts.class).equalTo("idAcc", idAcc),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<Accounts>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<Accounts> snapshot) {
                String accounts = null;
                try {
                    accounts = snapshot.getSnapshotObjects().get(0).getNameAcc();
                    snapshot.release();
                    tvAuthor.setText("Bếp trưởng: " + accounts);
                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }


            }
        });
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
}