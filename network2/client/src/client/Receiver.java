package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import msg.Msg;

public class Receiver extends Thread {
	InputStream is;
	ObjectInputStream ois;
	Socket socket;
	Sender sender;
	String IP;
	int PORT;
	String CID;
	String CAR_ID;

	SendData sendData;

//	SerialConnect serialConnect;

	public Receiver() {

	}

	public Receiver(Socket socket) {
		try {
//			serialConnect = new SerialConnect("COM8");
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		this.socket = socket;
		try {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setSender(Sender sender) {
		this.sender = sender;
		CID = sender.getMsg().getId();
		CAR_ID = sender.getMsg().getTid();
		sendData = new SendData(sender);
		new Thread(sendData).start();
	}

	public void setIp(String ip) {
		this.IP = ip;
	}

	public void setPort(int port) {
		this.PORT = port;
	}

	@Override
	public void run() {
		while (ois != null) {
			Msg msg = null;
			try {
				msg = (Msg) ois.readObject();

				System.out.println("From tabserver : " + msg.getId());
				System.out.println("From tabserver : " + msg.getTxt());
				System.out.println("From tabserver : " + msg.getTid());


				if (msg.getTid() == null | msg.getTid().equals("") | msg.getTid().equals("null")) {
					if (msg.getTxt().equals("1")) {
						sendData.setFlag(true);
						new Thread(sendData).start();
					} else if (msg.getTxt().equals("2")) {
						sendData.setFlag(false);
					}
				} 
//				else if (msg.getTid().equals("iot")) {
//					System.out.println("iot");
//					String id = "00000000";
//					String data = "";
//					if (msg.getTxt().equals("1")) {
//						data = "0000000000000001";
//					} else if (msg.getTxt().equals("0")) {
//						data = "0000000000000002";
//					}
//					String msg2 = id + data;
//					try {
//						serialConnect.send(msg2);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
			} catch (Exception e) {
				while (true) {
					try {
						System.out.println("Trying to reconnect to server");
						if (ois != null) {
							ois.close();
						}
						if (socket != null) {
							socket.close();
						}
						System.out.println("catch() IP : " + IP);
						System.out.println("catch() PORT : " + PORT);
						System.out.println("catch() CID : " + CID);
						new Client(IP, PORT, CID, CAR_ID);
						break;
					} catch (IOException e1) {
						System.out.println("Receiver run() catch() while(true) catch()");
						e1.printStackTrace();
					}
				}
				return;

//				try {
//					if (ois != null) {
//						ois.close();
//					}
//					if (socket != null) {
//						socket.close();
//					}
//					System.out.println("IoT Client Receiver die");
//					return;
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
			}
		}
	}

}
