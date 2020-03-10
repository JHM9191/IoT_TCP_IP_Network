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

	SendData sendData;

	public Receiver() {

	}

	public Receiver(Socket socket) {
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
		sendData = new SendData(sender);
		new Thread(sendData).start();
		CID = sender.getMsg().getId();
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

				if (msg.getTxt().equals("1")) {
					sendData.setFlag(true);
					new Thread(sendData).start();
				} else if (msg.getTxt().equals("0")) {
					sendData.setFlag(false);
				}

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
						new Client(IP, PORT, CID);
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
