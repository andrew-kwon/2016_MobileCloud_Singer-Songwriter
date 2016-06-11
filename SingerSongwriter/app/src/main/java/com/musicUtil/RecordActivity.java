package com.musicUtil;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonS3.UploadActivity;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class RecordActivity extends Activity {

    private static final String UPLOAD_URL = "http://52.207.214.66/singersong/songUpload.php";

    static final String RECORDED_FILE = Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/recorded.mp4";

    MediaPlayer player;
    MediaRecorder recorder;
    CountDownTimer t;

    int playbackPosition = 0;
    int cnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_main);
        Button recordBtn = (Button) findViewById(R.id.recordBtn);
        Button recordStopBtn = (Button) findViewById(R.id.recordStopBtn);
        Button playBtn = (Button) findViewById(R.id.playBtn);
        Button playStopBtn = (Button) findViewById(R.id.playStopBtn);
        Button uploadBtn = (Button) findViewById(R.id.uploadBtn);
        final TextView txtcount = (TextView) findViewById(R.id.countTextView);
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter");
        if (!file.isDirectory()) file.mkdirs();

        t = new CountDownTimer(Long.MAX_VALUE, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                cnt++;
                String time = new Integer(cnt).toString();
                long millis = cnt;
                int seconds = (int) (millis / 60);
                int minutes = seconds / 60;
                seconds = seconds % 60;
                txtcount.setText(String.format("%d:%02d:%02d", minutes, seconds, millis));

            }

            @Override
            public void onFinish() {
                cnt = 0;
            }
        };


        recordBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (recorder != null) {
                    recorder.stop();
                    recorder.release();
                    recorder = null;
                }// TODO Auto-generated method stub
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                recorder.setOutputFile(RECORDED_FILE);
                recorder.setAudioSamplingRate(44100);
                recorder.setAudioEncodingBitRate(96000);

                try {
                    Toast.makeText(getApplicationContext(),
                            "녹음을 시작합니다.", Toast.LENGTH_LONG).show();
                    recorder.prepare();
                    recorder.start();
                    t.cancel();
                    t.onFinish();
                    t.start();

                } catch (Exception ex) {
                    Log.e("SampleAudioRecorder", "Exception : ", ex);
                }
            }
        });
        recordStopBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (recorder == null)
                    return;
                recorder.stop();
                recorder.release();
                recorder = null;
                t.cancel();
                Toast.makeText(getApplicationContext(),
                        "녹음이 중지되었습니다.", Toast.LENGTH_LONG).show();
                // TODO Auto-generated method stub

            }
        });

        playBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                try {
                    playAudio(RECORDED_FILE);

                    Toast.makeText(getApplicationContext(), "음악파일 재생 시작됨.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        playStopBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (player != null) {
                    playbackPosition = player.getCurrentPosition();
                    player.pause();
                    Toast.makeText(getApplicationContext(), "음악 파일 재생 중지됨.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        uploadBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(RecordActivity.this);
                alert.setTitle("곡 제목");
                alert.setMessage("곡 제목을 입력하세요.");
                String userName = MainActivity.UserIDClass.getUserName();

                final String myUser = userName;

                // Set an EditText view to get user input
                final EditText input = new EditText(RecordActivity.this);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        if(value.equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"곡 제목을 입력하세요.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            MainActivity.UserIDClass.setSongName(value.toString());
                            File filePre = new File(Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/recorded.mp4");
                            File fileNow = new File(Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/" + myUser + "_" + value.toString() + ".mp4");
                            filePre.renameTo(fileNow);    //이름바꾸기
                            String Change_file = Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/" + myUser + "_" + value.toString() + ".mp4";   //경로설정
                            MainActivity.UserIDClass.setUploadFilepath(Change_file);                                  //올릴 페스 글로벌로 저장.
                            setText();
                        }
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                alert.show();
            }
        });

    }

    private void uploadSongDB() {                                                                   // DB에 곡 정보 등록하는 기능

        String userName=MainActivity.UserIDClass.getUserName();
        String userID=MainActivity.UserIDClass.getUserID();
        String songName=MainActivity.UserIDClass.getSongName();
        String contents=MainActivity.UserIDClass.getContents();
        String filepath=MainActivity.UserIDClass.getUploadFilepath();
        Bitmap userImage=MainActivity.UserIDClass.getUserImage();
        String userImageString=BitMapToString(userImage);
        String urlSuffix = "?username="+userName+"&userID="+userID+"&songName="+songName+"&contents="
                +contents+"&filepath="+filepath;

        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(RecordActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s.equals("successfully Upload")) {
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RecordActivity.this, UploadActivity.class);                     //바로 업로드 하는 기능
                    intent.putExtra("path", MainActivity.UserIDClass.getUploadFilepath());
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(UPLOAD_URL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferedReader.readLine();
                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

    public void setText()                                                          /////// 설명 추가하는 알람창
    {

        AlertDialog.Builder alert = new AlertDialog.Builder(RecordActivity.this);
        alert.setTitle("내용");
        alert.setMessage("간단한 설명을 입력하세요.");

        // Set an EditText view to get user input
        final EditText input = new EditText(RecordActivity.this);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                MainActivity.UserIDClass.setContents(value.toString());
                uploadSongDB();
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();

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
    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
/*
byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
 */
        return temp;
    }

}