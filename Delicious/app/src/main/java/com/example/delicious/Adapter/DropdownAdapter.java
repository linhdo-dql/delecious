package com.example.delicious.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delicious.Model.ItemDropDown;
import com.example.delicious.R;

import java.util.ArrayList;

public class DropdownAdapter extends BaseAdapter {
    ArrayList<ItemDropDown> itemDropDowns = new ArrayList<>();
    Context context;
    int intLayout;

    public DropdownAdapter(ArrayList<ItemDropDown> itemDropDowns, Context context, int intLayout) {
        this.itemDropDowns = itemDropDowns;
        this.context = context;
        this.intLayout = intLayout;
    }

    @Override
    public int getCount() {
        return itemDropDowns.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(intLayout, parent, false);
        ImageView imageView = convertView.findViewById(R.id.icon_item);
        imageView.setBackgroundResource(itemDropDowns.get(position).getIconsrc());
        TextView textView = convertView.findViewById(R.id.tv_item);
        textView.setText(itemDropDowns.get(position).getText());
        return convertView;
    }
}
