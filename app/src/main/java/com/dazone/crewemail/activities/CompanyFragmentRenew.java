package com.dazone.crewemail.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.dazone.crewemail.R;
import com.dazone.crewemail.adapter.AdapterOrganization;
import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.fragments.BaseFragment;
import com.dazone.crewemail.utils.Constants;
import com.dazone.crewemail.utils.Prefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class CompanyFragmentRenew extends BaseFragment {
    private RecyclerView rvOrganization;
    private AdapterOrganization adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_company_renew, container, false);
        initView(v);
        return v;
    }

    private void initView(View view) {
        rvOrganization = view.findViewById(R.id.rvOrganization);

        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<PersonData>>(){}.getType();

        ArrayList<PersonData> data = gson.fromJson(new Prefs().getStringValue(Constants.ORGANIZATION, ""), userListType);

        if(data != null && data.size() > 0) {
            initRecyclerView(data);
        }
    }

    public void initRecyclerView(ArrayList<PersonData> list) {
        adapter = new AdapterOrganization(list);

        rvOrganization.setAdapter(adapter);
    }

    public AdapterOrganization getAdapter() {
        return adapter;
    }
}
