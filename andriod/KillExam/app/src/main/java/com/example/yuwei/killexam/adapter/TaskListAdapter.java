package com.example.yuwei.killexam.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.map.HeaderTimeMapString;
import com.example.yuwei.killexam.taskFragments.EditTaskFragment;
import com.example.yuwei.killexam.taskFragments.TaskListFragment;
import com.example.yuwei.killexam.tools.Task;
import com.example.yuwei.killexam.tools.TaskTree;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by yuwei on 15/2/25.
 */
public class TaskListAdapter extends ArrayAdapter<Task> implements StickyListHeadersAdapter {
    View view;
    ViewHolder viewHolder;
    int viewHolderPosition;
    int resourceId;

    Task theTask;
    MainActivity mMainActivity;

    final int THE_MAX_CHINESE_WEIGHT = 36;

    private LayoutInflater inflater;
    List<Task> sortedTODOtasks;

    TaskListAdapter taskListAdapter;

    int firstVisiblePosition;

    public TaskListAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);

        inflater = LayoutInflater.from(context);
        mMainActivity = (MainActivity)context;
        sortedTODOtasks = objects;

        resourceId = textViewResourceId;

        taskListAdapter = this;

        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setActionBar();
                handler.postDelayed(this, 10);
            }
        };
        handler.postDelayed(runnable, 10);
    }


    @Override
    public int getCount() {
        return sortedTODOtasks.size();
    }

    @Override
    public Task getItem(int position) {
        return sortedTODOtasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        theTask = getItem(position);
        viewHolderPosition = position;

        if (convertView == null) {
            view = inflater.inflate(resourceId, null);

            viewHolder = new ViewHolder();
            initViewHolder();

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        setTaskHolder();

        return view;
    }


    private void setActionBar(){
        int currentPosition = TaskListFragment.taskListView.getFirstVisiblePosition();

        if (firstVisiblePosition != currentPosition && firstVisiblePosition >= currentPosition-1 && firstVisiblePosition <= currentPosition +1){
            firstVisiblePosition = currentPosition;
            int imageId = (new HeaderTimeMapString()).getImageId(sortedTODOtasks.get(firstVisiblePosition));
            mMainActivity.getSupportActionBar().setBackgroundDrawable(mMainActivity.getResources().getDrawable(imageId));
        }
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headHolder;
        if (convertView == null) {
            headHolder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            headHolder.timeTextView = (TextView) convertView.findViewById(R.id.timeHeader);
            headHolder.amountTextView = (TextView) convertView.findViewById(R.id.amountHeader);
            headHolder.headLayout = (LinearLayout) convertView.findViewById(R.id.headerLinearLayout);
            convertView.setTag(headHolder);
        } else {
            headHolder = (HeaderViewHolder) convertView.getTag();
        }
        String headerTimeString;
        HeaderTimeMapString headerTimeMapString = new HeaderTimeMapString();

        headerTimeString = headerTimeMapString.getValue(sortedTODOtasks.get(position));
        int imageId = headerTimeMapString.getImageId(sortedTODOtasks.get(position));
        int amount = getHeaderAmount(position);
        final String TASK = "个任务";

        headHolder.timeTextView.setText(headerTimeString);
        headHolder.headLayout.setBackground(mMainActivity.getResources().getDrawable(imageId));
        headHolder.amountTextView.setText(amount + TASK);

        return convertView;
    }

    private int getHeaderAmount(int position){
        long theHeadId = getHeaderId(position);
        int amount = 0;
        for (int i = 0; i < sortedTODOtasks.size(); i++){
            if (theHeadId == getHeaderId(i)){
                amount++;
            }
        }
        return  amount;
    }

    @Override
    public long getHeaderId(int position) {

        HeaderTimeMapString headerTimeMapString = new HeaderTimeMapString();
        int headerId = headerTimeMapString.getKey(sortedTODOtasks.get(position));

        return headerId;
    }


    private void initViewHolder() {

        viewHolder.tagColorImageView = (ImageView) view.findViewById(R.id.taskListColorTagImageView);

        viewHolder.space = (Space) view.findViewById(R.id.taskListSpace);

        viewHolder.isTaskFinishedCheckBox = (CheckBox) view.findViewById(R.id.taskListIsTaskFinishedCheckBox);

        viewHolder.taskFinishTimeTextView = (TextView) view.findViewById(R.id.taskListFinishTimeTextView);
    }

    private void setTaskHolder() {
        setColorImageView();
        setSpaceWidth();
        setIsTaskFinishedCheckBox();
        setTaskNameTextView();
        setTaskFinishTimeTextView();
    }

    private void setColorImageView() {
        int tagColorResId = theTask.getTagRes();

        viewHolder.tagColorImageView.setImageResource(tagColorResId);
    }

    private void setSpaceWidth() {
        int spaceAmount = getSpaceNumber(theTask);

        int spaceLength = 65;
        LinearLayout.LayoutParams spaceParams = (LinearLayout.LayoutParams) viewHolder.space.getLayoutParams();
        spaceParams.width = spaceLength * spaceAmount;
        viewHolder.space.setLayoutParams(spaceParams);

        LinearLayout.LayoutParams checkboxParams = (LinearLayout.LayoutParams) viewHolder.isTaskFinishedCheckBox.getLayoutParams();
        checkboxParams.width -= spaceParams.width/2;
        viewHolder.isTaskFinishedCheckBox.setLayoutParams(checkboxParams);


    }

    private void setIsTaskFinishedCheckBox() {

        Task task = sortedTODOtasks.get(viewHolderPosition);
        final CheckBox checkBox = viewHolder.isTaskFinishedCheckBox;

        if (task.getHasFinished() == 1){
            checkBox.setChecked(true);
        }
        else {
            checkBox.setChecked(false);
        }

        viewHolder.isTaskFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Task checkedTask = new Task();
                try {
                    checkedTask = TaskTree.getTask(buttonView.getText().toString(), THE_MAX_CHINESE_WEIGHT/4 - 2);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("checkedTask","checkedTask get wrong");
                }

                if (!TaskListFragment.isEditMode){
                    finishTask(checkedTask, buttonView, isChecked);
                }
                else{
                    enterEditTaskFragment(checkedTask);
                    checkBox.setChecked(false);
                }

            }
        });
    }

    private void finishTask(Task checkedTask, CompoundButton buttonView, boolean isChecked){
        int isFinished = isChecked?1:0;
        checkedTask.setHasFinished(isFinished);

        MyDatabaseHelper.updateTask(getContext(), checkedTask);

        TaskTree.renewSortedTaskArray(checkedTask, buttonView, taskListAdapter);
    }

    private void enterEditTaskFragment(Task task){
        Fragment fragment = EditTaskFragment.newInstance(mMainActivity, task);
        mMainActivity.replaceFragment(fragment);
    }

    private void setTaskNameTextView() {

        String taskName = theTask.getTaskName();

        String taskNameInList = getTaskNameShowing(taskName);
        viewHolder.isTaskFinishedCheckBox.setText(taskNameInList);
    }

    private String getTaskNameShowing(String taskName){

        int wholeWeight = 0;
        wholeWeight += getSpaceNumber(theTask) * 5;
        for (int i = 0; i < taskName.length(); i++){
            char theChar = taskName.charAt(i);
            int theWeight = 0;

            if (theChar == 'm' || theChar == 'w'){
                theWeight += 3;
            }
            else if (theChar == 'i' || theChar == '1' || theChar == 'l' || theChar == 't'|| theChar == 'j'){
                theWeight += 1;
            }
            else if (theChar<='z'&&theChar>='a' || theChar<='Z'&&theChar>='A' || theChar<='9'&&theChar>='0'){
                theWeight += 2;
            }
            else if (theChar == ' '){
                theWeight += 1;
            }
            else {
                theWeight += 4;
            }
            wholeWeight += theWeight;

            if (wholeWeight >= THE_MAX_CHINESE_WEIGHT){
                if (theWeight == 4 || theWeight == 3) {
                    return (taskName.substring(0, i - 1) + "...");
                }
                else{
                    return (taskName.substring(0, i) + "...");
                }
            }
        }

        return taskName;

    }

    private void setTaskFinishTimeTextView() {
        viewHolder.taskFinishTimeTextView.setText(theTask.getFinishedDate().listShowString());
    }

    private int getSpaceNumber(Task theTask) {
        return theTask.getTaskAttribute().getSelectedPosition();
    }

    class HeaderViewHolder {
        LinearLayout headLayout;
        TextView timeTextView;
        TextView amountTextView;
    }

    class ViewHolder {
        ImageView tagColorImageView;
        Space space;
        CheckBox isTaskFinishedCheckBox;
        TextView taskFinishTimeTextView;
    }
}
