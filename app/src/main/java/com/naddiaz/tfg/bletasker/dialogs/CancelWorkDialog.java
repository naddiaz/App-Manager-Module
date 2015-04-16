package com.naddiaz.tfg.bletasker.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.adapters.TaskListViewAdapter;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.database.WorksDbHelper;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSWorkState;
import com.naddiaz.tfg.bletasker.widget.MainActivity;

/**
 * Created by nad on 12/04/15.
 */
@SuppressLint("ValidFragment")
public class CancelWorkDialog extends DialogFragment {


    TaskListViewAdapter taskListViewAdapter;
    View child;
    int position;
    private WorksDbHelper workDB;
    private WSWorkState wsWorksState;
    private Work taskItem;

    public CancelWorkDialog(TaskListViewAdapter taskListViewAdapter, View childAt, int position, Work taskItem, WorksDbHelper worksDbHelper, WSWorkState wsWorksState) {
        this.taskListViewAdapter = taskListViewAdapter;
        this.child = childAt;
        this.position = position;
        this.taskItem = taskItem;
        this.wsWorksState = wsWorksState;
        this.workDB = worksDbHelper;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.dialog_cancel_work_message));
        builder.setMessage(getResources().getString(R.string.dialog_cancel_work_description));

        builder.setPositiveButton(getResources().getString(R.string.dialog_cancel_work), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                taskListViewAdapter.removeListItem(child,position);
                workDB.updateWorkState(taskItem, Work.STATE_CANCEL);
                wsWorksState.setWorkState(taskItem.getId_task(), Work.STATE_CANCEL);
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }
}