package com.naddiaz.tfg.managermodule;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by nad on 2/02/15.
 */
public class GetWS  extends AsyncTask<String, Void, Boolean> {

    String url;
    View rootView;
    JSONObject jObject;
    String[] params;

    public GetWS(View rootView, String url) {
        this.rootView = rootView;
        this.url = url;
    }


    @Override
    protected Boolean doInBackground(String... params) {
        try{
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);

            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream in = httpEntity.getContent();
            jObject = new JSONObject(new streamToString(in).convert());
            this.params = params;

            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result){

        for (int i = 0; i < params.length; i++) {
            int id = rootView.getResources().getIdentifier("txt_"+params[i],"id",rootView.getContext().getPackageName());
            Log.d("TRACE", String.valueOf(id) + ": ");
            TextView txt = (TextView) rootView.findViewById(id);
            try {
                txt.setText(jObject.getString(params[i]));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
