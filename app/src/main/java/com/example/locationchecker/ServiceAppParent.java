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

public class ServiceAppParent extends android.app.Service {

    protected static final int CHANNEL_ID = 1337;
    protected static final int NOTIFICATION_ID = 1337;
    private static String TAG = "Service";
    private static ServiceAppParent mCurrentService;
    private int counter = 0;

    protected LocationManager locationManager;
    private String sang;

    public ServiceAppParent() {
        super();
    }

    private FusedLocationProviderClient client;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;


    DatabaseReference check ;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            restartForeground();
        }
        mCurrentService = this;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "restarting Service !!");
        counter = 0;

        sang = intent.getStringExtra("to");
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

        //receive notification
        check = database.getReference().child("Kids").child(sang);
        check.addValueEventListener(new ValueEventListener() {
            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sos);
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Get Post object and use the values to update the UI
                Kid kid = snapshot.getValue(Kid.class);
                // ...
                if (kid!= null){
                    Boolean  check = Boolean.valueOf(kid.getSos());
                    Log.d("check", String.valueOf(check));
                    if (check.equals(true)){
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sos);
                        mediaPlayer.start();
                    } else {
                        if (mediaPlayer.isPlaying()){
                            mediaPlayer.pause();
                        } else {
                            return;
                        }
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            } catch (Exception e) {
                Log.e(TAG, "Error in notification " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy called");
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i(TAG, "onTaskRemoved called");
        Intent broadcastIntent = new Intent(Globals.RESTART_INTENT);
        sendBroadcast(broadcastIntent);
    }

}
