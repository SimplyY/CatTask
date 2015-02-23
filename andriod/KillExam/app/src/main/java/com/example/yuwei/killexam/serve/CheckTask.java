package com.example.yuwei.killexam.serve;

import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.taskFragments.editableTaskFragment;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuwei on 15/2/21.
 */
public class CheckTask {
//TODO: 将业务逻辑剥离于serve里，这里写对task的内容进行核查是否违法的业务

    static private editableTaskFragment mFragment;

    static private EditText mTaskNameEditText;
    static private Button mFinishDateButton;
    static private NumberPicker mSpendTimePickerHours;
    static private NumberPicker mSpendTimePickerMinutes;
    static private EditText mTaskContextText;
    static private Spinner mRemindMethodSpinner;
    static private Spinner mTaskAttributeSpinner;
    static private Button mCreateTaskButton;
    static private TextView mIsHasBelongTextView;

    static private MyDate mFinishDate;
    static private Task newTask;



    static int selectedAttributePositionNow;
    static int selectedAttributePositionBefore;

    public static void initCheck(editableTaskFragment fragment) {
        mFragment = fragment;

        mTaskAttributeSpinner = fragment.mTaskAttributeSpinner;
        mFinishDateButton = fragment.mFinishDateButton;
        mSpendTimePickerHours = fragment.mSpendTimePickerHours;
        mSpendTimePickerMinutes = fragment.mSpendTimePickerMinutes;
        mTaskContextText = fragment.mTaskContextText;
        mRemindMethodSpinner = fragment.mRemindMethodSpinner;
        mTaskAttributeSpinner = fragment.mTaskAttributeSpinner;
        mCreateTaskButton = fragment.mCreateTaskButton;
        mIsHasBelongTextView = fragment.mIsHasBelongTextView;

        mFinishDate = fragment.mFinishDate;
        newTask = fragment.newTask;
    }


    //  动态设置底部对父任务描述和最后一个button
    public static void setTextIsHasBelongCheckAttribute() {

        final int THE_MAX_NAME_LENGTH_IN_IS_HAS_BELONG_TEXT = 3;

        final String CREATE_TASK_CHINESE = "创建任务";
        final String BELONG_TASK_CHINESE = "父任务为  ";
        final String NULL_CHINESE = "空";
        final String CHOOSE_CHINESE = "点击选择";
        selectedAttributePositionNow = mTaskAttributeSpinner.getSelectedItemPosition();
        selectedAttributePositionBefore = newTask.getTaskAttribute().getPosition();
        if (selectedAttributePositionNow != 0) {
//已经设置好belong时
            if (newTask.isHasBelong() && selectedAttributePositionNow == selectedAttributePositionBefore) {
                String taskName = newTask.getBelongName();
                String name = taskName.length() > THE_MAX_NAME_LENGTH_IN_IS_HAS_BELONG_TEXT ? taskName.substring(0, THE_MAX_NAME_LENGTH_IN_IS_HAS_BELONG_TEXT) : taskName;
                mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + name);
                mCreateTaskButton.setText(CREATE_TASK_CHINESE);
            }
//还没设置好belong，或者更改了attribute，需重新设置belong
            else {
                mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + NULL_CHINESE);
                mCreateTaskButton.setText(CHOOSE_CHINESE);
            }
        } else {
//更改attribute为1级，但是之前设置好了belong
            if (newTask.isHasBelong() && mCreateTaskButton.getText().equals(CHOOSE_CHINESE)) {
                mIsHasBelongTextView.setText(BELONG_TASK_CHINESE + NULL_CHINESE);
                mCreateTaskButton.setText(CREATE_TASK_CHINESE);
            }
        }
    }

//核查所有的输入以及获取合法值
    public static boolean checkAll() {
        return checkAttribute() && checkTaskName() && checkContext() && checkFinishDate() && checkTime() && checkRimendMethod();
    }

