package com.naddiaz.tfg.bletasker.adapters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.database.WorksDbHelper;
import com.naddiaz.tfg.bletasker.dialogs.CancelWorkDialog;
import com.naddiaz.tfg.bletasker.dialogs.FinishWorkDialog;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSWorkState;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by nad on 12/04/15.
 */
public class TaskListViewAdapter extends BaseAdapter{

    private static final String TAG = "TaskListViewAdapter";

    private FragmentManager manager;

    private Context ctx;
    private ArrayList<Work> listArray;
    private String state;
    private LayoutInflater inflater;
    private WorksDbHelper workDB;
    private WSWorkState wsWorksState;
    TaskListViewAdapter taskListViewAdapter;

    public TaskListViewAdapter(Context ctx, ArrayList<Work> listArray, String state, FragmentManager manager){
        this.ctx = ctx;
        this.listArray = listArray;
        this.state = state;
        this.manager = manager;

        inflater  = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        workDB = new WorksDbHelper(ctx);
        wsWorksState = new WSWorkState(ctx,new UserPrefecences(ctx).readPreferences().getHash());

        this.taskListViewAdapter = this;
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
    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, final ViewGroup parent) {
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
        txtItemDescription.setText(taskItem.getDescription().toUpperCase());

        TextView txtItemPriority = (TextView) convertView.findViewById(R.id.txtItemPriority);
        txtItemPriority.setText(String.valueOf(taskItem.getPriority()));

        TextView txtItemNEmployees = (TextView) convertView.findViewById(R.id.txtItemNEmployees);
        txtItemNEmployees.setText(String.valueOf(taskItem.getN_employees()));

        LinearLayout lytItemTopBlock = (LinearLayout) convertView.findViewById(R.id.lytItemTopBlock);
        LinearLayout lytItemActions = (LinearLayout) convertView.findViewById(R.id.lytItemActions);
        LinearLayout lytItemBottomBlock = (LinearLayout) convertView.findViewById(R.id.lytItemBottomBlock);

        Button btnItemRun = (Button) convertView.findViewById(R.id.btnItemRun);
        if(this.state == Work.STATE_ACTIVE){
            btnItemRun.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_teal_500));
        }
        else {
            btnItemRun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workDB.updateWorkState(taskItem, Work.STATE_ACTIVE);
                    wsWorksState.setWorkState(taskItem.getId_task(), Work.STATE_ACTIVE);
                    removeListItem(parent.getChildAt(position),position);
                }
            });
        }



        Button btnItemFinish = (Button) convertView.findViewById(R.id.btnItemFinish);
        if(this.state == Work.STATE_COMPLETE){
            lytItemActions.setVisibility(View.GONE);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_blue_500));
        }
        else {
            btnItemFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FinishWorkDialog finishWorkDialog = new FinishWorkDialog(taskListViewAdapter,parent.getChildAt(position), position,taskItem,workDB,wsWorksState);
                    finishWorkDialog.show(manager, "FinishDialog");
                }
            });
        }
        Button btnItemPause = (Button) convertView.findViewById(R.id.btnItemPause);
        if(this.state == Work.STATE_PENDING){
            btnItemPause.setEnabled(false);
            btnItemFinish.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_orange_500));
        }
        else {
            btnItemPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    workDB.updateWorkState(taskItem, Work.STATE_PAUSE);
                    wsWorksState.setWorkState(taskItem.getId_task(), Work.STATE_PAUSE);
                    removeListItem(parent.getChildAt(position),position);
                }
            });
        }

        if(this.state == Work.STATE_PAUSE){
            btnItemPause.setEnabled(false);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_red_200));
        }

        final Button btnItemCancel = (Button) convertView.findViewById(R.id.btnItemCancel);
        if(this.state == Work.STATE_CANCEL){
            lytItemActions.setVisibility(View.GONE);
            lytItemTopBlock.setBackgroundDrawable(ctx.getResources().getDrawable(R.color.md_red_500));
        }
        else {
            btnItemCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CancelWorkDialog cancelWorkDialog = new CancelWorkDialog(taskListViewAdapter,parent.getChildAt(position), position,taskItem,workDB,wsWorksState);
                    cancelWorkDialog.show(manager,"CancelDialog");
                }
            });
        }
        return convertView;
    }

    public void removeListItem(final View rowView, final int position) {
        final Animation animation = AnimationUtils.loadAnimation(
                ctx, android.R.anim.slide_out_right);
        animation.setDuration(500);
        rowView.startAnimation(animation);
        rowView.postDelayed(new Runnable() {
            @Override
            public void run() {
                listArray.remove(position);
                notifyDataSetChanged();
            }
        }, animation.getDuration());
    }
}
