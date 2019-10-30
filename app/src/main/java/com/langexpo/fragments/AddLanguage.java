package com.langexpo.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.langexpo.R;


public class AddLanguage extends Fragment {

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_language, container, false);

        /*CardView cardView = (CardView) view.findViewById(R.id.card_view_add_question);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(view.getContext(), "Please enter correct Username", Toast.LENGTH_LONG);
            }
        });*/

        /*FirebaseStorage storage = FirebaseStorage.getInstance();*/
        // Get a non-default Storage bucket



        StorageReference storageRef = storage.getReference();
        String path = storageRef.getPath();
        button = (Button)view.findViewById(R.id.button_fragment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
