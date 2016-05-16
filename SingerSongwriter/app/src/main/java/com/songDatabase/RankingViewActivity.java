package com.songDatabase;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.mysampleapp.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class RankingViewActivity extends Activity {

    private EditText editTextUserName;
    private EditText editTextPassword;
    public static final String USER_NAME = "USERNAME";

    String selectFilepath[];
    String contents[];
    String listUsername[];
    String listSongname[];
    String listUserID[];
    int commentCount[];
    int likeCount[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_songlistview);

        setList();
    }


    public void setListView(String songList)
    {

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        String[] songListArray =songList.split("--:--");             // 전체 테이블 받아옴.

        listSongname = new String[songListArray.length];
        listUsername  = new String[songListArray.length];
        listUserID = new String[songListArray.length];
        selectFilepath = new String[songListArray.length];
        contents = new String[songListArray.length];
        commentCount = new int[songListArray.length];
        likeCount = new int[songListArray.length];

        for(int k=0; k<songListArray.length; k++){                // 테이블 별로 구분해서 split 짜르기.


            String listArray[];
            listArray=songListArray[k].split(":::");

            listUsername[k]=listArray[1];
            listUserID[k]=listArray[2];
            listSongname[k]=listArray[3];
            contents[k]=listArray[4];
            selectFilepath[k]=listArray[5];
            commentCount[k]=Integer.parseInt(listArray[6]);
            likeCount[k]=Integer.parseInt(listArray[7]);
        }


//        adapter.add(listUsername[k]+" <SongName : "+listSongname[k]+" >");





        // 아이템을 [클릭]시의 이벤트 리스너를 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ListView listView = (ListView) parent;
                String item = (String) listView.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG).show();
                final String getString = item;

                AlertDialog.Builder alert = new AlertDialog.Builder(RankingViewActivity.this);

                final String UserName = listUsername[position];
                final String SongName = listSongname[position];
                final String UserID = listUserID[position];
                final int commentCounts = commentCount[position];
                final int likeCounts = likeCount[position];
                final CharSequence[] items = {"다운받기", "좋아요"+" ("+likeCounts+")", "댓글보기" + " (" + commentCounts + ")", "취소"};

                alert.setTitle(SongName + " : " + contents[position])
                        .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                            public void onClick(DialogInterface dialog, int index) {
//
                                if (index == 0) {                                                                    //다운받기
                                    // intent onlyDownload ( 파일페스, item.filepath 잡아서 보내기 )
//                            String UserName = getString.split(" <SongName : ")[0];
//                            String fileName = getString.split("<SongName : ")[1].split(" >")[0];

                                    Intent intent = new Intent(RankingViewActivity.this, onlyDownloadActivity.class);
                                    intent.putExtra("key", UserName + "_" + SongName + ".mp4");
                                    startActivity(intent);

                                } else if (index == 1) {                                                              //좋아요

                                    // db 접속후 count 올리기 ---> 한사람이 하나밖에 못올리게 설정 !! 중요
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

                            }
                        });
                alert.show();

            }
        });
    }

    private void setList() {

        class LoginAsync extends AsyncTask<String, Void, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(RankingViewActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://52.207.214.66/singersong/songListView.php");
                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.charAt(0)==':'){

                    // add set list view
                    setListView(s);

                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute();

    }


    private void showComment(String UserID, String SongName)
    {
        Intent intent = new Intent(RankingViewActivity.this,ShowCommentActivity.class);
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

}
