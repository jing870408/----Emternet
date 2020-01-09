package com.example.emternet.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emternet.MainActivity;
import com.example.emternet.R;

public class NotificationsFragment extends Fragment {

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

        ImageButton nextPageBtn = root.findViewById(R.id.button);
        ImageButton nextPage2Btn = root.findViewById(R.id.button2);
        ImageButton nextPage3Btn = root.findViewById(R.id.button3);
        ImageButton nextPage4Btn = root.findViewById(R.id.button4);
        ImageButton nextPage5Btn = root.findViewById(R.id.button5);
        nextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NotificationsFragment =new Intent(getActivity(),page_instructions.class);
                getActivity().startActivity(NotificationsFragment);
            }
        });
        nextPage2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NotificationsFragment =new Intent(getActivity(),page_problem.class);
                getActivity().startActivity(NotificationsFragment);
            }


        });
        nextPage3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NotificationsFragment =new Intent(getActivity(),page_contactus.class);
                getActivity().startActivity(NotificationsFragment);
            }
        });
        nextPage4Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent NotificationsFragment =new Intent(getActivity(),page_developer.class);
                getActivity().startActivity(NotificationsFragment);
            }
        });
        nextPage5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();


                Intent NotificationsFragment =new Intent(getActivity(),MainActivity.class);
                getActivity().startActivity(NotificationsFragment);
                getActivity().finish();
            }
        });

        return root;
    }
}