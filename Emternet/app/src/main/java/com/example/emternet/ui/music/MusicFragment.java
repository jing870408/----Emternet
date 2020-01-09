package com.example.emternet.ui.music;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.emternet.Chating;
import com.example.emternet.Config;
import com.example.emternet.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MusicFragment extends Fragment {
    private YouTubePlayer YPlayer;
    YouTubePlayerSupportFragment youTubePlayerFragment;
    OkHttpClient okHttpClient;
    String song = "null";
    String url = "https://tkuim3asd.azurewebsites.net/api/SongListFs/";
    Handler handler;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_music, container, false);

        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commitAllowingStateLoss();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginData", Context.MODE_PRIVATE);
        String mood = sharedPreferences.getString("Mood","");
        String mdf = "";
        //開心 平平 沮喪 生氣 焦慮
        if (mood == "沮喪"){
            mdf = "sad";
        }else  if(mood == "生氣"){
            mdf = "happy";
        }else  if(mood == "焦慮"){
            mdf = "love";
        }else {
            String [] arr = {"love","happy","sad"};
            int i = new Random().nextInt(arr.length);
            mdf = arr[i];
        }
        getSong(mdf);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {


                    youTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {


                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            if(!b){
                                YPlayer = youTubePlayer;
                                YPlayer.setFullscreen(false);
                                YPlayer.loadVideo(song);
                                YPlayer.play();
                            }
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                        }
                    });
                }
            }
        };


        return root;
    }
    public void getSong(String mdf){
        okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url + mdf)
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = okHttpClient.newCall(request).execute();
                    if (response.isSuccessful()) {
                        song = response.body().string();
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
