package com.stonybrook.videoplayer.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


import com.opencsv.CSVWriter;
import com.stonybrook.videoplayer.MainActivity;
import com.stonybrook.videoplayer.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LightSensorListener implements SensorEventListener {

    String TAG="lightsensorLog";

    Player mainActivity;

    public LightSensorListener(Player mainActivity)
    {
        this.mainActivity=mainActivity;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)  {
        Log.d(TAG, "light sensor change:"+sensorEvent.values[0]);
        if(mainActivity.isHasStartedWriting())
        {
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = mainActivity.LIGHT_SENSOR_FILE_NAME;
            String filePath = baseDir + File.separator + fileName;
            File f = new File(filePath);
            CSVWriter writer;
            FileWriter mFileWriter;
            try{
                // File exist
                if(f.exists()&&!f.isDirectory())
                {
                    mFileWriter = new FileWriter(filePath, true);
                    writer = new CSVWriter(mFileWriter);
                }
                else
                {
                    writer = new CSVWriter(new FileWriter(filePath));
                }

                float[] sensorValues = (sensorEvent.values);
                String[] data=new String[sensorValues.length+1];
                SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
                jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
                data[0] = jdf.format(new Date(System.currentTimeMillis()));
                for(int i=0;i<sensorValues.length;i++)
                {
                    data[i+1]=String.valueOf(sensorValues[i]);
                }
                writer.writeNext(data);
                Log.d(TAG,"Writing data to"+mainActivity.LIGHT_SENSOR_FILE_NAME);

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
