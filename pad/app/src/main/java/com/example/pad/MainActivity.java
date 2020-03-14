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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
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


    // Network
    ServerSocket serverSocket;
    int port = 9999;
    ServerReadyThread serverReadyThread;
    public static HashMap<String, ObjectOutputStream> maps;
    public static HashMap<String, String> ids;

    // ServerReadyThread variables
    boolean aflag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ActionBar 숨기기
        getSupportActionBar().hide();

        maps = new HashMap<>();
        ids = new HashMap<>();

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

                    Log.d(TAG, "client disconnected");
                    maps.remove(socket.getInetAddress().toString());
                    ids.remove(socket.getInetAddress().toString());
                    displayData(new Msg("pad", null, null));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setList();
                        }
                    });
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

    class SendServer extends Thread {

        //        String urlstr = "http://192.168.43.2:8080/webspringserver/iotclient.top";
//        String urlstr = "http://52.78.108.32:8888/webspringserver/iotclient.top"; // AWS donghyun
        String urlstr = "http://15.165.163.102:8080/webspringserver/iotclient.top"; // AWS hyunmin

        public SendServer(String id, String txt) {
            urlstr += "?id=" + id + "&txt=" + txt;
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
                Log.d(TAG, "Client ID : " + msg.getId());
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
                    maps.remove(socket.getInetAddress().toString());
                    ids.remove(socket.getInetAddress().toString());
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

    private void displayData(final Msg msg) {
        final String id = msg.getId();
        final String txt = msg.getTxt();
        Log.d("---", listView.getCount() + "");
        if (adapter.getPosition(id) == 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("---", adapter.getPosition(id) + "");
                    name1.setText(id);
                    data1.setText(txt);
                }
            });
        } else if (adapter.getPosition(id) == 1) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("---", adapter.getPosition(id) + "");
                    name2.setText(id);
                    data2.setText(txt);
                }
            });
        } else if (adapter.getPosition(id) == 2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("---", adapter.getPosition(id) + "");
                    name3.setText(id);
                    data3.setText(txt);
                }
            });
        } else if (adapter.getPosition(id) == 3) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("---", adapter.getPosition(id) + "");
                    name4.setText(id);
                    data4.setText(txt);
                }
            });
        }
        new SendServer(msg.getId(), msg.getTxt()).start();
    }


    public void sendMsg(Msg msg) {
        String tid = msg.getTid();

        if (tid == null || tid.equals("")) {
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
            this.msg = msg;
        }

        @Override
        public void run() {
            String tid = msg.getTid();
            try {
                Collection<String> col = ids.keySet();
                Iterator<String> it = col.iterator();
                String sip = "";
                while (it.hasNext()) {
                    String key = it.next();
                    if (ids.get(key).equals(tid)) {
                        sip = key;
                        Log.d("===", "key : " + key);
                    }
                }
                Log.d("===", "sip : " + sip);
                if (!sip.equals("")) {
                    maps.get(sip).writeObject(msg);
                } else {
                    maps.get(tid).writeObject(msg);
                }
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
//    String sip = "70.12.231.236";
//    String sip = "192.168.43.2";
//    String sip = "52.78.108.32"; // AWS donghyun
    String sip = "15.165.163.102"; // AWS hyunmin
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
                        status.setText(ssocket.getInetAddress().getHostAddress()
                                + "\nConnected Sever");
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
                            status.setText(ip + "Retry Connecting" + finalI);
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
                                status.setText(ssocket.getInetAddress().getHostAddress()
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
            Msg msg = new Msg("tab_JHM", "tab_JHM_connected", null);
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
            }// socket closed //
            // 재접속하기위해 함수 호출  //
            String ip = values[0].getId();
            String tid = values[0].getTid();
            String state = values[0].getTxt();
            Log.d("===", "ip : " + ip + ", state : " + state + ", tid : " + tid);
//            if (state != null || !state.equals("")) {
            status.setText(state);
//            }
            Msg msg = new Msg("server", state, tid);
            sendMsg(msg);


//            if (ip == null || ip.equals("")) {
//
//                if (state.trim().equals("1")) {
//                    if (state.trim().equals("1")) {
//                        msg = new Msg("server", state, "ydh");
//                    } else if (state.trim().equals("2")) {
//                        msg = new Msg("server", state, "jmj");
//                    } else if (state.trim().equals("3")) {
//                        msg = new Msg("server", state, "hennie");
//                    } else if (state.trim().equals("4")) {
//                        msg = new Msg("server", state, "JHM");
//                    } else if (state.trim().equals("5")) {
//                        msg = new Msg("server", state, "hyunchu");
//                    }
//                } else if (state.trim().equals("0") && state != null) {
//                    Log.d("===", "ip : " + ip + ",state : " + state + ", txt : " + txt);
//                    if (state.trim().equals("1")) {
//                        msg = new Msg("server", "0", "ydh");
//                    } else if (txt.trim().equals("2")) {
//                        msg = new Msg("server", "0", "jmj");
//                    } else if (txt.trim().equals("3")) {
//                        msg = new Msg("server", "0", "hennie");
//                    } else if (txt.trim().equals("4")) {
//                        msg = new Msg("server", "0", "JHM");
//                    } else if (txt.trim().equals("5")) {
//                        msg = new Msg("server", "0", "hyunchu");
//                    }
//                }
//                sendMsg(msg);
//            }
        }
    }

}
