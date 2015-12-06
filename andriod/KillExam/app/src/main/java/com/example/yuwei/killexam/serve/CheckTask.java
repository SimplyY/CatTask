package com.example.yuwei.killexam.serve;

import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.taskFragments.EditTaskFragment;
import com.example.yuwei.killexam.taskFragments.EditableTaskFragment;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.map.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuwei on 15/2/21.
 */
public class CheckTask {
//  编辑模式时
    public static boolean isEditMode;

    private EditableTaskFragment mFragment;

    private EditText mTaskNameEditText;
    private NumberPicker mSpendTimePickerHours;
    private NumberPicker mSpendTimePickerMinutes;
    private Spinner mTaskAttributeSpinner;
    private TextView mIsHasBelongTextView;

    private MyDate mFinishDate;
    private Task newTask;

    int selectedAttributePositionNow;
    int selectedAttributePositionBefore;

    public CheckTask(EditableTaskFragment fragment) {
        mFragment = fragment;

        mTaskAttributeSpinner = fragment.mTaskAttributeSpinner;
        mSpendTimePickerHours = fragment.mSpendTimePickerHours;
        mSpendTimePickerMinutes = fragment.mSpendTimePickerMinutes;
        mTaskAttributeSpinner = fragment.mTaskAttributeSpinner;
        mIsHasBelongTextView = fragment.mIsHasBelongTextView;
        mTaskNameEditText = fragment.mTaskNameEditText;

        newTask = fragment.newTask;

    }

    public void setFinishDate() {
        mFinishDate = mFragment.mFinishDate;
    }


    //  动态设置底部对父任务描述和最后一个button
    public void setTextIsHasBelongCheckAttribute() {

        if (isEditMode){
            return;
        }
        final int THE_MAX_NAME_LENGTH_TEXT = 3;

        final String BELONG_TASK_CHINESE = "父任务为  ";
        final String NULL_CHINESE = "空";
        selectedAttributePositionNow = mTaskAttributeSpinner.getSelectedItemPosition();
        selectedAttributePositionBefore = newTask.getTaskAttribute().getSelectedPosition();
        if (selectedAttributePositionNow != 0  ) {
//  已经设置好belong时
            if (newTask.isHasBelong() && selectedAttributePositionNow == selectedAttributePositionBefore) {
                String taskName = newTask.getBelongName();
                String name = taskName.length() < THE_MAX_NAME_LENGTH_TEXT ? taskName:taskName.substring(0, THE_MAX_NAME_LENGTH_TEXT);
                mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + name);
            }
//  还没设置好belong，或者更改了attribute，需重新设置belong
            else {
                mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + NULL_CHINESE);
            }
        } else {
//  attribute为1级

            mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + NULL_CHINESE);
        }
    }

    //核查所有的输入以及获取合法值
    public boolean checkAll() {
        mFragment.setRemindMethod();
        mFragment.setColorTag();

        return checkAttribute() && checkTaskName() && checkFinishDate() && checkTime() ;
    }

    //当需要设置belong的时候return false，
    public boolean checkAttribute() {
        final String NEED_SET_BELONG = "需要选择父任务";

        boolean flag = !isNeedSetBelong();
        if (isNeedSetBelong()) {
            Toast.makeText(mFragment.getActivity().getApplication(), NEED_SET_BELONG,Toast.LENGTH_SHORT).show();
        }

        return flag;
    }
    public boolean isNeedSetBelong(){
        mFragment.setAttribute();

        if (newTask.getTaskAttribute().getSelectedPosition() != 0) {
            if (!newTask.isHasBelong() || selectedAttributePositionNow != selectedAttributePositionBefore) {

                return true;
            }
        }
        return false;
    }

    public void preserveSomeTaskInfo() {
        newTask.setTaskName(mTaskNameEditText.getText().toString());
        newTask.setSpendTime(mSpendTimePickerHours.getValue(), mSpendTimePickerMinutes.getValue());
        newTask.setFinishedDate(mFinishDate);

        mFragment.setColorTag();
        mFragment.setRemindMethod();
        mFragment.setAttribute();
    }

    public boolean checkTaskName() {
        final String TASK_NAME_HAS_SPACE = "任务名不能为空";
        final String TASK_NAME_HAS_EXIST = "任务名已存在";

        if (!checkEditText(mTaskNameEditText)) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TASK_NAME_HAS_SPACE, Toast.LENGTH_SHORT).show();
            return false;
        } else if (isNameHasExist(mTaskNameEditText)) {
            Toast.makeText(mFragment.getActivity().getApplication(), TASK_NAME_HAS_EXIST, Toast.LENGTH_SHORT).show();
            return false;
        }

        newTask.setTaskName(mTaskNameEditText.getText().toString());
        return true;
    }

    private boolean checkEditText(EditText text) {
        String template = text.getText().toString();
        if (template.equals("")) {
            return false;
        }
        Matcher space = Pattern.compile(" +").matcher(template);
        if (space.matches()) {
            return false;
        }
        Matcher changeLine = Pattern.compile("\n+").matcher(template);
        if (changeLine.find()){
            return false;
        }
        return true;
    }

    private boolean isNameHasExist(EditText text) {
        String name = text.getText().toString();
        if (isEditMode && name.equals(EditTaskFragment.oldTaskName)){
            return false;
        }
        return MyDatabaseHelper.checkNameHasExist(mFragment.getActivity().getApplicationContext(), name);
    }

    private boolean checkFinishDate() {
        final String TASK_TIME_CANNT_BE_EARLY_THAN_CURRENT = "任务完成时间不能比当前时间早";

        MyDate current = new MyDate();

//      默认时间为当前
        if (mFinishDate == null) {
            if (newTask.getFinishedDate() != null){
                mFinishDate = newTask.getFinishedDate();
            }
            else{
                mFinishDate = current;
                newTask.setFinishedDate(mFinishDate);
                return true;
            }
        }


        boolean checkDateIsRight = !mFinishDate.isBefore(current);

        if (checkDateIsRight == false) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TASK_TIME_CANNT_BE_EARLY_THAN_CURRENT, Toast.LENGTH_SHORT).show();

        }
        return checkDateIsRight;
    }


    private boolean checkTime() {
        final String TIME_CANNT_BE_ZERO = "花费时间不能为零";

        if (mSpendTimePickerMinutes.getValue() == 0 && mSpendTimePickerHours.getValue() == 0) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TIME_CANNT_BE_ZERO, Toast.LENGTH_SHORT).show();
            return false;
        }

        newTask.setSpendTime(mSpendTimePickerHours.getValue(), mSpendTimePickerMinutes.getValue());
        return true;
    }


}
