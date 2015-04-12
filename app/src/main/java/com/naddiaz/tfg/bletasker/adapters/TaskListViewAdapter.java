package com.naddiaz.tfg.bletasker.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.database.Work;

import java.util.ArrayList;

/**
 * Created by nad on 12/04/15.
 */
public class TaskListViewAdapter extends BaseAdapter{

    private static final String TAG = "TaskListViewAdapter";

    private Context ctx;
    private ArrayList<Work> listArray;
    private String state;
    private LayoutInflater inflater;

    public TaskListViewAdapter(Context ctx, ArrayList<Work> listArray, String state){
        this.ctx = ctx;
        this.listArray = listArray;
        this.state = state;
        inflater  = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.i(TAG,"COUNT: " + getCount());
    }
    @Override
    public int getCount() {
        return listArray.size();
    }

    @Override
    public Object getItem(int position) {
        return listArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG,"View: " + position + convertView);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item, null);
        }

        final Work taskItem = listArray.get(position);

        String date = taskItem.getCreated_at().substring(0,10);
        String hour = taskItem.getCreated_at().substring(11,19);

        TextView txtItemIdTask = (TextView) convertView.findViewById(R.id.txtItemIdTask);
        txtItemIdTask.setText(taskItem.getId_task());

        TextView txtItemDate = (TextView) convertView.findViewById(R.id.txtItemDate);
        txtItemDate.setText(date);

        TextView txtItemHour = (TextView) convertView.findViewById(R.id.txtItemHour);
        txtItemHour.setText(hour);

        TextView txtItemDescription = (TextView) convertView.findViewById(R.id.txtItemDescription);
        txtItemDescription.setText(taskItem.getDescription());

        TextView txtItemPriority = (TextView) convertView.findViewById(R.id.txtItemPriority);
        txtItemPriority.setText(String.valueOf(taskItem.getPriority()));

        TextView txtItemNEmployees = (TextView) convertView.findViewById(R.id.txtItemNEmployees);
        txtItemNEmployees.setText(String.valueOf(taskItem.getN_employees()));

        LinearLayout lytItemTopBlock = (LinearLayout) convertView.findViewById(R.id.lytItemTopBlock);
        LinearLayout lytItemActions = (LinearLayout) convertView.findViewById(R.id.lytItemActions);

        Button btnItemRun = (Button) convertView.findViewById(R.id.btnItemRun);
        if(this.state == Work.STATE_ACTIVE){
            btnItemRun.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_teal_500));
        }
        else {
            btnItemRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "TASK RUN: " + taskItem.getId_task());
                }
            });
        }

        Button btnItemPause = (Button) convertView.findViewById(R.id.btnItemPause);
        if(this.state == Work.STATE_PENDING){
            btnItemPause.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_orange_500));
        }
        else {
            btnItemPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "TASK PAUSE: " + taskItem.getId_task());
                }
            });
        }

        Button btnItemFinish = (Button) convertView.findViewById(R.id.btnItemFinish);
        if(this.state == Work.STATE_COMPLETE){
            btnItemFinish.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_blue_500));
        }
        else {
            btnItemFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "TASK FINISH: " + taskItem.getId_task());
                }
            });
        }

        if(this.state == Work.STATE_PAUSE){
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_red_500));
        }

        Button btnItemCancel = (Button) convertView.findViewById(R.id.btnItemCancel);
        if(this.state == Work.STATE_CANCEL){
            lytItemActions.setVisibility(View.INVISIBLE);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_red_500));
        }
        else {
            btnItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "TASK CANCEL: " + taskItem.getId_task());
                }
            });
        }
        return convertView;
    }
}
