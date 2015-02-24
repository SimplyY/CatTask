package com.example.yuwei.killexam.taskFragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yuwei.killexam.ChooseBelongActivity;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.Task;

/**
 * Created by yuwei on 15/2/23.
 */
public class editableTaskFragment extends Fragment {

    protected View mView;

    public Spinner mTaskAttributeSpinner;
    public Spinner mTaskColorTagSpinner;

    public EditText mTaskNameEditText;
    public Button mFinishDateButton;
    public NumberPicker mSpendTimePickerHours;
    public NumberPicker mSpendTimePickerMinutes;
    public EditText mTaskContextText;
    public Spinner mRemindMethodSpinner;
    public Button mCreateTaskButton;
    public TextView mIsHasBelongTextView;

    public MyDate mFinishDate;


    public Task newTask;

    private void initSpinner(int viewId, int arrayId){
        mTaskAttributeSpinner = (Spinner)mView.findViewById(viewId);
        mTaskAttributeSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> taskAttributeAdapter = ArrayAdapter.createFromResource(this.getActivity().getApplicationContext(),
                arrayId, android.R.layout.simple_spinner_item);

        taskAttributeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTaskAttributeSpinner.setAdapter(taskAttributeAdapter);

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
