package com.example.com3104_project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdaptorextendsBound extends FragmentStateAdapter {

    public ViewPagerAdaptorextendsBound(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new BusStopOutbound_Tab();
            case 1:
                return new BusStopInbound_Tab();
            default:
                return new BusStopOutbound_Tab();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
