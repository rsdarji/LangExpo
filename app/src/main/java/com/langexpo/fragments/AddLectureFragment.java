package com.langexpo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.langexpo.R;

public class AddLectureFragment extends Fragment {

    Button addLecture;
    public AddLectureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_lecture, container, false);

        addLecture = (Button)view.findViewById(R.id.button_add_lecture);
        addLecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
