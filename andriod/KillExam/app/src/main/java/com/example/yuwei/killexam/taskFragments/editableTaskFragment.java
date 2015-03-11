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

/**
 * Created by yuwei on 15/2/23.
 */
public abstract class editableTaskFragment extends Fragment
        implements View.OnClickListener , NumberPicker.OnValueChangeListener, NumberPicker.OnScrollListener,NumberPicker.Formatter,CalendarDatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener{

    protected View mView;

    public Spinner mTaskAttributeSpinner;
    public Spinner mTaskColorTagSpinner;

    public EditText mTaskNameEditText;
    public Button mFinishDateButton;
    public NumberPicker mSpendTimePickerHours;
    public NumberPicker mSpendTimePickerMinutes;
    public Spinner mRemindMethodSpinner;
    public Button mCreateTaskButton;
    public TextView mIsHasBelongTextView;

    public MyDate mFinishDate;


    public Task
            newTask;

    protected void setAdapterForSpinner(Spinner spinner, int arrayId){
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> Adapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                arrayId, android.R.layout.simple_spinner_item);

        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(Adapter);

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

    protected void setRemindMethodValue(){
        if (newTask.getRemindMethod() == null){
            SpinnerValue remindMethod = SpinnerValue.initSpinnerValue(R.array.task_remind_method_array, getResources());
            newTask.setRemindMethod(remindMethod);
        }
        mRemindMethodSpinner.setSelection(newTask.getRemindMethod().getSelectedPosition());

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
