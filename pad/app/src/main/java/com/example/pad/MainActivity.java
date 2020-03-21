package com.example.pad;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.suke.widget.SwitchButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import msg.Msg;

public class MainActivity extends AppCompatActivity {

    // Common
    String TAG = "===";

    // UI
//    TextView tvclient;
//    TextView tvserver;
//    TextView tv;

    ListView listView;
    TextView name1, name2, name3, name4,
            data1, data2, data3, data4,
            status;


    ArrayAdapter<String> adapter;


    TextView tv_server_state, tv_time;
    TextView tv_engine, tv_speed, tv_temp, tv_gas;
    com.suke.widget.SwitchButton switchButton;


    // Network
    ServerSocket serverSocket;
    int port = 9999;
    ServerReadyThread serverReadyThread;
    public static HashMap<String, ObjectOutputStream> maps;
    public static HashMap<String, String> ids;
    public static HashMap<String, String> cars;

    // ServerReadyThread variables
    boolean aflag = true;
    boolean isEngineOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar 숨기기
        getSupportActionBar().hide();

        maps = new HashMap<>();
        ids = new HashMap<>();
        cars = new HashMap<>();

        makeUi();


        serverReadyThread = new ServerReadyThread();
        serverReadyThread.start();

        FirebaseMessaging.getInstance().subscribeToTopic("temperature").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "subscribe to temperature complete";
                if (!task.isSuccessful()) {
                    msg = "subscribe to temperature fail";
                }
                Log.d(TAG, msg);
            }
        });

        new CurrentTimeThread().start();
        new WelcomeNoteThread().start();

    }

    class CurrentTimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!Thread.interrupted())
                try {
                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() // start actions in UI thread
                    {

                        @Override
                        public void run() {
                            tv_time.setText(getCurrentTime());
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    // 현재 시간을 반환
    public String getCurrentTime() {
        long time = System.currentTimeMillis();

        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        String str = dayTime.format(new Date(time));

        return str;
    }

    class WelcomeNoteThread extends Thread {

        String[] welcomeNotes = {
                "Welcome!",
                "You are car1",
                "Enjoy!!!"
        };

        @Override
        public void run() {
            super.run();
            int i = 0;
            while (!Thread.interrupted()) {
                if (i == 3) {
                    return;
                }
                try {
                    Thread.sleep(1000);

                    final int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText(welcomeNotes[finalI]);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ++i;

            }
        }
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeUi() {
        listView = findViewById(R.id.listView);
        name1 = findViewById(R.id.name1);
        name2 = findViewById(R.id.name2);
        name3 = findViewById(R.id.name3);
        name4 = findViewById(R.id.name4);
        data1 = findViewById(R.id.data1);
        data2 = findViewById(R.id.data2);
        data3 = findViewById(R.id.data3);
        data4 = findViewById(R.id.data4);
        status = findViewById(R.id.status);
//        tvclient = findViewById(R.id.tvclient);
//        tvserver = findViewById(R.id.tvserver);
//        tv = findViewById(R.id.tv);

        tv_server_state = findViewById(R.id.tv_server_state);
        tv_time = findViewById(R.id.tv_time);


        tv_engine = findViewById(R.id.tv_engine_state);

        tv_speed = findViewById(R.id.tv_speed);
        tv_temp = findViewById(R.id.tv_temp);
        tv_gas = findViewById(R.id.tv_gas);
        switchButton = findViewById(R.id.switch_button);

        switchButton.setChecked(false);
        switchButton.toggle();     //switch state
        switchButton.toggle(true);//switch without animation
        switchButton.setShadowEffect(true);//disable shadow effect
        switchButton.setEnabled(false);//disable button
        switchButton.setEnableEffect(false);//disable the switch animation
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        new ConnectThread(sip, sport, "pad").start();
    }

    class ServerReadyThread extends Thread {

        public ServerReadyThread() {

            try {
                serverSocket = new ServerSocket(port);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            while (aflag) {
                Socket socket = null;
                Log.d(TAG, "Server Ready");

                try {
                    socket = serverSocket.accept();
                    Log.d(TAG, "socket = serverSocket.accept()");
                    new ReceiverThread(socket).start();
                    Log.d(TAG, "new ReceiverThread(socket).start()");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setList();
                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setList();
                        }
                    });
                }
            }
        }
    }

    class SendServerLoginStatus extends Thread {
        //        String urlstr = "http://192.168.43.2:8080/webspringserver/iotclientloginstatus.top"; // Hotspot hyunmin
        String urlstr = "http://70.12.231.236:8080/webspringserver/iotclientloginstatus.top"; // Hotspot hyunmin
//        String urlstr = "http://52.78.108.32:8888/webspringserver/iotclient.top"; // AWS donghyun
//        String urlstr = "http://15.165.163.102:8080/webspringserver/iotclient.top"; // AWS hyunmin

        public SendServerLoginStatus(String ip, String carId) {
            urlstr += "?ip=" + ip + "&carId=" + carId + "&id=";
        }

        public SendServerLoginStatus(String ip, String carId, String id) {
            urlstr += "?ip=" + ip + "&carId=" + carId + "&id=" + id;
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

    class SendServer extends Thread {

        String urlstr = "http://70.12.231.236:8080/webspringserver/iotclient.top"; // Hotspot hyunmin
//        String urlstr = "http://192.168.43.2:8080/webspringserver/iotclient.top"; // Hotspot hyunmin
//        String urlstr = "http://52.78.108.32:8888/webspringserver/iotclient.top"; // AWS donghyun
//        String urlstr = "http://15.165.163.102:8080/webspringserver/iotclient.top"; // AWS hyunmin

        public SendServer(String id, String txt, String carId) {
            urlstr += "?id=" + id + "&txt=" + txt + "&carId=" + carId;
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

    public void setList() {
        adapter = new ArrayAdapter<String>(
                MainActivity.this,
                android.R.layout.simple_list_item_1,
                getIds()
        );
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        String status = "";
        if (getIds().size() != 0) {
            if (getIds().get(getIds().size() - 1).equals("engine")) {
                switchButton.setEnabled(true);
                switchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        if (isChecked) {
                            isEngineOn = true;
                            Log.d(TAG, "isChecked == true | isEngineOn = true");
                            sendMsg(new Msg("car1", "1", "engine"));
                            new SendServer("car1", "1", "engine").start();
                        } else {
                            isEngineOn = false;
                            Log.d(TAG, "isChecked == false | isEngineOn = false");
                            sendMsg(new Msg("car1", "0", "engine"));
                            new SendServer("car1", "0", "engine").start();
                        }
                    }
                });
                status = "엔진 상태 측정 장비가 로그인 되었습니다.";
            } else if (getIds().get(getIds().size() - 1).equals("speed")) {
                status = "속도 측정 장비가 로그인 되었습니다.";
            } else if (getIds().get(getIds().size() - 1).equals("temperature")) {
                status = "온도 측정 장비가 로그인 되었습니다.";
            } else if (getIds().get(getIds().size() - 1).equals("gas")) {
                status = "배기 가스량 측정 장비가 로그인 되었습니다.";
            }
            final String finalStatus = status;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_engine.setText(finalStatus);
                }
            });
        }
        if (getIds().size() == 4) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_engine.setText("모든 장비가 준비 되었습니다.");
                }
            });
        }

    }

    class ReceiverThread extends Thread {

        InputStream is;
        ObjectInputStream ois;

        OutputStream os;
        ObjectOutputStream oos;

        Socket socket;


        public ReceiverThread(Socket socket) {
            this.socket = socket;
            try {
                this.is = socket.getInputStream();
                this.ois = new ObjectInputStream(is);
                this.os = socket.getOutputStream();
                this.oos = new ObjectOutputStream(os);
            } catch (IOException e) {
                e.printStackTrace();
            }

            maps.put(socket.getInetAddress().toString(), oos);
            Log.d(TAG, "Client IP : " + socket.getInetAddress().toString());
            try {
                Msg msg = (Msg) ois.readObject();
                Log.d(TAG, "msg.getId() : " + msg.getId());

                ids.put(socket.getInetAddress().toString(), msg.getId());
                cars.put(socket.getInetAddress().toString(), msg.getTid());

                Log.d(TAG, "Client ID : " + msg.getId());
                new SendServerLoginStatus(socket.getInetAddress().toString(), msg.getTid(), msg.getId()).start();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {
            while (ois != null) {
                Msg msg = null;
                try {
                    msg = (Msg) ois.readObject();

                    Log.d(TAG, "ReceiverThread run() ois!=null");
                    displayData(msg);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
//                    e.printStackTrace();
                    Log.d(TAG, "client disconnected");
                    String carId = cars.get(socket.getInetAddress().toString());
                    new SendServerLoginStatus(socket.getInetAddress().toString(), carId).start();
                    maps.remove(socket.getInetAddress().toString());
                    ids.remove(socket.getInetAddress().toString());
                    cars.remove(socket.getInetAddress().toString());
                    displayData(new Msg("pad", null, null));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setList();
                        }
                    });
                    if (ois != null) {
                        try {
                            ois.close();
                            return;
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                }
            }
        }
    }

    // displays data after checking engine's state whether it is on or off.
    private void displayData(final Msg msg) {
        final String id = msg.getId();
        final String txt = msg.getTxt();
        final String tid = msg.getTid();

        Log.d(TAG, "displayData()\n" + msg.getId() + " | " + msg.getTxt() + " | " + msg.getTid());
        switch (id) {
            case "engine":
                if (txt.equals("1")) {
                    switchButton.setChecked(true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText("엔진 장비가 준비되었습니다.");
                            isEngineOn = true;
                        }
                    });
                } else if (txt.equals("0")) {
                    switchButton.setChecked(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText("엔진 장비를 종료합니다.");
                            tv_speed.setText("0 Km/h");
                            tv_temp.setText("0 °C");
                            tv_gas.setText("0 ppm");
                            isEngineOn = false;

                        }
                    });
                    // 장비들에게 매니져 혹은 패드(informatics)로부터 engine 을 껏다는 메세지 보내기.
