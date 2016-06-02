package com.meetUpfunc;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.RecyclerUtil.SongRecyclerAdapter;
import com.RecyclerUtil.RecyclerItemClickListener;
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

        class MeetUpAsync extends AsyncTask<String, Void, String> {

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(MeetUpListActivity.this, "Please wait", "Loading...");
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://52.207.214.66/singersong/meetUpListView.php");
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
                    setListView(result);

                }else {
                    Toast.makeText(getApplicationContext(), "Invalid User Name or Password", Toast.LENGTH_LONG).show();
                }
            }
        }

        MeetUpAsync la = new MeetUpAsync();
        la.execute();

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
