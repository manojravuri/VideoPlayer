package com.stonybrook.videoplayer.listeners;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


import com.opencsv.CSVWriter;
import com.stonybrook.videoplayer.MainActivity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AccelerometerListener implements SensorEventListener {

    String TAG="accelerometerLog";

    MainActivity mainActivity;

    public AccelerometerListener(MainActivity mainActivity)
    {
        this.mainActivity=mainActivity;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent)  {
        Log.d(TAG, "acceleration change: X:"+sensorEvent.values[0]+"  Y:"+sensorEvent.values[1]+"Z:  "+sensorEvent.values[2]);
        if(mainActivity.isHasStartedWriting())
        {
            String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
            String fileName = mainActivity.ACCELEROMETER_SENSOR_FILE_NAME;
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
            String[] data=new String[sensorValues.length];
            for(int i=0;i<sensorValues.length;i++)
            {
                data[i]=String.valueOf(sensorValues[i]);
            }
            writer.writeNext(data);


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
