package com.example.yuwei.killexam.taskFragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker.*;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.serve.CheckTask;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

/**
 * Created by yuwei on 15/2/16.
 */

public class CreateTaskFragment extends editableTaskFragment
        implements View.OnClickListener , OnValueChangeListener , OnScrollListener,Formatter,CalendarDatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "sectionNumber";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private int sectionNumber;

    CheckTask checkTask;
    
    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }

    public CreateTaskFragment() {
    }

    public CreateTaskFragment(int sectionNumber) {
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, sectionNumber);
        setArguments(args);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            sectionNumber = getArguments().getInt(ARG_PARAM1);
        }

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

        return mView;
    }


    private void initViews(){
        initTaskAttributeSpinner();

        initColorTagSpinner();

        initTaskNameText();

        initFinishDateButton();

        initSpendTimePicker();

        initRemindMethodSpinner();

        initCreateButton();

        initIsHasBelongTextView();

    }


    private void initTaskAttributeSpinner(){

        if (newTask.getTaskAttribute() == null){
            SpinnerValue taskAttribute = SpinnerValue.initSpinnerValue(R.array.task_attribute_array, getResources());
            newTask.setTaskAttribute(taskAttribute);
        }
        mTaskAttributeSpinner.setSelection(newTask.getTaskAttribute().getPosition());

    }

    private void initColorTagSpinner(){
        mTaskColorTagSpinner = (Spinner)mView.findViewById(R.id.taskColorTagSpinner);
        mTaskColorTagSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> taskColorTagAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.tag_color_array, android.R.layout.simple_spinner_item);


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

    private void initRemindMethodSpinner(){
        mRemindMethodSpinner = (Spinner)mView.findViewById(R.id.remindMethodSpinner);
        mRemindMethodSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> remindMethodAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                R.array.remind_method_array, android.R.layout.simple_spinner_item);
        remindMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRemindMethodSpinner.setAdapter(remindMethodAdapter);

        if (newTask.getRemindMethod() == null){
            SpinnerValue remindMethod = SpinnerValue.initSpinnerValue(R.array.remind_method_array, getResources());
            newTask.setRemindMethod(remindMethod);
        }
        mRemindMethodSpinner.setSelection(newTask.getRemindMethod().getPosition());

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
                .newInstance(this,now.getYear(), now.getMonth(),
                        now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }

//setFinishDate
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth){
        mFinishDate =  new MyDate(year, monthOfYear, dayOfMonth);
        newTask.setFinishedTime(mFinishDate);
        mFinishDateButton.setText(mFinishDate.toString());
        checkTask.setFinishDate();
    }



    private void writeTaskInDataBase(){
        MyDatabaseHelper.writeNewTask(this.getActivity().getApplicationContext(), newTask);
    }

    private void quit(){
        if (newTask.isHasBelong() == false) {
            quitCreateTaskFragment();
        }
    }

    private void quitCreateTaskFragment(){
        MainActivity activity = (MainActivity)this.getActivity();
        android.support.v4.app.FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        MainActivity.PlaceholderFragment placeholderFragment = MainActivity.PlaceholderFragment.newInstance(1);
        fragmentTransaction.replace(R.id.container, placeholderFragment)
                .commit();
    }


}
