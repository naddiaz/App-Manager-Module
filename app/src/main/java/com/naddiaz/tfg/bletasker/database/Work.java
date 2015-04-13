package com.naddiaz.tfg.bletasker.database;

import java.sql.Date;

/**
 * Created by nad on 12/04/15.
 */
public class Work {

    public static final String FIELD_ID_TASK = "id_task";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_STATE = "state";
    public static final String FIELD_PRIORITY = "priority";
    public static final String FIELD_N_EMPLOYEES = "n_employees";
    public static final String FIELD_CREATED_AT = "created_at";

    public static final String STATE_PENDING = "pending";
    public static final String STATE_ACTIVE = "active";
    public static final String STATE_PAUSE = "pause";
    public static final String STATE_CANCEL = "cancel";
    public static final String STATE_COMPLETE = "complete";

    public static final String GROUP_PENDING = "works_pending";
    public static final String GROUP_ACTIVE = "works_active";
    public static final String GROUP_PAUSE = "works_pause";
    public static final String GROUP_CANCEL = "works_stop";
    public static final String GROUP_COMPLETE = "works_complete";

    public static final String PENDING_TASK = "pending_task";


    private String id_task;
    private String description;
    private String state;
    private int priority;
    private int n_employees;
    private String created_at;


    public Work() {
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getN_employees() {
        return n_employees;
    }

    public void setN_employees(int n_employees) {
        this.n_employees = n_employees;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId_task() {
        return id_task;
    }

    public void setId_task(String id_task) {
        this.id_task = id_task;
    }

    public String toString(){
        return "[id_task = " + this.getId_task() + ", state = " + this.getState() + "]";
    }
}
