package client;

import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import msg.Msg;

public class Sender implements Runnable {
	OutputStream os;
	ObjectOutput oos;
	Socket socket;
	String IP;
	int PORT;
	Msg msg;

	public Sender() {

	}

	public Sender(Socket socket) {
		this.socket = socket;
		try {
			os = socket.getOutputStream();
			oos = new ObjectOutputStream(os);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Msg getMsg() {
		return msg;
	}

	public void setMsg(Msg msg) {
		this.msg = msg;
	}

	public void setIp(String ip) {
		this.IP = ip;
	}

	public void setPort(int port) {
		this.PORT = port;
	}

	@Override
	public void run() {
		if (oos != null) {
			try {
				oos.writeObject(msg);
			} catch (IOException e) {
				System.out.println("Error while sending msg");
				if (oos != null) {
					try {
						oos.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					try {
						socket = new Socket(IP, PORT);
						/////
						return;
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}

}
