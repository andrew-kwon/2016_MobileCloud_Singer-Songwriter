package com.musicUtil;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonS3.Constants;
import com.amazonS3.DownloadSelectionActivity;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.mysampleapp.MainActivity;
import com.mysampleapp.R;
import com.mysampleapp.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DownloadActivity displays a list of download records and a bunch of buttons
 * for managing the downloads.
 */
public class MusicDownloadActivity extends ListActivity {
    private static final String TAG = "MusicDownloadActivity";

    private static final int DOWNLOAD_SELECTION_REQUEST_CODE = 1;

    // Indicates no row element has beens selected
    private static final int INDEX_NOT_CHECKED = -1;

    private Button btnDownload;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    private Button btnDelete;
    private Button btnPauseAll;
    private Button btnCancelAll;
    private Button btnPlay;

    // This is the main class for interacting with the Transfer Manager
    private TransferUtility transferUtility;

    // The SimpleAdapter adapts the data about transfers to rows in the UI
    private SimpleAdapter simpleAdapter;

    // A List of all transfers
    private List<TransferObserver> observers;

    /**
     * This map is used to provide data to the SimpleAdapter above. See the
     * fillMap() function for how it relates observers to rows in the displayed
     * activity.
     */
    private ArrayList<HashMap<String, Object>> transferRecordMaps;
    private int checkedIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_download);
        // Initializes TransferUtility, always do this before using it.
        transferUtility = Util.getTransferUtility(this);
        checkedIndex = INDEX_NOT_CHECKED;
        transferRecordMaps = new ArrayList<HashMap<String, Object>>();
        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/SingerSongwriter");
        if(!file.isDirectory()) file.mkdirs();

        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Clear transfer listeners to prevent memory leak, or
        // else this activity won't be garbage collected.
        if (observers != null && !observers.isEmpty()) {
            for (TransferObserver observer : observers) {
                observer.cleanTransferListener();
            }
        }
    }

    /**
     * Gets all relevant transfers from the Transfer Service for populating the
     * UI
     */
    private void initData() {
        transferRecordMaps.clear();
        // Uses TransferUtility to get all previous download records.
        observers = transferUtility.getTransfersWithType(TransferType.DOWNLOAD);
        TransferListener listener = new DownloadListener();
        for (TransferObserver observer : observers) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            Util.fillMap(map, observer, false);
            transferRecordMaps.add(map);

            // Sets listeners to in progress transfers
            if (TransferState.WAITING.equals(observer.getState())
                    || TransferState.WAITING_FOR_NETWORK.equals(observer.getState())
                    || TransferState.IN_PROGRESS.equals(observer.getState())) {
                observer.setTransferListener(listener);
            }
        }
        simpleAdapter.notifyDataSetChanged();
    }

    private void initUI() {
        /**
         * This adapter takes the data in transferRecordMaps and displays it,
         * with the keys of the map being related to the columns in the adapter
         */
        simpleAdapter = new SimpleAdapter(this, transferRecordMaps,
                R.layout.record_item, new String[] {
                "checked", "fileName", "progress", "bytes", "state", "percentage"
        },
                new int[] {
                        R.id.radioButton1, R.id.textFileName, R.id.progressBar1, R.id.textBytes,
                        R.id.textState, R.id.textPercentage
                });
        simpleAdapter.setViewBinder(new ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                switch (view.getId()) {
                    case R.id.radioButton1:
                        RadioButton radio = (RadioButton) view;
                        radio.setChecked((Boolean) data);
                        return true;
                    case R.id.textFileName:
                        TextView fileName = (TextView) view;
                        fileName.setText((String) data);
                        return true;
                    case R.id.progressBar1:
                        ProgressBar progress = (ProgressBar) view;
                        progress.setProgress((Integer) data);
                        return true;
                    case R.id.textBytes:
                        TextView bytes = (TextView) view;
                        bytes.setText((String) data);
                        return true;
                    case R.id.textState:
                        TextView state = (TextView) view;
                        state.setText(((TransferState) data).toString());
                        return true;
                    case R.id.textPercentage:
                        TextView percentage = (TextView) view;
                        percentage.setText((String) data);
                        return true;
                }
                return false;
            }
        });
        setListAdapter(simpleAdapter);

        // Updates checked index when an item is clicked
        getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                if (checkedIndex != pos) {
                    transferRecordMaps.get(pos).put("checked", true);                 //지금꺼 체크
                    if (checkedIndex >= 0) {
                        transferRecordMaps.get(checkedIndex).put("checked", false);     //전에꺼 체크해제
                    }
                    checkedIndex = pos;                                                //현재로 바꿈
                    updateButtonAvailability();
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        });

        btnDownload = (Button) findViewById(R.id.buttonDownload);
        btnPause = (Button) findViewById(R.id.buttonPause);
        btnResume = (Button) findViewById(R.id.buttonResume);
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnDelete = (Button) findViewById(R.id.buttonDelete);
        btnPauseAll = (Button) findViewById(R.id.buttonPauseAll);
        btnCancelAll = (Button) findViewById(R.id.buttonCancelAll);
        btnPlay = (Button) findViewById(R.id.buttonPlay);

        // Launches an activity for the user to select an object in their S3
        // bucket to download
        btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MusicDownloadActivity.this, PlayMusicSelect.class);
                startActivityForResult(intent, DOWNLOAD_SELECTION_REQUEST_CODE);
            }
        });

        btnPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkedIndex >= 0 && checkedIndex < observers.size()) {

                    Object nowData = simpleAdapter.getItem(checkedIndex);
                    String myData=nowData.toString();
                    String myDataSet[] = myData.split(",");
                    String fileNameSet[] = myDataSet[2].split("=");
                    String fileName=fileNameSet[1];

                    Intent intent = new Intent(MusicDownloadActivity.this, MusicPlayerActivity.class);
                    intent.putExtra("pos",  fileName);
                    Toast.makeText(
                            MusicDownloadActivity.this,
                            "pos : " +  fileName ,
                            Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();

                }

            }
        });


        btnPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make sure the user has selected a transfer
                if (checkedIndex >= 0 && checkedIndex < observers.size()) {
                    Boolean paused = transferUtility.pause(observers.get(checkedIndex)
                            .getId());

                    /**
                     * If paused does not return true, it is likely because the
                     * user is trying to pause a download that is not in a
                     * pausable state (For instance it is already paused, or
                     * canceled).
                     */
                    if (!paused) {
                        Toast.makeText(
                                MusicDownloadActivity.this,
                                "Cannot Pause transfer.  You can only pause transfers in a WAITING or IN_PROGRESS state.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnResume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // Make sure the user has selected a transfer
                if (checkedIndex >= 0 && checkedIndex < observers.size()) {
                    TransferObserver resumed = transferUtility.resume(observers.get(checkedIndex)
                            .getId());
                    // Sets a new transfer listener to the original observer.
                    // This will overwrite existing listener.
                    observers.get(checkedIndex).setTransferListener(new DownloadListener());

                    /**
                     * If resume returns null, it is likely because the transfer
                     * is not in a resumable state (For instance it is already
                     * running).
                     */
                    if (resumed == null) {
                        Toast.makeText(
                                MusicDownloadActivity.this,
                                "Cannot resume transfer.  You can only resume transfers in a PAUSED state.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure a transfer is selected
                if (checkedIndex >= 0 && checkedIndex < observers.size()) {
                    Boolean canceled = transferUtility.cancel(observers.get(checkedIndex).getId());
                    /**
                     * If cancel returns false, it is likely because the
                     * transfer is already canceled
                     */
                    if (!canceled) {
                        Toast.makeText(
                                MusicDownloadActivity.this,
                                "Cannot cancel transfer.  You can only resume transfers in a PAUSED, WAITING, or IN_PROGRESS state.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Make sure a transfer is selected
                if (checkedIndex >= 0 && checkedIndex < observers.size()) {
                    // Deletes a record but the file is not deleted.
                    transferUtility.deleteTransferRecord(observers.get(checkedIndex).getId());
                    observers.remove(checkedIndex);
                    transferRecordMaps.remove(checkedIndex);
                    checkedIndex = INDEX_NOT_CHECKED;
                    updateButtonAvailability();
                    updateList();
                }
            }
        });

        btnPauseAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                transferUtility.pauseAllWithType(TransferType.DOWNLOAD);
            }
        });

        btnCancelAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                transferUtility.cancelAllWithType(TransferType.DOWNLOAD);
            }
        });

        updateButtonAvailability();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DOWNLOAD_SELECTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Start downloading with the key they selected in the
                // DownloadSelectionActivity screen.
                String key = data.getStringExtra("key");                     // DownloadSelection 에서 받아온 key값을 extra로 받아온다.
