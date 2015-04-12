package com.naddiaz.tfg.bletasker.listeners;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSDataHashRegistration;
import com.naddiaz.tfg.bletasker.widget.MainActivity;

/**
 * Created by nad on 9/04/15.
 */
public class DataHashRegisterListener implements View.OnClickListener {

    private static final String TAG = "DataHashRegisterListener";
    private EditText hashRegister;
    private Context ctx;

    public DataHashRegisterListener(Context ctx, EditText edTxtDataHashRegister) {
        this.ctx = ctx;
        this.hashRegister = edTxtDataHashRegister;
    }

    @Override
    public void onClick(View v) {
        new WSDataHashRegistration(ctx,hashRegister.getText().toString()).checkRegisterHash();
    }
}
