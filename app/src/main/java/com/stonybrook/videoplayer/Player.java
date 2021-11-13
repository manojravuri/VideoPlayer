package com.stonybrook.videoplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.stonybrook.videoplayer.listeners.AccelerometerListener;
import com.stonybrook.videoplayer.listeners.GyroscopeListener;
import com.stonybrook.videoplayer.listeners.LightSensorListener;

import java.util.Locale;

public class Player extends AppCompatActivity {

    public static final String TAG = "TAG";
    ProgressBar spiiner;
    ImageView fullScreenOp;
    FrameLayout frameLayout;
    VideoView videoPlayer;

    private SensorManager sensorManager;

    private Sensor gyroscope;

    private Sensor accelerometer;

    private Sensor lightSensor;

    private AccelerometerListener accelerometerListener;

    private GyroscopeListener gyroscopeListener;

    private LightSensorListener lightSensorListener;

    public String LIGHT_SENSOR_FILE_NAME="light_sensor_data.csv";

    public String GYRO_SENSOR_FILE_NAME="gyro_sensor_data.csv";

    public String ACCELEROMETER_SENSOR_FILE_NAME="accelerometer_sensor_data.csv";

    public boolean isHasStartedWriting() {
        return hasStartedWriting;
    }

    public void setHasStartedWriting(boolean hasStartedWriting) {
        this.hasStartedWriting = hasStartedWriting;
    }

    private boolean hasStartedWriting=false;

    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(Player.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(Player.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        ) {
            /////Toast.makeText(MainActivity.this, "You have already granted this permission!",
            //      Toast.LENGTH_SHORT).show();
        } else {
            requestStoragePermission();
        }

        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        gyroscope=sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        lightSensor=sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        accelerometerListener=new AccelerometerListener(this);

        gyroscopeListener=new GyroscopeListener(this);

        lightSensorListener=new LightSensorListener(this);

        sensorManager.registerListener(accelerometerListener,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(gyroscopeListener,gyroscope,SensorManager.SENSOR_DELAY_NORMAL);

        sensorManager.registerListener(lightSensorListener,lightSensor,SensorManager.SENSOR_DELAY_NORMAL);


        setContentView(R.layout.activity_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spiiner = findViewById(R.id.progressBar);
        fullScreenOp = findViewById(R.id.fullScreenOp);
        frameLayout = findViewById(R.id.frameLayout);

        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");

        getSupportActionBar().setTitle(v.getTitle());

        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);
        videoPlayer = findViewById(R.id.videoView);

        title.setText(v.getTitle());
        desc.setText(v.getDescription());
        Uri videoUrl = Uri.parse(v.getVideoUrl());
        videoPlayer.setVideoURI(videoUrl);
        MediaController mc = new MediaController(this);
        //videoPlayer.setMediaController(mc);

        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                GYRO_SENSOR_FILE_NAME=v.getTitle().toLowerCase(Locale.ROOT).replace(" ","_")+"_gyroscope_"+v.getVideoId()+".csv";
                LIGHT_SENSOR_FILE_NAME=v.getTitle().toLowerCase(Locale.ROOT).replace(" ","_")+"_light_sensor_"+v.getVideoId()+".csv";
                ACCELEROMETER_SENSOR_FILE_NAME=v.getTitle().toLowerCase(Locale.ROOT).replace(" ","_")+"_accelerometer_"+v.getVideoId()+".csv";
                setHasStartedWriting(true);
                videoPlayer.start();
                spiiner.setVisibility(View.GONE);
            }
        });

        videoPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                setHasStartedWriting(false);
                //videoPlayer.start();
                spiiner.setVisibility(View.GONE);
            }
        });

        videoPlayer.setOnClickListener(new VideoView.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if(videoPlayer.isPlaying())
                {
                    setHasStartedWriting(false);
                    videoPlayer.pause();
                }
                else
                {
                    setHasStartedWriting(true);
                    videoPlayer.start();
                }
            }
        });


        fullScreenOp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked on play");
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().hide();
                fullScreenOp.setVisibility(View.GONE);
                frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
            }
        });


    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Player.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        fullScreenOp.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().show();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int heightValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,getResources().getDisplayMetrics());
        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightValue)));
        videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,heightValue)));
        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            super.onBackPressed();
        }
    }
}