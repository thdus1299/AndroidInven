package com.teamdk.android.bakery.fragments.user;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.teamdk.android.bakery.fragments.user.adapter.UserMainListAdapter;
import com.teamdk.android.bakery.objectmanager.MemberListItem;
import com.teamdk.android.bakery.objectmanager.MemberObjectManager;
import com.teamdk.android.bakery.utility.ColorTheme;
import com.teamdk.android.bakery.R;
import com.teamdk.android.bakery.utility.LayoutManager;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.teamdk.android.bakery.utility.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class UserMain extends Fragment
{
    private View fragmentView;
    private RecyclerView userRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private UserMainListAdapter mAdapter;

    private CheckBox checkBoxStampAll;
    private FloatingActionMenu famMenu;
    private FloatingActionButton fabStampOk;
    private FloatingActionButton fabStampCancel;
    private LinearLayout layoutStamp;

    private UserEditDialog userEditDialog;
    private UserStampDialog userStampDialog;
    private DrawerLayout mDrawerLayout;

    private int sortStateId;
    private String sortStateOrder;
    private boolean stampSendMode = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.user_main, container, false);

        mAdapter = new UserMainListAdapter();
        mLayoutManager = new LinearLayoutManager(getContext());

        userRecyclerView = (RecyclerView) fragmentView.findViewById((R.id.recyclerView));
        userRecyclerView.setAdapter(mAdapter);
        userRecyclerView.setLayoutManager(mLayoutManager);

        setTextViewSearchResult(false);
        registerForContextMenu(userRecyclerView);

        SharedPreferenceManager mSharedPreferenceManager = new SharedPreferenceManager();

        View layoutFloating1 = fragmentView.findViewById(R.id.layout_floating1);
        View layoutFloating2 = fragmentView.findViewById(R.id.layout_floating2);
        if(mSharedPreferenceManager.getInt("set_menu", getContext()) == 0) {
            layoutFloating1.findViewById(R.id.fab_notice).setOnClickListener(onFabClickListener);
            layoutFloating2.setVisibility(View.GONE);
            famMenu = (FloatingActionMenu) layoutFloating1.findViewById(R.id.fam_menu);
        }
        else{
            layoutFloating2.findViewById(R.id.fab_notice).setOnClickListener(onFabClickListener);
            layoutFloating1.setVisibility(View.GONE);
        }

        RelativeLayout layoutSortByName = (RelativeLayout) fragmentView.findViewById(R.id.layout_sort_by_name);
        RelativeLayout layoutSortByPhone = (RelativeLayout) fragmentView.findViewById(R.id.layout_sort_by_phone);
        RelativeLayout layoutSortByPoint = (RelativeLayout) fragmentView.findViewById(R.id.layout_sort_by_point);

        mDrawerLayout = (DrawerLayout) fragmentView.findViewById(R.id.layout_drawer);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        sortStateId = R.id.layout_sort_by_name;
        setSortOn(layoutSortByName, "ASC");
        setSortWhite(layoutSortByPhone);
        setSortWhite(layoutSortByPoint);

        layoutSortByName.findViewById(R.id.button).setOnClickListener(onButtonSortClickListener);
        layoutSortByPhone.findViewById(R.id.button).setOnClickListener(onButtonSortClickListener);
        layoutSortByPoint.findViewById(R.id.button).setOnClickListener(onButtonSortClickListener);
        ((TextView)layoutSortByName.findViewById(R.id.view_text)).setText(R.string.user_sort_name);
        ((TextView)layoutSortByPhone.findViewById(R.id.view_text)).setText(R.string.user_sort_phone);
        ((TextView)layoutSortByPoint.findViewById(R.id.view_text)).setText(R.string.user_sort_point);

        famMenu = (FloatingActionMenu) fragmentView.findViewById(R.id.menu);
        fragmentView.findViewById(R.id.fab_add_user).setOnClickListener(onFabClickListener);
        fragmentView.findViewById(R.id.fab_stamp).setOnClickListener(onFabClickListener);
        fragmentView.findViewById(R.id.fab_notice).setOnClickListener(onFabClickListener);
        fabStampOk = (FloatingActionButton) fragmentView.findViewById(R.id.fab_stamp_ok);
        fabStampOk.setVisibility(View.INVISIBLE);
        fabStampOk.setEnabled(false);
        fabStampCancel = (FloatingActionButton) fragmentView.findViewById(R.id.fab_stamp_cancel);
        fabStampCancel.setVisibility(View.INVISIBLE);

        checkBoxStampAll = (CheckBox) fragmentView.findViewById(R.id.checkBoxStampAll);
        checkBoxStampAll.setVisibility(View.GONE);
        checkBoxStampAll.setOnClickListener(new CompoundButton.OnClickListener(){
            @Override
            public void onClick(View onClickView){
                mAdapter.setCheckAll(checkBoxStampAll.isChecked());
                mAdapter.setTextViewSearchResult((TextView) fragmentView.findViewById(R.id.textView_search_result));
            }
        });

        fabStampCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                famMenu.close(true);
                checkBoxStampAll.setVisibility(View.GONE);
                fabStampCancel.setVisibility(View.INVISIBLE);
                fabStampOk.setVisibility(View.INVISIBLE);
                famMenu.setVisibility(View.VISIBLE);
                mAdapter.updateCheckboxState(false);
                setTextViewSearchResult(false);
            }
        });

        fabStampOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                userStampDialog = new UserStampDialog(mAdapter.getListItemToStampDialog(), getActivity());
                userStampDialog.show();
                famMenu.close(true);
                checkBoxStampAll.setVisibility(View.GONE);
                fabStampCancel.setVisibility(View.INVISIBLE);
                fabStampOk.setVisibility(View.INVISIBLE);
                famMenu.setVisibility(View.VISIBLE);
                mAdapter.updateCheckboxState(false);
                setTextViewSearchResult(false);
            }
        });

        EditText editTextSearch = (EditText) fragmentView.findViewById(R.id.editText_search);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
            }
        });

        return fragmentView;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case LayoutManager.MENU_USER_MODIFY:
                // TODO : 수정을 눌렀을 경우
                MemberListItem userItem = mAdapter.getLongClickPosition();
                userEditDialog = new UserEditDialog(getContext(), userItem);
                userEditDialog.show();
                userEditDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(((UserEditDialog)dialog).getDismissMessage() == 1) {
                            MemberObjectManager.load(getContext());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case LayoutManager.MENU_USER_DELETE:
                // TODO : 삭제를 눌렀을 경우
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void setTextViewSearchResult(boolean StampMode)
    {
        TextView textViewSearchResult = (TextView) fragmentView.findViewById(R.id.textView_search_result);
        if(StampMode == false){
            textViewSearchResult.setText("등록된 회원 수 : " + mAdapter.getItemCount());
        }
        else{
            textViewSearchResult.setText(mAdapter.getItemCount() + "명 중 0명 선택됨");
        }
    }

    private void setSortWhite(RelativeLayout layout) {
        layout.findViewById(R.id.view_body_white).setVisibility(View.VISIBLE);
        layout.findViewById(R.id.view_body_color).setVisibility(View.INVISIBLE);
        layout.findViewById(R.id.view_asc).setVisibility(View.GONE);
        layout.findViewById(R.id.view_desc).setVisibility(View.GONE);
        TextView textSort = (TextView)layout.findViewById(R.id.view_text);
        textSort.setTextColor(ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_D3));
    }

    private void setSortOn(RelativeLayout layout, String state) {
        layout.findViewById(R.id.view_body_white).setVisibility(View.INVISIBLE);
        layout.findViewById(R.id.view_body_color).setVisibility(View.VISIBLE);
        if (state == "ASC") {
            layout.findViewById(R.id.view_asc).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.view_desc).setVisibility(View.GONE);
        } else {
            layout.findViewById(R.id.view_asc).setVisibility(View.GONE);
            layout.findViewById(R.id.view_desc).setVisibility(View.VISIBLE);
        }
        sortStateOrder = state;
        TextView textSort = (TextView)layout.findViewById(R.id.view_text);
        textSort.setTextColor(getResources().getColor(R.color.white));
    }

    private Button.OnClickListener onButtonSortClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View onClickView) {
            RelativeLayout includingLayout = (RelativeLayout)onClickView.getParent().getParent();
            if (includingLayout != null) {
                if (sortStateId != includingLayout.getId()) {
                    setSortOn(includingLayout, "ASC");
                    setSortWhite((RelativeLayout) fragmentView.findViewById(sortStateId));
                    sortStateId = includingLayout.getId();
                } else {
                    RelativeLayout parentLayout = (RelativeLayout)onClickView.getParent();
                    if (parentLayout.findViewById(R.id.view_asc).getVisibility() == View.GONE) {
                        setSortOn(includingLayout, "ASC");
                    } else {
                        setSortOn(includingLayout, "DESC");
                    }
                }
                switch (sortStateId) {
                    default:
                    case R.id.layout_sort_by_name:
                        mAdapter.setSortOption("NAME", sortStateOrder);
                        break;
                    case R.id.layout_sort_by_phone:
                        mAdapter.setSortOption("PHONE", sortStateOrder);
                        break;
                    case R.id.layout_sort_by_point:
                        mAdapter.setSortOption("POINT", sortStateOrder);
                        break;
                }
            }
        }
    };

    private View.OnClickListener onFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                case R.id.fab_add_user:
                    famMenu.close(true);
                    userEditDialog = new UserEditDialog(getContext());
                    userEditDialog.show();
                    userEditDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            MemberObjectManager.load(getContext());
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    break;
                case R.id.fab_stamp:
                    checkBoxStampAll.setVisibility(View.VISIBLE);
                    fabStampCancel.setVisibility(View.VISIBLE);
                    fabStampOk.setVisibility(View.VISIBLE);
                    famMenu.setVisibility(View.INVISIBLE);
                    mAdapter.updateCheckboxState(true);
                    setTextViewSearchResult(true);
                    break;
                case R.id.fab_notice:
                    famMenu.close(true);
                    UserNoticeMain fragmentNotice = new UserNoticeMain();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.user_main_layout, fragmentNotice);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
            }
        }
    };
}