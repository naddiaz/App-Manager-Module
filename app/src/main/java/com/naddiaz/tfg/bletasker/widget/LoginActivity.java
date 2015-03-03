package com.naddiaz.tfg.bletasker.widget;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.naddiaz.tfg.bletasker.listeners.CheckUserListener;
import com.naddiaz.tfg.bletasker.R;


public class LoginActivity extends ActionBarActivity {

    public static ActionBarActivity login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login = this;

        //Hide ActionBar
        getSupportActionBar().hide();

        SharedPreferences prefs = getSharedPreferences("bleTaskerPreferences", MODE_PRIVATE);
        Boolean prefSave = prefs.getBoolean("prefSave", false);

        if(prefSave){
            Intent homeActivityIntent = new Intent(getBaseContext(), HomeDrawerActivity.class);
            startActivity(homeActivityIntent);
        }
        else {
            setContentView(R.layout.activity_login);
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new PlaceholderFragment())
                        .commit();
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    @SuppressLint("ValidFragment")
    public class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);

            //Crear una nueva actividad tras el login

            EditText ed_user = (EditText) rootView.findViewById(R.id.ed_user);
            EditText ed_password = (EditText) rootView.findViewById(R.id.ed_password);
            Button btn_login = (Button) rootView.findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new CheckUserListener(LoginActivity.this,ed_user,ed_password));


            return rootView;
        }
    }
}
