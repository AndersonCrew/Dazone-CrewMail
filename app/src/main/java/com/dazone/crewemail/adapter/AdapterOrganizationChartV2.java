package com.dazone.crewemail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dazone.crewemail.R;
import com.dazone.crewemail.activities.NewOrganizationChart;
import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.utils.Prefs;
import com.dazone.crewemail.utils.Statics;
import com.dazone.crewemail.utils.Util;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Dazone on 7/11/2017.
 */

public class AdapterOrganizationChartV2 extends RecyclerView.Adapter<AdapterOrganizationChartV2.MyViewHolder> {
    private String TAG = "OrganizationChart";
    private List<PersonData> list;
    private List<PersonData> listTemp = new ArrayList<>();
    private List<PersonData> listTemp_2 = new ArrayList<>();
    private List<PersonData> listTemp_3 = new ArrayList<>();
    private ArrayList<Integer> userNos;
    private int isSearch = 0; // 0 -> normal : 1 -> search
    private int myId;
    private NewOrganizationChart instance;
    private int mg;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout iconWrapper, item_org_wrapper;
        public ImageView avatar;
        public ImageView folderIcon;
        public TextView name, position;
        public CheckBox row_check;
        public RelativeLayout relAvatar;

        public MyViewHolder(View view) {
            super(view);
            item_org_wrapper = view.findViewById(R.id.item_org_wrapper);

            avatar = view.findViewById(R.id.avatar);
            folderIcon = view.findViewById(R.id.ic_folder);
            relAvatar = view.findViewById(R.id.relAvatar);
            iconWrapper = view.findViewById(R.id.icon_wrapper);
            name = view.findViewById(R.id.name);
            position = view.findViewById(R.id.position);
            row_check = view.findViewById(R.id.row_check);
        }

        public void handler(final PersonData treeUserDTO, final int index) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int margin;
            if (isSearch == 0) {
                margin = treeUserDTO.getMargin();
            } else {
                margin = mg;
            }
            params.setMargins(margin, 0, 0, 0);
            item_org_wrapper.setLayoutParams(params);

