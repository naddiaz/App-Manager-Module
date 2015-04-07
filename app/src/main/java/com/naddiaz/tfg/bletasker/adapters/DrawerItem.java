package com.naddiaz.tfg.bletasker.adapters;

/**
 * Created by nad on 7/04/15.
 */
public class DrawerItem {
    private String name;
    private int iconId;

    public DrawerItem(String name) {
        this.name = name;
    }

    public DrawerItem(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
}
