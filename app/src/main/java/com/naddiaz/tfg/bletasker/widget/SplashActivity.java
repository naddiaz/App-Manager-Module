package com.naddiaz.tfg.bletasker.widget;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.listeners.DataHashRegisterListener;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSDataHashRegistration;
import com.naddiaz.tfg.bletasker.webservices.WSLoadWorks;

public class SplashActivity extends ActionBarActivity {

    protected static final String LOADING_FRAGMENT_TAG = "LoadingFragment";
    protected static final String READDATA_FRAGMENT_TAG = "ReadDataFragment";
    protected static final String ADVERTISEMT_FRAGMENT_TAG = "AdvertisementFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        UserPrefecences userPrefecences = new UserPrefecences(this.getApplication()).readPreferences();

        if(userPrefecences != null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PreLoadingFragment())
                    .commit();
            new WSDataHashRegistration(this,userPrefecences.getHash()).verifySessionHash();
        }
        else if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new LoadingFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A PreLoading Fragment, is only a simple animation fragment
     */
    public static class PreLoadingFragment extends Fragment {

        private static final String TAG = "PreLoadingFragment";

        public PreLoadingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
            return rootView;
        }
    }

    /**
     * A Loading Fragment, is only a simple animation fragment
     */
    public static class LoadingFragment extends Fragment {

        private static final String TAG = "LoadingFragment";
        private static final int LOADING_TIME = 1000;
        private static final int LOADING_TIME_REFRESH = 500;

        public LoadingFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash, container, false);
            new CountDownTimer(LOADING_TIME,LOADING_TIME/LOADING_TIME_REFRESH) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_side_rigth, R.animator.slide_side_rigth);
                    ft.replace(R.id.container, new AdvertisementFragment(), ADVERTISEMT_FRAGMENT_TAG);
                    ft.commit();
                }
            }.start();
            return rootView;
        }
    }

    /**
     * A Advertisement Fragment, is only welcome text
     */
    public static class AdvertisementFragment extends Fragment {

        private static final String TAG = "AdvertisementFragment";

        public AdvertisementFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash_advertisement, container, false);
            Button btnAdvertisementOk = (Button) rootView.findViewById(R.id.btnAdvertisementOk);
            btnAdvertisementOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.setCustomAnimations(R.animator.slide_side_rigth, R.animator.slide_side_rigth);
                    ft.replace(R.id.container, new ReadDataFragment(), READDATA_FRAGMENT_TAG);
                    ft.commit();
                }
            });
            return rootView;
        }
    }

    /**
     * A ReadData Fragment, need for generate SharedPreferences and check user
     */
    public static class ReadDataFragment extends Fragment {

        private static final String TAG = "ReadDataFragment";
        private static final int CONFIRM_ACTION = 0;

        public ReadDataFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_splash_data, container, false);
            final EditText edTxtDataHashRegister = (EditText) rootView.findViewById(R.id.edTxtDataHashRegister);
            edTxtDataHashRegister.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == CONFIRM_ACTION) {
                        new WSDataHashRegistration(getActivity(),edTxtDataHashRegister.getText().toString()).checkRegisterHash();
                        handled = true;
                    }
                    return handled;
                }
            });
            Button btnDataHashRegister = (Button) rootView.findViewById(R.id.btnDataHashRegister);
            btnDataHashRegister.setOnClickListener(new DataHashRegisterListener(getActivity(),edTxtDataHashRegister));
            return rootView;
        }
    }
}
