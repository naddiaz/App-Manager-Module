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

/**
 * Created by nad on 12/04/15.
 */
@SuppressLint("ValidFragment")
public class FinishWorkDialog extends DialogFragment {


    TaskListViewAdapter taskListViewAdapter;
    View child;
    int position;

    public FinishWorkDialog(TaskListViewAdapter taskListViewAdapter, View childAt, int position) {
        this.taskListViewAdapter = taskListViewAdapter;
        this.child = childAt;
        this.position = position;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.dialog_finish_work_message));

        builder.setPositiveButton(getResources().getString(R.string.dialog_finish_work), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                taskListViewAdapter.removeListItem(child,position);
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }
}