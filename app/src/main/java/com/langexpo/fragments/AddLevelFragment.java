package com.langexpo.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.langexpo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddLevelFragment extends Fragment {

    Button addLevel;
    public AddLevelFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_level, container, false);

        addLevel = (Button)view.findViewById(R.id.button_add_level);
        addLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
