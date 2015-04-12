package com.naddiaz.tfg.bletasker.widget;


import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

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
import com.naddiaz.tfg.bletasker.fragments.HomeFragment;
import com.naddiaz.tfg.bletasker.utils.UserPrefecences;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private CharSequence itemTitle;
    private String actualView = Work.STATE_ACTIVE;

    DrawerFrameLayout drawer;

    private String[] tagTasks;
    private final static String[] iconTasks = {"ic_action_new","ic_action_cancel","ic_action_pause","ic_action_discard","ic_action_accept"};
    private final static String[] actionsTasks = {"active","pending","pause","cancel","complete"};

    private String[] tagOptions;
    private final static String[] iconOptions = {"ic_action_chat","ic_action_refresh"};
    private final static String[] actionsOptions = {"message","history"};

    private String[] tagConfiguration;
    private final static String[] iconConfiguration = {"ic_action_logout","ic_action_unlink"};
    private final static String[] actionsConfiguration = {"logout","unlink"};

    private WorksDbHelper worksDbHelper;
    Context context;

    UserPrefecences userPrefecences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        context = getApplicationContext();
        if (checkPlayServices()) {
            userPrefecences = new UserPrefecences(getApplication()).readPreferences();
            worksDbHelper = new WorksDbHelper(context);
            //worksDbHelper.clearWorks();
            createDrawerNavigation();
            if (savedInstanceState == null) {
                selectItem(actualView);
                setTitle(itemTitle);
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
        Log.i(TAG,"onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(TAG,"onPause");
        SplashActivity.ctx.finish();
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
                        .setDescription("ID empleado: " + userPrefecences.getId_person())
                        .setOnProfileClickListener(new DrawerProfile.OnProfileClickListener() {
                            @Override
                            public void onClick(DrawerProfile drawerProfile) {
                                Toast.makeText(MainActivity.this, "Clicked profile", Toast.LENGTH_SHORT).show();
                            }
                        })
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
        drawer.addItem(
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
        }
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
            case "ic_action_new": return R.drawable.ic_action_new;
            case "ic_action_cancel": return R.drawable.ic_action_cancel;
            case "ic_action_pause": return R.drawable.ic_action_pause;
            case "ic_action_discard": return R.drawable.ic_action_discard;
            case "ic_action_accept": return R.drawable.ic_action_accept;
            case "ic_action_chat": return R.drawable.ic_action_chat;
            case "ic_action_refresh": return R.drawable.ic_action_refresh;
            case "ic_action_logout": return R.drawable.ic_action_logout;
            case "ic_action_unlink": return R.drawable.ic_action_unlink;
            default:return R.drawable.ic_action_new;
        }
    }

    private void selectItem(String action) {
        Log.i(TAG,"Action: " + action);
        switch (action){
            case Work.STATE_ACTIVE:
                ArrayList<Work> works_active = worksDbHelper.getWorksByState(Work.STATE_ACTIVE);
                ListView listView_active = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_active = new TaskListViewAdapter(context,works_active,Work.STATE_ACTIVE);
                listView_active.setAdapter(taskListViewAdapter_active);
                taskListViewAdapter_active.notifyDataSetChanged();
                setTitle(tagTasks[1]);
                actualView = Work.STATE_ACTIVE;
                break;
            case Work.STATE_PENDING:
                ArrayList<Work> works_pending = worksDbHelper.getWorksByState(Work.STATE_PENDING);
                ListView listView_pending = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_pending = new TaskListViewAdapter(context,works_pending,Work.STATE_PENDING);
                listView_pending.setAdapter(taskListViewAdapter_pending);
                taskListViewAdapter_pending.notifyDataSetChanged();
                setTitle(tagTasks[2]);
                actualView = Work.STATE_PENDING;
                break;
            case Work.STATE_PAUSE:
                ArrayList<Work> works_pause  = worksDbHelper.getWorksByState(Work.STATE_PAUSE);
                ListView listView_pause = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_pause = new TaskListViewAdapter(context,works_pause,Work.STATE_PAUSE);
                listView_pause.setAdapter(taskListViewAdapter_pause);
                taskListViewAdapter_pause.notifyDataSetChanged();
                setTitle(tagTasks[3]);
                actualView = Work.STATE_PAUSE;
                break;
            case Work.STATE_CANCEL:
                ArrayList<Work> works_cancel  = worksDbHelper.getWorksByState(Work.STATE_CANCEL);
                ListView listView_cancel = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_cancel = new TaskListViewAdapter(context,works_cancel,Work.STATE_CANCEL);
                listView_cancel.setAdapter(taskListViewAdapter_cancel);
                taskListViewAdapter_cancel.notifyDataSetChanged();
                setTitle(tagTasks[4]);
                actualView = Work.STATE_CANCEL;
                break;
            case Work.STATE_COMPLETE:
                ArrayList<Work> works_complete  = worksDbHelper.getWorksByState(Work.STATE_COMPLETE);
                ListView listView_complete = (ListView) findViewById(R.id.listview_tasks);
                TaskListViewAdapter taskListViewAdapter_complete = new TaskListViewAdapter(context,works_complete,Work.STATE_COMPLETE);
                listView_complete.setAdapter(taskListViewAdapter_complete);
                taskListViewAdapter_complete.notifyDataSetChanged();
                setTitle(tagTasks[5]);
                actualView = Work.STATE_COMPLETE;
                break;
            case "message":
                setTitle(tagOptions[1]);
                break;
            case "history":
                setTitle(tagOptions[2]);
                break;
            case "logout":
                LogoutDialog logoutDialog = new LogoutDialog();
                logoutDialog.show(getFragmentManager(), TAG);
                break;
            case "unlink":
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