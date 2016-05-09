package com.mysampleapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class sampleRegister extends Activity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    private Button buttonRegister;

    private static final String REGISTER_URL = "http://52.207.214.66/singersong/songUpload.php";
    private CheckBox place1;
    private CheckBox place2;
    private CheckBox place3;
    private CheckBox place4;
    private CheckBox place5;
    private int check_place1=0;
    private int check_place2=0;
    private int check_place3=0;
    private int check_place4=0;
    private int check_place5=0;
    private String checkList="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        place1 = (CheckBox) findViewById(R.id.place1);
        place2 = (CheckBox) findViewById(R.id.place2);
        place3 = (CheckBox) findViewById(R.id.place3);
        place4 = (CheckBox) findViewById(R.id.place4);
        place5 = (CheckBox) findViewById(R.id.place5);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == buttonRegister) {

                    if (place1.isChecked()) check_place1 = 1;
                    if(place2.isChecked()) check_place2=1;
                    if(place3.isChecked()) check_place3=1;
                    if(place4.isChecked()) check_place4=1;
                    if(place5.isChecked()) check_place5=1;
                    checkList=""+check_place1+""+check_place2+""+check_place3+""+check_place4+""+check_place5;
                    registerUser();
                }
            }
        });

    }
    private void registerUser() {
        String username = editTextUsername.getText().toString().trim().toLowerCase();
        String password = editTextPassword.getText().toString().trim().toLowerCase();

        register(username,password);
    }

    private void register(String username, String password) {


        String urlSuffix = "?username="+username+"&userID="+password+"&songName="+"1111"+"&contents="
                +"1111"+"&filepath="+"1111";

        class RegisterUser extends AsyncTask<String, Void, String>{

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(sampleRegister.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

                if(s.equals("successfully Upload")) {
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
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