//                    sendMsg(new Msg("engine", "0", "car1"));
                    sendMsg(new Msg("car1", "0", "engine"));
                }
                break;
            case "speed":
                if (isEngineOn) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "entered speed data display()");
                            tv_speed.setText(txt + " Km/h");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText("엔진 장비가 아직 준비되지 않았습니다.");
                        }
                    });
                }
                break;
            case "temperature":
                if (isEngineOn) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_temp.setText(txt + " °C");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText("엔진 장비가 아직 준비되지 않았습니다.");
                        }
                    });
                }
                break;
            case "gas":
                if (isEngineOn) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_gas.setText(txt + "ppm");
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_engine.setText("엔진 장비가 아직 준비되지 않았습니다.");
                        }
                    });
                }
                break;
        }

//        Log.d("---", listView.getCount() + "");
//        if (adapter.getPosition(id) == 0) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("---", adapter.getPosition(id) + "");
//                    name1.setText(id);
//                    data1.setText(txt);
//                }
//            });
//        } else if (adapter.getPosition(id) == 1) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("---", adapter.getPosition(id) + "");
//                    name2.setText(id);
//                    data2.setText(txt);
//                }
//            });
//        } else if (adapter.getPosition(id) == 2) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("---", adapter.getPosition(id) + "");
//                    name3.setText(id);
//                    data3.setText(txt);
//                }
//            });
//        } else if (adapter.getPosition(id) == 3) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Log.d("---", adapter.getPosition(id) + "");
//                    name4.setText(id);
//                    data4.setText(txt);
//                }
//            });
//        }
        new SendServer(msg.getId(), msg.getTxt(), msg.getTid()).start();
    }


    public void sendMsg(Msg msg) {
        String carId = msg.getId();

        if (carId == null || carId.equals("")) {
            Sender sender =
                    new Sender(msg);
            sender.start();
        } else {
            Sender2 sender2 =
                    new Sender2(msg);
            sender2.start();
        }

    } // end sendMsg

    class Sender extends Thread {
        Msg msg;

        public Sender(Msg msg) {
            Log.d(TAG, "entered Sender");
            this.msg = msg;
        }

        @Override
        public void run() {

            Collection<ObjectOutputStream> cols = maps.values();
            Iterator<ObjectOutputStream> its = cols.iterator();
            while (its.hasNext()) {
                try {
                    its.next().writeObject(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class Sender2 extends Thread {
        Msg msg;

        public Sender2(Msg msg) {
            Log.d(TAG, "entered Sender2");
            this.msg = msg;
        }

        @Override
        public void run() {
            String id = msg.getId();
            try {
                Collection<String> col = cars.keySet();
                Iterator<String> it = col.iterator();
                String sip = "";
                while (it.hasNext()) {
                    String key = it.next();
                    if (cars.get(key).equals(id)) {
                        sip = key;
                        Log.d("===", "key : " + key);
                        Log.d(TAG, sip);
                        maps.get(sip).writeObject(msg);
                    }
                }
//                if (!sip.equals("")) {
//                    maps.get(sip).writeObject(msg);
//                } else {
////                    maps.get(tid).writeObject(msg);
//                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    public ArrayList<String> getIds() {
        Collection<String>
                id = ids.values();
        Iterator<String> it = id.iterator();
        ArrayList<String> list = new ArrayList<String>();
        while (it.hasNext()) {
            list.add(it.next());
        }
        return list;
    }

    public void ckbt(View v) {
        Msg msg = null;
        if (v.getId() == R.id.button) {
            msg = new Msg("server", "1", null);
        } else if (v.getId() == R.id.button2) {
            msg = new Msg("server", "0", null);
        }
        sendMsg(msg);
    }


    // --------------------------CLIENT MODULE --------------------------------//
    String tabid = "tab1";
//    String sip = "192.168.43.2"; // phone hotspot ip
    //    String sip = "192.168.0.15"; // home wifi ip
    //    String sip = "70.12.113.219";

    //    String sip = "70.12.224.85";
    String sip = "70.12.231.236";
    //    String sip = "192.168.43.2"; // Hotspot hyunmin
    //    String sip = "52.78.108.32"; // AWS donghyun
//    String sip = "15.165.163.102"; // AWS hyunmin
    int sport = 8888;

    Socket ssocket;


    class ConnectThread extends Thread {

        String ip;
        int port;
        String id;

        public ConnectThread() {

        }

        public ConnectThread(String ip, int port, String id) {
            this.ip = ip;
            this.port = port;
            this.id = id;
        }

        @Override
        public void run() {
            try {
                // socket 찾는데 시간이 좀 걸리기때문에 시간제한을 둔다. //
                Thread.sleep(500);
                //ssocket.setSoTimeout(2000);
                ssocket = new Socket(ip, port);

//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tvserver.setText("Server Connected\n" + "IP: " + ssocket.getInetAddress().getHostAddress());
//
//                        Log.d("===", "hello : " + ssocket.getInetAddress().toString());
//
//                    }
//                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_server_state.setText("Connected Sever\n" + "(" + ssocket.getInetAddress().getHostAddress() + ")");
                        Log.d("===", ssocket.getInetAddress().toString());

                    }
                });
                // 에러 발생 시 재접속 시도 //
            } catch (Exception e) {

                int i = 0;

                while (true) {
                    i++;
                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            tvserver.setText("Retry Connecting " + finalI + "\nIP: " + ip);
//                        }
//                    });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_server_state.setText("Retry Connecting Server " + finalI + "\n (" + ip + ")");
                        }
                    });
                    // 재접속 시도 후 성공 시 text 변경 //
                    try {
                        Thread.sleep(500);
                        ssocket = new Socket(ip, port);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                tvserver.setText("Server Connected\n" + "IP: " + ssocket.getInetAddress().getHostAddress());
//                                Log.d("===", ssocket.getInetAddress().toString());
//
//                            }
//                        });
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_server_state.setText(ssocket.getInetAddress().getHostAddress()
                                        + "\nConnected Sever..");
                                Log.d("===", ssocket.getInetAddress().toString());

                            }
                        });
                        break;
                    } catch (Exception e1) {
                        //e1.printStackTrace();
                    }
                }
            }
            try {
                Log.d("===", ssocket.getInetAddress().toString());
                SReceiver sreceiver = new SReceiver(ssocket);
                sreceiver.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }// run End

    }// ConnectThread End


    class SReceiver extends AsyncTask<Void, Msg, Void> {
        InputStream is;
        ObjectInputStream ois;
        Socket socket;
        OutputStream os;
        ObjectOutputStream oos;

        public SReceiver(Socket socket) throws IOException {
            this.socket = socket;
            is = socket.getInputStream();
            ois = new ObjectInputStream(is);
        }

        @Override
        protected void onPreExecute() {
            try {
                os = socket.getOutputStream();
                oos = new ObjectOutputStream(os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Msg msg = new Msg("pad_Car1", "tab_JHM_connected", null);
            try {
                oos.writeObject(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            while (ois != null) {
                Msg msg = null;
                try {

                    // 서버가 꺼지면 여기서 exception 발생 //
                    msg = (Msg) ois.readObject();
                    Log.d(TAG, msg.getId() + " | " + msg.getTxt() + " | " + msg.getTid());
                    publishProgress(msg);

                } catch (Exception e) {

                    // 에러 발생 시 id System 으로 메시지 전송 //
                    msg = new Msg("System", "Server disconnected", null);
                    // 서버가 닫히면 다시 서버가 열릴 때 까지 접속을 재시도 해야 한다. //
                    publishProgress(msg);
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Msg... values) {

            String id = values[0].getId();
            // 위에서 에러가 나면 id 에 system 이라는 값을 주었다. //
            if (id.equals("System")) {
                if (ssocket != null) {
                    try {
                        ssocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                new ConnectThread(sip, sport, null).start();
                return;
            }
            String carId = values[0].getId();
            String message = values[0].getTxt();
            String control = values[0].getTid();


            Msg msg = new Msg(control, message, carId);
            sendMsg(msg);
            if (msg.getTid() != null && msg.getTid().equals("engine")) {
                displayData(msg);
            }

        }
    }

}
