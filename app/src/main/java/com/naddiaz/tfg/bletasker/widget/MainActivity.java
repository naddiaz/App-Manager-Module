package com.naddiaz.tfg.bletasker.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.heinrichreimersoftware.materialdrawer.DrawerFrameLayout;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerItem;
import com.heinrichreimersoftware.materialdrawer.structure.DrawerProfile;
import com.naddiaz.tfg.bletasker.R;
import com.naddiaz.tfg.bletasker.adapters.TaskListViewAdapter;
import com.naddiaz.tfg.bletasker.database.Work;
import com.naddiaz.tfg.bletasker.database.WorksDbHelper;
import com.naddiaz.tfg.bletasker.dialogs.LogoutDialog;
import com.naddiaz.tfg.bletasker.dialogs.UnlinkDialog;
import com.naddiaz.tfg.bletasker.services.BeaconManager;
import com.naddiaz.tfg.bletasker.utils.RSACrypt;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;
import com.naddiaz.tfg.bletasker.webservices.WSLoadWorks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final String TASKS_FRAGMENT_TAG = "TasksFragment";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private CharSequence itemTitle;
    private String actualView = Work.STATE_ACTIVE;

    private static final String ACTION_MESSAGE = "message";
    private static final String ACTION_HISTORY = "history";
    private static final String ACTION_LOGOUT = "logout";
    private static final String ACTION_UNLINK = "unlink";

    private static final String ICON_NEW = "ic_action_new";
    private static final String ICON_CANCEL = "ic_action_cancel";
    private static final String ICON_PAUSE = "ic_action_pause";
    private static final String ICON_DISCARD = "ic_action_discard";
    private static final String ICON_ACCEPT = "ic_action_accept";
    private static final String ICON_CHAT = "ic_action_chat";
    private static final String ICON_REFRESH = "ic_action_refresh";
    private static final String ICON_LOGOUT = "ic_action_logout";
    private static final String ICON_UNLINK = "ic_action_unlink";


    DrawerFrameLayout drawer;

    private String[] tagTasks;
    private final static String[] iconTasks = {ICON_NEW,ICON_CANCEL,ICON_PAUSE,ICON_DISCARD,ICON_ACCEPT};
    private final static String[] actionsTasks = {Work.STATE_ACTIVE,Work.STATE_PENDING,Work.STATE_PAUSE,Work.STATE_CANCEL,Work.STATE_COMPLETE};

    private String[] tagOptions;
    private final static String[] iconOptions = {ICON_CHAT,ICON_REFRESH};
    private final static String[] actionsOptions = {ACTION_MESSAGE,ACTION_HISTORY};

    private String[] tagConfiguration;
    private final static String[] iconConfiguration = {ICON_LOGOUT,ICON_UNLINK};
    private final static String[] actionsConfiguration = {ACTION_LOGOUT,ACTION_UNLINK};

    private WorksDbHelper worksDbHelper;
    private WSLoadWorks wsLoadWorks;
    Context context;


    UserPrefecences userPrefecences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        context = getApplicationContext();
        userPrefecences = new UserPrefecences(getApplication()).readPreferences();
        if (checkPlayServices()) {
            Log.i(TAG, "Check beacon manager state: " + userPrefecences.getBeaconManagerState());
            if(userPrefecences.getBeaconManagerState()){
                userPrefecences.saveBeaconManagerState(false);
                Intent intent = new Intent(MainActivity.this, BeaconManager.class);
                startService(intent);
            }

            worksDbHelper = new WorksDbHelper(context);
            wsLoadWorks = new WSLoadWorks(context,userPrefecences.getHash());
            createDrawerNavigation();
            if (savedInstanceState == null) {
                Bundle extras = getIntent().getExtras();
                if(extras == null) {
                    selectItem(actualView);
                    setTitle(itemTitle);
                }
                else {
                    Boolean pendingIntent = extras.getBoolean(Work.PENDING_TASK);
                    if(pendingIntent){
                        actualView = Work.STATE_PENDING;
                        selectItem(actualView);
                        setTitle(itemTitle);

                    }
                }
            }
        }
        else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
        Log.i(TAG,"onCreate");
    }

    @Override
    protected void onResume(){
        super.onResume();
        checkPlayServices();
        setTitle(itemTitle);
        wsLoadWorks.getWorks();
        Log.i(TAG,"onResume");
    }


    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"onPause");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    private void createDrawerNavigation(){
        itemTitle = getTitle();
        tagTasks = getResources().getStringArray(R.array.tagTasks);
        tagOptions = getResources().getStringArray(R.array.tagOptions);
        tagConfiguration = getResources().getStringArray(R.array.tagConfiguration);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.color.md_green_500));

        drawer = (DrawerFrameLayout) findViewById(R.id.drawer);
        drawer.setProfile(
                new DrawerProfile()
                        .setAvatar(getResources().getDrawable(R.drawable.logo_full))
                        .setBackground(getResources().getDrawable(R.color.green_grey))
                        .setName(userPrefecences.getWorker_name().toUpperCase())
                        .setDescription(getString(R.string.subheader_navigation) + userPrefecences.getId_person())
        );
        drawer.addItem(
                new DrawerItem().setTextPrimary(tagTasks[0])
        );
        drawer.addDivider();
        for(int i=1; i<tagTasks.length; i++) {
            final int finalI = i;
            drawer.addItem(
                    new DrawerItem()
                            .setTextPrimary(tagTasks[i])
                            .setImage(getResources().getDrawable(convertStringToDrawable(iconTasks[i - 1])))
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int position, int i2) {
                                    drawer.closeDrawer();
                                    selectItem(actionsTasks[finalI-1]);
                                }
                            })
            );
        }
        /*drawer.addItem(
                new DrawerItem().setTextPrimary(tagOptions[0])
        );
        drawer.addDivider();
        for(int i=1; i<tagOptions.length; i++) {
            final int finalI = i;
            drawer.addItem(
                    new DrawerItem()
                            .setTextPrimary(tagOptions[i])
                            .setImage(getResources().getDrawable(convertStringToDrawable(iconOptions[i-1])))
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int position, int i2) {
                                    drawer.closeDrawer();
                                    selectItem(actionsOptions[finalI-1]);
                                }
                            })
            );
        }*/
        //Configuration elements into the DrawerNavigation
        drawer.addItem(
                new DrawerItem().setTextPrimary(tagConfiguration[0])
        );
        drawer.addDivider();
        for(int i=1; i<tagConfiguration.length; i++) {
            final int finalI = i;
            drawer.addItem(
                    new DrawerItem()
                            .setTextPrimary(tagConfiguration[i])
                            .setImage(getResources().getDrawable(convertStringToDrawable(iconConfiguration[i-1])))
                            .setOnItemClickListener(new DrawerItem.OnItemClickListener() {
                                @Override
                                public void onClick(DrawerItem drawerItem, int position, int i2) {
                                    drawer.closeDrawer();
                                    selectItem(actionsConfiguration[finalI-1]);
                                }
                            })
            );
        }
    }

    private int convertStringToDrawable(String iconTask) {
        switch (iconTask){
            case ICON_NEW: return R.drawable.ic_action_new;
            case ICON_CANCEL: return R.drawable.ic_action_cancel;
            case ICON_PAUSE: return R.drawable.ic_action_pause;
            case ICON_DISCARD: return R.drawable.ic_action_discard;
            case ICON_ACCEPT: return R.drawable.ic_action_accept;
            case ICON_CHAT: return R.drawable.ic_action_chat;
            case ICON_REFRESH: return R.drawable.ic_action_refresh;
            case ICON_LOGOUT: return R.drawable.ic_action_logout;
            case ICON_UNLINK: return R.drawable.ic_action_unlink;
            default:return R.drawable.ic_action_new;
        }
    }

    protected void selectItem(String action) {
        Log.i(TAG,"Action: " + action);
        switch (action){
            case Work.STATE_ACTIVE:
                ArrayList<Work> works_active = worksDbHelper.getWorksByState(Work.STATE_ACTIVE);
                ListView listView_active = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_active = new TaskListViewAdapter(context,works_active,Work.STATE_ACTIVE,getFragmentManager());
                listView_active.setAdapter(taskListViewAdapter_active);
                taskListViewAdapter_active.notifyDataSetChanged();
                setTitle(tagTasks[1]);
                actualView = Work.STATE_ACTIVE;
                break;
            case Work.STATE_PENDING:
                ArrayList<Work> works_pending = worksDbHelper.getWorksByState(Work.STATE_PENDING);
                ListView listView_pending = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_pending = new TaskListViewAdapter(context,works_pending,Work.STATE_PENDING,getFragmentManager());
                taskListViewAdapter_pending.notifyDataSetChanged();
                listView_pending.setAdapter(taskListViewAdapter_pending);
                taskListViewAdapter_pending.notifyDataSetChanged();
                setTitle(tagTasks[2]);
                actualView = Work.STATE_PENDING;
                break;
            case Work.STATE_PAUSE:
                ArrayList<Work> works_pause  = worksDbHelper.getWorksByState(Work.STATE_PAUSE);
                ListView listView_pause = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_pause = new TaskListViewAdapter(context,works_pause,Work.STATE_PAUSE,getFragmentManager());
                listView_pause.setAdapter(taskListViewAdapter_pause);
                taskListViewAdapter_pause.notifyDataSetChanged();
                setTitle(tagTasks[3]);
                actualView = Work.STATE_PAUSE;
                break;
            case Work.STATE_CANCEL:
                ArrayList<Work> works_cancel  = worksDbHelper.getWorksByState(Work.STATE_CANCEL);
                ListView listView_cancel = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_cancel = new TaskListViewAdapter(context,works_cancel,Work.STATE_CANCEL,getFragmentManager());
                listView_cancel.setAdapter(taskListViewAdapter_cancel);
                taskListViewAdapter_cancel.notifyDataSetChanged();
                setTitle(tagTasks[4]);
                actualView = Work.STATE_CANCEL;
                break;
            case Work.STATE_COMPLETE:
                ArrayList<Work> works_complete  = worksDbHelper.getWorksByState(Work.STATE_COMPLETE);
                ListView listView_complete = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_complete = new TaskListViewAdapter(context,works_complete,Work.STATE_COMPLETE,getFragmentManager());
                listView_complete.setAdapter(taskListViewAdapter_complete);
                taskListViewAdapter_complete.notifyDataSetChanged();
                setTitle(tagTasks[5]);
                actualView = Work.STATE_COMPLETE;
                break;
            /*
            case ACTION_MESSAGE:
                setTitle(tagOptions[1]);
                break;
            case ACTION_HISTORY:
                setTitle(tagOptions[2]);
                break;
             */
            case ACTION_LOGOUT:
                LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.show(getFragmentManager(), TAG);
                break;
            case ACTION_UNLINK:
                UnlinkDialog unlinkDialog = new UnlinkDialog();
                unlinkDialog.show(getFragmentManager(),TAG);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(drawer.isDrawerOpen(Gravity.START)) {
            drawer.closeDrawer();
        }
        else {
            drawer.openDrawer();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        itemTitle = title;
        getSupportActionBar().setTitle(itemTitle);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        // Saving variables
        savedInstanceState.putString("actualView", actualView);

        // Call at the end
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState){
        // Call at the start
        super.onRestoreInstanceState(savedInstanceState);

        // Retrieve variables
        actualView = savedInstanceState.getString("actualView");
        selectItem(actualView);
    }
}