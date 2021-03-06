package com.chamojaya.androidaccelerometersensor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //To get the log cat statement
    private static final String TAG="MainActivity";

    //Define a sensor manager
    private SensorManager sensorManager;

    //Define sensor
    Sensor accelerometer;

    //Define three arraylist to store the values
    ArrayList currentAccelXlist = new ArrayList<String>();
    ArrayList currentTimelist = new ArrayList<String>();
    ArrayList currentVelocitylist = new ArrayList<String>();

    //Define variables
    float currentAccelX,prevTimeStamp,currentTimeStamp,interval,currentVelocityX;

    //Convert the time interval in to nano second value
    float Ns2S =1.0f / 1000000.0f;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get a logcat message
        Log.d(TAG,"onCreate: Initializing Sensor Services");
        //Get the sensor manager servicers
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //Get a sensor to accelerometer
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //Register the listners
        sensorManager.registerListener(MainActivity.this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        Log.d(TAG,"onCreate: Register accelerometer Listener");

//        addFragment();

    }

//    private void addFragment(){
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        TestFragment fragment=TestFragment.newInstance("","");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//    }




    public void onStartClick(View view) {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStopClick(View view) {
        sensorManager.unregisterListener(this);
    }
    protected void onResume() {
        super.onResume();
//        String fileName ="myfile.txt";
//        String fileContent = idEditor.getText().toString()+","+nameEditor.getText().toString();
//        try {
//            writer = new FileWriter("myfile.txt",true);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    protected void onPause() {
        super.onPause();

//        if(writer != null) {
//            try {
//                writer.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }


//    public void readFile() {
//        try {
//            FileInputStream fileInputStream = openFileInput("Tutorial File.txt");
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            StringBuffer stringBuffer = new StringBuffer();
//
//            String lines;
//            while ((lines = bufferedReader.readLine()) != null) {
//                stringBuffer.append(lines + "\n");
//            }
//
////            displayText.setText(stringBuffer.toString());
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //intialize the acceleration value
         currentAccelX = sensorEvent.values[0];
         //intialize the the time
         currentTimeStamp = sensorEvent.timestamp;
         if(prevTimeStamp==0){
             prevTimeStamp = currentTimeStamp;
         }
        //Calculate the time interval
        interval = (currentTimeStamp - prevTimeStamp ) * Ns2S;
        prevTimeStamp =currentTimeStamp;
        //Get a thresholder to get values less than 0.3 as zero
        if(Math.abs(currentAccelX)< 0.3) currentAccelX =0 ;
        //Calculate the velocity
       currentVelocityX += currentAccelX * interval;
         //Get the accelerometer value X
        Log.d(TAG,"onSensorChanged: X"+ currentVelocityX+ "Time Stamps "+ interval+ " Current Velocity" +currentVelocityX);

        //Create a list with a adapter
        currentAccelXlist.add(currentAccelX);
        ArrayAdapter adapter = new ArrayAdapter<String> (this,R.layout.activity_listview,currentAccelXlist);
        ListView lv =(ListView) findViewById(R.id.xValue);
        lv.setAdapter(adapter);

        for(int i=0; i<=currentAccelXlist.size();i++){

//                    float z = (float) currentAccelXlist.get(i + 1);
//                    float diff = (float) currentAccelXlist.get(i + 1) - currentAccelX;
                    if (currentAccelX == 0) {
//                        return currentAccelXlist.get(i+1);
                        writeFile(currentAccelXlist.get(i+1),currentTimelist);
                        Log.d("hi" + currentAccelX, "djoa");
                        return;
                    }

        }
                //if the UI show the values more than 15 remove the the first value from the table and update it
        if(currentAccelXlist.size()>15) {
                    currentAccelXlist.remove(0);
                    adapter.notifyDataSetChanged();
        }



        currentTimelist.add(interval);
        ArrayAdapter adapter2 = new ArrayAdapter<String> (this,R.layout.activity_listview,currentTimelist);
        ListView lv2 =(ListView) findViewById(R.id.timeValue);
        lv2.setAdapter(adapter2);
        if(currentTimelist.size()>15) {
            currentTimelist.remove(0);
            adapter.notifyDataSetChanged();
        }

        currentVelocitylist.add(currentVelocityX);
        ArrayAdapter adapter3 = new ArrayAdapter<String> (this,R.layout.activity_listview,currentVelocitylist);
        ListView lv3 =(ListView) findViewById(R.id.velocityValue);
        lv3.setAdapter(adapter3);
        if(currentVelocitylist.size()>15) {
            currentVelocitylist.remove(0);
             adapter.notifyDataSetChanged();
        }


    }
//Write the File
    private void writeFile(Object o, ArrayList currentTimelist) {
//       final String fileContent = new String("Acceleration " +o
//              + ";Time Interval" + interval + ";");
       try {
        FileOutputStream fileOutputStream = openFileOutput("File.txt", MODE_PRIVATE);
//       fileOutputStream.write(fileContent.getBytes());
        fileOutputStream.close();
       Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, TargetActivity.class);
        startActivity(intent);

   }

    //



}
