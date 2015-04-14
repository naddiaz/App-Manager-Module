package com.naddiaz.tfg.bletasker.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSGcmRegistration;

/**
 * Created by nad on 12/04/15.
 */
public class UnlinkDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(getResources().getString(R.string.dialog_unlink_message));
        builder.setMessage(getResources().getString(R.string.dialog_unlink_message_description));

        builder.setPositiveButton(getResources().getString(R.string.dialog_unlink_device), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                UserPrefecences userPreferences = new UserPrefecences(getActivity()).readPreferences();
                new WSGcmRegistration(getActivity(),userPreferences.getId_airport(),userPreferences.getId_person(),userPreferences.getWorker_name(),userPreferences.getHash()).unlinkRegistrationId();
                userPreferences.clearPreferences();
                getActivity().finish();
            }
        });

        builder.setNegativeButton(getResources().getString(R.string.dialog_no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        return builder.create();
    }
}