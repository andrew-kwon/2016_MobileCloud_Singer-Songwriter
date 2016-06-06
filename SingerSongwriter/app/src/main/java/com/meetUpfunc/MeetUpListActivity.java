package com.meetUpfunc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.RecyclerUtil.SongRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
import com.amazonS3.UploadActivity;
import com.mysampleapp.MainActivity;
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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016-06-02.
 */
public class MeetUpListActivity extends Activity {


    SongRecyclerAdapter adapter;
    RecyclerView recyclerView;
    static Context myContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetuplist);

        myContext=getApplicationContext();
        recyclerView = (RecyclerView) findViewById(R.id.my_meetup_view);
        setList();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position)
            {
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
    }

    public static Context getContext() { return myContext;}
    private void setList() {
//        String urlSuffix = "?username="+userName+"&userID="+userID+"&songName="+songName+"&contents="
//                +contents+"&filepath="+filepath;
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
                if(s.equals("successfully Upload")) {
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
                    URL url = new URL("http://52.207.214.66/singersong/meetUpListView.php"+s);
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




    public void setListView(String songList) {

//        rowItems = new ArrayList<songData>();
        String[] songListArray = songList.split("--:--");             // 전체 테이블 받아옴.


        for (int k = 0; k < songListArray.length - 1; k++) {
            String listArray[];
            listArray = songListArray[k].split(":::");

//            listUsername[k] = listArray[1];
//            songData item = new songData(listSongname[k], contents[k], listUsername[k],
//                    "" + likeCount[k],listUserID[k]);
//            rowItems.add(item);
        }

//        adapter = new RecyclerAdapter(this, rowItems);
        recyclerView.setAdapter(adapter);

    }

}
