package com.meetUpfunc;



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

import com.RecyclerUtil.MeetupPeopleRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.mysampleapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShowListMeetUpPeople extends Activity {


    private static final String MEETUP_PEOPLE_URL = "http://52.207.214.66/meetUp/showListMeetUpPeople.php";
    String ornerID;
    String meetName;
    String authority;

    String listPeopleName[];
    String listPeopleID[];

    MeetupPeopleRecyclerAdapter adapter;
    RecyclerView recyclerView;
    static Context myContext;
    List<MeetPeopleData> rowItems;
    UploadMeetData myDB;




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
        setContentView(R.layout.activity_meetup_peoplelist);
        myContext=getApplicationContext();
        myDB = new UploadMeetData();


        recyclerView = (RecyclerView) findViewById(R.id.my_meetup_people_view);

        Intent intent = getIntent();
        ornerID = intent.getStringExtra("ornerID");
        meetName = intent.getStringExtra("meetName");
        authority = intent.getStringExtra("authority");


        setList(ornerID, meetName);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(authority.equals("1"))
                {
                    alertShow(position);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
    }

    public static Context getContext() { return myContext;}
    private void setList(String ornerID,String meetName) {
        String urlSuffix="?ornerID="+ornerID+"&meetName="+meetName;

        class MeetupListClass extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ShowListMeetUpPeople.this, "Please Wait",null, true, true);
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
                    URL url = new URL(MEETUP_PEOPLE_URL+s);
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
        ru.execute(urlSuffix);
    }


    public void setListView(String meetList) {

        rowItems = new ArrayList<MeetPeopleData>();
        if(!meetList.isEmpty()) {
            String[] meetListArray = meetList.split("--:--");             // 전체 테이블 받아옴.

            listPeopleName = new String[meetListArray.length];
            listPeopleID = new String[meetListArray.length];

            for (int k = 0; k < meetListArray.length - 1; k++) {
                String listArray[];
                listArray = meetListArray[k].split(":::");
                listPeopleID[k]=listArray[1];
                listPeopleName[k] = listArray[2];

                MeetPeopleData item = new MeetPeopleData(meetName, listArray[1], listArray[2]);
                rowItems.add(item);
            }
        }
        adapter = new MeetupPeopleRecyclerAdapter(this, rowItems);
        recyclerView.setAdapter(adapter);

    }


    public void alertShow(int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(ShowListMeetUpPeople.this);
        final String userName = listPeopleName[position];
        final String userID = listPeopleID[position];

        final CharSequence[] items = {"모임장 권한부여", "취소"};

        alert.setTitle(userName)
                .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                    public void onClick(DialogInterface dialog, int index) {


                        if (index == 0) { //위치수정

                            myDB.updateAuthority(getApplicationContext(),meetName,ornerID,userID);
                        }
                        else if (index == 1) {//취소
                        }
                    }
                });
            alert.show();
    }

}
