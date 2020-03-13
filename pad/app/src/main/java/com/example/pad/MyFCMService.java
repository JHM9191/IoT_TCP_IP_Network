package com.example.pad;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import msg.Msg;

import static com.example.pad.MainActivity.maps;

public class MyFCMService extends FirebaseMessagingService {

    String TAG = "===";

    String msg, title;

    NotificationManagerCompat notificationManager;


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {


        title = remoteMessage.getNotification().getTitle();
        msg = remoteMessage.getNotification().getBody();


        Log.d(TAG, "title : " + title + " msg : " + msg);

        HashMap<String, ObjectOutputStream> iotmaps = MainActivity.maps;

        Collection<ObjectOutputStream> cols = iotmaps.values();
        Iterator<ObjectOutputStream> its = cols.iterator();
        while (its.hasNext()) {
            try {
                its.next().writeObject(new Msg(title, msg, "iot"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String channelId = "channel";
        String channelName = "Channel_name";
        int importance = NotificationManager.IMPORTANCE_LOW;


        notificationManager = NotificationManagerCompat.from(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ioticon)
                .setContentTitle(title)
                .setContentText(msg)
                .setAutoCancel(true)
                .setVibrate(new long[]{1, 1000});

        notificationManager.notify(0, mBuilder.build());
    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        FirebaseMessaging.getInstance().subscribeToTopic("temperature");

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
                Toast.makeText(MyFCMService.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
