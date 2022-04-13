package com.dazone.crewemail.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dazone.crewemail.activities.CompanyFragmentRenew;
import com.dazone.crewemail.activities.SearchFragmentRenew;
import com.dazone.crewemail.data.PersonData;

import java.util.ArrayList;

public class AdapterOrganizationPager extends FragmentPagerAdapter {

    public AdapterOrganizationPager(FragmentManager fm, CompanyFragmentRenew companyFragmentRenew, SearchFragmentRenew searchFragmentRenew) {
        super(fm);
        this.companyFragmentRenew = companyFragmentRenew;
        this.searchFragmentRenew = searchFragmentRenew;
    }
    private CompanyFragmentRenew companyFragmentRenew;
    private SearchFragmentRenew searchFragmentRenew;

    @Override
    public Fragment getItem(int i) {
        if(i == 0) {
            return companyFragmentRenew;
        }

        return searchFragmentRenew;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
