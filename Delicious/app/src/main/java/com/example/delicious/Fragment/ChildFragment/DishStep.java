package com.example.delicious.Fragment.ChildFragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.delicious.API.CloudDB.FoodStep;
import com.example.delicious.Adapter.StepAdapter;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneObjectList;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.ContentValues.TAG;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;
import static com.example.delicious.DishDetail.idDish;

public class DishStep extends Fragment {
    View rootView;
    StepAdapter stepAdapter;
    RecyclerView rcvStep;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_dish_step, container, false);
        initView();
        return rootView;
    }
    public void initView() {
        rcvStep = rootView.findViewById(R.id.rcv_step_dish);
        rcvStep.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        stepAdapter = new StepAdapter(getActivity());
        setList();
        rcvStep.setAdapter(stepAdapter);
    }

    private void setList() {
        Task<CloudDBZoneSnapshot<FoodStep>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FoodStep.class).equalTo("idF", idDish),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY
        );
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodStep>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodStep> snapshot) {
                CloudDBZoneObjectList<FoodStep> foodStep = snapshot.getSnapshotObjects();
                ArrayList<FoodStep> foodSteps = new ArrayList<>();
                try {
                    while (foodStep.hasNext()) {
                        FoodStep foodStep1 = foodStep.next();
                        foodSteps.add(foodStep1);
                    }

                } catch (AGConnectCloudDBException e) {
                    Log.w(TAG, "processQueryResult: " + e.getMessage());
                } finally {
                    snapshot.release();
                }
                stepAdapter.setData(compareList(foodSteps));
            }
        });
    }
    public ArrayList<FoodStep> compareList(ArrayList<FoodStep> x)
    {
        x.sort(new Comparator<FoodStep>() {
            @Override
            public int compare(FoodStep o1, FoodStep o2) {
                if (o1.getIdStep().compareTo(o2.getIdStep()) < 0) {
                    return -1;
                } else if (o1.getIdStep().compareTo(o2.getIdStep()) > 0) {
                    return 1;
                }
                return 0;
            }
        });
        return x;
    }
}