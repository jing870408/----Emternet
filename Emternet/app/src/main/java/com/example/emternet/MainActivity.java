package com.example.emternet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import android.widget.EditText;


import java.io.IOException;


import okhttp3.FormBody;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    OkHttpClient okHttpClient;
    String url = "https://tkuim3asd.azurewebsites.net/api/Members/";
    String url2 = "https://tkuim3asd.azurewebsites.net/api/AccountEmos/";
    Handler handler;
    private EditText editText1;
    private EditText editText2;
    String str1;
    String str2;
    public MainActivity() {
    }
    /*
    final Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        if(msg.what == 1) {
            textView.setText(stringBuilder.toString());
        }
    }
};

    * */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById((R.id.editText2));
        SharedPreferences sharedPreferences = getSharedPreferences("LoginData",MODE_PRIVATE);
        String acc = sharedPreferences.getString("Account","");
        String pass = sharedPreferences.getString("Password","");

        Log.i("as",""+ acc + pass);
        if (acc != "" && pass != ""){
            Intent intent = new Intent(MainActivity.this,Island.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
    public void click_to_login(View view) {
        LoginPost();
    }
    /*public String LoginPost(){
        okHttpClient = new OkHttpClient();
        String strUrl = url + editText1.getText().toString() + "/" + editText2.getText().toString();

        Request request = new Request.Builder().url(strUrl).build();

        try ( Response response = okHttpClient.newCall(request).execute()) {
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "false";
    }*/


    private void LoginPost() {

        str1 = editText1.getText().toString();
        str2 = editText2.getText().toString();

        String strUrl = url + editText1.getText().toString() + "/" + editText2.getText().toString();
        RequestBody formBody = new FormBody.Builder()
                .add(str1,str2)
                .build();
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(strUrl)
                .post(formBody)
                .build();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {
                    final SharedPreferences sharedPreferences = getSharedPreferences("LoginData",MODE_PRIVATE);
                    sharedPreferences.edit().putString("Account",str1).apply();
                    sharedPreferences.edit().putString("Password",str2).apply();
                    sharedPreferences.edit().putString("Mood",null).apply();
                    okHttpClient = new OkHttpClient();
                    final Request request = new Request.Builder()
                            .url(url2 + str1)
                            .build();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response = null;
                            try {
                                response = okHttpClient.newCall(request).execute();
                                if (response.isSuccessful()) {
                                    sharedPreferences.edit().putString("Mood",response.body().string()).apply();
                                    Message msg = new Message();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                } else {
                                    throw new IOException("Unexpected code " + response);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else if(msg.what == 2){
                    Log.d ("ty", "1234567");
                    Intent intent = new Intent(MainActivity.this,Island.class);
                    startActivity(intent);
                    MainActivity.this.finish();
                }
                else {
                    Log.i("WY","" + "error");
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        Message msg = new Message();
                        String str4 = "true";

                        if (response.body().string().equals(str4)) {
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                        else {
                            msg.what = 0;
                            handler.sendMessage(msg);
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

    public void click_to_regist(View view) {
        Intent intent = new Intent(MainActivity.this,Regist.class);
        startActivity(intent);
    }

}
