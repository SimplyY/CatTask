package com.example.yuwei.killexam.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yuwei.killexam.R;

import java.util.List;

/**
 * Created by yuwei on 15/2/19.
 */
public class BelongTaskAdapter extends ArrayAdapter<String>{
    View view;
    ViewHolder viewHolder;
    int resourceId;

    public BelongTaskAdapter(Context context, int textViewResourceId, List<String> objects){
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        String taskName = getItem(position);

        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);

            viewHolder = new ViewHolder();
            viewHolder.taskFinishCheckBox = (TextView)view.findViewById(R.id.belongTaskTextView);

            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.taskFinishCheckBox.setText(taskName);

        return view;
    }


    class ViewHolder{
        TextView taskFinishCheckBox;
    }
}
