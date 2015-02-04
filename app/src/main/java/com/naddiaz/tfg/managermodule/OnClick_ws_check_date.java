package com.naddiaz.tfg.managermodule;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nad on 4/02/15.
 */
public class OnClick_ws_check_date implements View.OnClickListener {


    String url;
    View rootView;

    public OnClick_ws_check_date(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void onClick(View v) {
        EditText day = (EditText) rootView.findViewById(R.id.edtxt_day);
        EditText month = (EditText) rootView.findViewById(R.id.edtxt_month);
        EditText year = (EditText) rootView.findViewById(R.id.edtxt_year);
        url = "http://tfg.naddiaz.com:3000/ws/" + day.getText().toString() + "/" + month.getText().toString() + "/" + year.getText().toString();
        GetWS ws = new GetWS(rootView,url);
        ws.execute("diff","day_week");
    }
}
