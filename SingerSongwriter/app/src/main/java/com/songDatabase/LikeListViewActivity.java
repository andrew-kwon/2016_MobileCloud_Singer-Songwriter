package com.songDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.RecyclerUtil.SongRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class LikeListViewActivity extends Activity  {


    static Context likeListContext;
    String selectFilepath[];
    String contents[];
    String listUsername[];
    String listSongname[];
    String listUserID[];
    int commentCount[];
    int likeCount[];
    List<songData> likeitems;
    SongRecyclerAdapter adapter;
    RecyclerView recyclerView;
    static String LIKEURL="http://52.207.214.66/singersong/changeLike.php";


    MediaPlayer mp;
    SeekBar seekBar;
    TextView text;

    public static Context getLikeListContect()
    {
        return likeListContext;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_likelistview);

        likeListContext=getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.like_recycler_view);
        setList();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                alertShow(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));


        mp = new MediaPlayer();

        seekBar = (SeekBar)findViewById(R.id.playbar_like);
        text=(TextView)findViewById(R.id.text1_like);
        seekBar.setVisibility(ProgressBar.VISIBLE);

    }

    @Override
    public void onBackPressed()
    {
        Intent backtoComment = new Intent();
        setResult(RESULT_OK, backtoComment);
        mp.stop();
        mp.seekTo(0);
        finish();
    }



    public void setListView(String songList) {

        String[] songListArray = songList.split("--:--");             // 전체 테이블 받아옴.

        likeitems = new ArrayList<songData>();

        selectFilepath = new String[songListArray.length];
        contents = new String[songListArray.length];
        listUsername = new String[songListArray.length];
        listSongname = new String[songListArray.length];
        listUserID  = new String[songListArray.length];
        commentCount = new int[songListArray.length];
        likeCount = new int[songListArray.length];


        for (int k = 0; k < songListArray.length-1; k++) {
                String listArray[];
                listArray = songListArray[k].split(":::");

                listUsername[k] = listArray[1];
                listUserID[k] = listArray[2];
                listSongname[k] = listArray[3];
                contents[k] = listArray[4];
                selectFilepath[k] = listArray[5];
                commentCount[k] = Integer.parseInt(listArray[6]);
                likeCount[k] = Integer.parseInt(listArray[7]);

                songData item = new songData(listSongname[k], contents[k], listUsername[k],
                        "" + likeCount[k],listUserID[k]);
                likeitems.add(item);

            }


        MainActivity.UserIDClass.setRecyclerAdapterType("LikeList");
        adapter = new SongRecyclerAdapter(this, likeitems);     //좋아요리스트에 추가한다.
        recyclerView.setAdapter(adapter);
    }


    public void alertShow(int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(LikeListViewActivity.this);

        final String UserName = listUsername[position];
        final String SongName = listSongname[position];
        final String UserID = listUserID[position];
        final int likeCounts = likeCount[position];
        final int commentcount = commentCount[position];
        final CharSequence[] items = {"바로듣기", "다운받기", "좋아요" + " (" + likeCounts + ")", "댓글보기" + " (" + commentcount + ")", "취소"};

        alert.setTitle(SongName + " : " + contents[position])
                .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                    public void onClick(DialogInterface dialog, int index) {
                        if (index==0)
                        {

                            mp.stop();
                            mp.seekTo(0);
                            try {
                                startSongStream(SongName, UserID, UserName);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }
                        else if (index == 1) {
                            Intent intent = new Intent(LikeListViewActivity.this, onlyDownloadActivity.class);
                            intent.putExtra("key", UserName + "_" + SongName + ".mp4");
                            startActivity(intent);
                        } else if (index == 2) {                                                              //좋아요
                            UploadDatabaseManager myLikeDB = new UploadDatabaseManager();
                            myLikeDB.upLikeCount(getApplicationContext(), SongName, UserID);
//                                    if(MainActivity.UserIDClass.getLikeTrue()) setList(); // 좋아요 성공하면 최신화
                            setList();

                        } else if (index == 3) {                                                           //댓글보기
                            // dbContents intent 해서 보여주기. 여기서는 userName,userID, songName 일치하는 댓글 보여주기 ........... 만약 곡 이름이 같다면 업로드 못하게 설정.
                            showComment(UserID, SongName);
                        } else if (index == 4) {                                                            //취소
                            // 아무것도 하지 않음
                        }
                    }});
        alert.show();

    }


    private void startSongStream(String SongName, String UserID, String UserName) throws UnsupportedEncodingException {

        try {
            SongName= URLEncoder.encode(SongName, "UTF-8");
            UserName=URLEncoder.encode(UserName,"UTF-8");
            Log.v("AUDIOHTTPPLAYER", "https://s3.amazonaws.com/mysongs3/" + UserName + "_" + SongName + ".mp4");
            mp.setDataSource("https://s3.amazonaws.com/mysongs3/" + UserName + "_" + SongName + ".mp4");
            mp.prepare();

            seekBar.setMax(mp.getDuration());

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser) {
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
            mp.start();
            Thread();
        } catch (IOException e) {
            Log.v("AUDIOHTTPPLAYER", e.getMessage());
        }
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




    private void showComment(String UserID, String SongName)
    {
        Intent intent = new Intent(LikeListViewActivity.this,ShowCommentActivity.class);
        intent.putExtra("SongName", SongName);
        intent.putExtra("UserID", UserID);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            setList();
        }
    }

    private void setList()
    {
        String urlSuffix = "?UserID="+ MainActivity.UserIDClass.getUserID();


        class RegisterUser extends AsyncTask<String, Void, String>
        {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LikeListViewActivity.this, "Please Wait",null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if(s=="") ;
                else {
                    setListView(s);
                }
            }
            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {


                    URL url = new URL(LIKEURL+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));


                    String result;                                 /////////////// 여러줄 받아오기위한 버퍼작업
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line =  bufferedReader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();

                    return result;
                }
                catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }

}

