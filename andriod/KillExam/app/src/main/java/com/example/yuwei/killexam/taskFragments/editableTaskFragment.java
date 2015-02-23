package com.example.yuwei.killexam.taskFragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yuwei.killexam.ChooseBelongActivity;
import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.tools.MyDate;
import com.example.yuwei.killexam.tools.Task;

/**
 * Created by yuwei on 15/2/23.
 */
public class editableTaskFragment extends Fragment {

    protected View mView;

    public EditText mTaskNameEditText;
    public Button mFinishDateButton;
    public NumberPicker mSpendTimePickerHours;
    public NumberPicker mSpendTimePickerMinutes;
    public EditText mTaskContextText;
    public Spinner mRemindMethodSpinner;
    public Spinner mTaskAttributeSpinner;
    public Button mCreateTaskButton;
    public TextView mIsHasBelongTextView;

    public MyDate mFinishDate;


    public Task newTask;

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
