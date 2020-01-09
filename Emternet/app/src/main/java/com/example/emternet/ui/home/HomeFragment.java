package com.example.emternet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emternet.R;
import com.example.emternet.ui.angry_island;
import com.example.emternet.ui.disgust_island;
import com.example.emternet.ui.happy_island;
import com.example.emternet.ui.normal_island;
import com.example.emternet.ui.sad_island;

public class HomeFragment extends Fragment {


    private Button btn_sad,btn_angry,btn_happy,btn_normal,btn_disgust;
    private Intent go_happy,go_angry,go_normal,go_disgust,go_sad;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        allview(root);
        allexecution();

        return root;
    }
    public void allview(View root) {

        btn_angry = root.findViewById(R.id.button_angry);
        btn_sad = root.findViewById(R.id.button_sad);
        btn_happy = root.findViewById(R.id.button_happy);
        btn_normal = root.findViewById(R.id.button_normal);
        btn_disgust = root.findViewById(R.id.button_disgust);

        go_angry = new Intent(getActivity(), angry_island.class);
        go_sad = new Intent(getActivity(), sad_island.class);
        go_disgust = new Intent(getActivity(), disgust_island.class);
        go_normal = new Intent(getActivity(), normal_island.class);
        go_happy = new Intent(getActivity(), happy_island.class);
    }

    public void allexecution() {
        View.OnClickListener click = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();

                if (id==R.id.button_happy){
                    startActivity(go_happy);
                }
                else if(id==R.id.button_disgust){
                    startActivity(go_disgust);
                }
                else if(id==R.id.button_sad){
                    startActivity(go_sad);
                }
                else if(id==R.id.button_normal){
                    startActivity(go_normal);
                }
                else if(id==R.id.button_angry){
                    startActivity(go_angry);
                }
            }
        };
        btn_disgust.setOnClickListener(click);
        btn_happy.setOnClickListener(click);
        btn_sad.setOnClickListener(click);
        btn_normal.setOnClickListener(click);
        btn_angry.setOnClickListener(click);
    }

}