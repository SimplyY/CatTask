package com.example.yuwei.killexam.taskFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.MyDate;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class ChooseRimindTimeFragment extends Fragment
        implements NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener,NumberPicker.Formatter{

    private MainActivity mMainActivity;
    private View mView;
    public NumberPicker mBeginPickerHours;
    public NumberPicker mBeginPickerMinutes;

    public NumberPicker mEndPickerHours;
    public NumberPicker mEndPickerMinutes;

    public Button mButton;


    public ChooseRimindTimeFragment(MainActivity mainActivity){
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
        return mView;
    }

    private void initPickers(){
        mBeginPickerHours = (NumberPicker)mView.findViewById(R.id.beginRimindHours);
        mBeginPickerHours.setOnScrollListener(this);
        mBeginPickerHours.setFormatter(this);
        mBeginPickerHours.setMaxValue(23);
        mBeginPickerHours.setMinValue(0);
        mBeginPickerHours.setValue(12);

        mBeginPickerMinutes = (NumberPicker)mView.findViewById(R.id.beginRimindMinutes);
        mBeginPickerMinutes.setOnScrollListener(this);
        mBeginPickerMinutes.setFormatter(this);
        mBeginPickerMinutes.setMaxValue(60);
        mBeginPickerMinutes.setMinValue(0);
        mEndPickerMinutes.setValue(0);


        mEndPickerHours = (NumberPicker)mView.findViewById(R.id.endRimindHours);
        mEndPickerHours.setOnScrollListener(this);
        mEndPickerHours.setFormatter(this);
        mEndPickerHours.setMaxValue(23);
        mEndPickerHours.setMinValue(0);
        mEndPickerHours.setValue(12);

        mEndPickerMinutes = (NumberPicker)mView.findViewById(R.id.endRimindMinutes);
        mEndPickerMinutes.setOnScrollListener(this);
        mEndPickerMinutes.setFormatter(this);
        mEndPickerMinutes.setMaxValue(60);
        mEndPickerMinutes.setMinValue(0);
        mEndPickerMinutes.setValue(0);
    }

    private void initButton(){
        mButton = (Button)mView.findViewById(R.id.chooseRimendTimeButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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