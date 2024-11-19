package com.example.delicious.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicious.Model.Component;
import com.example.delicious.R;

import java.util.ArrayList;

import static com.example.delicious.R.color.bgedt;

public class DishComponentAdapter extends RecyclerView.Adapter<DishComponentAdapter.ComponentViewHolder> {
    @NonNull
    ArrayList<Component> components = new ArrayList<>();
    Context context;

    public DishComponentAdapter(Context context) {
        this.context = context;
    }

        public void setData(ArrayList<Component> components)
    {
        this.components = components;
        notifyDataSetChanged();
    }

    @Override
    public ComponentViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_component2, parent, false);
        return new ComponentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DishComponentAdapter.ComponentViewHolder holder, int position) {
        holder.name_component.setText(components.get(position).getName());
        holder.number.setText(""+components.get(position).getNumber());
        holder.determine.setText(components.get(position).getDetermine());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    holder.itemComDish.setBackgroundColor(Color.parseColor("#F8B601"));
                }
                else {
                    holder.itemComDish.setBackgroundResource(bgedt);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(components.size()!=0) {
            return components.size();
        }
        return 0;
    }

    public class ComponentViewHolder extends RecyclerView.ViewHolder {
        TextView name_component, number, determine;
        CheckBox checkBox;
        RelativeLayout itemComDish;
        public ComponentViewHolder(@NonNull View itemView) {
            super(itemView);
            name_component = itemView.findViewById(R.id.cb_name_component_dish);
            number = itemView.findViewById(R.id.number_com_dish);
            determine = itemView.findViewById(R.id.determine_com_dish);
            checkBox = itemView.findViewById(R.id.cb_component_dish);
            itemComDish = itemView.findViewById(R.id.item_component2);
        }
    }
}
