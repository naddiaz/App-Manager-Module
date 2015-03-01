package com.naddiaz.tfg.bletasker.listeners;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.services.BeaconIntentService;
import com.naddiaz.tfg.bletasker.widget.HomeActivity;
import com.naddiaz.tfg.bletasker.widget.LoginActivity;

/**
 * Created by nad on 1/03/15.
 */
public class CheckUserListener implements View.OnClickListener {

    EditText ed_user;
    EditText ed_password;

    PendingIntent openActivity;

    ActionBarActivity parentActivity;
    Context ctx;

    private static final int PENDING_DEFAULT_NOTIFICATION = 1;

    public CheckUserListener(ActionBarActivity parentActivity,EditText ed_user, EditText ed_password) {
        this.parentActivity = parentActivity;
        this.ed_user = ed_user;
        this.ed_password = ed_password;
    }

    @Override
    public void onClick(View v) {
        this.ctx = v.getContext();
        //No hay registro de usuarios en el WS por lo que solamente se guarda el
        //ID usuario en SharedPreferences para transferir los datos entre activities

        if(!ed_user.getText().toString().equals("")){
            if(setUserPreferences(ed_user.getText().toString())){

                //Creamos un intent para el PendingIntent
                Intent intent = new Intent();
                intent.setClass(ctx,HomeActivity.class);
                //Creamos el PendingIntent
                openActivity = PendingIntent.getActivity(ctx, 0, intent, 0);

                Notification.Builder builder = new Notification.Builder(ctx);
                NotificationManager notificationManager = (NotificationManager)ctx.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(PENDING_DEFAULT_NOTIFICATION,getDefaultNotification(builder));

                //Creamos un intent para el BeaconIntentService
                Intent beaconIntent = new Intent(parentActivity, BeaconIntentService.class);
                ctx.startService(beaconIntent);
                //Creamos un intent para el HomeActivity
                Intent homeActivityIntent = new Intent(parentActivity, HomeActivity.class);
                ctx.startActivity(homeActivityIntent);
            }
        }


    }

    private Notification getDefaultNotification(Notification.Builder builder) {
        builder
                .setSmallIcon(R.drawable.bletaskermini)
                .setContentTitle("BLE Tasker")
                .setContentText("Welcome <WORKER USERNAME>")
                .setOngoing(true)
                .addAction(R.drawable.ic_action_accept,"Open",openActivity)
                .addAction(R.drawable.ic_action_overflow,"View Tasks",null);

        return builder.build();
    }

    public boolean setUserPreferences(String id){
        SharedPreferences prefs = ctx.getSharedPreferences("bleTaskerPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("prefSave", true);
        editor.putString("IDuser", id);
        if (editor.commit()) return true;
        else return false;
    }
}
