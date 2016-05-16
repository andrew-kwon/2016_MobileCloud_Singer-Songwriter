package com.songDatabase;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.mysampleapp.MainActivity;
import com.mysampleapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShowCommentActivity extends Activity {


    private static final String REGISTER_URL = "http://52.207.214.66/singersong/showComments.php";
    static String UserID;
    static String SongName;
    String listContents[];
    String listUserName[];
    private Button addComment;

    @Override
    public void onBackPressed()
    {
        Intent backtoComment = new Intent();
        setResult(RESULT_OK, backtoComment);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showcomment);

        Intent intent = getIntent();
        UserID = intent.getStringExtra("UserID");
        SongName = intent.getStringExtra("SongName");

        addComment= (Button) findViewById(R.id.addComment);                          // 마이페이지 기능구현

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ShowCommentActivity.this);
                alert.setTitle("댓글 달기");
                final EditText input = new EditText(ShowCommentActivity.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                           String comment = input.getText().toString();
                           String myName = MainActivity.UserIDClass.getUserName();
                           comment=comment.replaceAll("\\s","　");
                           UploadDatabaseManager myCommentDB= new UploadDatabaseManager();
                           myCommentDB.sendComment(getApplicationContext(),myName, comment, SongName, UserID);
                           showComment(UserID,SongName);
                           }
                 });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }      });
                alert.show();
            }
        });

        showComment(UserID,SongName);

    }

    private void showComment(String UserID, String SongName) {


        String urlSuffix = "?UserID="+UserID+"&SongName="+SongName;

        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowCommentActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
//                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();                 //// s를 리스트 뷰로 출력
                setCommentsView(s);
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(REGISTER_URL+s);
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


    public void setCommentsView(String commentsList)
    {

        ListView listView = (ListView) findViewById(R.id.showCommentListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        listView.setAdapter(adapter);
        String[] commentsListArray =commentsList.split("--:--");             // 전체 테이블 받아옴.

        listUserName = new String[commentsListArray.length];
        listContents = new String[commentsListArray.length];

//        Toast.makeText(getApplicationContext(),""+commentsList,Toast.LENGTH_LONG).show();

        for(int k=0; k<commentsListArray.length-1; k++){                // 테이블 별로 구분해서 split 짜르기.      /////// 왜 length -1 일까.....?
//
            listUserName[k]=commentsListArray[k].split(":::")[1];
            listContents[k]=commentsListArray[k].split(":::")[2];
            adapter.add(listUserName[k]+" : "+listContents[k]);
        }

        // 아이템을 [클릭]시의 이벤트 리스너를 등록
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }



}
