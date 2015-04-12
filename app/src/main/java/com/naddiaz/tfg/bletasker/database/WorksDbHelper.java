package com.naddiaz.tfg.bletasker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by nad on 12/04/15.
 */

public class WorksDbHelper extends SQLiteOpenHelper {

    private static final String TAG = "WorksDbHelper";

    private static final String DATABASE_NAME = "BLETASKER";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "WORK";


    private static final String CREATE_TABLE_IF_NOT_EXIST_WORK = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NAME + "("
            + Work.FIELD_ID_TASK + " INTEGER PRIMARY KEY,"
            + Work.FIELD_DESCRIPTION + " TEXT,"
            + Work.FIELD_PRIORITY + " INTEGER,"
            + Work.FIELD_N_EMPLOYEES + " INTEGER,"
            + Work.FIELD_STATE + " TEXT,"
            + Work.FIELD_CREATED_AT + " TEXT)";

    public WorksDbHelper(Context context){
        super(context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_IF_NOT_EXIST_WORK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void createWork(Work work){
        SQLiteDatabase db = this.getWritableDatabase();

        if(this.getWork(work.getId_task()) == null) {
            ContentValues values = new ContentValues();
            values.put(Work.FIELD_ID_TASK, work.getId_task());
            values.put(Work.FIELD_DESCRIPTION, work.getDescription());
            values.put(Work.FIELD_PRIORITY, work.getPriority());
            values.put(Work.FIELD_N_EMPLOYEES, work.getN_employees());
            values.put(Work.FIELD_STATE, work.getState());
            values.put(Work.FIELD_CREATED_AT, work.getCreated_at());

            db.insert(TABLE_NAME, null, values);
        }
    }

    public Work getWork(String id_task){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + Work.FIELD_ID_TASK + " = '" + id_task + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor == null)
            return null;
        else{
            if(cursor.moveToFirst()) {
                Work work = new Work();
                work.setId_task(cursor.getString(cursor.getColumnIndex(Work.FIELD_ID_TASK)));
                work.setDescription(cursor.getString(cursor.getColumnIndex(Work.FIELD_DESCRIPTION)));
                work.setPriority(cursor.getInt(cursor.getColumnIndex(Work.FIELD_PRIORITY)));
                work.setN_employees(cursor.getInt(cursor.getColumnIndex(Work.FIELD_N_EMPLOYEES)));
                work.setState(cursor.getString(cursor.getColumnIndex(Work.FIELD_STATE)));
                work.setCreated_at(cursor.getString(cursor.getColumnIndex(Work.FIELD_CREATED_AT)));
                return work;
            }
            else{
                return null;
            }
        }
    }

    public List<Work> getWorksByState(String state){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + Work.FIELD_STATE + " = '" + state + "' ORDER BY " + Work.FIELD_CREATED_AT + " DESC";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor == null)
            return null;
        else{
            cursor.moveToFirst();
            List<Work> works = new ArrayList<>();
            while(cursor.moveToNext()) {
                Work work = new Work();
                work.setId_task(cursor.getString(cursor.getColumnIndex(Work.FIELD_ID_TASK)));
                work.setDescription(cursor.getString(cursor.getColumnIndex(Work.FIELD_DESCRIPTION)));
                work.setPriority(cursor.getInt(cursor.getColumnIndex(Work.FIELD_PRIORITY)));
                work.setN_employees(cursor.getInt(cursor.getColumnIndex(Work.FIELD_N_EMPLOYEES)));
                work.setState(cursor.getString(cursor.getColumnIndex(Work.FIELD_STATE)));
                work.setCreated_at(cursor.getString(cursor.getColumnIndex(Work.FIELD_CREATED_AT)));
                works.add(work);
            }
            return works;
        }
    }

    private String getDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}