//当需要设置belong的时候return false，
    private static boolean checkAttribute() {
        SpinnerValue attribute = newTask.getTaskAttribute();
        newTask.getTaskAttribute().setSelectedName(mTaskAttributeSpinner.getSelectedItem().toString());

        if (attribute.getPosition() != 0) {
            if (newTask.isHasBelong() == false || selectedAttributePositionNow != selectedAttributePositionBefore) {
                preserveSomeTaskInfo();
                mFragment.setTaskBelong();
                return false;
            }
        }
        return true;
    }

    private static void preserveSomeTaskInfo() {
        newTask.setTaskName(mTaskNameEditText.getText().toString());
        newTask.setTaskContext(mTaskContextText.getText().toString());
    }

    private static boolean checkTaskName() {
        final String TASK_NAME_HAS_SAPCE = "任务名不能为空，或者有空格";
        final String TASK_NAME_HAS_EXIST = "任务名已存在";

        if (checkEditText(mTaskNameEditText) == false) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TASK_NAME_HAS_SAPCE, Toast.LENGTH_SHORT).show();
            return false;
        } else if (isNameHasExist(mTaskNameEditText)) {
            Toast.makeText(mFragment.getActivity().getApplication(), TASK_NAME_HAS_EXIST, Toast.LENGTH_SHORT).show();
            return false;
        }

        newTask.setTaskName(mTaskNameEditText.getText().toString());
        return true;
    }

    private static boolean checkEditText(EditText text) {
        String template = text.getText().toString();
        if (template.equals("")) {
            return false;
        }
        Matcher space = Pattern.compile(" +").matcher(template);
        if (space.find()) {
            return false;
        }
        return true;
    }

    private static boolean isNameHasExist(EditText text) {
        String name = text.getText().toString();
        return MyDatabaseHelper.checkNameHasExist(mFragment.getActivity().getApplicationContext(), name);
    }

    private static boolean checkFinishDate() {
        final String TIME_MUST_BE_SELECTED = "任务时间必须选择";
        final String TASK_TIME_CANNT_BE_EARLY_THAN_CURRUNT = "任务完成时间不能比当前时间早";

        MyDate current = new MyDate();
        boolean checkDate = true;

        if (mFinishDate == null) {
            checkDate = false;
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TIME_MUST_BE_SELECTED, Toast.LENGTH_SHORT).show();
            return checkDate;
        }

        if (mFinishDate.getYear() < current.getYear()) {
            checkDate = false;
        }
        if (mFinishDate.getYear() == current.getYear()) {
            if (mFinishDate.getMonth() < current.getMonth()) {
                checkDate = false;
            }
            if (mFinishDate.getMonth() == current.getMonth()) {
                if (mFinishDate.getDay() < current.getDay()) {
                    checkDate = false;
                }
            }
        }

        if (checkDate == false) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TASK_TIME_CANNT_BE_EARLY_THAN_CURRUNT, Toast.LENGTH_SHORT).show();

        }
        return checkDate;
    }

    private static boolean checkContext() {
        final String TASK_CONTEXT_CANNT_BE_NULL = "内容不能为空";

        if (checkEditText(mTaskContextText) == false) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TASK_CONTEXT_CANNT_BE_NULL, Toast.LENGTH_SHORT).show();
            return false;
        }

        newTask.setTaskContext(mTaskContextText.getText().toString());
        return true;
    }

    private static boolean checkTime() {
        final String TIME_CANNT_BE_ZERO = "时间不能为零";

        if (mSpendTimePickerMinutes.getValue() == 0 && mSpendTimePickerHours.getValue() == 0) {
            Toast.makeText(mFragment.getActivity().getApplicationContext(), TIME_CANNT_BE_ZERO, Toast.LENGTH_SHORT).show();
            return false;
        }

        newTask.setSpendHours(mSpendTimePickerHours.getValue());
        newTask.setSpendMinutes(mSpendTimePickerMinutes.getValue());
        return true;
    }

    private static boolean checkRimendMethod() {

        String remindMethodName = mRemindMethodSpinner.getSelectedItem().toString();

        SpinnerValue remindMethod = newTask.getRemindMethod();
        remindMethod.setSelectedName(remindMethodName);

        return true;
    }

}
