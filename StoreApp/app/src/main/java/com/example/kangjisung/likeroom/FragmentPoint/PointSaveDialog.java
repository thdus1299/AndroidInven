package com.example.kangjisung.likeroom.FragmentPoint;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kangjisung.likeroom.MemberListItem;
import com.example.kangjisung.likeroom.R;
import com.example.kangjisung.likeroom.Util.Utility;

public class PointSaveDialog extends Dialog {
    private TextView mTextViewValue;
    private TextView mTextViewGuide;
    private int layoutInputBoxSize = -1;
    private int maxLength = 8;
    private double pointRate = 0.05;

    private MemberListItem modifyItem;

    private String strValue = "";
    private int value = 0;

    PointSaveDialog(Context context, MemberListItem object) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.modifyItem = object;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.point_save_dialog);

        TextView mTextViewName = (TextView) findViewById(R.id.tv_name);
        TextView mTextViewPhone = (TextView) findViewById(R.id.tv_phone);
        mTextViewGuide = (TextView) findViewById(R.id.tv_guide);
        mTextViewValue = (TextView) findViewById(R.id.tv_value);
        mTextViewName.setText(modifyItem.getName());
        mTextViewPhone.setText(Utility.convertPhoneNumber(modifyItem.getPhone()));

        ((Button) findViewById(R.id.btn_1)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_2)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_3)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_4)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_5)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_6)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_7)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_8)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_9)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_0)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_cor)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.btn_del)).setOnClickListener(onButtonNumberClickListener);
        ((Button) findViewById(R.id.button_ok)).setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View onClickView) {
                // TODO : 완료 버튼 동작 삽입
            }
        });

        initializeDialogTitleBar();
        setPoint();
        /*
        final RelativeLayout layoutInputBox = (RelativeLayout)findViewById(R.id.layout_inputbox);

        mBackIdentifyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playInputBoxAnimation(0, layoutInputBoxSize);

                mBackButton.setOnClickListener(mBackClickListener);
                mOKButton.setOnClickListener(mOKClickListener);
            }
        };
        mOKClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playInputBoxAnimation(layoutInputBoxSize, 0);

                mBackButton.setOnClickListener(mBackIdentifyClickListener);
                mOKButton.setOnClickListener(mOKIdentifyClickListener);
            }
        };
        mBackButton.setOnClickListener(mBackClickListener);
        mOKButton.setOnClickListener(mOKClickListener);
        */
    }

    public Button.OnClickListener onButtonNumberClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View onClickView) {
            if(strValue.length() < maxLength || onClickView.getId() == R.id.btn_del || onClickView.getId() == R.id.btn_cor) {
                switch (onClickView.getId()) {
                    case R.id.btn_1:
                        strValue = strValue + "1";
                        break;
                    case R.id.btn_2:
                        strValue = strValue + "2";
                        break;
                    case R.id.btn_3:
                        strValue = strValue + "3";
                        break;
                    case R.id.btn_4:
                        strValue = strValue + "4";
                        break;
                    case R.id.btn_5:
                        strValue = strValue + "5";
                        break;
                    case R.id.btn_6:
                        strValue = strValue + "6";
                        break;
                    case R.id.btn_7:
                        strValue = strValue + "7";
                        break;
                    case R.id.btn_8:
                        strValue = strValue + "8";
                        break;
                    case R.id.btn_9:
                        strValue = strValue + "9";
                        break;
                    case R.id.btn_0:
                        if(strValue.length() > 0){
                            strValue = strValue + "0";
                        }
                        break;
                    case R.id.btn_cor:
                        strValue = "";
                        break;
                    case R.id.btn_del:
                        if (strValue.length() > 0) {
                            strValue = strValue.substring(0, strValue.length() - 1);
                        }
                        break;
                }
                if(strValue.length() > 0) {
                    value = Integer.parseInt(strValue);
                    mTextViewValue.setText(String.format("%,d", value));
                }
                else{
                    value = 0;
                    mTextViewValue.setText("0");
                }
            }
           setPoint();
        }
    };

    private void setPoint()
    {
        int nowPoint = 0;
        if(modifyItem.getPoint() != "" && modifyItem.getPoint() != null){
            nowPoint = Integer.parseInt(modifyItem.getPoint());
        }
        mTextViewGuide.setText(String.format("%,dP 적립 : %,d → %,d", (int)(value * pointRate), nowPoint, nowPoint + (int)(value * pointRate)));
    }

    private void initializeDialogTitleBar()
    {
        TextView mTextViewTitle = (TextView)findViewById(R.id.textView_title);
        Button mBackButton = (Button)findViewById(R.id.button_dialog_back);
        Button mOKButton = (Button)findViewById(R.id.button_dialog_ok);
        RelativeLayout layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        RelativeLayout layoutOk = (RelativeLayout) findViewById(R.id.layout_ok);
        layoutOk.setVisibility(View.GONE);

        mTextViewTitle.setText("포인트 적립");
        mBackButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View onClickView){
                cancel();
            }
        });
    }

    private void playInputBoxAnimation(int start, int end) {
        final RelativeLayout layoutInputBox = (RelativeLayout)findViewById(R.id.layout_inputbox);
        if(layoutInputBoxSize == -1){
            layoutInputBoxSize = start = layoutInputBox.getHeight();
        }

        ValueAnimator slideAnimator = ValueAnimator.ofInt(start, end).setDuration(200);
        slideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer)animation.getAnimatedValue();
                layoutInputBox.getLayoutParams().height = value.intValue();
                layoutInputBox.requestLayout();
            }
        });

        AnimatorSet set = new AnimatorSet();
        set.play(slideAnimator);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }
}