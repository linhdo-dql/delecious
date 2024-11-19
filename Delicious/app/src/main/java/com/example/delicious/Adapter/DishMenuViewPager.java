package com.example.delicious.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.delicious.Fragment.ChildFragment.DishComment;
import com.example.delicious.Fragment.ChildFragment.DishComponent;
import com.example.delicious.Fragment.ChildFragment.DishProfile;
import com.example.delicious.Fragment.ChildFragment.DishStep;
import com.example.delicious.Fragment.Cook;
import com.example.delicious.Fragment.Favorite;
import com.example.delicious.Fragment.New;
import com.example.delicious.Fragment.Pesonal;
import com.example.delicious.Fragment.Share;

public class DishMenuViewPager extends FragmentStateAdapter {


    public DishMenuViewPager(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0: return new DishProfile();
            case 1: return new DishComponent();
            case 2: return new DishStep();
            case 3: return new DishComment();
            default: return new DishProfile();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
