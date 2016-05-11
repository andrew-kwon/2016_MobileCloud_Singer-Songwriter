package com.newspid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.mysampleapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016-05-10.
 */
public class CommentDBclass {


    private static final String REGISTER_URL = "http://52.207.214.66/singersong/sendComment.php";

    CommentDBclass() {

    }

    public void sendComment(Context context, String myName, String comment, String SongName, String UserID) {

        sendTODB(context, myName, comment, SongName, UserID);
    }

    private void sendTODB(Context context, String myName, String comment, String SongName, String UserID) {


        String urlSuffix = "?myName="+myName+"&comment="+comment+"&SongName="+SongName+"&UserID="
                +UserID;
        final Context mycontext = context;

        class RegisterUser extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(mycontext,s,Toast.LENGTH_LONG).show();

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
}
