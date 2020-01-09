package com.example.emternet.ui.dashboard;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.emternet.MainActivity;
import com.example.emternet.R;
import com.example.emternet.Regist;
import com.example.emternet.emo_list;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DashboardFragment extends Fragment {
    private int year, month, day;
    private TextView textView,textView_m,textView_t;
    private ImageView imageView,imageView_b;
    Handler handler,handler2;
    String url = "http://Tkuim3asdemternet-env-1.nhigxjvkwf.us-east-2.elasticbeanstalk.com/api/EmoVarDatas/";
    OkHttpClient okHttpClient;
    String account;
    String mood,mood_t;
    String date;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mine, container, false);
        textView = root.findViewById(R.id.textView5);
        textView_m = root.findViewById(R.id.textView3);
        textView_t = root.findViewById(R.id.textView8);
        imageView = root.findViewById (R.id.imageView8);
        imageView_b =root.findViewById (R.id.imageView_b);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        day = c.get(Calendar.DAY_OF_MONTH);
        date = ""+year+month+day;
        textView.setText(""+year+"/"+month+"/"+day);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        year = year;
                        month = monthOfYear;
                        day = dayOfMonth;
                        updateDisplay();
                    }
                };
                DatePickerDialog d = new DatePickerDialog(getActivity(), mDateSetListener,year,month, day);
                d.show();
            }
        });
            getmood();
            handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    if (msg.what == 1) {
                        Log.i("Friend", mood);
                        Drawable d;
                        View.OnClickListener write_list = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getActivity(), emo_list.class);
                                startActivity(intent);
                            }
                        };
                        View.OnClickListener nothing = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        };
                        if (mood_t.equals("Depress")) {
                            d = getResources().getDrawable(R.drawable.ic_sad_mine);
                            imageView_b.setBackground(d);
                            textView_m.setText("沮喪");
                            textView_m.setTextColor(Color.parseColor("#2E3192"));
                            textView.setTextColor(Color.parseColor("#2E3192"));
                            textView_t.setText("別難過，我們一起加油吧!");
                            textView_t.setOnClickListener(nothing);
                            textView_t.setTextColor(Color.parseColor("#FFFFFF"));
                        } else if (mood_t.equals("Anxiety")) {
                            d = getResources().getDrawable(R.drawable.ic_disgust_mine);
                            imageView_b.setBackground(d);
                            textView_m.setText("焦慮");
                            textView_m.setTextColor(Color.parseColor("#2E3192"));
                            textView.setTextColor(Color.parseColor("#2E3192"));
                            textView_t.setText("好煩好煩啊!天啊!");
                            textView_t.setOnClickListener(nothing);
                            textView_t.setTextColor(Color.parseColor("#333333"));
                        } else if (mood_t.equals("Happy")) {
                            d = getResources().getDrawable(R.drawable.ic_happy_mine);
                            imageView_b.setBackground(d);
                            textView_m.setText("開心");
                            textView_m.setTextColor(Color.parseColor("#F15A24"));
                            textView.setTextColor(Color.parseColor("#F15A24"));
                            textView_t.setText("今後也要一直開心下去喔!");
                            textView_t.setOnClickListener(nothing);
                            textView_t.setTextColor(Color.parseColor("#FF8C74"));
                        } else if (mood_t.equals("Calm")) {
                            d = getResources().getDrawable(R.drawable.ic_normal_mine);
                            imageView_b.setBackground(d);
                            textView_m.setText("平平");
                            textView_m.setTextColor(Color.parseColor("#333333"));
                            textView.setTextColor(Color.parseColor("#333333"));
                            textView_t.setText("或許心無雜念便能修成正果");
                            textView_t.setOnClickListener(nothing);
                            textView_t.setTextColor(Color.parseColor("#006837"));
                        } else if (mood_t.equals("Angry")) {
                            d = getResources().getDrawable(R.drawable.ic_angry_mine);
                            imageView_b.setBackground(d);
                            textView_m.setText("生氣");
                            textView_m.setTextColor(Color.parseColor("#FFFFFF"));
                            textView.setTextColor(Color.parseColor("#FFFFFF"));
                            textView_t.setText("再氣肝就要炸開啦!");
                            textView_t.setOnClickListener(nothing);
                            textView_t.setTextColor(Color.parseColor("#42210B"));
                        } else {
                            if(getActivity()!=null) {
                                d = getResources().getDrawable(R.drawable.ic_null_mine);
                                imageView_b.setBackground(d);
                                textView_m.setText("沒有情緒呦");
                                textView_m.setTextColor(Color.parseColor("#FFFFFF"));
                                textView.setTextColor(Color.parseColor("#FFFFFF"));
                                textView_t.setText("快點我補上情緒資料吧!");
                                textView_t.setOnClickListener(write_list);
                                textView_t.setTextColor(Color.parseColor("#42210B"));
                            }
                        }

                        //平平,開心,焦慮,生氣,沮喪
                    }
                }

            };


        return root;
    }
    private void updateDisplay() {

        GregorianCalendar c = new GregorianCalendar(year, month, day);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
        date = (sdf.format(c.getTime())).toString();
        Log.i("date",date);
        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("LoginData",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Date",sdf2.format(c.getTime()));
        textView.setText(sdf2.format(c.getTime()));
        getmood();


    }
    public void getmood(){
        //2019/9/16

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        account = sharedPreferences.getString("Account","");
        okHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add(account,date)
                .build();
        final Request request = new Request.Builder()
                .url(url + account + "/" + date)
                .post(formBody)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        mood = response.body().string();
                        Log.d (TAG, mood);

                        mood_t = mood;


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

}

