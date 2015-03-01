package com.naddiaz.tfg.bletasker;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by nad on 1/03/15.
 */
public class CheckUserListener implements View.OnClickListener {

    EditText ed_user;
    EditText ed_password;


    public CheckUserListener(EditText ed_user, EditText ed_password) {
        this.ed_user = ed_user;
        this.ed_password = ed_password;
    }

    @Override
    public void onClick(View v) {

        //No hay registro de usuarios en el WS por lo que solamente se guarda el
        //ID usuario en SharedPreferences para transferir los datos entre activities

        if(!ed_user.getText().toString().equals("")){
            if(setUserPreferences(v,ed_user.getText().toString())){
                Toast.makeText(v.getContext(),"Preferencias Guardadas",Toast.LENGTH_SHORT).show();
            }
        }


    }

    public boolean setUserPreferences(View v, String id){
        SharedPreferences prefs = v.getContext().getSharedPreferences("bleTaskerPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("prefSave", true);
        editor.putString("IDuser", id);
        if (editor.commit()) return true;
        else return false;
    }
}
