package com.dazone.crewemail.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.dazone.crewemail.R;
import com.dazone.crewemail.adapter.AdapterOrganizationChartV2;
import com.dazone.crewemail.data.ErrorData;
import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.database.OrganizationUserDBHelper;
import com.dazone.crewemail.dto.Tree.OrgTree;
import com.dazone.crewemail.interfaces.OnGetAllOfUser;
import com.dazone.crewemail.webservices.HttpRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NewOrganizationChart extends AppCompatActivity {
    private String TAG = "NewOrganizationChart";
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private AdapterOrganizationChartV2 mAdapter;
    private List<PersonData> list = new ArrayList<>();
    private ArrayList<PersonData> listTemp;
    private ArrayList<PersonData> mDepartmentList;
    private ArrayList<PersonData> temp = new ArrayList<>();
    private ArrayList<PersonData> mPersonList = new ArrayList<>();
    private ArrayList<PersonData> mSelectedPersonList = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_organization_chart_layout);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        initView();
        initDB();

    }

    public void scrollToEndList(int size) {
        recyclerView.smoothScrollToPosition(size);
    }

    void initView() {

        recyclerView = findViewById(R.id.rv);
        NewOrganizationChart instance = this;
        mAdapter = new AdapterOrganizationChartV2(this, list, true, instance,null);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

    }



    void initDB() {
        initWholeOrganization();
    }

    private void initWholeOrganization() {
        // build offline version
        // Get offline data
        String root_link = HttpRequest.getInstance().sRootLink;
        mDepartmentList = OrganizationUserDBHelper.getDepartments(root_link);
        listTemp = OrganizationUserDBHelper.getDepartmentUsers(root_link);

        if (mDepartmentList != null && mDepartmentList.size() > 0) {
            buildTree(mDepartmentList, false);
            Log.d(TAG, "1");
        } else { // Get department from server
            Log.d(TAG, "2");
            HttpRequest.getInstance().getDepartment( new OnGetAllOfUser() {
                @Override
                public void onGetAllOfUserSuccess(ArrayList<PersonData> list) {
                    buildTree(list,true);
                }

                @Override
                public void onGetAllOfUserFail(ErrorData errorData) {

                }
            });
        }
    }

    private void buildTree(final ArrayList<PersonData> treeUserDTOs, boolean isFromServer) {
        if (treeUserDTOs != null) {
            if (isFromServer) {
                convertData(treeUserDTOs);
            } else {
                temp.clear();
                temp.addAll(treeUserDTOs);
            }

            for (PersonData treeUserDTO : temp) {
                if (treeUserDTO.getPersonList() != null && treeUserDTO.getPersonList().size() > 0) {
                    treeUserDTO.setPersonList(null);
                }
            }

            // sort data by order
            Collections.sort(temp, (r1, r2) -> {
                if (r1.getSortNo() > r2.getSortNo()) {
                    return 1;
                } else if (r1.getSortNo() == r2.getSortNo()) {
                    return 0;
                } else {
                    return -1;
                }
            });

            for (PersonData treeUserDTOTemp : mDepartmentList) {
                    PersonData treeUserDTO = new PersonData();
                    for (PersonData u : mSelectedPersonList) {
                        if (treeUserDTOTemp.getUserNo() == u.getDepartNo()) {
                            treeUserDTO.setIsCheck(true);
                            break;
                        }
                    }

                    temp.add(treeUserDTO);
                }

            mPersonList = new ArrayList<>();
            mPersonList.addAll(temp);

            PersonData dto = null;

            try {
                dto = OrgTree.buildTree(mPersonList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (dto != null) {
                list = dto.getPersonList();
                mAdapter.updateList(list);
            }
        }
    }

    public void convertData(List<PersonData> treeUserDTOs) {
        if (treeUserDTOs != null && treeUserDTOs.size() != 0) {
            for (PersonData dto : treeUserDTOs) {
                if (dto.getPersonList() != null && dto.getPersonList().size() > 0) {
                    temp.add(dto);
                    convertData(dto.getPersonList());
                } else {
                    temp.add(dto);
                }
            }
        }
    }
}