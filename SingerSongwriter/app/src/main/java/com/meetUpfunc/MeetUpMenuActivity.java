package com.meetUpfunc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mysampleapp.R;

/**
 * Created by Administrator on 2016-06-05.
 */
public class MeetUpMenuActivity extends Activity {

    Button JoinList;
    Button meetupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetupmenu);

        meetupList = (Button) findViewById(R.id.buttonMeetupList);
        JoinList = (Button) findViewById(R.id.buttonJoinList);

        meetupList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetUpMenuActivity.this,MeetUpListActivity.class);
                startActivity(intent);
            }
        });

        JoinList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetUpMenuActivity.this,MeetUpJoinListActivity.class);
                startActivity(intent);
            }
        });
    }

}
