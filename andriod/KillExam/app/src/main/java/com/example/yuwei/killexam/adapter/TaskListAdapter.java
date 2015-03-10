package com.example.yuwei.killexam.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Space;
import android.widget.TextView;

import com.example.yuwei.killexam.MainActivity;
import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.map.HeaderTimeMapString;
import com.example.yuwei.killexam.map.SpinnerValue;
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

    final int THE_MAX_LENGTH = 10;

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

        setActionBar();

        return view;
    }

    private void setActionBar(){
        int position = TaskListFragment.taskListView.getFirstVisiblePosition();

        if (firstVisiblePosition != position){
            firstVisiblePosition = position;
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
        int tagColorResId = getTagResId(theTask);

        viewHolder.tagColorImageView.setImageResource(tagColorResId);
    }

    private void setSpaceWidth() {
        int spaceAmount = getSpaceNumber(theTask);

        int spaceLength = 60;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewHolder.space.getLayoutParams();

        layoutParams.width = spaceLength * spaceAmount;

        viewHolder.space.setLayoutParams(layoutParams);

    }

    private void setIsTaskFinishedCheckBox() {

        Task task = sortedTODOtasks.get(viewHolderPosition);
        CheckBox checkBox = viewHolder.isTaskFinishedCheckBox;

        if (task.getHasFinished()==1){
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
                    checkedTask = TaskTree.getTask(buttonView.getText().toString(), THE_MAX_LENGTH);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("checkedTask","checkedTask get wrong");
                }

                MyDatabaseHelper.updateIsTaskFinished(getContext(), checkedTask, isChecked);

                TaskTree.renewSortedTaskArray(checkedTask, buttonView, taskListAdapter);
            }
        });
    }

    private void setTaskNameTextView() {

        String taskName = theTask.getTaskName();
        String taskNameInList = taskName.length() < THE_MAX_LENGTH + 1 ? taskName : (taskName.substring(0, THE_MAX_LENGTH) + "...");
        viewHolder.isTaskFinishedCheckBox.setText(taskNameInList);
    }

    private void setTaskFinishTimeTextView() {
        viewHolder.taskFinishTimeTextView.setText(theTask.getFinishedDate().listString());
    }

    private int getSpaceNumber(Task theTask) {
        return theTask.getTaskAttribute().getSelectedPosition();
    }

    private int getTagResId(Task theTask) {
        SpinnerValue tagColorSpinnerValue = theTask.getTagColor();
        int tagResId = R.drawable.tag_color_green;
        switch (tagColorSpinnerValue.getSelectedName()) {
            case "白色":
                tagResId = R.drawable.tag_color_green;
                break;
            case "紫色":
                tagResId = R.drawable.tag_color_purple;
                break;
            case "蓝色":
                tagResId = R.drawable.tag_color_blue;
                break;
            case "红色":
                tagResId = R.drawable.tag_color_red;
                break;
        }
        return tagResId;
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
