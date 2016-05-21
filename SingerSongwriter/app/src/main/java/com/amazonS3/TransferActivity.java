package com.amazonS3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.mysampleapp.R;
import com.songDatabase.LikeListViewActivity;

/*
 * This is the beginning screen that lets the user select if they want to upload or download
 */
public class TransferActivity extends Activity {

    private Button btnDownload;
    private Button btnUpload;
    private Button btnLikeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        initUI();
    }

    private void initUI() {
        btnDownload = (Button) findViewById(R.id.buttonDownloadMain);
        btnUpload = (Button) findViewById(R.id.buttonUploadMain);
        btnLikeList=(Button)findViewById(R.id.buttonLikeList);

        btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TransferActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });

        btnUpload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TransferActivity.this, UploadActivity.class);
                startActivity(intent);
            }
        });
        btnLikeList.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(TransferActivity.this, LikeListViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
