package top.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import msg.Msg;

public class Client {

	Socket socket;
	Sender sender;
	String cid;
	Receiver receiver;
	String connect_sever_ip;

	public Client() {
	}

	public Client(String address, int port) throws IOException {

		try {
			socket = new Socket(address, port);

		} catch (Exception e) {
			while (true) {
				System.out.println("Retry..");
				try {
					Thread.sleep(1000);
					socket = new Socket(address, port);
					break;
				} catch (Exception e1) {
					// e1.printStackTrace();
				}
			}
		}

		System.out.println("Connected Server:" + address);
		receiver = new Receiver(socket);
		sender = new Sender(socket);

	}

	class Receiver extends Thread {
		InputStream is;
		ObjectInputStream ois;

		public Receiver(Socket socket) throws IOException {
			is = socket.getInputStream();
			ois = new ObjectInputStream(is);
		}

		@Override
		public void run() {
			while (ois != null) {
				Msg msg = null;
				try {
					msg = (Msg) ois.readObject();
					System.out.println(msg.getId() + "CONNECT IP CHECK");
					connect_sever_ip = msg.getId();

				} catch (Exception e) {
					System.out.println("Server Die");
					break;
				}
			}

			try {
				if (ois != null) {
					ois.close();
				}
				if (socket != null) {
					socket.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class Sender implements Runnable {

		OutputStream os;
		ObjectOutputStream oos;
		Msg msg;

		public Sender(Socket socket) throws IOException {
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
		}

		public void setMsg(Msg msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			if (oos != null) {
				try {
					oos.writeObject(msg);
				} catch (IOException e) {

					try {
						if (oos != null) {
							oos.close();
						}
						if (socket != null) {
							socket.close();
						}
					} catch (Exception e1) {
						e1.printStackTrace();
						try {
							new Client(connect_sever_ip, 8888);
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

					}

				}
			}
		}

	}

	public void startClient(Msg FromServeletMsg) {
		Msg msg = FromServeletMsg;
		System.out.println("Sending msg to pad : " + msg.getId() + " | " + msg.getTxt() + " | " + msg.getTid());
		sender.setMsg(msg);
		new Thread(sender).start();
	}

}