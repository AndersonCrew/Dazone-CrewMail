package com.dazone.crewemail.activities;

import android.os.Bundle;

import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.fragments.OrganizationFragment;
import com.dazone.crewemail.utils.StaticsBundle;
import com.dazone.crewemail.utils.Util;

import java.util.ArrayList;

/**
 * Created by Sherry on 12/30/15.
 */
public class OrganizationActivity extends ToolBarActivity {
    private ArrayList<PersonData> selectedPersonList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayToolBarBackButton(true);
    }

    @Override
    protected void addFragment(Bundle bundle, int mainContainer) {
        if(bundle == null){
            Bundle myBundle = getIntent().getExtras();
            int type = myBundle.getInt("TYPE");
            selectedPersonList = myBundle.getParcelableArrayList(StaticsBundle.BUNDLE_LIST_PERSON);
            OrganizationFragment fm = OrganizationFragment.newInstance(selectedPersonList,type);
            Util.replaceFragment(getSupportFragmentManager(), fm, mainContainer, false);
        }
    }
}
