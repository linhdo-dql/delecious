package com.example.delicious.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.delicious.API.CloudDB.FoodComponent;
import com.example.delicious.Model.ItemDropDown;
import com.example.delicious.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ComponentViewHolder> {
    @NonNull
    ArrayList<FoodComponent> components = new ArrayList<>();
    Context context;

    public ComponentAdapter(Context context) {
        this.context = context;
    }

        public void setData(ArrayList<FoodComponent> components)
    {
        this.components = components;
        notifyDataSetChanged();
    }

    @Override
    public ComponentViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_component, parent, false);
        return new ComponentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ComponentAdapter.ComponentViewHolder holder, int position) {
        holder.index.setText(""+(position+1));
        holder.name_component.setText(components.get(position).getNameCom());
        holder.number.setText(""+components.get(position).getNumberCom());
        holder.determine.setText(components.get(position).getDetermineCom());
        holder.itemComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_addcomponent);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView tv_tt_dialog_add_com = dialog.findViewById(R.id.tt_dialog_add_com);
                tv_tt_dialog_add_com.setText("Sửa nguyên liệu");
                EditText tv_name_component = dialog.findViewById(R.id.edt_name_com_dia);
                tv_name_component.setText(components.get(position).getNameCom());
                EditText tv_number_component = dialog.findViewById(R.id.edt_number_com_dia);
                tv_number_component.setText(components.get(position).getNumberCom());
                Spinner spinner_type_com = dialog.findViewById(R.id.spinner_component_type);
                DropdownAdapter dropdownAdapter = new DropdownAdapter(getComponentType(),context, R.layout.dropdown_item);
                spinner_type_com.setAdapter(dropdownAdapter);
                TextView btnadd = dialog.findViewById(R.id.btn_com_add_dialog);
                btnadd.setText("Sửa");
                btnadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String com_name = tv_name_component.getText().toString().trim();
                        String com_number = tv_number_component.getText().toString().trim();
                        if(com_number.length()!=0 && com_name.length()!=0) {
                            components.get(position).setNameCom(com_name);
                            components.get(position).setNumberCom(com_number);
                            components.get(position).setDetermineCom(getComponentType().get(spinner_type_com.getSelectedItemPosition()).getText());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                        else  {
                            Toast.makeText(context, "Vui lòng nhập đủ yêu cầu", Toast.LENGTH_LONG).show();
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
    }

    @Override
    public int getItemCount() {
        if(components.size()!=0) {
            return components.size();
        }
        return 0;
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

    public class ComponentViewHolder extends RecyclerView.ViewHolder {
        TextView index, name_component, number, determine;
        RelativeLayout itemComponent;
        public ComponentViewHolder(@NonNull View itemView) {
            super(itemView);
            index = itemView.findViewById(R.id.tv_listindex);
            name_component = itemView.findViewById(R.id.tv_name_compo);
            number = itemView.findViewById(R.id.tv_number);
            determine = itemView.findViewById(R.id.tv_determine);
            itemComponent = itemView.findViewById(R.id.item_component);
        }
    }
}
