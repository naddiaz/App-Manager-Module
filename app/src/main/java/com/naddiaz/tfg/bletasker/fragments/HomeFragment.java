package com.naddiaz.tfg.bletasker.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naddiaz.tfg.bletasker.R;

/**
 * Created by nad on 7/04/15.
 */
public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment Class";
    public static final String HOME_FRAGMENT_NUMBER = "homefragment_number";

    public HomeFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        int i = getArguments().getInt(HOME_FRAGMENT_NUMBER);
        String article = getResources().getStringArray(R.array.Tags)[i];

        getActivity().setTitle(article);
        TextView headline = (TextView)rootView.findViewById(R.id.headline);
        headline.append(" "+article);

        return rootView;
    }
}
