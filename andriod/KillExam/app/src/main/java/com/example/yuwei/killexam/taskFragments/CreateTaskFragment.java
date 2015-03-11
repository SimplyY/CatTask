package com.example.yuwei.killexam.taskFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.map.TitleMapString;
import com.example.yuwei.killexam.serve.CheckTask;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.Task;

/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends editableTaskFragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    CheckTask checkTask;

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    private MainActivity mMainActivity;

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
    public void onSaveInstanceState(Bundle savedInsrtanceState){
        super.onSaveInstanceState(savedInsrtanceState);
        savedInsrtanceState.putSerializable("task", newTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_task, container, false);
        initViews();

        checkTask = new CheckTask(this);

        setButtonTextDepnedBelong();

//      让toolbar有高富帅般的白色（默认背景）
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

        initCreateButton();

        initIsHasBelongTextView();
    }


    private void initAttributeSpinner(){
        mTaskAttributeSpinner = (Spinner)mView.findViewById(R.id.taskAttributeSpinner);

        setAdapterForSpinner(mTaskAttributeSpinner, R.array.task_attribute_array);
        setAttributeValue();
    }

    private void initColorTagSpinner(){
        mTaskColorTagSpinner = (Spinner)mView.findViewById(R.id.taskColorTagSpinner);
        setAdapterForSpinner(mTaskColorTagSpinner, R.array.tag_color_array);

        setTagColorValue();
    }

    private void initRemindMethodSpinner(){
        mRemindMethodSpinner = (Spinner)mView.findViewById(R.id.task_remindMethodSpinner);
        setAdapterForSpinner(mRemindMethodSpinner, R.array.task_remind_method_array);

        setRemindMethodValue();
    }

    //any spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        setButtonTextDepnedBelong();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private void setButtonTextDepnedBelong(){

        checkTask.setTextIsHasBelongCheckAttribute();

    }

    private void initTaskNameText(){
        mTaskNameEditText = (EditText) mView.findViewById(R.id.taskNameText);
        setNameText();
    }

    private void setNameText(){
        String taskName = newTask.getTaskName();
        if (taskName != null){
            mTaskNameEditText.setText(taskName);
        }
    }

    private void initFinishDateButton(){
        mFinishDateButton = (Button)mView.findViewById(R.id.finishDatePicker);
        mFinishDateButton.setOnClickListener(this);
    }


    private void initSpendTimePicker(){
        mSpendTimePickerHours = (NumberPicker)mView.findViewById(R.id.spendTimePickerHours);
        mSpendTimePickerHours.setOnScrollListener(this);
        mSpendTimePickerHours.setFormatter(this);
        mSpendTimePickerHours.setMaxValue(99);
        mSpendTimePickerHours.setMinValue(0);

        mSpendTimePickerMinutes = (NumberPicker)mView.findViewById(R.id.spendTimePickerMinutes);
        mSpendTimePickerMinutes.setOnScrollListener(this);
        mSpendTimePickerMinutes.setFormatter(this);
        mSpendTimePickerMinutes.setMaxValue(59);
        mSpendTimePickerMinutes.setMinValue(0);

        mSpendTimePickerHours.setValue(newTask.getSpendHours());
        mSpendTimePickerMinutes.setValue(newTask.getSpendMinutes());

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


    private void initCreateButton(){
        mCreateTaskButton = (Button)mView.findViewById(R.id.createTask);
        mCreateTaskButton.setOnClickListener(this);
    }

    private void initIsHasBelongTextView(){
        mIsHasBelongTextView = (TextView) mView.findViewById(R.id.isHasBelongTextView);
    }


//Buttons onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//finishDatePicker on pick
            case R.id.finishDatePicker:
                finishDataPick();
                break;
//mCreateTaskButton onclick
            case R.id.createTask:
                if(checkTask.checkAll()){
                    writeTaskInDataBase();
                    quit();
                }

                break;
        }
    }

    public void finishDataPick(){
        FragmentManager fm = getChildFragmentManager();
        MyDate now = new MyDate();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this,now.getYear(), now.getMonth() - 1,
                        now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }

//setFinishDate
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth){
        mFinishDate =  new MyDate(year, monthOfYear + 1, dayOfMonth);
        newTask.setFinishedDate(mFinishDate);
        mFinishDateButton.setText(mFinishDate.toString());
        checkTask.setFinishDate();
    }

    private void writeTaskInDataBase(){
        MyDatabaseHelper.writeNewTask(this.getActivity().getApplicationContext(), newTask);
    }

    private void quit(){
        initTaskList();
        mMainActivity.replaceFragmentFromDrawer();
    }

    private void initTaskList(){
        MainActivity.mTitleMap = new TitleMapString();
        mMainActivity.initToolBar();

        TaskListFragment.initTaskList();

    }
}
