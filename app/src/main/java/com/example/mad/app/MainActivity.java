package com.example.mad.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button btnStart, btnstop, btnreset,btn_resume;
    TextView txtTimer, firstlat, firstlong;
    Handler customHandler = new Handler();
    public  static MainActivity mainActivity;
    long startTime = 0L, timeInMilliseconds = 0L, timeSwapBuff = 0L, updateTime = 0L;


    public static final long MILLIS_TO_MINUTES = 60000;
    public static final long MILLS_TO_HOURS = 3600000;

    Runnable updateTimeThread = new Runnable() {
        @Override
        public void run() {

                long  since = (System.currentTimeMillis() - startTime);

                //convert the resulted time difference into hours, minutes, seconds and milliseconds
                int seconds = (int) (since / 1000) % 60;
                int minutes = (int) ((since / (MILLIS_TO_MINUTES)) % 60);
                int hours = (int) ((since / (MILLS_TO_HOURS))); //this does not reset to 0!
                int millis = (int) since % 1000; //the last 3 digits of millisecs
                txtTimer.setText(String.format("%02d:%02d:%02d:%02d",hours,minutes,seconds,millis));
                updateTime = timeSwapBuff + timeInMilliseconds;
                customHandler.postDelayed(this, 10L);


        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity=this;
        btnStart = (Button) findViewById(R.id.btn_start);
        btnstop = (Button) findViewById(R.id.btn_stop);
        btnreset = (Button) findViewById(R.id.btn_reset);
        txtTimer = (TextView) findViewById(R.id.disp_time);
        firstlat = (TextView) findViewById(R.id.first_lat);
        firstlong = (TextView) findViewById(R.id.first_long);
        btn_resume =(Button)findViewById(R.id.btn_pause);


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracker tracker = new Tracker(getApplicationContext());
                Location loc_start = tracker.localisation();

                startTime = System.currentTimeMillis();
                    customHandler.postDelayed(updateTimeThread, 10L);

                    if (loc_start != null) {
                        double endlat, endlong;
                        endlat = loc_start.getLatitude();
                        endlong = loc_start.getLongitude();
                        String latitude1 = Double.toString(endlat);
                        String longitude1 = Double.toString(endlong);
                        Toast.makeText(getApplicationContext(), "lasttlat: " + latitude1 + " lastlong : " + longitude1, Toast.LENGTH_LONG).show();

                        firstlat.setText(latitude1);
                        firstlong.setText(longitude1);

                    }

            }
        });

        btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCode();
                Tracker endpost = new Tracker(getApplicationContext());
                Location loc_stop = endpost.localisation();
                String time = txtTimer.getText().toString();
                int seconds = getsecondtime(time);
                Toast.makeText(MainActivity.this, "" + seconds + "", Toast.LENGTH_LONG).show();

                String lat1 = firstlat.getText().toString();
                String long1 = firstlong.getText().toString();

                Float latini = Float.parseFloat(lat1);
                Float longini = Float.parseFloat(long1);
                double endlat, endlong;

                if (loc_stop != null) {
                    endlat = loc_stop.getLatitude();
                    endlong = loc_stop.getLongitude();
                    // String latitude2 = Double.toString(endlat);
                    //String longitude2 = Double.toString(endlong);
                    //Toast.makeText(getApplicationContext(), "lasttlat: " + latitude2 + " lastlong : " + longitude2, Toast.LENGTH_LONG).show();
                    float results[]= new float[6];
                    //computing the ditance between the two gps points
                    Location.distanceBetween(latini,longini,endlat,endlong,results);
                    float distance = results[0];
                    // computing the speed,perfomance

                    float vitesse = (distance/seconds);
                   // Toast.makeText(getApplicationContext(), "distance: "+distance, Toast.LENGTH_LONG).show();
                   // Toast.makeText(getApplicationContext(), "Performance: "+vitesse, Toast.LENGTH_LONG).show();

                    //this section inserts the data in the database in backgroundtask

                    String dis = Float.toString(distance);
                    String sec = Integer.toString(seconds);
                    String vitess = Float.toString(vitesse);

                    BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext()) ;
                    backgroundTask.execute(new String[]{"insert",dis,sec,vitess,"get"});

                }
                else {
                    Toast.makeText(getApplicationContext(), "No last location", Toast.LENGTH_LONG).show();
                }


            }
        });

        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runCode();
                txtTimer.setText(String.format("%02d:%02d:%02d:%02d",0,0,0,0));
            }
        });


        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   runCode();
            }
        });

    }




  //resume the time
    public void runCode(){
        timeSwapBuff += timeInMilliseconds;
        customHandler.removeCallbacks(updateTimeThread);
    }

    //method for converting the time in seconds
    public static int getsecondtime(String value) {

        String[] parts = value.split(":");

        if (parts.length < 2 || parts.length > 4)
            return 0;

        int millis =0 ,seconds = 0, minutes = 0, hours = 0;
        if(parts.length==2){
            millis = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[0]);
        }

        if (parts.length == 3) {
            millis = Integer.parseInt(parts[2]);
            seconds = Integer.parseInt(parts[1]);
            minutes = Integer.parseInt(parts[0]);
        } else if (parts.length == 4) {
            millis = Integer.parseInt(parts[3]);
            seconds = Integer.parseInt(parts[2]);
            minutes = Integer.parseInt(parts[1]);
            hours = Integer.parseInt(parts[0]);
        }

        return seconds + (minutes * 60) + (hours * 3600)+(millis/1000);
    }

}
