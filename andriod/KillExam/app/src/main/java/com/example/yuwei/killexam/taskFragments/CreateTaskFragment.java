package com.example.yuwei.killexam.taskFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.serve.CheckTask;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.Task;

import info.hoang8f.widget.FButton;

/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends EditableTaskFragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    public FButton mChooseBelongTaskButton;

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    public CreateTaskFragment(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            newTask = (Task)savedInstanceState.get("task");
        }

        Task theTask = (Task)this.getActivity().getIntent().getParcelableExtra("task");

        if (theTask!= null){
            newTask = theTask;
        }

        if (newTask == null){
            newTask = new Task();
        }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("task", newTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_task, container, false);
        mMainActivity.currentFragment = this;

        mMainActivity.onCreateOptionsMenu(mMainActivity.mMenu);

        initViews();
        setViewsValues();

        checkTask = new CheckTask(this);

        checkTask.setTextIsHasBelongCheckAttribute();

        mMainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

//      让toolbar为白色（默认背景）
        mMainActivity.getSupportActionBar().setBackgroundDrawable(null);

        return mView;
    }


    private void initViews(){
        initAttributeSpinner();

        initColorTagSpinner();

        initRemindMethodSpinner();

        initTaskNameText();

        initSpendTimePicker();

        initFinishDateButton();

        initChooseBelongTaskButton();

        initIsHasBelongTextView();
    }
    private void initAttributeSpinner(){
        mTaskAttributeSpinner = (Spinner)mView.findViewById(R.id.taskAttributeSpinner);

        setAdapterForSpinner(mTaskAttributeSpinner, R.array.task_attribute_array);

    }
    private void initColorTagSpinner(){
        mTaskColorTagSpinner = (Spinner)mView.findViewById(R.id.taskColorTagSpinner);
        setAdapterForSpinner(mTaskColorTagSpinner, R.array.tag_color_array);

    }
    private void initRemindMethodSpinner(){
        mRemindMethodSpinner = (Spinner)mView.findViewById(R.id.remindMethodSpinner);
        setAdapterForSpinner(mRemindMethodSpinner, R.array.task_remind_method_array);
    }
    private void initTaskNameText(){
        mTaskNameEditText = (EditText) mView.findViewById(R.id.taskNameText);
    }
    private void initFinishDateButton(){
        mFinishDateButton = (FButton)mView.findViewById(R.id.finishDatePicker);
        initFinishButtonText();
        mFinishDateButton.setOnClickListener(this);
    }
    private void initSpendTimePicker(){
        mSpendTimePickerHours = (NumberPicker)mView.findViewById(R.id.spendTimePickerHours);
        mSpendTimePickerMinutes = (NumberPicker)mView.findViewById(R.id.spendTimePickerMinutes);
        initTimePickerValue();
    }
    private void initChooseBelongTaskButton(){
        mChooseBelongTaskButton = (FButton)mView.findViewById(R.id.chooseBelongTask);
        mChooseBelongTaskButton.setOnClickListener(this);
    }
    private void initIsHasBelongTextView(){
        mIsHasBelongTextView = (TextView) mView.findViewById(R.id.isHasBelongTextView);
    }



    //any spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent != null && parent.getChildAt(0) != null) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
            ((TextView) parent.getChildAt(0)).setTextSize(20);
            checkTask.setTextIsHasBelongCheckAttribute();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //  setFinishDate
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth){
        mFinishDate =  new MyDate(year, monthOfYear + 1, dayOfMonth);
        newTask.setFinishedDate(mFinishDate);
        mFinishDateButton.setText(mFinishDate.toString());
        checkTask.setFinishDate();
    }

//  Buttons onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//  finishDatePicker on pick
            case R.id.finishDatePicker:
                finishDataPick();
                break;
//  chooseBelongButton onclick
            case R.id.chooseBelongTask:
                if (!checkTask.checkTaskName()){
                    break;
                }

                if(mTaskAttributeSpinner.getSelectedItemPosition() != 0){
                    checkTask.preserveSomeTaskInfo();
                    setTaskBelong();
                }
                else{
                    Toast.makeText(getActivity().getApplication(), "不需要设置父任务", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void finishDataPick(){
        FragmentManager fm = getChildFragmentManager();
        MyDate now = new MyDate();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this,now.getYear(), now.getMonth() - 1, now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }


    @Override
    protected void writeTaskInDataBase(){
        MyDatabaseHelper.writeNewTask(this.getActivity().getApplicationContext(), newTask);
    }


}
