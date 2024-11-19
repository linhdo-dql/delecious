package com.example.delicious.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delicious.API.CloudDB.Accounts;
import com.example.delicious.API.CloudDB.DishFavorite;
import com.example.delicious.API.CloudDB.FoodInfo;
import com.example.delicious.DishDetail;
import com.example.delicious.R;
import com.huawei.agconnect.cloud.database.CloudDBZoneQuery;
import com.huawei.agconnect.cloud.database.CloudDBZoneSnapshot;
import com.huawei.agconnect.cloud.database.exceptions.AGConnectCloudDBException;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static com.example.delicious.API.CloudDB.CloudDBZoneWrapper.mCloudDBZone;

public class ItemDishAdapter extends RecyclerView.Adapter<ItemDishAdapter.ItemDishViewHolder> {
    ArrayList<FoodInfo> foodInfos = new ArrayList<>();
    Context context;
    int i = 0;

    public ItemDishAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<FoodInfo> foodInfos)
    {
        this.foodInfos = foodInfos;
        notifyDataSetChanged();
    }

    @Override
    public ItemDishViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_dish, parent, false);
        return new ItemDishViewHolder(v);
    }

    @Override
    public void onBindViewHolder( ItemDishAdapter.ItemDishViewHolder holder, int position) {

        holder.idF.setText(foodInfos.get(position).getIdF());
        holder.typeF.setText(foodInfos.get(position).getTypeF());
        setBgTypeF(foodInfos.get(position).getTypeF(), holder.bg_typeF);
        holder.nameF.setText(foodInfos.get(position).getNameF());
        String amountViewF = foodInfos.get(position).getAmountviewF().trim();
        if (amountViewF.equals("0")) {
            holder.amountViewF.setText("0");
        } else {
            holder.amountViewF.setText(foodInfos.get(position).getAmountviewF());
        }
        String amountLikeF = foodInfos.get(position).getAmountlikeF().trim();
        if (amountLikeF.isEmpty()) {
            holder.amountLikeF.setText("0");
        } else {
            holder.amountLikeF.setText(foodInfos.get(position).getAmountlikeF());
        }
        setUserName(foodInfos.get(position).getAuthorF(), holder.nameAuthorF);
        Glide.with(context).load(foodInfos.get(position).getAvatarF()).into(holder.avatarF);
        holder.itemDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DishDetail.class);
                intent.putExtra("id_f", foodInfos.get(position).getIdF());
                addView(foodInfos.get(position).getIdF());
                context.startActivity(intent);
            }
        });

        SharedPreferences preferences = context.getSharedPreferences("Tempt", MODE_PRIVATE);
        checkLiked(preferences.getString("id_acc",""), foodInfos.get(position).getIdF(), holder.icLiked);
    }

    @Override
    public int getItemCount() {
        if(foodInfos.size()!=0)
        {
            return foodInfos.size();
        }
        return 0;
    }
    @SuppressLint("ResourceAsColor")
    public void setBgTypeF(String s, CardView c)
    {
        switch (s)
        {
            case "Món chính": c.setCardBackgroundColor(Color.parseColor("#99FF0000")); break;
            case "Món chay": c.setCardBackgroundColor(Color.parseColor("#994CAF50")); break;
            case "Món bánh": c.setCardBackgroundColor(Color.parseColor("#99FF9800")); break;
            case "Món mì": c.setCardBackgroundColor(Color.parseColor("#99FF6D00")); break;
            case "Món trộn": c.setCardBackgroundColor(Color.parseColor("#992196F3")); break;
            case "Món súp": c.setCardBackgroundColor(Color.parseColor("#99304FFE")); break;
            case "Hải sản": c.setCardBackgroundColor(Color.parseColor("#99DD2C00")); break;
            case "Hoa quả": c.setCardBackgroundColor(Color.parseColor("#9964DD17")); break;
            case "Đồ uống": c.setCardBackgroundColor(Color.parseColor("#996200EA")); break;
            case "Kem": c.setCardBackgroundColor(Color.parseColor("#9900B8D4")); break;
        }
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
                    tvAuthor.setText(accounts);
                } catch (AGConnectCloudDBException e) {
                    e.printStackTrace();
                }


            }
        });
    }
    public void addView(String s) {
        Task<CloudDBZoneSnapshot<FoodInfo>> queryTask = mCloudDBZone.executeQuery(
                CloudDBZoneQuery.where(FoodInfo.class).equalTo("idF", s),
                CloudDBZoneQuery.CloudDBZoneQueryPolicy.POLICY_QUERY_FROM_CLOUD_ONLY);
        queryTask.addOnSuccessListener(new OnSuccessListener<CloudDBZoneSnapshot<FoodInfo>>() {
            @Override
            public void onSuccess(CloudDBZoneSnapshot<FoodInfo> foodss) {
                try {
                    FoodInfo f = foodss.getSnapshotObjects().get(0);
                    String x = f.getAmountviewF();
                    int y = Integer.parseInt(x) + 1;
                    f.setAmountviewF(String.valueOf(y));
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
    public void checkLiked(String idAcc, String idF, ImageView imageView)
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
                    imageView.setImageResource(R.drawable.ic_heart2);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_like);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("err", e.getMessage());
            }
        });
    }
    class ItemDishViewHolder extends RecyclerView.ViewHolder {
        TextView idF, nameF, amountViewF, amountLikeF, nameAuthorF, typeF;
        ImageView avatarF, icLiked;
        CardView bg_typeF, itemDish;
        public ItemDishViewHolder(View itemView) {
            super(itemView);
            idF = itemView.findViewById(R.id.tv_idF);
            nameF = itemView.findViewById(R.id.tv_nameF);
            amountViewF = itemView.findViewById(R.id.tv_amountViewF);
            amountLikeF = itemView.findViewById(R.id.tv_amountLikeF);
            nameAuthorF = itemView.findViewById(R.id.tv_name_AuthorF);
            typeF = itemView.findViewById(R.id.tv_typeF);
            avatarF = itemView.findViewById(R.id.img_avatarF);
            bg_typeF = itemView.findViewById(R.id.bg_typeF);
            itemDish = itemView.findViewById(R.id.itemDish);
            icLiked = itemView.findViewById(R.id.icon_like);
        }
    }


}
