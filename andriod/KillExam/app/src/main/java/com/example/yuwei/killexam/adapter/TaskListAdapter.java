package com.example.yuwei.killexam.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.database.MyDatabaseHelper;
import com.example.yuwei.killexam.map.HeaderTimeMapString;
import com.example.yuwei.killexam.map.SpinnerValue;
import com.example.yuwei.killexam.tools.Task;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by yuwei on 15/2/25.
 */
public class TaskListAdapter extends ArrayAdapter<Task> implements StickyListHeadersAdapter {
    View view;
    ViewHolder viewHolder;
    int resourceId;

    Task theTask;
    Context mContext;

    private LayoutInflater inflater;
    List<Task> tasks;

    public TaskListAdapter(Context context, int textViewResourceId, List<Task> objects) {
        super(context, textViewResourceId, objects);

        inflater = LayoutInflater.from(context);
        mContext = context;
        tasks = objects;

        resourceId = textViewResourceId;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Task getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        theTask = getItem(position);

        if (convertView == null) {
            view = inflater.inflate(resourceId, null);

            viewHolder = new ViewHolder();
            initViewHolder(viewHolder);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        setTaskHolder(viewHolder);

        return view;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headHolder;
        if (convertView == null) {
            headHolder = new HeaderViewHolder();
            convertView = inflater.inflate(R.layout.header, parent, false);
            headHolder.timeTextView = (TextView)convertView.findViewById(R.id.timeHeader);
            headHolder.amountTextView = (TextView)convertView.findViewById(R.id.amountHeader);
            headHolder.headLayout = (LinearLayout)convertView.findViewById(R.id.headerLinearLayout);
            convertView.setTag(headHolder);
        } else {
            headHolder = (HeaderViewHolder)convertView.getTag();
        }
        String headerTimeString;
        HeaderTimeMapString headerTimeMapString = new HeaderTimeMapString();
        headerTimeString = headerTimeMapString.getValue(tasks.get(position));
        int imageId = headerTimeMapString.getImageId(tasks.get(position));

        int amount = 0;

        headHolder.timeTextView.setText(headerTimeString);
        headHolder.headLayout.setBackground(mContext.getResources().getDrawable(imageId));
        headHolder.amountTextView.setText(String.valueOf(amount));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        int headerId;

        HeaderTimeMapString headerTimeMapString = new HeaderTimeMapString();
        headerId = headerTimeMapString.getKey(tasks.get(position));

        return headerId;
    }


    private void initViewHolder(ViewHolder viewHolder) {

        viewHolder.tagColorImageView = (ImageView) view.findViewById(R.id.taskListColorTagImageView);

        viewHolder.space = (Space) view.findViewById(R.id.taskListSpace);

        viewHolder.isTaskFinishedCheckBox = (CheckBox) view.findViewById(R.id.taskListIsTaskFinishedCheckBox);


        viewHolder.taskNameTextView = (TextView) view.findViewById(R.id.taskListNameTextView);

        viewHolder.taskFinishTimeTextView = (TextView) view.findViewById(R.id.taskListFinishTimeTextView);
    }

    private void setTaskHolder(ViewHolder viewHolder) {
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
        viewHolder.isTaskFinishedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MyDatabaseHelper.updateIsTaskFinished(getContext(), theTask, isChecked);
            }
        });
    }

    private void setTaskNameTextView() {
        final int THE_MAX_LENGTH = 8;

        String taskName = theTask.getTaskName();
        String taskNameInList = taskName.length() < THE_MAX_LENGTH ? taskName : (taskName.substring(0, THE_MAX_LENGTH) + "...");
        viewHolder.taskNameTextView.setText(taskNameInList);
    }

    private void setTaskFinishTimeTextView() {
        viewHolder.taskFinishTimeTextView.setText(theTask.getFinishedDate().listString());
    }

    private int getSpaceNumber(Task theTask) {
        return theTask.getTaskAttribute().getPosition();
    }

    private int getTagResId(Task theTask) {
        SpinnerValue tagColorSpinnerValue = theTask.getTagColor();
        int tagResId = R.drawable.tag_color_white;
        switch (tagColorSpinnerValue.getSelectedName()) {
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

    class HeaderViewHolder {
        LinearLayout headLayout;
        TextView timeTextView;
        TextView amountTextView;
    }

    class ViewHolder {
        ImageView tagColorImageView;
        Space space;
        CheckBox isTaskFinishedCheckBox;
        TextView taskNameTextView;
        TextView taskFinishTimeTextView;
    }
}
