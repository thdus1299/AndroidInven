package com.example.kangjisung.likeroom.FragmentProduct;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.AppCompatImageView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.kangjisung.likeroom.FragmentProduct.ListView.ProductListItem;
import com.example.kangjisung.likeroom.R;
import com.example.kangjisung.likeroom.SQLiteDatabaseControl.ClientDataBase;
import com.example.kangjisung.likeroom.Util.ColorTheme;
import com.example.kangjisung.likeroom.Util.NoScrollViewPager;
import com.example.kangjisung.likeroom.Util.FirstPageFragmentListener;
import com.example.kangjisung.likeroom.Util.Utility;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class ProductMain extends Fragment {
    private Button btnSellToday;
    private Button btnMuchStore;
    public static NoScrollViewPager noScrollViewPager;
    private TextView tvFragmentItemMain;
    private TextView tvFragmentItemMainDate;
    static public FirstPageFragmentListener firstPageListener;
    private ProductEditDialog productAddDialog;
    private Button buttonSelectDate;
    private FloatingActionMenu famMenu;
    private View rootView;
    private Calendar nowDate;
    private Calendar selectedDate;
    private TabLayout tabLayout;

    // TODO : 정렬 초기값 결정
    private int tabPosition = 1;
    private int sortStateImageView = R.id.iv_sort_name;
    private int sortStateTextView = R.id.tv_sort_name;

    public ProductMain() {
    }
    public ProductMain(FirstPageFragmentListener listener) {
        firstPageListener=listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.product_main, container, false);

        ///////init///////
        btnSellToday=(Button)rootView.findViewById(R.id.btn_fragment_item_main_sell_today);
        btnMuchStore=(Button)rootView.findViewById(R.id.btn_fragment_item_main_store_match);
        btnSellToday.setOnClickListener(onClickSelectButton);
        btnMuchStore.setOnClickListener(onClickSelectButton);

        noScrollViewPager=(NoScrollViewPager) rootView.findViewById(R.id.vp_fragment_item_main);
        tvFragmentItemMain=(TextView) rootView.findViewById(R.id.tv_fragment_item_main);
        tvFragmentItemMainDate=(TextView) rootView.findViewById(R.id.tv_fragment_item_main_date);

        final ProductMainPagerAdapter adapter = new ProductMainPagerAdapter(getFragmentManager());
        noScrollViewPager.setAdapter(adapter);
        noScrollViewPager.setPagingDisabled();
        noScrollViewPager.setOffscreenPageLimit(3);
        btnMuchStore.setOnClickListener(onClickSelectButton);
        btnSellToday.setOnClickListener(onClickSelectButton);
        //////////////////
        changeSelection(0);

        nowDate = Calendar.getInstance();
        tvFragmentItemMain.setText("일일판매량");
        tvFragmentItemMainDate.setText((new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)).format(nowDate.getTime()));
        buttonSelectDate = (Button) rootView.findViewById(R.id.button_select_date);
        buttonSelectDate.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View onClickView){
                Context contextThemeWrapper = new ContextThemeWrapper(getContext(), ColorTheme.getTheme());
                LayoutInflater localInflater = getActivity().getLayoutInflater().cloneInContext(contextThemeWrapper);
                View view = localInflater.inflate(R.layout.alert_calendar, null);
                DatePicker mDatePicker = (DatePicker) view.findViewById(R.id.calendarView);
                selectedDate = nowDate;

                int year = selectedDate.get(Calendar.YEAR);
                int month = selectedDate.get(Calendar.MONTH);
                int day = selectedDate.get(Calendar.DAY_OF_MONTH);

                mDatePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedDate = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nowDate = selectedDate;
                        tvFragmentItemMainDate.setText((new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)).format(nowDate.getTime()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                /*
                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.alert_calendar, null);
                CalendarView calendarView = (CalendarView)view.findViewById(R.id.calendarView);
                selectDate = nowDate;
                long nowDateToLong = selectDate.getTimeInMillis();
                calendarView.setDate(nowDateToLong, true, true);
                calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                        selectDate = new GregorianCalendar( year, month, dayOfMonth );
                    }
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(view);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nowDate = selectDate;
                        tvFragmentItemMainDate.setText((new SimpleDateFormat("yyyy년 M월 d일", Locale.KOREA)).format(nowDate.getTime()));
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                //dialog.setTitle(ProductObjManager.get(position).getName());
                //dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                dialog.show();
                */
            }
        });

        rootView.findViewById(R.id.fab_add).setOnClickListener(onFabClickListener);
        rootView.findViewById(R.id.fab_sort).setOnClickListener(onFabClickListener);

        DrawerLayout layoutDrawer = (DrawerLayout) rootView.findViewById(R.id.layout_product_drawer);
        layoutDrawer.setScrimColor(ContextCompat.getColor(getContext(), R.color.alpha80));
        Button mButtonSortName = (Button) layoutDrawer.findViewById(R.id.btn_sort_name);
        Button mButtonSortAdd = (Button) layoutDrawer.findViewById(R.id.btn_add);
        Button mButtonSortModify = (Button) layoutDrawer.findViewById(R.id.btn_modify);
        mButtonSortName.setOnClickListener(onButtonSortClickListener);
        mButtonSortAdd.setOnClickListener(onButtonSortClickListener);
        mButtonSortModify.setOnClickListener(onButtonSortClickListener);

        tabLayout = (TabLayout) layoutDrawer.findViewById(R.id.tabLayout);
        TabLayout.Tab tab = tabLayout.getTabAt(tabPosition);
        tab.select();
        tabLayout.setTabTextColors(ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_D2), ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_D4));
        onSortItem();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                onSortItem();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return rootView;
    }

    public static int longClickPosition;
    private ProductEditDialog mProductEditDialog;
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        switch(item.getItemId()){
            case Utility.MENU_PRODUCT_MODIFY:
                ProductListItem mProductListItem = ProductObjManager.get(longClickPosition);
                mProductEditDialog = new ProductEditDialog(getContext(), mProductListItem);
                mProductEditDialog.show();
                mProductEditDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(((ProductEditDialog)dialog).getDismissMessage() == 1) {
                            // TODO : 여기서 리스트 갱신
                        }
                    }
                });
                break;
            case Utility.MENU_PRODUCT_DELETE:
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                dialogBuilder.setMessage("선택한 항목을 삭제하시겠습니까?");
                dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO : 삭제 쿼리
                        // TODO : 여기서 리스트 갱신
                    }
                });
                dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog mAlertDialog = dialogBuilder.create();
                mAlertDialog.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    public Button.OnClickListener onClickSelectButton = new Button.OnClickListener() {
        @Override
        public void onClick(View onClickView) {
            switch(onClickView.getId()){
                default:
                case R.id.btn_fragment_item_main_sell_today:
                    noScrollViewPager.setCurrentItem(0, false);
                    tvFragmentItemMain.setText("일일판매량");
                    changeSelection(0);
                    /*  여기서 날짜 갱신
                    tvFragmentItemMainDate.setText("");
                    */
                    break;
                case R.id.btn_fragment_item_main_store_match:
                    noScrollViewPager.setCurrentItem(1, false);
                    tvFragmentItemMain.setText("최적재고량");
                    changeSelection(1);

                    /*    여기서 날짜 갱신
                    tvFragmentItemMainDate.setText("");
                    */
                    break;
            }
        }
    };

    public void changeSelection(int selectSwitch)
    {
        AppCompatImageView acivSelectIcon, acivSelectDot, acivUnselectIcon, acivUnselectDot;

        switch(selectSwitch){
            default:
            case 0:
                acivSelectIcon = (AppCompatImageView)rootView.findViewById(R.id.aciv_sell_today_icon);
                acivSelectDot = (AppCompatImageView)rootView.findViewById(R.id.aciv_sell_today_dot);
                acivUnselectIcon = (AppCompatImageView)rootView.findViewById(R.id.aciv_much_store_icon);
                acivUnselectDot = (AppCompatImageView)rootView.findViewById(R.id.aciv_much_store_dot);
                break;
            case 1:
                acivSelectIcon = (AppCompatImageView)rootView.findViewById(R.id.aciv_much_store_icon);
                acivSelectDot = (AppCompatImageView)rootView.findViewById(R.id.aciv_much_store_dot);
                acivUnselectIcon = (AppCompatImageView)rootView.findViewById(R.id.aciv_sell_today_icon);
                acivUnselectDot = (AppCompatImageView)rootView.findViewById(R.id.aciv_sell_today_dot);
                break;
        }

        acivSelectIcon.getBackground().setColorFilter(ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_D3), PorterDuff.Mode.SRC_IN);
        acivSelectDot.getBackground().setColorFilter(ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_D3), PorterDuff.Mode.SRC_IN);
        acivUnselectIcon.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.alpha40), PorterDuff.Mode.SRC_IN);
        acivUnselectDot.getBackground().setColorFilter(ContextCompat.getColor(getContext(), R.color.transparent), PorterDuff.Mode.SRC_IN);
    }

    public static ProductMain createInstance(FirstPageFragmentListener listener){
        ProductMain fragmentItemMain=new ProductMain();
        fragmentItemMain.firstPageListener=listener;
        return fragmentItemMain;
    }

    private Button.OnClickListener onButtonSortClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View onClickView){
            rootView.findViewById(sortStateImageView).setVisibility(View.INVISIBLE);
            ((TextView) rootView.findViewById(sortStateTextView)).setTextColor(ContextCompat.getColor(getContext(), R.color.gray80));
            switch(onClickView.getId()){
                case R.id.btn_sort_name:
                    sortStateImageView = R.id.iv_sort_name;
                    sortStateTextView = R.id.tv_sort_name;
                    onSortItem();
                    break;
                case R.id.btn_add:
                    sortStateImageView = R.id.iv_sort_add;
                    sortStateTextView = R.id.tv_sort_add;
                    onSortItem();
                    break;
                case R.id.btn_modify:
                    sortStateImageView = R.id.iv_sort_modify;
                    sortStateTextView = R.id.tv_sort_modify;
                    onSortItem();
                    break;
                default:
                    break;
            }
        }
    };

    private void onSortItem()
    {
        switch(sortStateImageView){
            case R.id.iv_sort_name:
                if(tabPosition == 0){
                    ProductObjManager.sortByNameAsc();
                }
                else{
                    ProductObjManager.sortByNameDesc();
                }
                break;
            case R.id.iv_sort_add:
                // TODO ; 여기에 정렬 추가
                break;
            case R.id.iv_sort_modify:
                // TODO : 여기에 정렬 추가
                break;
        }
        rootView.findViewById(sortStateImageView).setVisibility(View.VISIBLE);
        ((TextView) rootView.findViewById(sortStateTextView)).setTextColor(ColorTheme.getThemeColorRGB(getContext(), R.attr.theme_color_type1));
    }

    private View.OnClickListener onFabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                default:
                case R.id.fab_add:
                    productAddDialog = new ProductEditDialog(getContext());
                    productAddDialog.show();
                    productAddDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            // TODO : 새 제품 추가 다이얼로그에서 완료 버튼 클릭 시 동작 삽입
                        }
                    });
                    break;
                case R.id.fab_sort:
                    DrawerLayout drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.layout_product_drawer);
                    drawerLayout.openDrawer(Gravity.RIGHT);
                    break;
            }
        }
    };
}