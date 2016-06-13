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
    private static final String SHOWPEOPLE_URL = "http://52.207.214.66/meetUp/showListMeetUpPeople.php";
    private static final String UPDATETIME_URL = "http://52.207.214.66/meetUp/updateMeetTime.php";
    private static final String UPDATEPLACE_URL = "http://52.207.214.66/meetUp/updateMeetPlace.php";
    private static final String UPAUTHORITY_URL = "http://52.207.214.66/meetUp/updateAuthority.php";

    String FUNCTION_URL;
    String returnString="";
    boolean returnBoolean;
    int functionNum=0;

    UploadMeetData() {

    }

    public void addMyParty(Context context, String MeetName, String OrnerID) {

        String urlSuffix = "?myUserID="+ MainActivity.UserIDClass.getUserID()+"&MeetName="+MeetName+"&OrnerID="+OrnerID+"&myUserName="+MainActivity.UserIDClass.getUserName();

        FUNCTION_URL=ADDMYMEETUP_URL;
        functionNum=2;
        sendTODB(context, urlSuffix);
    }

    public void updateLocation(Context context,String meetName, String ornerID, String latitude, String longitude, String placeName)
    {
        Toast.makeText(context,latitude+":"+longitude,Toast.LENGTH_LONG).show();
        placeName=placeName.replaceAll("\\s","_");
        String urlSuffix = "?meetName="+meetName+"&ornerID="+ornerID+"&latitude="+latitude+"&longitude="+longitude+"&placeName="+placeName;
        FUNCTION_URL=UPDATEPLACE_URL;
        functionNum=2;
        sendTODB(context,urlSuffix);

    }

    public void updateTime(Context context,String meetName, String ornerID ,String meetDate, String meetTime)
    {

        Toast.makeText(context,meetDate+"."+meetTime,Toast.LENGTH_LONG).show();
        String urlSuffix = "?meetName="+meetName+"&ornerID="+ornerID+"&meetDate="+meetDate+"&meetTime="+meetTime;
        FUNCTION_URL=UPDATETIME_URL;
        functionNum=2;
        sendTODB(context, urlSuffix);

    }

    public void updateAuthority(Context context,String meetName, String ornerID,String userID)
    {
//        Toast.makeText(context,meetDate+"."+meetTime,Toast.LENGTH_LONG).show();
        String urlSuffix = "?meetName="+meetName+"&ornerID="+ornerID+"&userID="+userID;
        FUNCTION_URL=UPAUTHORITY_URL;
        functionNum=2;
        sendTODB(context, urlSuffix);


    }

//    public boolean addMeetUpFunction(Context context,String setMeetName,String  setDate,String setTime,
//                          String setPlaceName,String setLatitude,String setLongtitude,String setContent){
//
//        setPlaceName = setPlaceName.replaceAll("\\s","_");
//
//    String urlSuffix = "?MeetName="+setMeetName.trim()+"&OrnerID="+MainActivity.UserIDClass.getUserID()
//            +"&setDate="+setDate+"&setTime="+setTime+"&setPlaceName="+setPlaceName.trim()+"&setLatitude="+setLatitude+"&setLongtitude="+setLongtitude
//            +"&setContent="+setContent+"&OrnerName="+MainActivity.UserIDClass.getUserName()+"&myUserName="+MainActivity.UserIDClass.getUserName();
//        FUNCTION_URL=ADDMEETUP_URL;
//        functionNum=1;
//        sendTODB(context, urlSuffix);
//        return returnBoolean;
//    }


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
                    if(s!=null) returnBoolean=true;
                    else returnBoolean = false;
                }
                if(functionNum==2)
                {
                    Toast.makeText(mycontext,s,Toast.LENGTH_LONG).show();

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
