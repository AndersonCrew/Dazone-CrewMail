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

import com.dazone.crewemail.DaZoneApplication;
import com.dazone.crewemail.R;
import com.dazone.crewemail.data.PersonData;
import com.dazone.crewemail.fragments.OrganizationFragment;
import com.dazone.crewemail.interfaces.OnOrganizationSelectedEvent;
import com.dazone.crewemail.utils.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maidinh on 6/1/2017.
 */

public class AdapterOrganizationChart extends RecyclerView.Adapter<AdapterOrganizationChart.MyViewHolder> {
    OnOrganizationSelectedEvent mSelectedEvent;
    private String TAG = "sss>>OrganizationChart";
    private List<PersonData> list;
    private List<PersonData> listTemp = new ArrayList<>();
    private List<PersonData> listTemp_2 = new ArrayList<>();
    private ArrayList<PersonData> selectedPersonList;
    private int isSearch = 0; // 0 -> normal : 1 -> search
    private Context context;
    private OrganizationFragment instance;
    private int mg;

    public AdapterOrganizationChart(Context context, List<PersonData> list, OrganizationFragment instance,
                                    ArrayList<PersonData> userNos) {
        this.context = context;
        this.list = list;
        this.instance = instance;
        this.mg = Util.getDimenInPx(R.dimen.dimen_20_40) * 2;
        this.selectedPersonList = userNos;
    }

