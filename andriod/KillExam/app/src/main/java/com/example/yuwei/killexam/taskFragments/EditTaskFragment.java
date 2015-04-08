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

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.serve.CheckTask;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.Task;

import info.hoang8f.widget.FButton;

/**
 * Created by yuwei on 15/3/18.
 */
public class EditTaskFragment extends EditableTaskFragment {

    public static String oldTaskName;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    public static EditTaskFragment editTaskFragment;

    public void setNewTask(Task newTask) {
        this.newTask = newTask;
    }


    public static EditTaskFragment newInstance(MainActivity mainActivity, Task task){
        editTaskFragment = new EditTaskFragment(mainActivity);

        editTaskFragment.setNewTask(task);
        oldTaskName = task.getTaskName();

        return editTaskFragment;
    }

    public EditTaskFragment(MainActivity mainActivity) {
        mMainActivity = mainActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            newTask = (Task) savedInstanceState.get("task");
        }

        Task theTask = (Task) this.getActivity().getIntent().getParcelableExtra("task");

        if (theTask != null) {
            newTask = theTask;
        }

        if (newTask == null) {
            newTask = new Task();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInsrtanceState) {
        super.onSaveInstanceState(savedInsrtanceState);
        savedInsrtanceState.putSerializable("task", newTask);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        mMainActivity.currentFragment = editTaskFragment;
        initViews();
        setViewsValues();

        checkTask = new CheckTask(this);
        CheckTask.isEditMode = true;

        checkTask.setTextIsHasBelongCheckAttribute();

        mMainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mMainActivity.getSupportActionBar().setBackgroundDrawable(null);

        return mView;
    }


    private void initViews() {

        mMainActivity.onCreateOptionsMenu(mMainActivity.mMenu);

        initMenu();

        initAttributeSpinner();

        initColorTagSpinner();

        initRemindMethodSpinner();

        initTaskNameText();

        initSpendTimePicker();

        initFinishDateButton();

    }

    private void initMenu(){
    }
    private void initAttributeSpinner() {
        mTaskAttributeSpinner = (Spinner) mView.findViewById(R.id.editTaskAttributeSpinner);

        setAdapterForSpinner(mTaskAttributeSpinner, R.array.task_attribute_array);
    }
    private void initColorTagSpinner() {
        mTaskColorTagSpinner = (Spinner) mView.findViewById(R.id.editTaskColorTagSpinner);
        setAdapterForSpinner(mTaskColorTagSpinner, R.array.tag_color_array);

    }
    private void initRemindMethodSpinner() {
        mRemindMethodSpinner = (Spinner) mView.findViewById(R.id.editRemindMethodSpinner);
        setAdapterForSpinner(mRemindMethodSpinner, R.array.task_remind_method_array);

    }
    private void initTaskNameText() {
        mTaskNameEditText = (EditText) mView.findViewById(R.id.editTaskNameText);
    }
    private void initFinishDateButton() {
        mFinishDateButton = (FButton) mView.findViewById(R.id.editFinishDatePicker);
        mFinishDateButton.setOnClickListener(this);
    }
    private void initSpendTimePicker() {
        mSpendTimePickerHours = (NumberPicker) mView.findViewById(R.id.editSpendTimePickerHours);

        mSpendTimePickerMinutes = (NumberPicker) mView.findViewById(R.id.editSpendTimePickerMinutes);

    }


    //any spinner selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        if (parent == null || parent.getChildAt(0) == null){
            return;
        }
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        ((TextView) parent.getChildAt(0)).setTextSize(20);
        checkTask.setTextIsHasBelongCheckAttribute();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    //setFinishDate
    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        mFinishDate = new MyDate(year, monthOfYear + 1, dayOfMonth);
        newTask.setFinishedDate(mFinishDate);
        mFinishDateButton.setText(mFinishDate.toString());
        checkTask.setFinishDate();
    }



    //Buttons onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//  finishDatePicker on pick
            case R.id.editFinishDatePicker:
                finishDataPick();
                break;
        }
    }

    @Override
    protected void writeTaskInDataBase(){
        MyDatabaseHelper.updateTask(this.getActivity().getApplicationContext(), newTask, oldTaskName);
    }



    public void deleteTask(){
        MyDatabaseHelper.deleteTask(this.getActivity().getApplicationContext(), newTask);
        enterTaskListFragment();
    }

    public void finishDataPick() {
        FragmentManager fm = getChildFragmentManager();
        MyDate now = new MyDate();
        CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                .newInstance(this, now.getYear(), now.getMonth() - 1,
                        now.getDay());
        calendarDatePickerDialog.setTargetFragment(this, 1);
        calendarDatePickerDialog.show(fm, FRAG_TAG_DATE_PICKER);
    }



}
