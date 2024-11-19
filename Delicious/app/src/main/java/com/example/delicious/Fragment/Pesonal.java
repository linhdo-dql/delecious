package com.example.delicious.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.delicious.MainActivity;
import com.example.delicious.R;

import static android.content.Context.MODE_PRIVATE;
import static com.example.delicious.MainActivity.avatarAcc;
import static com.example.delicious.MainActivity.b;
import static com.example.delicious.MainActivity.nameAcc;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Pesonal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pesonal extends Fragment {
    View rootView;
    SharedPreferences sharedPreferences;
    Switch daynightSwitch;
    TextView tvUserNameSt;
    ImageView avatarSetting;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Pesonal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pesonal.
     */
    // TODO: Rename and change types and number of parameters
    public static Pesonal newInstance(String param1, String param2) {
        Pesonal fragment = new Pesonal();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_pesonal, container, false);
        initView();
        dayNightMode();
        return rootView;
    }
    public void initView() {
        daynightSwitch = rootView.findViewById(R.id.day_night_switch);
    }
    public void dayNightMode()
    {
        tvUserNameSt = rootView.findViewById(R.id.tv_userName_st);
        tvUserNameSt.setText(nameAcc);
        avatarSetting = rootView.findViewById(R.id.avatar_st);
        Glide.with(getActivity()).load(avatarAcc).into(avatarSetting);
        if(b)
        {
            daynightSwitch.setChecked(true);
        }
        daynightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    daynightSwitch.setChecked(true);
                    sharedPreferences = getActivity().getSharedPreferences("day_night_mode", MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("mode", true).apply();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    daynightSwitch.setChecked(false);
                    sharedPreferences = getActivity().getSharedPreferences("day_night_mode", MODE_PRIVATE);
                    sharedPreferences.edit().putBoolean("mode", false).apply();
                }
            }
        });

    }
}