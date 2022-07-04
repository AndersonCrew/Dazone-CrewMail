package com.dazone.crewemail.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dazone.crewemail.activities.CompanyFragmentRenew;
import com.dazone.crewemail.activities.SearchFragmentRenew;

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
