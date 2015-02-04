package com.naddiaz.tfg.managermodule;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


/**
 * Created by nad on 2/02/15.
 */
public class OnClick_ws_date implements View.OnClickListener {

    String url="http://tfg.naddiaz.com:3000/ws";
    View rootView;

    public OnClick_ws_date(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void onClick(View v) {
        GetWS ws = new GetWS(rootView,url);
        Log.d("TRACE",url);
        ws.execute("year","month","day","hour","minute","second");
    }

}
