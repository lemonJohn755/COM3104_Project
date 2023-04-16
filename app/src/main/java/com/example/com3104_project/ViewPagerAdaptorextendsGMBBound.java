package com.example.com3104_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdaptorextendsGMBBound extends FragmentStateAdapter {

    public ViewPagerAdaptorextendsGMBBound(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new GMBOutbound_Tab();
            case 1:
                return new GMBInbound_Tab();
            default:
                return new GMBOutbound_Tab();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

}
