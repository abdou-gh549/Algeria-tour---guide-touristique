package com.algeriatour.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private class FragmentAndImageID{
        Fragment m_fragment;
        int imageId;
        FragmentAndImageID(Fragment f, int id){
            m_fragment = f;
            imageId    = id;
        }
    }

    private ArrayList<FragmentAndImageID> m_fragments = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }



     public void addFragment(Fragment fragment, int imageId){
        m_fragments.add(new FragmentAndImageID(fragment, imageId));
    }

    public int getFragmentImageId(int id){
        return m_fragments.get(id).imageId;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("viewPagerTest", "getItem: entred pos = " + position);

        return m_fragments.get(position).m_fragment;
    }

    @Override
    public int getCount() {
        return m_fragments.size();
    }
}
