package com.musicUtil;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mysampleapp.R;

import java.io.IOException;


public class MusicPlayerActivity extends Activity {

    Button btn1, btn2, btn3;
    MediaPlayer mp;
    SeekBar seekBar;
    TextView text;
    TextView songName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        songName= (TextView)findViewById(R.id.songNameText);
        btn1 = (Button)findViewById(R.id.button1);
        btn2 = (Button)findViewById(R.id.button2);
        btn3 = (Button)findViewById(R.id.button3);

        text = (TextView)findViewById(R.id.text1);

        Intent intent = getIntent();
        String musicPosition = intent.getStringExtra("pos");


        Uri file = Uri.parse(musicPosition);

        String musicNameSet[]=musicPosition.split("SingerSongwriter/");
        String musicName = musicNameSet[1];
        songName.setText(musicName);

//        mp = MediaPlayer.create(MusicPlayerActivity.this, R.raw.music);
        mp = MediaPlayer.create(MusicPlayerActivity.this,file);         // 보내진 파일이 music 파일이 아니라면 error가 생김.

        seekBar = (SeekBar)findViewById(R.id.playbar);

        seekBar.setVisibility(ProgressBar.VISIBLE);
        seekBar.setMax(mp.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    mp.seekTo(progress);
                }
                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                text.setText(strTime);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.start();

                Thread();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                try
                {
                    mp.prepare();
                }
                catch(IOException ie)
                {
                    ie.printStackTrace();
                }
                mp.seekTo(0);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
            }
        });


    }


    @Override
    public void onBackPressed()
    {
        Intent backtoDown = new Intent();
        setResult(RESULT_OK, backtoDown);
        mp.stop();
        finish();

    }
    public void Thread(){
        Runnable task = new Runnable(){


            public void run(){
                // 음악이 재생중일때
                while(mp.isPlaying()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    seekBar.setProgress(mp.getCurrentPosition());
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }

}
