package com.mysampleapp;


import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.amazonS3.TransferActivity;
import com.amazonS3.UploadActivity;


public class RecordActivity extends Activity {
    static final String RECORDED_FILE = "/sdcard/recorded.mp4";

    MediaPlayer player;
    MediaRecorder recorder;

    int playbackPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);

        Button recordBtn = (Button) findViewById(R.id.recordBtn);
        Button recordStopBtn = (Button) findViewById(R.id.recordStopBtn);
        Button playBtn = (Button) findViewById(R.id.playBtn);
        Button playStopBtn = (Button) findViewById(R.id.playStopBtn);
        Button uploadBtn = (Button) findViewById(R.id.uploadBtn);

        recordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(recorder != null){
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }// TODO Auto-generated method stub
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
                recorder.setOutputFile(RECORDED_FILE);
                try{
                    Toast.makeText(getApplicationContext(),
                            "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
                    recorder.prepare();
                    recorder.start();
                }catch (Exception ex){
                    Log.e("SampleAudioRecorder", "Exception : ", ex);
                }
            }
        });
        recordStopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(recorder == null)
                    return;

                recorder.stop();
                recorder.release();
                recorder = null;

                Toast.makeText(getApplicationContext(),
                        "녹음이 중지되었습니다.", Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub

            }
        });




        playBtn.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                try{
                    playAudio(RECORDED_FILE);

                    Toast.makeText(getApplicationContext(), "음악파일 재생 시작됨.", 2000).show();
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        playStopBtn.setOnClickListener(new OnClickListener(){
            public void onClick(View view){
                if(player != null){
                    playbackPosition = player.getCurrentPosition();
                    player.pause();
                    Toast.makeText(getApplicationContext(), "음악 파일 재생 중지됨.",2000).show();
                }
            }
        });

        uploadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, UploadActivity.class);                     //바로 업로드 하는 기능
                intent.putExtra("path",RECORDED_FILE);
                startActivity(intent);
                finish();
            }
        });

    }
    private void playAudio(String url) throws Exception{
        killMediaPlayer();

        player = new MediaPlayer();
        player.setDataSource(url);
        player.prepare();
        player.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if(player != null){
            try {
                player.release();
            } catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    protected void onPause(){
        if(recorder != null){
            recorder.release();
            recorder = null;
        }
        if (player != null){
            player.release();
            player = null;
        }

        super.onPause();

    }
}