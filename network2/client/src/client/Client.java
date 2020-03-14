package client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import msg.Msg;

public class Client {

	Socket socket;
	Receiver receiver;
	public static String CAN_ID;
	public static Sender sender;
	boolean flag = true;
	SendData sendData;
	

//	static String IP = "192.168.43.1";
//	static String IP = "192.168.0.12";
//	static int PORT = 8888;

	public Client() {
	}

	public Client(String ip, int port, String CID) {
		this.CAN_ID = CID;
		while (flag) {
			try {
				socket = new Socket(ip, port);
				flag = false;
			} catch (IOException e) {
				System.out.println("Retry...");
				try {
					Thread.sleep(1000);
					socket = new Socket(ip, port);
					flag = false;
				} catch (InterruptedException e1) {
					System.out.println("ERROR : Thread.sleep(1000); | InterruptedException");
				} catch (UnknownHostException e1) {
					System.out.println("ERROR : socket = new Socket(ip, port); | UnknownHostException");
				} catch (IOException e1) {
					System.out.println("ERROR : socket = new Socket(ip, port); | IOException");
				}

			}
		}
		System.out.println("Connected to server.");
		System.out.println("Server ip : " + ip);
		System.out.println("Server port : " + port);
		sender = new Sender(socket); // Sender class is runnable implemented class.
		Msg msg = new Msg(CID, null, null); // This is for client id to be displayed in the listview of
											// tabserver.
		sender.setMsg(msg);
		sender.setIp(ip);
		sender.setPort(port);
		new Thread(sender).start(); // this line will start the Thread.
		receiver = new Receiver(socket);
		receiver.setIp(ip);
		receiver.setPort(port);
		receiver.setSender(sender);
		receiver.start();
	}
}
