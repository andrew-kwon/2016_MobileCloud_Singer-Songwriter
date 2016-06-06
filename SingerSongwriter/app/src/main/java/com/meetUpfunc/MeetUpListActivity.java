package com.meetUpfunc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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

import com.RecyclerUtil.MeetupRecyclerAdapter;
import com.RecyclerUtil.SongRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.amazonS3.UploadActivity;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;
import com.songDatabase.UploadDatabaseManager;
import com.songDatabase.onlyDownloadActivity;
import com.songDatabase.songData;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetUpListActivity extends Activity {


    MeetupRecyclerAdapter adapter;
    RecyclerView recyclerView;
    static Context myContext;
    List<MeetData> rowItems;
    String[] listMeetname;
    String[] listOrderID;
    UploadMeetData myDB;
    String joinURL="http://52.207.214.66/meetUp/meetUpJoinListView.php";
    String listURL="http://52.207.214.66/meetUp/meetUpListView.php";
    String FUNC_URL="";
    String urlParse="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetuplist);

        myContext=getApplicationContext();
        myDB = new UploadMeetData();
        recyclerView = (RecyclerView) findViewById(R.id.my_meetup_view);


        Intent intent = getIntent();
        String join = intent.getStringExtra("join");
        if(join.equals("FALSE")){
            FUNC_URL=listURL;

        }
        else{
            FUNC_URL=joinURL;
            urlParse="?myUserID="+MainActivity.UserIDClass.getUserID();
        }

            setList();
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {alertShow(position);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
    }

    public static Context getContext() { return myContext;}
    private void setList() {
        String urlSuffix="";

        class MeetupListClass extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MeetUpListActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                setListView(s);

            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(FUNC_URL+s);
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

        MeetupListClass ru = new MeetupListClass();
        ru.execute(urlParse);
    }

    public void setListView(String meetList) {

        rowItems = new ArrayList<MeetData>();
        String[] meetListArray = meetList.split("--:--");             // 전체 테이블 받아옴.

        listMeetname = new String[meetListArray.length-1];
        listOrderID = new String[meetListArray.length-1];
        for (int k = 0; k < meetListArray.length - 1; k++) {
            String listArray[];
            listArray = meetListArray[k].split(":::");

//            String meetupName, String userID,
//                    String content)

            listMeetname[k]=listArray[1];
            listOrderID[k]=listArray[8];
            MeetData item = new MeetData(listArray[1],listArray[2],listArray[8]);
            rowItems.add(item);
        }

        adapter = new MeetupRecyclerAdapter(this, rowItems);
        recyclerView.setAdapter(adapter);

    }


    public void alertShow(int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(MeetUpListActivity.this);

        final String MeetName = listMeetname[position];
        final String OrderID = listOrderID[position];
        final CharSequence[] items = {"참여하기", "위치보기", "참여자보기" , "채팅하기" , "취소"};

        alert.setTitle(MeetName)
                .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                    public void onClick(DialogInterface dialog, int index) {
                        if (index==0)
                        {
                            myDB.addMyParty(myContext, MeetName, OrderID);

                        }
                        else if (index == 1) {
                            //mapview(latitute,longtitude)

                        } else if (index == 2) {
                            //String = showListMeetUpPeople      (회원 ID, 회원 이름 ) --> 리스트뷰로 알려줌

                        } else if (index == 3) {
                            //gotochatting
                        }
                        else if (index == 4) {                                                            //취소
                            // 아무것도 하지 않음
                        }
                    }
                });
        alert.show();

    }

}