            folderIcon.setImageResource(treeUserDTO.isFlag() ? R.drawable.ic_folder_open : R.drawable.ic_folder_close);
            String nameString = treeUserDTO.getDepartName();
            String namePosition = "";
            try {
                namePosition = treeUserDTO.getPositionName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (treeUserDTO.getType() == 2) {
                String url = new Prefs().getServerSite() + treeUserDTO.getUrlAvatar();

                avatar.setImageResource(R.drawable.avatar_l);
                ImageLoader.getInstance().displayImage(url, avatar, Statics.options2);

                position.setVisibility(View.VISIBLE);
                position.setText(namePosition);
                folderIcon.setVisibility(View.GONE);
                relAvatar.setVisibility(View.VISIBLE);
            } else {
                position.setVisibility(View.GONE);
                relAvatar.setVisibility(View.GONE);
                folderIcon.setVisibility(View.VISIBLE);
            }
            name.setText(nameString);
            item_org_wrapper.setOnClickListener(view -> {
                if (treeUserDTO.getType() != 2) {
                    if (treeUserDTO.isFlag()) {
                        Log.d(TAG, "collapse");
                        collapse(index, treeUserDTO);
                        treeUserDTO.setFlag(false);
                    } else {
                        Log.d(TAG, "expand");
                        boolean flag = false;
                        if (index == list.size() - 1) {
                            flag = true;
                        }
                        expand(index, treeUserDTO, flag);
                        treeUserDTO.setFlag(true);
                    }
                } else {
                    boolean flag_2 = treeUserDTO.isCheck();
                    boolean flag = false;
                    if (flag_2) {
                        flag = false;
                        row_check.setChecked(false);
                        treeUserDTO.setIsCheck(false);
                    } else {
                        flag = true;
                        row_check.setChecked(true);
                        treeUserDTO.setIsCheck(true);
                    }

                    int LEVEL = treeUserDTO.getLevel();
                    int LEVEL_TEMP = treeUserDTO.getLevel();

                    if (flag) {
                        for (int i = 0; i < listTemp.size(); i++) {
                            PersonData obj = listTemp.get(i);

                            if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                    && treeUserDTO.getType() == obj.getType()
                                    && treeUserDTO.getSortNo() == obj.getSortNo()
                                    && treeUserDTO.getDepartName().equals(obj.getDepartName())) {

                                listTemp.get(i).setIsCheck(flag);

                                break;
                            }
                        }
                    } else {
                        list.get(index).setIsCheck(false);
                        for (int i = index; i >= 0; i--) {
                            PersonData obj = list.get(i);
                            if (LEVEL > obj.getLevel()) {
                                list.get(i).setIsCheck(false);
                                LEVEL = obj.getLevel();

                            }
                        }

                        int k = -10;
                        for (int i = 0; i < listTemp.size(); i++) {
                            PersonData obj = listTemp.get(i);
                            if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                    && treeUserDTO.getType() == obj.getType()
                                    && treeUserDTO.getSortNo() == obj.getSortNo()
                                    && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                                k = i;
                                listTemp.get(i).setIsCheck(false);
                                break;
                            }
                        }
                        if (k >= 0) {

                            for (int i = k; i >= 0; i--) {
                                PersonData obj = listTemp.get(i);
                                if (LEVEL_TEMP > obj.getLevel()) {

                                    listTemp.get(i).setIsCheck(false);
                                    LEVEL_TEMP = obj.getLevel();
                                }
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });
            row_check.setChecked(treeUserDTO.isCheck());
            row_check.setOnClickListener(view -> {
                Log.d(TAG, "row_check onClick");
                boolean flag = row_check.isChecked();
                treeUserDTO.setIsCheck(flag);
                int LEVEL = treeUserDTO.getLevel();
                int LEVEL_TEMP = treeUserDTO.getLevel();

                if (treeUserDTO.getType() == 2) {
                    if (flag) {
                        for (int i = 0; i < listTemp.size(); i++) {
                            PersonData obj = listTemp.get(i);

                            if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                    && treeUserDTO.getType() == obj.getType()
                                    && treeUserDTO.getSortNo() == obj.getSortNo()
                                    && treeUserDTO.getDepartName().equals(obj.getDepartName())) {

                                listTemp.get(i).setIsCheck(flag);

                                break;
                            }
                        }
                    } else {
                        list.get(index).setIsCheck(false);
                        for (int i = index; i >= 0; i--) {
                            PersonData obj = list.get(i);
                            if (LEVEL > obj.getLevel()) {
                                list.get(i).setIsCheck(false);
                                LEVEL = obj.getLevel();

                            }
                        }

                        int k = -10;
                        for (int i = 0; i < listTemp.size(); i++) {
                            PersonData obj = listTemp.get(i);
                            if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                    && treeUserDTO.getType() == obj.getType()
                                    && treeUserDTO.getSortNo() == obj.getSortNo()
                                    && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                                k = i;
                                listTemp.get(i).setIsCheck(false);
                                break;
                            }
                        }
                        if (k >= 0) {

                            for (int i = k; i >= 0; i--) {
                                PersonData obj = listTemp.get(i);
                                if (LEVEL_TEMP > obj.getLevel()) {

                                    listTemp.get(i).setIsCheck(false);
                                    LEVEL_TEMP = obj.getLevel();
                                }
                            }
                        }
                    }

                } else {
                    if (flag) {
                        int a = index + 1;
                        if (a < list.size()) {
                            for (int i = a; i < list.size(); i++) {
                                PersonData obj = list.get(i);
                                if (LEVEL < obj.getLevel()) {
                                    list.get(i).setIsCheck(true);
                                } else {
                                    break;
                                }
                            }
                        }
                        int temp = 0;
                        for (int i = 0; i < listTemp.size(); i++) {
                            PersonData obj = listTemp.get(i);
                            if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                    && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                    && treeUserDTO.getType() == obj.getType()
                                    && treeUserDTO.getSortNo() == obj.getSortNo()
                                    && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                                listTemp.get(i).setIsCheck(flag);
                                temp = i;
                                break;
                            }
                        }
                        int c = temp + 1;
                        if (c < listTemp.size()) {
                            for (int i = c; i < listTemp.size(); i++) {
                                PersonData obj = listTemp.get(i);
                                if (LEVEL < obj.getLevel()) {
                                    listTemp.get(i).setIsCheck(true);
                                } else {
                                    break;
                                }
                            }
                        }

                    } else {
                        if (LEVEL == 0) {
                            int a = index + 1;
                            if (a < list.size()) {
                                for (int i = a; i < list.size(); i++) {
                                    PersonData obj = list.get(i);
                                    if (LEVEL < obj.getLevel()) {
                                        list.get(i).setIsCheck(false);
                                    } else {
                                        break;
                                    }
                                }
                            }
                            int temp = 0;
                            for (int i = 0; i < listTemp.size(); i++) {
                                PersonData obj = listTemp.get(i);
                                if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                        && treeUserDTO.getType() == obj.getType()
                                        && treeUserDTO.getSortNo() == obj.getSortNo()
                                        && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                                    listTemp.get(i).setIsCheck(flag);
                                    temp = i;
                                    break;
                                }
                            }
                            int c = temp + 1;
                            if (c < listTemp.size()) {
                                for (int i = c; i < listTemp.size(); i++) {
                                    PersonData obj = listTemp.get(i);
                                    if (LEVEL < obj.getLevel()) {
                                        listTemp.get(i).setIsCheck(false);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        } else {

                            for (int i = index; i >= 0; i--) {
                                PersonData obj = list.get(i);
                                if (LEVEL > obj.getLevel()) {
                                    list.get(i).setIsCheck(false);
                                    LEVEL = obj.getLevel();
                                }
                            }

                            int temp = 0;
                            for (int i = 0; i < listTemp.size(); i++) {
                                PersonData obj = listTemp.get(i);
                                if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                        && treeUserDTO.getType() == obj.getType()
                                        && treeUserDTO.getSortNo() == obj.getSortNo()
                                        && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                                    listTemp.get(i).setIsCheck(flag);
                                    temp = i;
                                    break;
                                }
                            }
                            for (int i = temp; i >= 0; i--) {
                                PersonData obj = listTemp.get(i);
                                if (LEVEL > obj.getLevel()) {
                                    listTemp.get(i).setIsCheck(false);
                                    LEVEL = obj.getLevel();
                                }
                            }
                            int level = treeUserDTO.getLevel();
                            for (int i = temp + 1; i < listTemp.size(); i++) {
                                PersonData obj = listTemp.get(i);
                                if (level < obj.getLevel()) {
                                    listTemp.get(i).setIsCheck(false);
                                } else {
                                    break;
                                }
                            }
                            // for child list
                            if (index + 1 < list.size()) {
                                for (int i = index + 1; i < list.size(); i++) {
                                    PersonData obj = list.get(i);
                                    if (level < obj.getLevel()) {
                                        list.get(i).setIsCheck(false);
                                    } else {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                notifyDataSetChanged();
            });
        }
    }

    boolean isAdd(List<PersonData> lst, PersonData treeUserDTO) {
        return true;
    }


    void addList(PersonData obj, int margin, int level) {
        margin += Util.getDimenInPx(R.dimen.dimen_20_40);
        obj.setMargin(margin);

        level += 1;
        obj.setLevel(level);

        if (userNos != null) {
            for (int a : userNos) {
                if (a == obj.getDepartNo()) {
                    obj.setIsCheck(true);
                    break;
                }
            }
        }

        this.listTemp.add(obj);

        this.listTemp_2.add(obj);
        this.listTemp_3.add(obj);
        if (obj.getPersonList() != null) {
            if (obj.getPersonList().size() > 0) {
                boolean hasType2 = false;
                boolean hasType0 = false;

                for (PersonData dto : obj.getPersonList()) {
                    if (dto.getType() == 2) {
                        hasType2 = true;
                    }
                    if (dto.getType() == 0) {
                        hasType0 = true;
                    }
                }
                if (hasType2 && hasType0) {
                    Collections.sort(obj.getPersonList(), (r1, r2) -> r1.getSortNo() - r2.getSortNo());
                }
                for (PersonData dto1 : obj.getPersonList()) {
                    addList(dto1, margin, level);
                }
            }
        }
    }


    public void updateList(List<PersonData> list) {
        if (list != null && list.size() > 0) {
            Log.d(TAG, "start updateList");
            this.list.clear();
            final int tempMargin = Util.getDimenInPx(R.dimen.dimen_20_40) * -1;
            for (PersonData obj : list) {
                addList(obj, tempMargin, -1);
            }
            Log.d(TAG, "finish addListTemp");
            List<PersonData> lst = this.listTemp_2;
            for (int i = 0; i < lst.size(); i++) {
                PersonData obj = lst.get(i);
                int level = obj.getLevel();
                boolean flag = obj.isFlag();
                if (!flag) {
                    if (i + 1 < lst.size()) {
                        for (int j = i + 1; j < lst.size(); j++) {
                            PersonData nextObj = lst.get(j);
                            if (level < nextObj.getLevel()) {
                                lst.remove(j);
                                j--;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
            this.list = lst;

            this.notifyDataSetChanged();
            int k = 0;
            for (int i = 0; i < this.list.size(); i++) {
                if (this.list.get(i).getDepartmentParentNo() == myId) {
                    k = i;
                    break;
                }
            }
            if (instance != null)
                instance.scrollToEndList(k);

        }

    }

    public List<PersonData> getList() {
        return listTemp;
    }

    public AdapterOrganizationChartV2(Context context, List<PersonData> list, boolean mIsDisableSelected, NewOrganizationChart instance, ArrayList<Integer> userNos) {
        this.list = list;
        this.instance = instance;
        this.mg = Util.getDimenInPx(R.dimen.dimen_20_40) * 2;
        this.userNos = userNos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_organization_chart_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PersonData treeUserDTO = list.get(position);
        holder.handler(treeUserDTO, position);

    }

    void collapse(final int position, final PersonData treeUserDTO) {
        int levelCur = list.get(position).getLevel();
        Log.d(TAG, "levelCur:" + levelCur);
        int a = position + 1;
        if (a < list.size()) {
            for (int i = a; i < list.size(); i++) {
                PersonData obj = list.get(i);
                int level = obj.getLevel();
                if (levelCur < level) {
                    Log.d(TAG, "remove: " + obj.getDepartName());
                    list.remove(i);
                    i--;
                } else {
                    break;
                }
            }
            notifyDataSetChanged();
        }


    }

    private void expand(int position, PersonData treeUserDTO, boolean flag) {
        int levelCur = treeUserDTO.getLevel();
        int index = position + 1;
        // get index of list
        int indexListTemp = 0;
        for (int i = 0; i < listTemp.size(); i++) {
            PersonData obj = listTemp.get(i);
            if (obj.getType() != 2) {
                if (treeUserDTO.getDepartNo() == obj.getDepartNo()
                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                        && treeUserDTO.getType() == obj.getType()
                        && treeUserDTO.getSortNo() == obj.getSortNo()
                        && treeUserDTO.getDepartName().equals(obj.getDepartName())) {
                    indexListTemp = i;
                    break;
                }
            }
        }

        int a = indexListTemp + 1;
        if (a < listTemp.size()) {
            for (int i = a; i < listTemp.size(); i++) {
                PersonData object = listTemp.get(i);
                if (levelCur < object.getLevel()) {
                    object.setFlag(true);
                    if (isAdd(this.list, object)) {
                        list.add(index, object);
                        index++;
                    }
                } else {
                    break;
                }
            }
        }

        notifyDataSetChanged();
        if (flag) {
            if (instance != null)
                instance.scrollToEndList(position + 1);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
