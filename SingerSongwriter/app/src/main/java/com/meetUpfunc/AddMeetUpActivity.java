package com.meetUpfunc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mysampleapp.MainActivity;
import com.mysampleapp.R;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class AddMeetUpActivity extends Activity {


    private static int RESULT_LOAD_IMAGE = 1;
    String UPLOAD_MEET_IMAGE = "http://52.207.214.66/meetUp/uploadMeetImage.php";
    Button buttonDate,buttonTime;
    Button buttonMeetName,buttonPlaceName;
    Button buttonAdd, buttonGetPlace;
    ImageView imageView;
    UploadMeetData myDB;
    String setTime="null";
    String setDate="null";
    String setMeetName="";
    String setLatitude="null";
    String setLongitude="null";
    String setContent="null";
    String setPlaceName="null";
    String ba1;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmeetup);
        myDB=new UploadMeetData();

        Calendar calendar2 = Calendar.getInstance();
        String nowTime = calendar2.getTime().toString();
        final DatePickerDialog dialog = new DatePickerDialog(this, listener, 2016, 5, 10);
        final TimePickerDialog timedialog = new TimePickerDialog(this, timelistener, 10, 00, false);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        buttonPlaceName=(Button) findViewById(R.id.btn_placeName);
        buttonPlaceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddMeetUpActivity.this);
                alert.setTitle("장소이름");
                alert.setMessage("장소를 입력하세요.");

                // Set an EditText view to get user input
                final EditText input = new EditText(AddMeetUpActivity.this);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        buttonPlaceName.setText(value);
                        setPlaceName=value;
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                alert.show();
            }
        });
        buttonMeetName =(Button) findViewById(R.id.btn_meetName);
        buttonMeetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddMeetUpActivity.this);
                alert.setTitle("이름");
                alert.setMessage("만남 이름을 입력하세요.");

                // Set an EditText view to get user input
                final EditText input = new EditText(AddMeetUpActivity.this);
                alert.setView(input);
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = input.getText().toString();
                        buttonMeetName.setText(value);
                        setMeetName=value;
                        setMeetName=setMeetName.replaceAll("\\s","_");
                    }
                });
                alert.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Canceled.
                            }
                        });

                alert.show();
            }
        });

        buttonDate = (Button) findViewById(R.id.button_date);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dialog.show();
            }
        });

        buttonTime = (Button) findViewById(R.id.button_time);
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                timedialog.show();
            }
        });

        buttonGetPlace = (Button) findViewById(R.id.btn_select_place);
        buttonGetPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(AddMeetUpActivity.this,setMapsActivity.class);
                startActivityForResult(intent, 1);


            }
        });


        buttonAdd = (Button) findViewById(R.id.button_add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean returnAdd = myDB.addMeetUp(getApplicationContext(),setMeetName, setDate, setTime,
                        setPlaceName, setLatitude,setLongitude,setContent);
                new uploadMeetImageToServer().execute();
//
//                Intent backtoComment = new Intent();
//                setResult(RESULT_OK, backtoComment);
//                finish();


            }
        });

    }



    private TimePickerDialog.OnTimeSetListener timelistener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
// 설정버튼 눌렀을 때
            Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            buttonTime.setText(hourOfDay + ":" + minute);
            setTime=hourOfDay + ":" + minute;
        }
    };

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener()
    {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear++;
            Toast.makeText(getApplicationContext(), year + "년" + monthOfYear + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
            buttonDate.setText(year + "." + monthOfYear + "." + dayOfMonth);
            setDate=year+"." +monthOfYear + "." +dayOfMonth;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            Bitmap resized=null;
            try {
                bmp = getBitmapFromUri(selectedImage);

               resized = Bitmap.createScaledBitmap(bmp,bmp.getWidth()/4,bmp.getHeight()/4,true);


                ba1 = BitMapToString(resized);
                Log.e("base64", "-----" + ba1);
                // Upload image to server
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            imageView.setImageBitmap(resized);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);      //이미지 조정
        }

        if(resultCode==2 && data !=null)
        {
//         Toast.makeText(getApplicationContext(), "" + data.getStringExtra("place_lati") + " : " + data.getStringExtra("place_longti"), Toast.LENGTH_LONG).show();
            setLatitude=data.getStringExtra("place_lati");
            setLongitude=data.getStringExtra("place_longti");
            buttonGetPlace.setText("등록완료");

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public String BitMapToString(Bitmap bitmap){

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);

        return temp;
    }
    public final class uploadMeetImageToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(AddMeetUpActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

            try {
                setMeetName = URLEncoder.encode(setMeetName, "UTF-8");
            }catch (IOException e) {
                Log.v("AUDIOHTTPPLAYER", e.getMessage());
            }

            nameValuePairs.add(new BasicNameValuePair("base64", ba1));
            nameValuePairs.add(new BasicNameValuePair("ImageName", ""+setMeetName+"_"+MainActivity.UserIDClass.getUserID() + ".jpg"));
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(UPLOAD_MEET_IMAGE);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }


}