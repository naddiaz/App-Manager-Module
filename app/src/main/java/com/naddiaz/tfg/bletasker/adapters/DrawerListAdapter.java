package com.naddiaz.tfg.bletasker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.naddiaz.tfg.bletasker.R;

import java.util.List;

/**
 * Created by nad on 7/04/15.
 */

public class DrawerListAdapter extends ArrayAdapter<DrawerItem> {


    public DrawerListAdapter(Context context, List<DrawerItem> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(position == 0 || position == 6){
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)parent.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.drawer_list_label, null);
            }
            TextView name = (TextView) convertView.findViewById(R.id.nameLabel);

            DrawerItem item = getItem(position);
            name.setText(item.getName());

        }
        else{
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater)parent.getContext().
                        getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.drawer_list_item, null);
            }
            ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            DrawerItem item = getItem(position);
            icon.setImageResource(item.getIconId());
            name.setText(item.getName());
        }


        return convertView;
    }
}
