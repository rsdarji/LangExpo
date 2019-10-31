package com.langexpo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.langexpo.R;


public class AddLanguage extends Fragment {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Button addLanguage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_language, container, false);

        addLanguage = (Button)view.findViewById(R.id.button_add_language);
        addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
