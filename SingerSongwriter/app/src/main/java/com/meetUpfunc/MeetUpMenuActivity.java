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
    Button addMeetUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetupmenu);

        meetupList = (Button) findViewById(R.id.buttonMeetupList);
        JoinList = (Button) findViewById(R.id.buttonJoinList);
        addMeetUp = (Button) findViewById(R.id.buttonAddMeet);


        meetupList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetUpMenuActivity.this,MeetUpListActivity.class);
                intent.putExtra("join", "FALSE");
                startActivity(intent);
            }
        });

        JoinList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetUpMenuActivity.this,MeetUpListActivity.class);
                intent.putExtra("join", "TRUE");
                startActivity(intent);
            }
        });

        addMeetUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                // addMeetup( 알림창 --> 모임이름, 내용, 위치, 위치이름 , 사진추가 )
                Intent intent = new Intent (MeetUpMenuActivity.this, AddMeetUpActivity.class);
                startActivityForResult(intent,1);


            }
        });
    }

}
