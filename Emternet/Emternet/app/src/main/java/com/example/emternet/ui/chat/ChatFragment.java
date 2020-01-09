package com.example.emternet.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.emternet.Island;
import com.example.emternet.MainActivity;
import com.example.emternet.R;
import com.example.emternet.ui.dashboard.DashboardViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatFragment extends Fragment {

    private ListView listView;
    private ListAdapter listAdapter;
    SharedPreferences sharedPreferences;
    OkHttpClient okHttpClient;
    final String url = "http://tkuim3asd.azurewebsites.net/api/Friendships/";
    String Friendlist;
    Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        listView = root.findViewById(R.id.listView);
        Log.i("qw",""+123);
        getFriend();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {
                    Log.i("df",Friendlist);
                    //處理Friendlist
                    Friendlist = Friendlist.replaceAll("\\[","");
                    Friendlist = Friendlist.replaceAll("\\]","");
                    Friendlist = Friendlist.replaceAll("\"","");
                    Log.i("df",Friendlist);
                    final String [] Friendship = Friendlist.split(",");

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, Friendship);
                    listView.setAdapter(arrayAdapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String text = Friendship[position];
                        }
                    });


                }
            }
        };


        return root;
    }
    public void getFriend(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginData",Context.MODE_PRIVATE);
        String _account = sharedPreferences.getString("Account","");
        okHttpClient = new OkHttpClient();
        Log.i("qw",""+123);
        final Request request = new Request.Builder()
                .url(url+_account)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Message msg = new Message();

                        if(!(Friendlist = response.body().string()).equals("沒朋友")){
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }else {
                            Log.i("qw","沒朋友");
                        }

                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

