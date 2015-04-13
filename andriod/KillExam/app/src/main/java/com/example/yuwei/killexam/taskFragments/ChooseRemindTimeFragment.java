package com.example.yuwei.killexam.taskFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.MyTime;

public class ChooseRemindTimeFragment extends Fragment
        implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener,NumberPicker.Formatter{

    private MainActivity mMainActivity;
    private View mView;
    public NumberPicker mBeginPickerHours;
    public NumberPicker mBeginPickerMinutes;

    public NumberPicker mEndPickerHours;
    public NumberPicker mEndPickerMinutes;

    public Button mButton;

    private MyTime mBeginRemindTime;
    private MyTime mEndRemindTime;


    public ChooseRemindTimeFragment(MainActivity mainActivity){
        mMainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_choose_remind_time, container, false);
        initPickers();
        initButton();

        mMainActivity.getSupportActionBar().setBackgroundDrawable(null);
        return mView;
    }

    private void initPickers(){
        mBeginRemindTime = new MyTime();
        mEndRemindTime = new MyTime();
        mBeginRemindTime.hours = 10;
        mEndRemindTime.hours = 10;
        MyDatabaseHelper.getRemindTime(mMainActivity, mBeginRemindTime, mEndRemindTime);

        mBeginPickerHours = (NumberPicker)mView.findViewById(R.id.beginRemindHours);
        mBeginPickerHours.setOnScrollListener(this);
        mBeginPickerHours.setFormatter(this);
        mBeginPickerHours.setMaxValue(23);
        mBeginPickerHours.setMinValue(0);
        mBeginPickerHours.setValue(mBeginRemindTime.hours);

        mBeginPickerMinutes = (NumberPicker)mView.findViewById(R.id.beginRemindMinutes);
        mBeginPickerMinutes.setOnScrollListener(this);
        mBeginPickerMinutes.setFormatter(this);
        mBeginPickerMinutes.setMaxValue(59);
        mBeginPickerMinutes.setMinValue(0);
        mBeginPickerMinutes.setValue(mBeginRemindTime.minutes);


        mEndPickerHours = (NumberPicker)mView.findViewById(R.id.endRemindHours);
        mEndPickerHours.setOnScrollListener(this);
        mEndPickerHours.setFormatter(this);
        mEndPickerHours.setMaxValue(23);
        mEndPickerHours.setMinValue(0);
        mEndPickerHours.setValue(mEndRemindTime.hours);

        mEndPickerMinutes = (NumberPicker)mView.findViewById(R.id.endRemindMinutes);
        mEndPickerMinutes.setOnScrollListener(this);
        mEndPickerMinutes.setFormatter(this);
        mEndPickerMinutes.setMaxValue(59);
        mEndPickerMinutes.setMinValue(0);
        mEndPickerMinutes.setValue(mBeginRemindTime.minutes);
    }

    private void initButton(){
        mButton = (Button)mView.findViewById(R.id.chooseRimendTimeButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeginRemindTime = new MyTime(mBeginPickerHours.getValue(), mBeginPickerMinutes.getValue());
                mEndRemindTime = new MyTime(mEndPickerHours.getValue(), mEndPickerMinutes.getValue());

                if(!mEndRemindTime.isLaterThan(mBeginRemindTime)){
                    Toast.makeText(mMainActivity, "截止时间不能早于起始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyDatabaseHelper.writeRemindTime(mMainActivity, mBeginRemindTime, mEndRemindTime);

                Toast.makeText(mMainActivity, "提醒时间已设置", Toast.LENGTH_SHORT).show();
            }
        });
    }

    ///numberpicker record spendTime
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal){

    }
    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState){

    }
    ///numberpicker formatter
    @Override
    public String format(int value){
        return value<10 ? "0"+value:""+value;
    }



}