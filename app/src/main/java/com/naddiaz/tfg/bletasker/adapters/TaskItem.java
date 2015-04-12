package com.naddiaz.tfg.bletasker.adapters;

/**
 * Created by nad on 12/04/15.
 */
public class TaskItem {

    private String id_task;
    private String description;
    private String state;
    private int priority;
    private int n_employees;
    private String hour;
    private String date;

    public TaskItem(){

    }


    public String getId_task() {
        return id_task;
    }

    public void setId_task(String id_task) {
        this.id_task = id_task;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getN_employees() {
        return n_employees;
    }

    public void setN_employees(int n_employees) {
        this.n_employees = n_employees;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
