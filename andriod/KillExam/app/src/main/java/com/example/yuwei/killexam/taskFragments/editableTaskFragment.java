package com.example.yuwei.killexam.taskFragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.example.yuwei.killexam.ChooseBelongActivity;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.map.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

import info.hoang8f.widget.FButton;

/**
 * Created by yuwei on 15/2/23.
 */
public abstract class editableTaskFragment extends Fragment
        implements View.OnClickListener , NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener,NumberPicker.Formatter,CalendarDatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    protected View mView;

    public Spinner mTaskAttributeSpinner;
    public Spinner mTaskColorTagSpinner;

    public EditText mTaskNameEditText;
    public FButton mFinishDateButton;
    public NumberPicker mSpendTimePickerHours;
    public NumberPicker mSpendTimePickerMinutes;
    public Spinner mRemindMethodSpinner;
    public FButton mCreateTaskButton;
    public TextView mIsHasBelongTextView;

    public MyDate mFinishDate;
    public Task newTask;



    protected void setAdapterForSpinner(Spinner spinner, int arrayId){
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                arrayId, android.R.layout.simple_spinner_item);

        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);

    }

    //numberpicker formatter
    @Override
    public String format(int value) {
        return value < 10 ? "0" + value : "" + value;
    }

    //  numberPicker record spendTime
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal){

    }
    @Override
    public void onScrollStateChange(NumberPicker view, int scrollState){

    }

    public void setViewsValues(){
        setAttributeValue();
        setTagColorValue();
        setRemindMethodValue();
        setNameText();
        setRemindMethodValue();
        setFinishButtonText();
        setTimePickerValue();
    }
    protected void setAttributeValue(){
        if (newTask.getTaskAttribute() == null){
            SpinnerValue taskAttribute = SpinnerValue.initSpinnerValue(R.array.task_attribute_array, getResources());
            newTask.setTaskAttribute(taskAttribute);
        }

        mTaskAttributeSpinner.setSelection(newTask.getTaskAttribute().getSelectedPosition());
    }
    protected void setTagColorValue(){
        if (newTask.getTagColor() == null){
            SpinnerValue tagColor = SpinnerValue.initSpinnerValue(R.array.tag_color_array, getResources());
            newTask.setTagColor(tagColor);
        }

        mTaskColorTagSpinner.setSelection(newTask.getTagColor().getSelectedPosition());
    }
    private void setNameText(){
        String taskName = newTask.getTaskName();
        if (taskName != null){
            mTaskNameEditText.setText(taskName);
        }
    }
    protected void setRemindMethodValue(){
        if (newTask.getRemindMethod() == null){
            SpinnerValue remindMethod = SpinnerValue.initSpinnerValue(R.array.task_remind_method_array, getResources());
            newTask.setRemindMethod(remindMethod);
        }
        mRemindMethodSpinner.setSelection(newTask.getRemindMethod().getSelectedPosition());

    }

    public void setFinishButtonText(){
        if (newTask.getFinishedDate() != null){
            mFinishDateButton.setText(newTask.getFinishedDate().toString());
            mFinishDate = newTask.getFinishedDate();
        }
    }
    public void setTimePickerValue(){
        mSpendTimePickerHours.setOnScrollListener(this);
        mSpendTimePickerHours.setFormatter(this);
        mSpendTimePickerHours.setMaxValue(99);
        mSpendTimePickerHours.setMinValue(0);

        mSpendTimePickerMinutes.setOnScrollListener(this);
        mSpendTimePickerMinutes.setFormatter(this);
        mSpendTimePickerMinutes.setMaxValue(59);
        mSpendTimePickerMinutes.setMinValue(0);

        if (newTask.getSpendHours() != 0){
            mSpendTimePickerHours.setValue(newTask.getSpendHours());
        }
        else{
            mSpendTimePickerHours.setValue(1);
        }

        if (newTask.getSpendMinutes() != 0){
            mSpendTimePickerMinutes.setValue(newTask.getSpendMinutes());
        }
    }

    public void setTaskBelong(){
        enterChooseTaskBelongActivity();
    }

    private void enterChooseTaskBelongActivity(){
        MainActivity activity = (MainActivity)this.getActivity();
        Intent intent = new Intent(activity,ChooseBelongActivity.class);
        intent.putExtra("task", newTask);
        startActivity(intent);

    }
}
