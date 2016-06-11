package com.meetUpfunc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.RecyclerUtil.MeetupRecyclerAdapter;
import com.RecyclerUtil.SongRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.amazonS3.UploadActivity;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;
import com.songDatabase.ShowCommentActivity;
import com.songDatabase.SongListViewActivity;
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
    UploadMeetData myDB;
    String joinURL="http://52.207.214.66/meetUp/meetUpJoinListView.php";
    String listURL="http://52.207.214.66/meetUp/meetUpListView.php";
    String FUNC_URL="";
    String urlParse="";

    String[] listMeetname, listOrderID;
    String[] listLati, listLong, listPlaceName, listTime;
    String[] listCountPeople, listAuthority;
    String join;

    String getLatitude;
    String getLongitude;

    String getDate;
    String getTime;

    DatePickerDialog datedialog;
    TimePickerDialog timedialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetuplist);

        myContext=getApplicationContext();
        myDB = new UploadMeetData();
        recyclerView = (RecyclerView) findViewById(R.id.my_meetup_view);
        datedialog = new DatePickerDialog(this, listener, 2016, 5, 10);
        timedialog = new TimePickerDialog(this, timelistener, 10, 00, false);

        Intent intent = getIntent();
        join = intent.getStringExtra("join");
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
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        GridLayoutManager layoutManager2 = new GridLayoutManager(getApplicationContext(),2);

        recyclerView.setLayoutManager(layoutManager2);
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
        if(!meetList.isEmpty()) {
            String[] meetListArray = meetList.split("--:--");             // 전체 테이블 받아옴.

            listMeetname = new String[meetListArray.length];
            listOrderID = new String[meetListArray.length];
            listLati = new String[meetListArray.length];
            listLong = new String[meetListArray.length];
            listPlaceName = new String[meetListArray.length];
            listTime = new String[meetListArray.length];
            listCountPeople = new String[meetListArray.length];
            listAuthority = new String[meetListArray.length];

            for (int k = 0; k < meetListArray.length - 1; k++) {
                String listArray[];
                listArray = meetListArray[k].split(":::");
                listMeetname[k] = listArray[1];
                listPlaceName[k]=listArray[2];
                listLati[k]=listArray[3];
                listLong[k]=listArray[4];
                listTime[k]=listArray[5]+"."+listArray[6];
                listOrderID[k] = listArray[9];
                listCountPeople[k]=listArray[10];
                listAuthority[k]=listArray[11];
                MeetData item = new MeetData(listArray[1], listArray[2], listArray[9],listArray[10]);
                rowItems.add(item);
            }
        }
        adapter = new MeetupRecyclerAdapter(this, rowItems);
        recyclerView.setAdapter(adapter);

    }

    public void alertShow(int position){

        AlertDialog.Builder alert = new AlertDialog.Builder(MeetUpListActivity.this);

        final String MeetName = listMeetname[position];
        final String OrderID = listOrderID[position];
        final String latitude = listLati[position];
        final String longitude = listLong[position];
        final String meetTime = listTime[position];
        final String meetPlaceName = listPlaceName[position];
        final String countPeople = listCountPeople[position];
        final String getAuthority = listAuthority[position];
        final CharSequence[] items = {"참여하기", "위치보기"+" [ "+meetPlaceName+" ]","시간"+" [ "+meetTime+" ]", "참여자보기"+" [ "+countPeople+"명 ]" , "채팅하기" , "취소"};
        final CharSequence[] authorityItems ={"위치수정"+" [ "+meetPlaceName+" ]","시간수정"+" [ "+meetTime+" ]","참여자 관리"+" [ "+countPeople+"명 ]","취소"};

        if(join.equals("TRUE")&&getAuthority.equals("1"))
        {   alert.setTitle(MeetName)
                .setItems(authorityItems, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                    public void onClick(DialogInterface dialog, int index) {


                        if (index == 0) { //위치수정

                            Intent intent = new Intent(MeetUpListActivity.this, setMapsActivity.class);
                            intent.putExtra("place_lati", latitude);
                            intent.putExtra("place_longi", longitude);
                            startActivityForResult(intent, 1);


                            AlertDialog.Builder alert = new AlertDialog.Builder(MeetUpListActivity.this);
                            alert.setTitle("Log");
                            alert.setMessage("위치 제목을 입력하세요.");
                            // Set an EditText view to get user input
                            final EditText input = new EditText(MeetUpListActivity.this);
                            alert.setView(input);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    String getPlaceName = input.getText().toString();
                                    if (getPlaceName.equals("")) {
                                        Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        myDB.updateLocation(myContext, MeetName, OrderID, getLatitude, getLongitude, getPlaceName);
                                        setList();
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


                        } else if (index == 1) { //시간수정

                            AlertDialog.Builder alert = new AlertDialog.Builder(MeetUpListActivity.this);
                            alert.setTitle("Log");
                            alert.setMessage("시간을 수정하시겠습니까?.");
                            // Set an EditText view to get user input
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    myDB.updateTime(myContext, MeetName, OrderID,getDate,getTime);
                                    setList();
                                }
                            });
                            alert.setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            // Canceled.
                                        }
                                    });
                            alert.show();
                            timedialog.show();
                            datedialog.show();
                        } else if (index == 2) {
                         //참여자 관리
                            Intent intent = new Intent(MeetUpListActivity.this,ShowListMeetUpPeople.class);
                            intent.putExtra("meetName", MeetName);
                            intent.putExtra("ornerID", OrderID);
                            intent.putExtra("authority","1");
                            startActivityForResult(intent, 1);


                        } else if (index == 3) {//취소
                        }
                    }
                });
            alert.show();
        }
        else {
            alert.setTitle(MeetName)
                    .setItems(items, new DialogInterface.OnClickListener() {                               ///메뉴선택별 기능 추가
                        public void onClick(DialogInterface dialog, int index) {
                            if (index == 0) {
                                myDB.addMyParty(myContext, MeetName, OrderID);
                            } else if (index == 1) {

                                Intent intent = new Intent(MeetUpListActivity.this, getMapsActivity.class);
                                intent.putExtra("place_lati", latitude);
                                intent.putExtra("place_longi", longitude);
                                startActivityForResult(intent, 1);

                            } else if (index == 2) {
                                //캘린더에 등록하기.
                            } else if (index == 3) {
                                //String = showListMeetUpPeople      (회원 ID, 회원 이름 ) --> 리스트뷰로 알려줌
                                Intent intent = new Intent(MeetUpListActivity.this,ShowListMeetUpPeople.class);
                                intent.putExtra("meetName", MeetName);
                                intent.putExtra("ornerID", OrderID);
                                intent.putExtra("authority","0");
                                startActivityForResult(intent, 1);

                            } else if (index == 4) {                            //gotochatting
                            } else if (index == 5) {
                                // 아무것도 하지 않음

                            }
                        }
                    });
            alert.show();
        }

    }


    private TimePickerDialog.OnTimeSetListener timelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// 설정버튼 눌렀을 때
//            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();

            getTime=hourOfDay + ":" + minute;
        }
    };

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
//            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            getDate=year+"." +monthOfYear + "." +dayOfMonth;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
        }

        if(resultCode==2 && data !=null)
        {
//         Toast.makeText(getApplicationContext(), "" + data.getStringExtra("place_lati") + " : " + data.getStringExtra("place_longti"), Toast.LENGTH_LONG).show();
            getLatitude=data.getStringExtra("place_lati");
            getLongitude=data.getStringExtra("place_longti");

        }

    }

}