    public void setCheckLayout(PersonData treeUserDTO, CheckBox row_check, boolean flag_2, int index) {
        boolean flag;
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

                if (treeUserDTO.getUserNo() == obj.getUserNo()
                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                        && treeUserDTO.getType() == obj.getType()
                        && treeUserDTO.getSortNo() == obj.getSortNo()
                        && treeUserDTO.getFullName().equals(obj.getFullName())) {

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
                if (treeUserDTO.getUserNo() == obj.getUserNo()
                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                        && treeUserDTO.getType() == obj.getType()
                        && treeUserDTO.getSortNo() == obj.getSortNo()
                        && treeUserDTO.getFullName().equals(obj.getFullName())) {
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

    public void setCheckBox(PersonData treeUserDTO, boolean flag, int index) {
        treeUserDTO.setIsCheck(flag);
        int LEVEL = treeUserDTO.getLevel();
        int LEVEL_TEMP = treeUserDTO.getLevel();

        if (treeUserDTO.getType() == 2) {
            if (flag) {
                for (int i = 0; i < listTemp.size(); i++) {
                    PersonData obj = listTemp.get(i);

                    if (treeUserDTO.getUserNo() == obj.getUserNo()
                            && treeUserDTO.getDepartNo() == obj.getDepartNo()
                            && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                            && treeUserDTO.getType() == obj.getType()
                            && treeUserDTO.getSortNo() == obj.getSortNo()
                            && treeUserDTO.getFullName().equals(obj.getFullName())) {

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
                    if (treeUserDTO.getUserNo() == obj.getUserNo()
                            && treeUserDTO.getDepartNo() == obj.getDepartNo()
                            && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                            && treeUserDTO.getType() == obj.getType()
                            && treeUserDTO.getSortNo() == obj.getSortNo()
                            && treeUserDTO.getFullName().equals(obj.getFullName())) {
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
                    if (treeUserDTO.getUserNo() == obj.getUserNo()
                            && treeUserDTO.getDepartNo() == obj.getDepartNo()
                            && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                            && treeUserDTO.getType() == obj.getType()
                            && treeUserDTO.getSortNo() == obj.getSortNo()
                            && treeUserDTO.getFullName().equals(obj.getFullName())) {
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
                        if (treeUserDTO.getUserNo() == obj.getUserNo()
                                && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                && treeUserDTO.getType() == obj.getType()
                                && treeUserDTO.getSortNo() == obj.getSortNo()
                                && treeUserDTO.getFullName().equals(obj.getFullName())) {
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
                        if (treeUserDTO.getUserNo() == obj.getUserNo()
                                && treeUserDTO.getDepartNo() == obj.getDepartNo()
                                && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                                && treeUserDTO.getType() == obj.getType()
                                && treeUserDTO.getSortNo() == obj.getSortNo()
                                && treeUserDTO.getFullName().equals(obj.getFullName())) {
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

    }


    boolean isAdd(List<PersonData> lst, PersonData treeUserDTO) {
        return true;
    }

    void addList(PersonData obj, int margin, int level) {
        margin += Util.getDimenInPx(R.dimen.dimen_20_40);
        obj.setMargin(margin);
        level += 1;
        obj.setLevel(level);

        int DepartNo = obj.getDepartNo();
        int UserNo = obj.getUserNo();
        int ParentNo = obj.getDepartmentParentNo();


        obj.setIsCheck(false);
        if (selectedPersonList != null) {
            for (PersonData a : selectedPersonList) {
                if (a.getUserNo() == UserNo && a.getDepartNo() == DepartNo && a.getDepartmentParentNo() == ParentNo) {
                    obj.setIsCheck(true);
                    break;
                }
            }
        }
        obj.setFlag(true);

        if (isAdd(listTemp, obj)) {
            this.listTemp.add(obj);
            this.listTemp_2.add(obj);
        }


        if (obj.getPersonList() != null && obj.getPersonList().size() != 0) {
            for (PersonData dto1 : obj.getPersonList()) {
                addList(dto1, margin, level);
            }
        }
    }

    public void updateList(List<PersonData> list,List<PersonData> mselectlisted) {
        if (list != null && list.size() > 0) {
            final int tempMargin = Util.getDimenInPx(R.dimen.dimen_20_40) * -1;
            for (PersonData obj : list) {
                addList(obj, tempMargin, -1);
            }
            this.list = this.listTemp_2;
            if(mselectlisted!=null&& mselectlisted.size()>0) {
                for (PersonData p : this.list) {
                    for (PersonData m : mselectlisted) {
                        if (p.getUserID()!=null&&p.getUserID().equals(m.getUserID())) {
                            p.setIsCheck(true);
                        }
                    }
                }
            }
            this.notifyDataSetChanged();
        }

    }

    public List<PersonData> getList() {
        return listTemp;
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
        int a = position + 1;
        if (a < list.size()) {
            for (int i = a; i < list.size(); i++) {
                PersonData obj = list.get(i);
                int level = obj.getLevel();
                if (levelCur < level) {
                    list.remove(i);
                    i--;
                } else {
                    break;
                }
            }
            notifyDataSetChanged();
        }


    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private void expand(int position, PersonData treeUserDTO, boolean flag) {
        int levelCur = treeUserDTO.getLevel();
        int index = position + 1;
        int indexListTemp = 0;
        for (int i = 0; i < listTemp.size(); i++) {
            PersonData obj = listTemp.get(i);
            if (obj.getType() != 2) {
                if (treeUserDTO.getUserNo() == obj.getUserNo()
                        && treeUserDTO.getDepartNo() == obj.getDepartNo()
                        && treeUserDTO.getDepartmentParentNo() == obj.getDepartmentParentNo()
                        && treeUserDTO.getType() == obj.getType()
                        && treeUserDTO.getSortNo() == obj.getSortNo()
                        && treeUserDTO.getFullName().equals(obj.getFullName())) {
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
            Log.d(TAG, "scrollToEndList");
            if (instance != null)
                instance.scrollToEndList(position + 1);

        }
    }

    public void setOnSelectedEvent(OnOrganizationSelectedEvent selectedEvent) {
        this.mSelectedEvent = selectedEvent;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


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
            String nameString = treeUserDTO.getFullName();
            String namePosition = "";
            try {
                namePosition = treeUserDTO.getPositionName();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (treeUserDTO.getType() == 2) {
                String url = DaZoneApplication.getInstance().getPrefs().getServerSite() + treeUserDTO.getUrlAvatar();
                Picasso.with(context).load(url)
                        .error(R.drawable.avatar_l)
                        .placeholder(R.drawable.avatar_l)
                        .into(avatar);
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
                    setCheckLayout(treeUserDTO, row_check, flag_2, index);
                }
            });


            row_check.setChecked(treeUserDTO.isCheck());
            row_check.setOnClickListener(view -> {
                boolean flag = row_check.isChecked();
                setCheckBox(treeUserDTO, flag, index);
                if (mSelectedEvent != null) {
                    mSelectedEvent.onOrganizationCheck(flag, treeUserDTO);
                }
            });
        }
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


}