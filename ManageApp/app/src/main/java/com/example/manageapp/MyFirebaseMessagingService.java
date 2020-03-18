package com.example.manageapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    String TAG = "===";

    String msg, title;

    NotificationManagerCompat notificationManager;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();

//        Log.d(TAG, "title : " + title + " msg : " + msg);


        Intent intent = new Intent("filter_string");
        // put your all data using put extra
        intent.putExtra("control", title);
        intent.putExtra("data", msg);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);



//        String channelId = "channel";
//        String channelName = "Channel_name";
//        int importance = NotificationManager.IMPORTANCE_LOW;
//
//
//        notificationManager = NotificationManagerCompat.from(this);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
//            notificationManager.createNotificationChannel(mChannel);
//        }
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
//                .setSmallIcon(R.drawable.ioticon)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setAutoCancel(true)
//                .setVibrate(new long[]{1, 1000});
//
//        notificationManager.notify(0, mBuilder.build());
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("===", "getting new token fail!");
                    return;
                }

                String token = task.getResult().getToken();

                String msg = getString(R.string.msg_token_fmt, token);
                Log.d(TAG, msg);
                Toast.makeText(MyFirebaseMessagingService.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
