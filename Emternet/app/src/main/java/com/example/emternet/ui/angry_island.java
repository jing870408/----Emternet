package com.example.emternet.ui;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.emternet.Chating;
import com.example.emternet.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class angry_island extends Activity {

    private FrameLayout frameLayout;//!!!!!!!!
    private int screenWidth, screenHeight;
    private ImageView[] imageView;
    private TextView[] textView;
    private ImageView imageView2;
    private TextView textView2;
    Handler handler;
    OkHttpClient okHttpClient;
    String mm = "Angry";
    String url = "https://tkuim3asd.azurewebsites.net/api/AccountEmos/";
    String account;
    String upfriendlist;
    String[] slist;
    int sx,sy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_angry_island);

        SharedPreferences sharedPreferences = getSharedPreferences ("LoginData", MODE_PRIVATE);
        account = sharedPreferences.getString ("Account", "");
        getFriendinhere ();

        handler = new Handler () {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage (msg);

                if (msg.what == 1) {
                    Log.i ("Friend", upfriendlist);
                    upfriendlist = upfriendlist.replaceAll ("\\[", "");
                    upfriendlist = upfriendlist.replaceAll ("\\]", "");
                    upfriendlist = upfriendlist.replaceAll ("\"", "");
                    slist = upfriendlist.split (",");

                    make_people ();
                }
            }
        };
    }

    private void getFriendinhere() {
        RequestBody formBody = new FormBody.Builder()
                .add(account,mm)
                .build();
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url + account + "/" + mm)
                .post(formBody)
                .build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        upfriendlist = response.body().string();
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    } else {
                        throw new IOException("Unexpected code " + response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void make_people() {
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        screenHeight = outMetrics.heightPixels;
        frameLayout =findViewById(R.id.fuuuck);



        imageView = new ImageView[slist.length];
        textView = new TextView[slist.length];
        //int n=0;
        for(int n=0; n<slist.length;n++) {
            imageView[n] = new ImageView(this);
            textView[n] = new TextView(this);
        }


        frameLayout.removeAllViews();

        gogo_people();
    }

    private void gogo_people() {

        int viewWidth = 192;
        int viewHeight = 192;
        int x,y,real_x,real_y;
        int[] x_list = new int[slist.length];
        int[] y_list = new int[slist.length];
        x_list[0] = 0;
        y_list[0] = 0;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(viewWidth, viewHeight);
        AnimationDrawable anim = new AnimationDrawable();

        for(int p=0; p<slist.length;p++) {

            x = getRandomInt (screenWidth - viewWidth);
            y = getRandomInt (screenHeight / 2 - 180);
            real_x = x;
            real_y = screenHeight / 2 + y - viewHeight;

            boolean ss = true;
            for(int d=0;d<=p;d++){

                if(((x_list[d]-real_x<222)&&(x_list[d]-real_x>-222))&&((y_list[d]-real_y<222)&&(y_list[d]-real_y>-222))){
                    p = p-1;
                    ss = false;
                    break;
                }else{
                    continue;
                }
            }
            if((ss==true)&&(p!=0)){
                x_list[p] = real_x;
                y_list[p] = real_y;
                Log.d (TAG, String.valueOf (real_x));
                imageView[p].setX(real_x);
                imageView[p].setY(real_y);
                textView[p].setX(x + 50);
                textView[p].setY(screenHeight / 2 + y - viewHeight + 180);
                textView[p].setTextColor (Color.parseColor("#000000"));

                frameLayout.addView(imageView[p], lp);
                frameLayout.addView(textView[p], lp);
                textView[p].setText(slist[p]);
                imageView[p].setId (p);
                imageView[p].setBackground (getResources().getDrawable(R.drawable.ic_normal_people));
                anim.addFrame(getResources().getDrawable(R.drawable.ic_normal_people), 100);
                imageView[p].setImageDrawable(anim);
            }

        }click_to_chat();
    }

    private int getRandomInt(int i) {
        Random random = new Random();
        return random.nextInt(i);
    }
    public void click_to_chat(){

        View.OnClickListener touch = new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                int xx = view.getId();
                for (int i = 0;i< slist.length;i++){
                    if(xx==i){
                        SharedPreferences sharedPreferences = getSharedPreferences("ChatWith",Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("Friend" , slist[i]).apply();  //取得好友
                        Intent myIntent = new Intent(angry_island.this, Chating.class);
                        startActivity(myIntent);
                    }
                }
            }
        };

        for(int r = 0;r<slist.length;r++){
            imageView[r].setOnClickListener (touch);
        }
    }
}