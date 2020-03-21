package com.example.manageapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

//enum Controls {
//    ENGINE, SPEED, TEMPERATURE, GAS
//}

public class MainActivity extends AppCompatActivity {


    String TAG = "===";
    int num = 0;

    String carId = "car1";
    String control;
    String data;
    ArrayList<String> carIds;

    public TextView tv_name_engine, tv_name_speed, tv_name_temp, tv_name_gas;
    public TextView tv_connection_state, tv_speed, tv_temperature, tv_gas;
    public ProgressBar progressBar, pb_engine, pb_speed, pb_temperature, pb_gas;
    public ImageView iv_engine, iv_speed, iv_temp, iv_gas;
    public Spinner spinner_cars;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("carId", carId);
        outState.putString("control", control);
        outState.putString("data", data);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ActionBar 숨기기
        getSupportActionBar().hide();

//        if (savedInstanceState != null) {
//            control = savedInstanceState.getString("control");
//            data = savedInstanceState.getString("data");
//            connectionCheck(carId, control, data);
//        }

        makeUI();

        carIds = new ArrayList<>();
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

        new ConnectServer().start();

        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this);
        lbm.registerReceiver(receiver, new IntentFilter("filter_string"));
    }


    // Connect to server in the beginning.
    class ConnectServer extends Thread {
        //        String urlstr = "http://192.168.43.2:8080/webspringserver/checkServerConnection.top?managerId=JHM";
        String urlstr = "http://70.12.231.236:8080/webspringserver/checkServerConnection.top?managerId=JHM";

        public ConnectServer() {
        }

        @Override
        public void run() {

            try {
                URL url = new URL(urlstr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                con.getInputStream();
                Looper.prepare();
                tv_connection_state.setText("서버 접속 성공");
                tv_connection_state.setTextColor(Color.WHITE);
                progressBar.setVisibility(View.GONE);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tv_connection_state.setText("서버와 접속 시도중 " + ++num);

//                        Toast.makeText(MainActivity.this, "서버와 접속이 끊어졌습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        new ConnectServer().start();
                    }
                });
            }
        }
    }


    // Displays whether all parts are logged in to server.
    public void connectionCheck(String carId, String control, String data) {
        Log.d(TAG, control + " | " + data);
        if (data.equals("77777")) { // 77777 : login
            if (control.equals("engine")) {
                iv_engine.setVisibility(View.VISIBLE);
                pb_engine.setVisibility(View.GONE);
            } else if (control.equals("speed")) {
                iv_speed.setVisibility(View.VISIBLE);
                pb_speed.setVisibility(View.GONE);
            } else if (control.equals("temperature")) {
                iv_temp.setVisibility(View.VISIBLE);
                pb_temperature.setVisibility(View.GONE);
            } else if (control.equals("gas")) {
                iv_gas.setVisibility(View.VISIBLE);
                pb_gas.setVisibility(View.GONE);
            }
            Toast.makeText(getApplicationContext(), control + " is connected", Toast.LENGTH_SHORT).show();
        } else if (data.equals("88888")) { // 88888 : logout
            if (control.equals("engine")) {
                iv_engine.setVisibility(View.GONE);
                pb_engine.setVisibility(View.VISIBLE);
            } else if (control.equals("speed")) {
                iv_speed.setVisibility(View.GONE);
                pb_speed.setVisibility(View.VISIBLE);
                tv_speed.setText("0 Km/h");
            } else if (control.equals("temperature")) {
                iv_temp.setVisibility(View.GONE);
                pb_temperature.setVisibility(View.VISIBLE);
            } else if (control.equals("gas")) {
                iv_gas.setVisibility(View.GONE);
                pb_gas.setVisibility(View.VISIBLE);
            }
            Toast.makeText(getApplicationContext(), control + " is disconnected", Toast.LENGTH_SHORT).show();
        }
    }

    // Displays whether all parts are logged in to server.
    public void displayData(String carId, String control, final String data) {
        if (control.equals("engine")) {
            if (data.equals("1")) {
                Toast.makeText(this, "Engine START", Toast.LENGTH_SHORT).show();
            } else if (data.equals("0")) {
                Toast.makeText(this, "Engine STOP", Toast.LENGTH_SHORT).show();
            }
        } else if (control.equals("speed") | carId.equals("speed")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_speed.setText(data + " Km/h");
                }
            });
        } else if (control.equals("temperature") | carId.equals("temperature")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_temperature.setText(data + " °C");
                }
            });
        } else if (control.equals("gas")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_gas.setText(data + " ppm");
                }
            });
        }
    }

    // Receives all FCM notifications from webspringserver. (carId, control, data)
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                carId = intent.getStringExtra("carId");
                control = intent.getStringExtra("control");
                data = intent.getStringExtra("data");
                Log.d(TAG, "carId : " + carId + " control : " + control + " data : " + data);

                if (control.equals("login") && data.equals("1")) {
                    new SendWebServer("car1", "1", null);
                } else if (data.equals("77777") | data.equals("88888")) {
                    connectionCheck(carId, control, data);
                } else {
                    Log.d(TAG, "data received. displaying data..");
                    displayData(carId, control, data);
                }


            }
        }
    };

    // Creates UI.
    private void makeUI() {

        tv_name_engine = findViewById(R.id.tv_name_engine);
        tv_name_speed = findViewById(R.id.tv_name_speed);
        tv_name_temp = findViewById(R.id.tv_name_temp);
        tv_name_gas = findViewById(R.id.tv_name_gas);

        tv_connection_state = findViewById(R.id.tv_connection_state);
        tv_speed = findViewById(R.id.tv_speed);
        tv_temperature = findViewById(R.id.tv_temperature);
        tv_gas = findViewById(R.id.tv_gas);

        progressBar = findViewById(R.id.progressBar);
        pb_engine = findViewById(R.id.pb_engine);
        pb_speed = findViewById(R.id.pb_speed);
        pb_temperature = findViewById(R.id.pb_temp);
        pb_gas = findViewById(R.id.pb_gas);


        iv_engine = findViewById(R.id.iv_engine);
        iv_speed = findViewById(R.id.iv_speed);
        iv_temp = findViewById(R.id.iv_temp);
        iv_gas = findViewById(R.id.iv_gas);

        spinner_cars = findViewById(R.id.spinner_cars);

    }


    // These are actions when buttons are clicked.
    public void btck(View v) {
        switch (v.getId()) {
            case R.id.btn_engine_on:
                Toast.makeText(this, "Engine ON Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "1", "engine").start();
                break;
            case R.id.btn_engine_off:
                Toast.makeText(this, "Engine OFF Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "0", "engine").start();
                break;
            case R.id.btn_speed_up:
                Toast.makeText(this, "Speed UP Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "1", "speed").start();
                break;
            case R.id.btn_speed_down:
                Toast.makeText(this, "Speed DOWN Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "0", "speed").start();
                break;
            case R.id.btn_cold_1:
                Toast.makeText(this, "Aircon Level 1 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "1", "cold").start();
                break;
            case R.id.btn_cold_2:
                Toast.makeText(this, "Aircon Level 2 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "2", "cold").start();
                break;
            case R.id.btn_cold_3:
                Toast.makeText(this, "Aircon Level 3 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "3", "cold").start();
                break;
            case R.id.btn_cold_4:
                Toast.makeText(this, "Aircon Level 4 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "4", "cold").start();
                break;
            case R.id.btn_cold_5:
                Toast.makeText(this, "Aircon Level 5 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "5", "cold").start();
                break;
            case R.id.btn_cold_off:
                Toast.makeText(this, "Aircon OFF Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "0", "temperature").start();
                break;
            case R.id.btn_hot_1:
                Toast.makeText(this, "Heater Level 1 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "1", "hot").start();
                break;
            case R.id.btn_hot_2:
                Toast.makeText(this, "Heater Level 2 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "2", "hot").start();
                break;
            case R.id.btn_hot_3:
                Toast.makeText(this, "Heater Level 3 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "3", "hot").start();
                break;
            case R.id.btn_hot_4:
                Toast.makeText(this, "Heater Level 4 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "4", "hot").start();
                break;
            case R.id.btn_hot_5:
                Toast.makeText(this, "Heater Level 5 Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "5", "hot").start();
                break;
            case R.id.btn_hot_off:
                Toast.makeText(this, "Heater OFF Clicked", Toast.LENGTH_SHORT).show();
                new SendWebServer("car1", "0", "temperature").start();
                break;
            case R.id.bt_deviceCheck:
                new CheckCarState().start();
        }
    }


    // Checks from webspringserver whether all or specific part is logged in or not.
    class CheckCarState extends Thread {

        String urlstr = "http://70.12.231.236:8080/webspringserver/checkCarState.top?control=";
//        String urlstr = "http://192.168.43.2:8080/webspringserver/checkCarState.top?control=";

        String[] controls = null;

        // Checks all parts
        public CheckCarState() {
            this.controls = new String[]{
                    "engine",
                    "speed",
                    "temperature",
                    "gas"
            };
        }

        // Checks specific part
        public CheckCarState(String control) {
            this.controls = new String[]{control};
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < controls.length; i++) {
                    String send = urlstr + controls[i] + "&data=&carId=" + carId;
                    Log.d(TAG, send);
                    URL url = new URL(send);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.getInputStream();
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "서버와 접속이 끊어졌습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    // Sends control message to springwebserver
    class SendWebServer extends Thread {
        //        String urlstr = "http://52.78.108.32:8080/webspringserver/webclient.top"; // AWS donghyun
//        String urlstr = "http://15.165.163.102:8080/webspringserver/webclient.top"; // AWS hyunmin
//        String urlstr = "http://192.168.43.2:8080/webspringserver/webclient.top";
        String urlstr = "http://70.12.231.236:8080/webspringserver/webclient.top";


        public SendWebServer(String carId, String message, String control) {
            urlstr += "?carId=" + carId + "&message=" + message + "&control=" + control;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(urlstr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.getInputStream();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "서버와 접속이 끊어졌습니다.\n잠시 후 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}
