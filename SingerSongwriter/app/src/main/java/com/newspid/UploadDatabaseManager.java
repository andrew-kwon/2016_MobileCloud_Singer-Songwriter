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

import com.mysampleapp.MainActivity;
import com.mysampleapp.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016-05-10.
 */
public class UploadDatabaseManager {


    private static final String REGISTER_URL = "http://52.207.214.66/singersong/sendComment.php";
    private static final String COUNTUPLIKE_URL = "http://52.207.214.66/singersong/countUpLike.php";
    String FUNCTION_URL;
    int functionNum=0;

    UploadDatabaseManager() {

    }

    public void upLikeCount(Context context, String SongName, String UserID) {

        String urlSuffix = "?myUserID="+ MainActivity.UserIDClass.getUserID()+"&SongName="+SongName+"&UserID="+UserID;
        sendTODB(context, urlSuffix);
        FUNCTION_URL=COUNTUPLIKE_URL;
        functionNum=2;
    }


    public void sendComment(Context context, String myName, String comment, String SongName, String UserID) {

        String urlSuffix = "?myName="+myName+"&comment="+comment+"&SongName="+SongName+"&UserID="
                +UserID;
        sendTODB(context, urlSuffix);
        FUNCTION_URL=REGISTER_URL;
        functionNum=1;
    }

    private void sendTODB(Context context, String urlSuffix) {

        final Context mycontext = context;

        class RegisterUser extends AsyncTask<String, Void, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(functionNum==1)Toast.makeText(mycontext,s,Toast.LENGTH_LONG).show();
                if(functionNum==2)
                {
                    if(s.equals("SuccessSuccess")) {
                        Toast.makeText(mycontext,"!좋아요!",Toast.LENGTH_LONG).show();
//                        MainActivity.UserIDClass.setLikeTrue(true);                /// 바뀌는게 늦게 변하기 때문에 안먹힘.
                    }
                    else {
                        Toast.makeText(mycontext,s,Toast.LENGTH_LONG).show();
//                        MainActivity.UserIDClass.setLikeTrue(false);
                    }
                }
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(FUNCTION_URL+s);
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
