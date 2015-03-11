package com.example.yuwei.killexam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yuwei.killexam.R;
import com.example.yuwei.killexam.tools.Task;

import java.util.List;

/**
 * Created by yuwei on 15/2/19.
 */
public class BelongTaskAdapter extends ArrayAdapter<Task>{
    View view;
    ViewHolder viewHolder;
    int resourceId;

    public BelongTaskAdapter(Context context, int textViewResourceId, List<Task> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Task task = getItem(position);
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.taskNameText = (TextView)view.findViewById(R.id.belongTaskTextView);
            viewHolder.finishedTimeText = (TextView)view.findViewById(R.id.belongTaskFinishTimeText);

            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.taskNameText.setText(task.getTaskName());
        viewHolder.finishedTimeText.setText(task.getFinishedDate().listShowString());

        return view;
    }


    class ViewHolder{
        TextView taskNameText;
        TextView finishedTimeText;
    }
}
