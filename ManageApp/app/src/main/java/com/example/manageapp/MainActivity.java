package com.example.manageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    String TAG = "===";

    EditText et_ip;
    RadioGroup radioGroup;
    RadioButton rb_start, rb_end;
    String ip;
    String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeUi();

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
    }

    private void makeUi() {
        et_ip = findViewById(R.id.et_ip);
        rb_start = findViewById(R.id.rb_start);
        rb_end = findViewById(R.id.rb_end);
        radioGroup = findViewById(R.id.rg);
    }


    public void ckbt(View view) {
        ip = et_ip.getText().toString();

        RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_start) {
                    state = "1";
                } else if (checkedId == R.id.rb_end) {
                    state = "0";
                }
            }
        };
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        Log.d(TAG, "state : " + state);

        new SendWebServer(ip, state).start();

    }

    class SendWebServer extends Thread {

//        String urlstr = "http://52.78.108.32:8080/webspringserver/webclient.top"; // AWS donghyun
        String urlstr = "http://15.165.163.102:8080/webspringserver/webclient.top"; // AWS hyunmin
//        String urlstr = "http://192.168.43.2:8080/webspringserver/webclient.top";


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
}
