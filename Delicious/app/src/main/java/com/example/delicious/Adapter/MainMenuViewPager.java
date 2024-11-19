package com.example.delicious.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.delicious.Fragment.Cook;
import com.example.delicious.Fragment.Favorite;
import com.example.delicious.Fragment.New;
import com.example.delicious.Fragment.Pesonal;
import com.example.delicious.Fragment.Share;

public class MainMenuViewPager extends FragmentStateAdapter {


    public MainMenuViewPager(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0: return new New();
            case 1: return new Cook();
            case 2: return new Favorite();
            case 3: return new Share();
            case 4: return new Pesonal();
            default: return new New();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
