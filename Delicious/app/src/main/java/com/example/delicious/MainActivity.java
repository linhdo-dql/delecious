package com.example.delicious;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.API.CloudDB.CloudDBZoneWrapper;
import com.example.delicious.API.PushKit.MyPushService;
import com.example.delicious.Adapter.MainMenuViewPager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.agconnect.cloud.storage.core.AGCStorageManagement;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.feature.service.AuthService;

import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.huawei.agconnect.cloud.database.AGConnectCloudDB.initialize;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    ViewPager2 viewPager2;
    DrawerLayout mDrawerLayout;
    ImageView btnShowDrawer, avatar;
    TextView userName, account;
    NavigationView navigationView;
    public static AGCStorageManagement mAGCStorageManagement;
    CloudDBZoneWrapper cloudDBZoneWrapper = new CloudDBZoneWrapper();
    SharedPreferences sharedPreferences = null;
    int back = 0;
    public static String idAcc;
    public static String nameAcc;
    public static String avatarAcc;

    public static boolean b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initService();
        AGConnectUser authAccount = AGConnectAuth.getInstance().getCurrentUser();
        authAccount.getProviderId();
        Intent intent = getIntent();
        if(intent!=null)
        {
            idAcc = intent.getStringExtra("id_acc");
            SharedPreferences sharedPreferences = getSharedPreferences("Tempt", MODE_PRIVATE);
            sharedPreferences.edit().putString("id_acc", idAcc).apply();
        }
        Toast.makeText(this, ""+intent.getStringExtra("id_acc"), Toast.LENGTH_SHORT).show();
        sharedPreferences = getSharedPreferences("day_night_mode", MODE_PRIVATE);
        b = sharedPreferences.getBoolean("mode", true);
        if(b) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        AnhXa();

    }
    @SuppressLint("UseSupportActionBar")
    public void AnhXa() {

        mDrawerLayout = findViewById(R.id.drawerLayout);
        btnShowDrawer = findViewById(R.id.btn_show_drawer);
        btnShowDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.menu_new:
                        viewPager2.setCurrentItem(0);
                        break;
                    case R.id.menu_list_cook:
                        viewPager2.setCurrentItem(1);
                        break;
                    case R.id.menu_like:
                        viewPager2.setCurrentItem(2);
                        break;
                    case R.id.menu_share:
                        viewPager2.setCurrentItem(3);
                        break;
                    case R.id.menu_profile:
                        viewPager2.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
        viewPager2 = findViewById(R.id.viewPager);
        MainMenuViewPager mainMenuViewPager = new MainMenuViewPager(this);
        viewPager2.setAdapter(mainMenuViewPager);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:bottomNavigationView.getMenu().findItem(R.id.menu_new).setChecked(true); break;
                    case 1:bottomNavigationView.getMenu().findItem(R.id.menu_list_cook).setChecked(true); break;
                    case 2:bottomNavigationView.getMenu().findItem(R.id.menu_like).setChecked(true); break;
                    case 3:bottomNavigationView.getMenu().findItem(R.id.menu_share).setChecked(true); break;
                    case 4:bottomNavigationView.getMenu().findItem(R.id.menu_profile).setChecked(true); break;
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
        View headerView = navigationView.getHeaderView(0);
        avatar = headerView.findViewById(R.id.mainAvatar);
        userName = headerView.findViewById(R.id.tv_AuthorDN);
        account = headerView.findViewById(R.id.tv_accDN);
        getAcc(avatar, userName, account);

    }

    public void initService(){
        //Cloud DB
        initialize(MainActivity.this);
        cloudDBZoneWrapper.login();
        cloudDBZoneWrapper.createObjectType();
        cloudDBZoneWrapper.closeCloudDBZone();
        cloudDBZoneWrapper.openCloudDBZone();
        //Cloud Storage
        mAGCStorageManagement = AGCStorageManagement.getInstance();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        back++;
        if(back==1) {
            Toast.makeText(this, "Ấn 1 lần nữa để thoát", Toast.LENGTH_SHORT).show();
            onStart();
            new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    back=0;
                }
            }.start();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(1);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_new:
                viewPager2.setCurrentItem(0);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_list_cook:
                viewPager2.setCurrentItem(1);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_like:
                viewPager2.setCurrentItem(2);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_share:
                viewPager2.setCurrentItem(3);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.menu_profile:
                viewPager2.setCurrentItem(4);
                mDrawerLayout.closeDrawers();
                break;
            case R.id.acc_change_pass:
                dialogChangePass();
                break;
            case R.id.acc_signout:
                 AGConnectAuth.getInstance().signOut();
                 startActivity(new Intent(MainActivity.this, LoginActivity.class));
                 break;

        }
        return true;
    }
    public void dialogChangePass() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_changepass);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void getAcc(ImageView avatar, TextView userName, TextView account)
    {
        Task<CloudDBZoneSnapshot<Accounts>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(Accounts.class).equalTo("idAcc", idAcc),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<Accounts>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<Accounts> snapshot) {
                try {
                    Accounts accounts = snapshot.getSnapshotObjects().get(0);
                    avatarAcc = accounts.getSeAcc();
                    Glide.with(getApplicationContext()).load(avatarAcc).into(avatar);
                    nameAcc = accounts.getNameAcc();
                    userName.setText(nameAcc);
                    account.setText("Tài khoản: "+accounts.getMpAcc());
                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}