package com.example.delicious.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.delicious.ListDish;
import com.example.delicious.R;


public class Cook extends Fragment implements View.OnClickListener {
    View rootView;
    RelativeLayout la_monchinh, la_monchay, la_monbanh, la_montron, la_monmi, la_monsuop, la_haisan, la_hoaqua, la_douong, la_kem;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_cook, container, false);
                initView();
                return rootView;
    }
    public void initView()
    {
        la_monchinh = rootView.findViewById(R.id.la_mon_chinh);
        la_monchinh.setOnClickListener(this);
        la_monchay = rootView.findViewById(R.id.la_mon_chay);
        la_monchay.setOnClickListener(this);
        la_monbanh = rootView.findViewById(R.id.la_mon_banh);
        la_monbanh.setOnClickListener(this);
        la_douong = rootView.findViewById(R.id.la_do_uong);
        la_douong.setOnClickListener(this);
        la_haisan = rootView.findViewById(R.id.la_hai_san);
        la_haisan.setOnClickListener(this);
        la_monsuop = rootView.findViewById(R.id.la_mon_soup);
        la_monsuop.setOnClickListener(this);
        la_montron = rootView.findViewById(R.id.la_mon_tron);
        la_montron.setOnClickListener(this);
        la_monmi = rootView.findViewById(R.id.la_mon_mi);
        la_monmi.setOnClickListener(this);
        la_hoaqua = rootView.findViewById(R.id.la_hoa_qua);
        la_hoaqua.setOnClickListener(this);
        la_kem = rootView.findViewById(R.id.la_kem);
        la_kem.setOnClickListener(this);
    }
    /*public void arrayList() {
        ArrayList<ItemCook> itemCooks = new ArrayList<>();
        itemCooks.add(new ItemCook(R.drawable.bg_monchinh, R.drawable.ic_f_maint, "Món chính"));
        itemCooks.add(new ItemCook(R.drawable.bg_monchay, R.drawable.ic_f_heatht, "Món chay"));
        itemCooks.add(new ItemCook(R.drawable.bg_monbanh, R.drawable.ic_f_caket, "Món bánh"));
        itemCooks.add(new ItemCook(R.drawable.bg_montron, R.drawable.ic_f_saladt, "Món trộn"));
        itemCooks.add(new ItemCook(R.drawable.bg_mon_mi, R.drawable.ic_f_noddlet, "Món mì"));
        itemCooks.add(new ItemCook(R.drawable.bg_monsoup, R.drawable.ic_f_soupt, "Món súp"));
        itemCooks.add(new ItemCook(R.drawable.bg_haisan, R.drawable.ic_f_crabt, "Hải sản"));
        itemCooks.add(new ItemCook(R.drawable.bg_hoaqua, R.drawable.ic_f_fruitt, "Hoa quả"));
        itemCooks.add(new ItemCook(R.drawable.ic_do_uong, R.drawable.ic_f_drinkt, "Đồ uống"));
        itemCooks.add(new ItemCook(R.drawable.bg_kem, R.drawable.ic_f_icreamt, "Kem"));
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.la_mon_chinh: intentListDish("Món chính");  break;
            case R.id.la_mon_chay: intentListDish("Món chay"); break;
            case R.id.la_mon_banh: intentListDish("Món bánh"); break;
            case R.id.la_mon_tron: intentListDish("Món trộn"); break;
            case R.id.la_mon_mi: intentListDish("Món mì"); break;
            case R.id.la_mon_soup: intentListDish("Món súp"); break;
            case R.id.la_hai_san: intentListDish("Hải sản"); break;
            case R.id.la_hoa_qua: intentListDish("Hoa quả"); break;
            case R.id.la_do_uong: intentListDish("Đồ uống"); break;
            case R.id.la_kem: intentListDish("Kem"); break;
        }
    }
    public void intentListDish(String s)
    {
        Intent intent = new Intent(getActivity(), ListDish.class);
        intent.putExtra("dish",s);
        getActivity().startActivity(intent);
    }
}