package com.example.emternet;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.jar.Attributes;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class Regist extends AppCompatActivity {

    EditText Nameedit;
    EditText Accountedit;
    EditText Passwordedit;
    EditText Emailedit;
    OkHttpClient okHttpClient;
    String strUrl = "http://Tkuim3asdemternet-env-1.nhigxjvkwf.us-east-2.elasticbeanstalk.com/api/Members/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        Nameedit = (EditText)findViewById(R.id.editText3);
        Accountedit = (EditText)findViewById(R.id.editText4);
        Passwordedit = (EditText)findViewById(R.id.editText5);
        Emailedit = (EditText)findViewById(R.id.editText7);
    }
    public void click_gohome(View view){
        Intent intent = new Intent(Regist.this,MainActivity.class);
        startActivity(intent);
        Regist.this.finish();
    }
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public void click_to_registed(View view){
        String Name = Nameedit.getText().toString();
        String Account = Accountedit.getText().toString();
        String Password = Passwordedit.getText().toString();
        String Email = Emailedit.getText().toString();

        AccountData accountData = new AccountData(Account,Password,Name,Email);
        Gson gson = new Gson();
        String json = gson.toJson(accountData);

        Log.i("tw",json);

        okHttpClient = new OkHttpClient();

        RequestBody body = RequestBody.create(json,JSON);

        final Request request = new Request.Builder()
                .url(strUrl)
                .post(body)
                .build();

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {
                    Intent intent = new Intent(Regist.this,MainActivity.class);
                    startActivity(intent);
                    Regist.this.finish();
                    Log.i("WY","" + "請登入");
                }else {
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
                    String TRUE = "true";
                    if (response.isSuccessful()) {
                        Message msg = new Message();
                        if (response.body().string().equals(TRUE)) {
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    } else {
                        Log.i("WY", "ec" + response);
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    class AccountData{
        String Account;
        String Password;
        String Name;
        String Email;
        boolean IsAdmin;
        public  AccountData(String _account,String _password,String _name,String _email){
            Account = _account;
            Password = _password;
            Name = _name;
            Email = _email;
            IsAdmin = false;
        }
    }

}
