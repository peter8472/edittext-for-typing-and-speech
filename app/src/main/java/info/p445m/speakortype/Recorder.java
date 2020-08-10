package info.p445m.speakortype;
/*
code from
https://developer.android.com/guide/topics/media/mediarecorder
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

public class Recorder extends AppCompatActivity {

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String mFileName = null;

    private MediaRecorder mRecorder = null;

    private MediaPlayer   mPlayer = null;
    

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};
    private boolean mStartRecording=true;
    private Button snackbardummy;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }
    private void snack(CharSequence message) {
        Snackbar.make(snackbardummy, message, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();


    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    @SuppressLint("SetTextI18n")
    private void startRecording() {

        mRecorder = new MediaRecorder();

        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            snack("prepared!");
            
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            TextView t= findViewById(R.id.outputr);
            t.setText("preparte fail, maybe start called in an invalid state: 4");
            return;
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }



    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Record to the external cache directory for visibility
        
        File mycaxhdir = getExternalCacheDir();

        // on kindle getExternalCacheDir always
        // returns /storage/sdcard1, even when not available
        // don't know what huawei does
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
            mycaxhdir= getCacheDir();
        } 
        mFileName = mycaxhdir.getAbsolutePath();
        mFileName += "/audiorecordtest.3gp";
        Log.v(LOG_TAG, String.format("output file set to %s", mFileName));

        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);


        setContentView(R.layout.activity_recorder);
        Button record = findViewById(R.id.record_button);
        snackbardummy=record;
        record.setOnClickListener(
                new View.OnClickListener() {
                    Button record = findViewById(R.id.record_button);
                    @Override
                    public void onClick(View view) {
                        onRecord(mStartRecording);
                        if (mStartRecording) {
                            record.setText("Stop recording");
                        } else {
                            record.setText("Start recording");
                        }
                        mStartRecording = !mStartRecording;

                    }
                }


        );


        
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}