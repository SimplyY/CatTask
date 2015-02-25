package com.example.yuwei.killexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.tools.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

import java.util.List;

/**
 * Created by yuwei on 15/2/25.
 */
public class TaskListAdapter extends ArrayAdapter<Task>{
    View view;
    ViewHolder viewHolder;
    int resourceId;

    Task theTask;

    public TaskListAdapter(Context context, int textViewResourceId, List<Task> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        theTask = getItem(position);

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            initViewHolder(viewHolder);

            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        setTaskHolder(viewHolder);

        return view;
    }

    private void initViewHolder(ViewHolder viewHolder){

        viewHolder.tagColorImageView = (ImageView)view.findViewById(R.id.taskListColorTagImageView);

        viewHolder.spaceTextView = (TextView)view.findViewById(R.id.taskListSpaceTextView);

        viewHolder.isTaskFinishedCheckBox = (CheckBox)view.findViewById(R.id.taskListIsTaskFinishedCheckBox);


        viewHolder.taskNameTextView = (TextView)view.findViewById(R.id.taskListNameTextView);

        viewHolder.taskFinishTimeTextView = (TextView)view.findViewById(R.id.taskListFinishTimeTextView);
    }

    private void setTaskHolder(ViewHolder viewHolder){
        setColorImageView();
        setSpaceTextView();
        setIsTaskFinishedCheckBox();
        setTaskNameTextView();
        setTaskFinishTimeTextView();
    }

    private void setColorImageView(){
        int tagColorResId = getTagResId(theTask);

        viewHolder.tagColorImageView.setImageResource(tagColorResId);
    }

    private void setSpaceTextView(){
        int spaceNumber = getSpaceNumber(theTask);

        int spaceLength = viewHolder.spaceTextView.getWidth();
        viewHolder.spaceTextView.setWidth(spaceLength * spaceNumber);

    }

    private void setIsTaskFinishedCheckBox(){
        viewHolder.isTaskFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyDatabaseHelper.updateIsTaskFinished(getContext(), theTask, isChecked);
            }
        });
    }

    private void setTaskNameTextView(){

        String taskName = theTask.getTaskName();
        String taskNameInList = taskName.length()<5 ? taskName:(taskName.substring(0,5) + "...");
        viewHolder.taskNameTextView.setText(taskNameInList);
    }

    private void setTaskFinishTimeTextView(){
        viewHolder.taskFinishTimeTextView.setText(theTask.getFinishedTime().listString());
    }

    private int getSpaceNumber(Task theTask){
        return theTask.getTaskAttribute().getPosition();
    }

    private int getTagResId(Task theTask){
        SpinnerValue tagColorSpinnerValue = theTask.getTagColor();
        int tagResId = R.drawable.tag_color_white;
        switch (tagColorSpinnerValue.getSelectedName()){
            case "白色":
                tagResId = R.drawable.tag_color_white;
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

    class ViewHolder{
        ImageView tagColorImageView;
        TextView spaceTextView;
        CheckBox isTaskFinishedCheckBox;
        TextView taskNameTextView;
        TextView taskFinishTimeTextView;
    }
}
