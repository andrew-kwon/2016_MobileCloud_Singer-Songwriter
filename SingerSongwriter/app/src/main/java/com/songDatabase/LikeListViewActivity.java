package com.songDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.RecyclerUtil.RecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;

import org.apache.http.HttpEntity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class LikeListViewActivity extends Activity  {

    String listSongname[];
    String listUserID[];

    String likeListSongname[];
    String likeListUserName[];
    String likeListUserID[];
    String likeContents[];
//    int commentCounts[];
    String likeCounts[];
    int likenum;
    List<songData> rowItems;
    List<songData> likeitems;
    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    static String LIKEURL="http://52.207.214.66/singersong/LikeListView.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_likelistview);

        likenum=0;
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
        // 아이템을 [클릭]시의 이벤트 리스너를 등록


    }


    public void setListView(String songList) {

        rowItems = MainActivity.UserIDClass.getMySongItems();
        if(rowItems==null)
        {
            Toast.makeText(getApplicationContext(),"모아보기를 먼저 클릭하세요",Toast.LENGTH_SHORT).show();

        }
        else {
            String[] songListArray = songList.split("--:--");             // 전체 테이블 받아옴.

            likeitems = new ArrayList<songData>();
            likenum = 0;
            listSongname = new String[songListArray.length];
            listUserID = new String[songListArray.length];

            likeListSongname = new String[songListArray.length];
            likeListUserID = new String[songListArray.length];
            likeListUserName = new String[songListArray.length];
            likeContents = new String[songListArray.length];
            likeCounts = new String[songListArray.length];

            for (int k = 0; k < songListArray.length - 1; k++) {      //좋아요 리스트 받아오기
                String listArray[];
                listArray = songListArray[k].split(":::");
                listSongname[k] = listArray[1];
                listUserID[k] = listArray[2];
            }

            for (int k = 0; k < rowItems.size(); k++) {

                for (int j = 0; j < songListArray.length - 1; j++) {                            //좋아요 누른 곡만 새로운 item에 넣는다
                    if (rowItems.get(k).getUserID().equals(listUserID[j])
                            && rowItems.get(k).getSongName().equals(listSongname[j])) {
                        likeitems.add(rowItems.get(k));
                        likeListUserName[likenum] = rowItems.get(k).getUserName();
                        likeListUserID[likenum] = rowItems.get(k).getUserID();
                        likeListSongname[likenum] = rowItems.get(k).getSongName();
                        likeContents[likenum] = rowItems.get(k).getContent();
                        likeCounts[likenum] = rowItems.get(k).getLikeCount();
                        likenum++;
                    }
                }
            }


            adapter = new RecyclerAdapter(this, likeitems);     //좋아요리스트에 추가한다.
            recyclerView.setAdapter(adapter);
        }
    }

    public void alertShow(int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(LikeListViewActivity.this);

        final String UserName = likeListUserName[position];
        final String SongName = likeListSongname[position];
        final String UserID = likeListUserID[position];
        final String likeCount = likeCounts[position];
        final CharSequence[] items = {"다운받기", "좋아요" + " (" + likeCount + ")", "댓글보기" + "취소"};

        alert.setTitle(SongName + " : " + likeContents[position])
                .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                    public void onClick(DialogInterface dialog, int index) {
                        if (index == 0) {
                            Intent intent = new Intent(LikeListViewActivity.this, onlyDownloadActivity.class);
                            intent.putExtra("key", UserName + "_" + SongName + ".mp4");
                            startActivity(intent);
                        } else if (index == 1) {                                                              //좋아요
                            UploadDatabaseManager myLikeDB = new UploadDatabaseManager();
                            myLikeDB.upLikeCount(getApplicationContext(), SongName, UserID);
//                                    if(MainActivity.UserIDClass.getLikeTrue()) setList(); // 좋아요 성공하면 최신화
                            setList();

                        } else if (index == 2) {                                                           //댓글보기
                            // dbContents intent 해서 보여주기. 여기서는 userName,userID, songName 일치하는 댓글 보여주기 ........... 만약 곡 이름이 같다면 업로드 못하게 설정.
                            showComment(UserID, SongName);
                        } else if (index == 3) {                                                            //취소
                            // 아무것도 하지 않음
                        }
                    }});
        alert.show();

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

