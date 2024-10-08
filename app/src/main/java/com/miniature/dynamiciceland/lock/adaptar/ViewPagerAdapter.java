package com.miniature.dynamiciceland.lock.adaptar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.miniature.dynamiciceland.lock.fragment.SettingsFragment;
import com.miniature.dynamiciceland.lock.fragment.ThemeFragment;
import com.miniature.dynamiciceland.lock.utils.Constants;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public int getItemCount() {
        return 1;
    }

    public ViewPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public Fragment createFragment(int i) {
        if (i == 0) {
            return new SettingsFragment();
        }
        if (i != 1) {
            return new SettingsFragment();
        }
        return ThemeFragment.newInstance(Constants.GETTING_APP, 3);
    }
}
