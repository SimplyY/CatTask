package com.example.yuwei.killexam.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import com.example.yuwei.killexam.R;

import java.util.List;

/**
 * Created by yuwei on 15/2/19.
 */
public class TaskAdapter extends ArrayAdapter<Task>{
    View view;
    ViewHolder viewHolder;
    int resourceId;

    public TaskAdapter(Context context, int textViewResourceId, List<Task> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Task task = getItem(position);

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.taskFinishCheckBox = (CheckBox)view.findViewById(R.id.taskFinishCheckBox);

            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.taskFinishCheckBox.setText(task.getTaskName());

        return view;
    }


    class ViewHolder{
        CheckBox taskFinishCheckBox;
    }
}
