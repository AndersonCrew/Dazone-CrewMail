package com.dazone.crewemail.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dazone.crewemail.R;
import com.dazone.crewemail.adapter.MemberAdapter;
import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.utils.Constants;
import com.dazone.crewemail.utils.Prefs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class SearchFragmentRenew extends Fragment {

    private RecyclerView rvMembers;
    private MemberAdapter adapter;
    private ArrayList<PersonData> listMember;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_renew, container, false);
        rvMembers = v.findViewById(R.id.rvMembers);
        getData();
        return v;
    }

    private void getData() {
        Gson gson = new Gson();

        Type userListType = new TypeToken<ArrayList<PersonData>>(){}.getType();

        ArrayList<PersonData> data = gson.fromJson(new Prefs().getStringValue(Constants.ORGANIZATION, ""), userListType);

        if(data != null && data.size() > 0) {
            listMember = new ArrayList<>();
            for(PersonData personData : data) {
                if(personData.getListMembers() != null && personData.getListMembers().size() > 0) {
                    for(PersonData member : personData.getListMembers()) {
                        if(!listMember.contains(member)) {
                            listMember.add(member);
                        }
                    }
                }

                if(personData.getPersonList() != null && personData.getPersonList().size() > 0) {
                    checkDepartment(listMember, personData.getPersonList());
                }
            }

            adapter = new MemberAdapter(listMember, new MemberAdapter.IAllMemberChecked() {
                @Override
                public void onAllMemberChecked() {

                }

                @Override
                public void onAllMemberNonChecked() {

                }
            });

            rvMembers.setAdapter(adapter);
        }
    }

    private void checkDepartment(ArrayList<PersonData> listMember, ArrayList<PersonData> listDepartment) {
        for(PersonData department : listDepartment) {
            if(department.getListMembers() != null && department.getListMembers().size() > 0) {
                for(PersonData member : department.getListMembers()) {
                    if(!listMember.contains(member)) {
                        listMember.add(member);
                    }
                }
            }

            if(department.getPersonList() != null && department.getPersonList().size() > 0) {
                checkDepartment(listMember, department.getPersonList());
            }
        }
    }

    public MemberAdapter getAdapter() {
        return adapter;
    }

}