//
//                Toast.makeText(getApplicationContext(),
//                        "key :  " + key , Toast.LENGTH_LONG).show();

                beginDownload(key);
            }
        }
    }

    /*
     * Begins to download the file specified by the key in the bucket.
     */
    private void beginDownload(String key) {
        // Location to download files from S3 to. You can choose any accessible
        // file.
        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/" + key);             // mobile device의 직접적으로 저장할 위치 받아옴

        Toast.makeText(getApplicationContext(),
                Environment.getExternalStorageDirectory().getPath() + "/SingerSongwriter/" + key, Toast.LENGTH_LONG).show();


        // Initiate the download

        TransferObserver observer = transferUtility.download(Constants.BUCKET_NAME, key, file);       // download 펑션 부르기
        /*
         * Note that usually we set the transfer listener after initializing the
         * transfer. However it isn't required in this sample app. The flow is
         * click upload button -> start an activity for image selection
         * startActivityForResult -> onActivityResult -> beginUpload -> onResume
         * -> set listeners to in progress transfers.
         */
        // observer.setTransferListener(new DownloadListener());

    }

    /*
     * Updates the ListView according to observers, by making transferRecordMap
     * reflect the current data in observers.
     */
    private void updateList() {
        TransferObserver observer = null;
        HashMap<String, Object> map = null;
        for (int i = 0; i < observers.size(); i++) {
            observer = observers.get(i);
            map = transferRecordMaps.get(i);
            Util.fillMap(map, observer, i == checkedIndex);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    /*
     * Enables or disables buttons according to checkedIndex.
     */
    private void updateButtonAvailability() {
        boolean availability = checkedIndex >= 0;
        btnPause.setEnabled(availability);
        btnResume.setEnabled(availability);
        btnCancel.setEnabled(availability);
        btnDelete.setEnabled(availability);
    }

    /*
     * A TransferListener class that can listen to a download task and be
     * notified when the status changes.
     */
    private class DownloadListener implements TransferListener {
        // Simply updates the list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "onError: " + id, e);
            updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            updateList();
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG, "onStateChanged: " + id + ", " + state);
            updateList();
        }
    }
}
