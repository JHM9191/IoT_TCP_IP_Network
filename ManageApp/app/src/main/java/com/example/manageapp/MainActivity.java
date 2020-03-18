package com.example.manageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {


    String TAG = "===";
    public TextView tv_connection_state, tv_speed, tv_temperature, tv_gas;


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

//        savedInstanceState.putAll();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ActionBar 숨기기
        getSupportActionBar().hide();

        makeUI();
        FirebaseMessaging.getInstance().subscribeToTopic("temperature_manage").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "subscribe to temperature complete";
                if (!task.isSuccessful()) {
                    msg = "subscribe to temperature fail";
                }
                Log.d(TAG, msg);
            }
        });

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));

    }

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String control = intent.getStringExtra("control");
                String data = intent.getStringExtra("data");
                Log.d(TAG, "control : " + control + " data : " + data);
                if (control.equals("temperature")) {
                    tv_temperature.setText(data + " °C");
                } else if (control.equals("speed")) {
                    tv_speed.setText(data + " km/h");
                } else if (control.equals("gas")) {
                    tv_gas.setText(data + "");
                }
            }
        }
    };

    private void makeUI() {

        tv_connection_state = findViewById(R.id.tv_connection_state);
        tv_speed = findViewById(R.id.tv_speed);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_gas = findViewById(R.id.tv_gas);


    }

    public void btck(View v) {
        switch (v.getId()) {
            case R.id.btn_engine_on:
                Toast.makeText(this, "Engine ON", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_engine_off:
                Toast.makeText(this, "Engine OFF", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_speed_up:
                Toast.makeText(this, "Speed UP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_speed_down:
                Toast.makeText(this, "Speed DOWN", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_1:
                Toast.makeText(this, "Aircon Level 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_2:
                Toast.makeText(this, "Aircon Level 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_3:
                Toast.makeText(this, "Aircon Level 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_4:
                Toast.makeText(this, "Aircon Level 4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_5:
                Toast.makeText(this, "Aircon Level 5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_cold_off:
                Toast.makeText(this, "Aircon OFF", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_1:
                Toast.makeText(this, "Heater Level 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_2:
                Toast.makeText(this, "Heater Level 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_3:
                Toast.makeText(this, "Heater Level 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_4:
                Toast.makeText(this, "Heater Level 4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_5:
                Toast.makeText(this, "Heater Level 5", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_hot_off:
                Toast.makeText(this, "Heater OFF", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class SendWebServer extends Thread {

        //        String urlstr = "http://52.78.108.32:8080/webspringserver/webclient.top"; // AWS donghyun
//        String urlstr = "http://15.165.163.102:8080/webspringserver/webclient.top"; // AWS hyunmin
        String urlstr = "http://192.168.43.2:8080/webspringserver/webclient.top";


        public SendWebServer(String ip, String state) {
            urlstr += "?ip=" + ip + "&state=" + state;

        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlstr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.getInputStream();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


//    class GetTemperature extends Thread {
//        Intent intent = getIntent();
//
//
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tv_temperature.setText(intent.getStringExtra("temperature").toString());
//                }
//            });
//
//
//        }
//    }


}
