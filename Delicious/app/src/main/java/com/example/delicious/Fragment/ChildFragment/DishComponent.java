package com.example.delicious.Fragment.ChildFragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.delicious.API.CloudDB.FoodComponent;
import com.example.delicious.Adapter.DishComponentAdapter;
import com.example.delicious.Model.Component;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.example.delicious.DishDetail.idDish;

public class DishComponent extends Fragment {
    View rootView;
    RecyclerView rcvComponent;
    DishComponentAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dish_component, container, false);
        initView();
        return rootView;
    }
    public void initView() {
        rcvComponent = rootView.findViewById(R.id.rcv_component_dish);
        rcvComponent.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new DishComponentAdapter(getActivity());
        setList();
        rcvComponent.setAdapter(adapter);
    }

    private void setList() {
        Task<CloudDBZoneSnapshot<FoodComponent>> queryTask = mCloudDBZone.executeQuery(
          CloudDBZoneQuery.where(FoodComponent.class).equalTo("idF", idDish),
          CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodComponent>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodComponent> snapshot) {
                CloudDBZoneObjectList<FoodComponent> foodCom = snapshot.getSnapshotObjects();
                ArrayList<Component> foodComponents = new ArrayList<>();
                try {
                    while (foodCom.hasNext()) {
                        FoodComponent foodComponent = foodCom.next();
                        foodComponents.add(new Component(foodComponent.getNumberCom(), foodComponent.getNameCom(), foodComponent.getDetermineCom(),false));
                    }

                } catch (AGConnectCloudDBException e) {
                    Log.w(TAG, "processQueryResult: " + e.getMessage());
                } finally {
                    snapshot.release();
                }
                adapter.setData(foodComponents);
            }
        });
    }
}