package com.example.delicious;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;
import com.example.delicious.API.CloudDB.DishFavorite;
import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.Adapter.DishMenuViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.huawei.agconnect.cloud.database.AGConnectCloudDB.initialize;

public class DishDetail extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    SharedPreferences sharedPreferences;
    public static String idDish;
    public static AGCStorageManagement mAGCStorageManagement;
    CloudDBZoneWrapper cloudDBZoneWrapper = new CloudDBZoneWrapper();
    ImageView btnBack;
    TextView nameDish;
    FloatingActionButton floatingActionButton;
    boolean click = false;
    String idacc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        SharedPreferences preferences = getSharedPreferences("Tempt", MODE_PRIVATE);
        idacc = preferences.getString("id_acc","");
        try {
            AnhXa();
            initService();
            Intent intent = getIntent();
            idDish = intent.getStringExtra("id_f");
            getNameDish();
            checkLiked(idacc, idDish);
        }
        catch (Exception e)
        {
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void AnhXa() {
        nameDish = findViewById(R.id.tv_name_dish);
        bottomNavigationView = findViewById(R.id.bottom_menu_dish);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dish_profile:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.dish_component:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.dish_step:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.dish_comment:
                        viewPager2.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
        viewPager2 = findViewById(R.id.viewPager2);
        DishMenuViewPager dishMenuViewPager = new DishMenuViewPager(this);
        viewPager2.setAdapter(dishMenuViewPager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:bottomNavigationView.getMenu().findItem(R.id.dish_profile).setChecked(true); break;
                    case 1:bottomNavigationView.getMenu().findItem(R.id.dish_component).setChecked(true); break;
                    case 2:bottomNavigationView.getMenu().findItem(R.id.dish_step).setChecked(true); break;
                    case 3:bottomNavigationView.getMenu().findItem(R.id.dish_comment).setChecked(true); break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

            }
        });
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DishDetail.this, MainActivity.class));
            }
        });
        floatingActionButton = findViewById(R.id.float_a_like);
    }
    public void getNameDish()
    {
        if(mCloudDBZone==null)
        {
            Log.e("Tag", "open CDBZ!");
        }
        Task<CloudDBZoneSnapshot<FoodInfo>> queryTask = mCloudDBZone.executeQuery(
          CloudDBZoneQuery.where(FoodInfo.class).equalTo("idF", idDish),
          CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodInfo> foodInfoCloudDBZoneSnapshot) {
                try {
                    nameDish.setText(foodInfoCloudDBZoneSnapshot.getSnapshotObjects().get(0).getNameF());
                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }
            }
        });
    };

    public void initService(){
        //Cloud DB
        initialize(DishDetail.this);
        cloudDBZoneWrapper.login();
        cloudDBZoneWrapper.createObjectType();
        //cloudDBZoneWrapper.closeCloudDBZone();
        cloudDBZoneWrapper.openCloudDBZone();
        //Cloud Storage
        mAGCStorageManagement = AGCStorageManagement.getInstance();
    }
    public void likeDish(View v) {
        if(click)
        {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart1));
            floatingActionButton.setColorFilter(Color.parseColor("#FFFFFF"));
            setlike(idDish, false);
            controlLike(idDish,idacc, false);
            click = false;
        }
        else {
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_heart2));
            floatingActionButton.setColorFilter(Color.parseColor("#FF0000"));
            setlike(idDish, true);
            controlLike(idDish,idacc, true );
            click = true;
        }
    }
    public void setlike(String s, boolean ctrl) {
        Task<CloudDBZoneSnapshot<FoodInfo>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FoodInfo.class).equalTo("idF", s),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodInfo> foodss) {
                try {
                    FoodInfo f = foodss.getSnapshotObjects().get(0);
                    String x = f.getAmountlikeF();
                    int y = Integer.parseInt(x);
                    y = ctrl ? (y+1): (y-1);
                    f.setAmountlikeF(String.valueOf(y));
                    Task upsertTask = mCloudDBZone.executeUpsert(f);
                    upsertTask.addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }
                foodss.release();
            }
        });
    }
    public void controlLike(String idF, String idAcc, boolean check)
    {
        if(check)
        {
          DishFavorite dishFavorite = new DishFavorite(DishFavorite.class, "df_"+new SimpleDateFormat("ddMMHHmmss").format(Calendar.getInstance().getTime()),idAcc, idF, new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
          Task upsertTask =mCloudDBZone.executeUpsert(dishFavorite);
          upsertTask.addOnSuccessListener(new OnSuccessListener() {
              @Override
              public void onSuccess(Object o) {
                  Toast.makeText(DishDetail.this, "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(Exception e) {

              }
          });
        }
        else
        {
            Task<CloudDBZoneSnapshot<DishFavorite>> queryTask =  mCloudDBZone.executeQuery(
              CloudDBZoneQuery.where(DishFavorite.class).equalTo("idF",idF).equalTo("idAcc", idAcc),
              CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
            );
            queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<DishFavorite>>() {
                @Override
                public void onSuccess(CloudDBZoneSnapshot<DishFavorite> snapshot) {
                    try {
                        Task<Integer> del = mCloudDBZone.executeDelete(snapshot.getSnapshotObjects().get(0));
                        del.addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                Toast.makeText(DishDetail.this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (AGConnectCloudDBException e) {
                        e.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                        Log.e("err", e.getMessage());
                }
            });
        }
    }
    public void checkLiked(String idAcc, String idF)
    {
        Task<CloudDBZoneSnapshot<DishFavorite>> queryTask =  mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(DishFavorite.class).equalTo("idF",idF).equalTo("idAcc", idAcc),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<DishFavorite>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<DishFavorite> snapshot) {
                if(snapshot.getSnapshotObjects().size()!=0)
                {
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DishDetail.this, R.drawable.ic_heart2));
                    floatingActionButton.setColorFilter(Color.parseColor("#FF0000"));
                    click = true;
                }
                else {
                    floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DishDetail.this, R.drawable.ic_heart1));
                    floatingActionButton.setColorFilter(Color.parseColor("#FFFFFF"));
                    click = false;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("err", e.getMessage());
            }
        });
    }

}