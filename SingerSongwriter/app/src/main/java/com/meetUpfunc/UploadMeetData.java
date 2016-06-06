package com.meetUpfunc;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.mysampleapp.MainActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016-05-10.
 */
public class UploadMeetData {


    private static final String ADDMYMEETUP_URL = "http://52.207.214.66/meetUp/addMyParty.php";
    private static final String SHOWPEOPLE_URL = "http://52.207.214.66/singersong/showListMeetUpPeople.php";
    String FUNCTION_URL;
    String returnString="";
    int functionNum=0;

    UploadMeetData() {

    }

    public void addMyParty(Context context, String MeetName, String OrnerID) {

        String urlSuffix = "?myUserID="+ MainActivity.UserIDClass.getUserID()+"&MeetName="+MeetName+"&OrnerID="+OrnerID;

        FUNCTION_URL=ADDMYMEETUP_URL;
        functionNum=2;
        sendTODB(context, urlSuffix);
    }


    public String showListMeetUpPeople(Context context, String MeetName, String OrnerID) {

        String urlSuffix = "?MeetName="+MeetName+"&OrnerID="+OrnerID;
        FUNCTION_URL=SHOWPEOPLE_URL;
        functionNum=1;
        sendTODB(context, urlSuffix);
        return returnString;
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
                if (functionNum == 1){
                    returnString=s;
                    Toast.makeText(mycontext, s, Toast.LENGTH_LONG).show();
                }
                if(functionNum==2)
                {
                    Toast.makeText(mycontext, s, Toast.LENGTH_LONG).show();
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
