package com.example.emternet.ui.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.emternet.Chating;
import com.example.emternet.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatFragment extends Fragment {

    private FloatingActionButton fab;
    private ListView listView;
    private ListAdapter listAdapter;
    EditText editText;
    AlertDialog.Builder builder;
    SharedPreferences sharedPreferences;
    OkHttpClient okHttpClient;
    final String url = "https://tkuim3asd.azurewebsites.net/api/Friendships/";
    String Friendlist;
    Handler handler;
    String _account;
    String [] Friendship;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        listView = root.findViewById(R.id.listView);
        fab = root.findViewById(R.id.floatingActionButton);
        getFriend();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {

                    //處理Friendlist
                    Friendlist = Friendlist.replaceAll("\\[","");
                    Friendlist = Friendlist.replaceAll("\\]","");
                    Friendlist = Friendlist.replaceAll("\"","");

                    Friendship = Friendlist.split(",");
                    if (getActivity()!=null) {
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Friendship);
                        listView.setAdapter(arrayAdapter);
                    }
                }
                else if(msg.what == 2){
                    getFriend();
                }
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ChatWith",Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("Friend",Friendship[i]).apply();

                        Intent myIntent = new Intent(getActivity(), Chating.class);
                        startActivity(myIntent);
                    }
                });
            }
        };

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(getActivity());
                builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("請輸入好友帳號")
                        .setTitle("新增好友");
                editText = new EditText(getActivity());
                builder.setView(editText);

                //新增好友按鈕
                builder.setPositiveButton("新增", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String friend = editText.getText().toString();
                        String strUrl = url + _account + "/" + friend;
                        String str1 = _account;
                        String str2 = friend;
                        RequestBody formBody = new FormBody.Builder()
                                .add(str1,str2)
                                .build();
                        okHttpClient = new OkHttpClient();
                        final Request request = new Request.Builder()
                                .url(strUrl)
                                .post(formBody)
                                .build();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Response response = null;
                                try {
                                    response = okHttpClient.newCall(request).execute();
                                    if (response.isSuccessful()) {
                                        Message msg = new Message();

                                        if((Friendlist = response.body().string()).equals("true")){
                                            msg.what = 2;
                                            handler.sendMessage(msg);
                                        }else {
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
                });
                builder.show();

            }
        });

        return root;
    }
    public void getFriend(){
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("LoginData",Context.MODE_PRIVATE);
        _account = sharedPreferences.getString("Account","");
        okHttpClient = new OkHttpClient();
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

