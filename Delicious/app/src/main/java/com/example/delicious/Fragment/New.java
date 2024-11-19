package com.example.delicious.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.Adapter.DropdownAdapter;
import com.example.delicious.Adapter.ItemDishAdapter;
import com.example.delicious.Adapter.SlideDishAdapter;
import com.example.delicious.Model.ItemDropDown;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.hwid.I;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;

public class New extends Fragment {
    View rootView;
    RecyclerView rcvDish, rcvDishFavorest;
    ItemDishAdapter dishAdapter;
    ViewPager2 viewPagerSlideDish;
    SlideDishAdapter slideDishAdapter;
    Spinner spinnerFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_new, container, false);
                initView();
                queryFoodInfo();
                return rootView;
    }
    public void initView() {
        rcvDish = rootView.findViewById(R.id.rcv_dish);
        rcvDish.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        dishAdapter = new ItemDishAdapter(getActivity());
        rcvDish.setAdapter(dishAdapter);
        spinnerFilter = rootView.findViewById(R.id.spinner_filter);
        DropdownAdapter adapter = new DropdownAdapter(getListFilter(), getActivity(), R.layout.dropdown_item2);
        spinnerFilter.setAdapter(adapter);
        //
        rcvDishFavorest = rootView.findViewById(R.id.rcv_favoritest);
        rcvDishFavorest.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        slideDishAdapter = new SlideDishAdapter(getActivity());
        rcvDishFavorest.setAdapter(slideDishAdapter);
        final int time_delay = 3000;
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int count = 0;
            @Override
            public void run() {
                if(count<slideDishAdapter.getItemCount())
                {
                    rcvDishFavorest.scrollToPosition(count++);
                    handler.postDelayed(this, time_delay);
                    if (count==slideDishAdapter.getItemCount())
                    {
                        count = 0;
                    }
                }
            };
        };
        handler.postDelayed(runnable,time_delay);
    }
    public void queryFoodInfo() {
        if (mCloudDBZone == null) {
            Log.w(TAG, "CloudDBZone is null, try re-open it");
            return;
        }
        Task<CloudDBZoneSnapshot<FoodInfo>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FoodInfo.class),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodInfo> snapshot) {
                Log.e("s", "a" );
                CloudDBZoneObjectList<FoodInfo> foodInfocusor = snapshot.getSnapshotObjects();
                ArrayList<FoodInfo> foodInfos1 = new ArrayList<>();
                try {
                    while (foodInfocusor.hasNext()) {
                        FoodInfo bookInfo = foodInfocusor.next();
                        foodInfos1.add(bookInfo);
                    }

                } catch (AGConnectCloudDBException e) {
                    Log.w(TAG, "processQueryResult: " + e.getMessage());
                } finally {
                    snapshot.release();
                }
                dishAdapter.setData(foodInfos1);
                spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==0){
                            dishAdapter.setData(sxTime(foodInfos1));
                    }else if(position==1)
                    {
                            dishAdapter.setData(sxView(foodInfos1));
                    }
                }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        dishAdapter.setData(sxTime(foodInfos1));
                    }
                });
                slideDishAdapter.setDate(sxLike(foodInfos1));
            }
        });
    }
    public ArrayList<ItemDropDown> getListFilter() {
        ArrayList<ItemDropDown> filters = new ArrayList<>();
        filters.add(new ItemDropDown(R.drawable.ic_new12,"Mới"));
        filters.add(new ItemDropDown(R.drawable.ic_favorite4,"Xem nhiều nhất"));
        return filters;
    }
    public ArrayList<FoodInfo> sxTime(ArrayList<FoodInfo> x)
    {
        SimpleDateFormat formats = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Collections.sort(x, new Comparator<FoodInfo>() {
            @Override
            public int compare(FoodInfo o1, FoodInfo o2) {
                Date date1 = new Date();
                Date date2 = new Date();
                try {
                    date1  = formats.parse(o1.getTimeupF());
                    date2 = formats.parse(o2.getTimeupF());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(date1.after(date2))
                {
                    return -1;
                }
                else if(date1.before(date2))
                {
                    return  1;
                }
                else
                {
                    return 0;
                }
            };
        });
        return x;
    }
    public ArrayList<FoodInfo> sxView(ArrayList<FoodInfo> x)
    {

        Collections.sort(x, new Comparator<FoodInfo>() {
            @Override
            public int compare(FoodInfo o1, FoodInfo o2) {
                    if(Integer.parseInt(o1.getAmountviewF()) > Integer.parseInt(o2.getAmountviewF()))
                    {
                        return -1;
                    }
                    else if(Integer.parseInt(o1.getAmountviewF()) < Integer.parseInt(o2.getAmountviewF()))
                    {
                        return 1;
                    }
                    else {
                        return 0;
                    }

            };
        });
        return x;
    }
    public ArrayList<FoodInfo> sxLike(ArrayList<FoodInfo> x)
    {
        ArrayList<FoodInfo> favoritest = new ArrayList<>();
        Collections.sort(x, new Comparator<FoodInfo>() {
            @Override
            public int compare(FoodInfo o1, FoodInfo o2) {
                if(Integer.parseInt(o1.getAmountlikeF()) > Integer.parseInt(o2.getAmountlikeF()))
                {
                    return -1;
                }
                else if(Integer.parseInt(o1.getAmountlikeF()) < Integer.parseInt(o2.getAmountlikeF()))
                {
                    return 1;
                }
                else {
                    return 0;
                }

            };
        });
        favoritest.add(x.get(0));
        favoritest.add(x.get(1));
        favoritest.add(x.get(2));
        return favoritest;
    }

}