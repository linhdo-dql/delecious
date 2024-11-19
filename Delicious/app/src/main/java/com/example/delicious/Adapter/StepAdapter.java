package com.example.delicious.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.delicious.API.CloudDB.FoodStep;
import com.example.delicious.R;
import com.google.android.material.textfield.TextInputEditText;
import com.huawei.agconnect.cloud.database.Text;

import java.util.ArrayList;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {
    @NonNull
    ArrayList<FoodStep> steps = new ArrayList<>();
    Context context;
    String urisD="";
    public StepAdapter(Context context) {
        this.context = context;
    }

    public void setData(ArrayList<FoodStep> steps)
    {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @Override
    public StepViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_step, parent, false);
        return new StepViewHolder(v);
    }
    public void onActivityResult(String s) {
            this.urisD = s;
    }
    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepViewHolder holder, int position) {
        holder.stepindex.setText("Bước "+(position+1));
        holder.stepcontent.setText(steps.get(position).getContentStep().toString());
        String url = steps.get(position).getImgStep();
        if(url.contains("delicious-glq1s"))
        {
            Glide.with(context).load(url).into(holder.imgStep);
        }
        else {
            holder.imgStep.setImageURI(Uri.parse(steps.get(position).getImgStep()));
        }
        holder.itemStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_updatestep);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView textView = dialog.findViewById(R.id.tv_index_step_u);
                textView.setText(holder.stepindex.getText().toString());
                TextInputEditText editText = dialog.findViewById(R.id.edt_step_dia_u);
                editText.setText(holder.stepcontent.getText().toString());
                ImageView imageStepDia = dialog.findViewById(R.id.img_step_dialog_u);
                imageStepDia.setImageURI(Uri.parse(steps.get(position).getImgStep()));
                imageStepDia.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        ((Activity) context).startActivityForResult(Intent.createChooser(intent,"Title"), 11032);
                    }
                });
                TextView btnAddStep = dialog.findViewById(R.id.btn_step_add_dialog_u);
                btnAddStep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        steps.get(position).setImgStep(urisD);
                        Text a = new Text(editText.getText().toString());
                        steps.get(position).setContentStep(a);
                        notifyDataSetChanged();
                        Log.e("step",steps.toString());
                        dialog.dismiss();
                    }
                });
                ImageView imgClose = dialog.findViewById(R.id.btn_close_step_dialog_u);
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        if(steps.size()!=0) {
            return steps.size();
        }
        return 0;
    }

    public class StepViewHolder extends RecyclerView.ViewHolder {
        TextView stepindex, stepcontent;
        ImageView imgStep;
        RelativeLayout itemStep;
        public StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepindex = itemView.findViewById(R.id.tv_stepindex);
            stepcontent = itemView.findViewById(R.id.tv_step_content);
            imgStep = itemView.findViewById(R.id.img_step);
            itemStep = itemView.findViewById(R.id.item_step);
        }
    }
}
