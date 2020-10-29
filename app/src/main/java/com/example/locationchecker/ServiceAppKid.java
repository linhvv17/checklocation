package com.example.locationchecker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.locationchecker.model.Kid;
import com.example.locationchecker.model.model.MapDTO;
import com.example.locationchecker.utilities.Notification;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServiceAppKid extends android.app.Service {

//    private String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
//            Settings.Secure.ANDROID_ID);
    protected static final int CHANNEL_ID = 1337;
    protected static final int NOTIFICATION_ID = 1337;
    private static String TAG = "Service";
    private static ServiceAppKid mCurrentServiceAppKid;
    private int counter = 0;

    protected LocationManager locationManager;
    private String sang;

    public ServiceAppKid() {
        super();
    }

    private FusedLocationProviderClient client;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;

    DatabaseReference noti = database.getReference().child("Notification");

    DatabaseReference check ;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentServiceAppKid = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;
        sang = intent.getStringExtra("kidto");
        Log.d("SANG",sang);
        ref = database.getReference().child("KidMaps").child(sang);


        // it has been killed by Android and now it is restarted. We must make sure to have reinitialised everything
        if (intent == null) {
            ProcessMainClass bck = new ProcessMainClass();
            bck.launchService(this);
        }

        // make sure you call the startForeground on onStartCommand because otherwise
        // when we hide the notification on onScreen it will nto restart in Android 6 and 7
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            restartForeground();
        }

        startTimer();

        // return start sticky so if it is killed by android, it will be restarted with Intent null
        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void restartForeground() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //check permission

            try {
                Notification notification = new Notification();
                startForeground(NOTIFICATION_ID, notification.setNotification(this, "Kids Tracking", "Kids Tracking đang chạy!", R.drawable.ic_sleep));
                Log.i(TAG, "restarting foreground successful");
                getLocations();
                startTimer();
            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }

    private void getLocations() {
        Log.i(TAG, "getLocations");

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
        stoptimertask();
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved called");
        // restart the never ending service
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);

    }

    private static Timer timer;
    private static TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {

        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        client = LocationServices.getFusedLocationProviderClient(ServiceAppKid.getmCurrentServiceAppKid());
                        if (ActivityCompat.checkSelfPermission(ServiceAppKid.getmCurrentServiceAppKid(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ServiceAppKid.getmCurrentServiceAppKid(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onSuccess(Object o) {
                                if (o != null) {
//                                    Log.i("Starting Location!", );
                                    Location location = (Location) o;
                                    Date currentTime = Calendar.getInstance().getTime();
                                    ref.child(String.valueOf(currentTime)).setValue(new MapDTO(location.getLatitude(), location.getLongitude()));
                                    Log.i("Starting Location getLatitude!", String.valueOf(location.getLatitude()));
                                    Log.i("Starting Location getLongitude!", String.valueOf(location.getLongitude()));
                                }
                            }
                        });
                    }
                }, 0, 60, TimeUnit.SECONDS);



        Log.i(TAG, "Starting timer");

        //set a new Timer - if one is already running, cancel it to avoid two running at the same time
        stoptimertask();
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        Log.i(TAG, "Scheduling...");
        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 0, 1000); //
    }

    public void initializeTimerTask() {
        Log.i(TAG, "initialising TimerTask");
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
            }
        };
    }
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public static ServiceAppKid getmCurrentServiceAppKid() {
        return mCurrentServiceAppKid;
    }

    public static void setmCurrentServiceAppKid(ServiceAppKid mCurrentServiceAppKid) {
        ServiceAppKid.mCurrentServiceAppKid = mCurrentServiceAppKid;
    }

}
