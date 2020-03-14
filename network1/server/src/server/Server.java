package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import msg.Msg;

public class Server {

	int port;

	ServerSocket serverSocket;
	HashMap<String, ObjectOutputStream> maps = new HashMap<>();

	boolean aflag = true; // while roop 안에서 사용할 flag

	public Server() {

	}

	// 포트를 받아오는 인스턴스 메써드 //
	public Server(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		System.out.println("Start Server");
		// 소켓에 서브 쓰레드 생성 //
		Runnable r = new Runnable() {

			@Override
			public void run() {
				// 소켓을 계속 새로 만들기 떄문에 멤버변수가 아닌 쓰레드 안의 while 안에 넣기. //
				Socket socket = null;
				while (aflag) {
					try {
						System.out.println("Server is Ready.. ");
						socket = serverSocket.accept();
						System.out.println(socket.getInetAddress() + ": Connected");
						System.out.println(socket.getInetAddress().getHostAddress());
						System.out.println(socket.getPort() + "");
						// 소켓에서 어떤 IP 를 사용했는지 확인할 수 있다. //
						// 소켓이 만들어지면 makeOut 이라는 함수 호출 //
						makeOut(socket);
//						System.out.println(socket.getInputStream().read() + "");
//						while (socket.getInputStream().read() == -1) {
//							System.out.println("asdf");
//							continue;
//						}
						new Receiver(socket).start();
						System.out.println("HI");
						// outputstream 을 만들어주고 끝낼 예정

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		};
		new Thread(r).start();
	}

	public void makeOut(Socket socket) throws IOException {
		OutputStream os;
		ObjectOutputStream oos;

		os = socket.getOutputStream();
		oos = new ObjectOutputStream(os);
		maps.put(socket.getInetAddress().toString(), oos);
		System.out.println("Visitor ip : " + socket.getInetAddress().toString());
		System.out.println("Visitor size : " + maps.size());

	}

	// Client 의 Sender 와 통신할 Server 의 Receiver //
	class Receiver extends Thread {

		InputStream is;
		ObjectInputStream ois;

		// Receiver 에 소켓이 있기때문에 여기서 oos 만든다. //
//		OutputStream os;
//		ObjectOutputStream oos;

		Socket socket;

		public Receiver(Socket socket) {
			System.out.println("1");
			// Client 에서는 Output stream , Server 에서는 Input Stream //
			this.socket = socket;
			System.out.println("2");
			System.out.println(socket.isConnected());
			System.out.println((socket == null) + "");
//			is = socket.getInputStream();
//			System.out.println(socket.getInputStream().read() + "");
//			System.out.println(is.read() + "");
			System.out.println("3");
			try {
				is = socket.getInputStream();
				ois = new ObjectInputStream(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("4");

//			os = socket.getOutputStream();
//			oos = new ObjectOutputStream(os);

			// 들어온 접속자의 주소를 키값으로 , object 값을 value 로 해쉬맵 maps 에 저장 //
//			maps.put(socket.getInetAddress().toString(), oos);
//			System.out.println("Visitors: " + maps.size());
		}

		@Override
		public void run() {
			System.out.println("5");
			// while : Client 와 연결이 끊어지지 않는 한 계속 //
			System.out.println("6");
			while (ois != null) {
				System.out.println("7");
				Msg msg = null;
				try {
					System.out.println("Receiver run() while(ois !=null) try");

					msg = (Msg) ois.readObject();
					System.out.println(
							socket.getInetAddress() + ":" + msg.getId() + ":" + msg.getTxt() + ":" + msg.getTid());
//					msg = (Msg) ois.readObject();
					// 받은 data를 찍는다. //

//					if (msg.getTxt().equals("q")) {
//						System.out.println(socket.getInetAddress() + msg.getId() + ":Exit");
//						// q 를 누르면 나간 것이므로 //
//						Thread.sleep(1000);
//						maps.remove(socket.getInetAddress().toString());
//						System.out.println("Visitors:" + maps.size());
//						break;
//					}
//					int count = 0;
//					// 브로드캐스트 //
//					while (true) {
//
//						Thread.sleep(1000);
//						System.out.println("reconnecting pad server : " + ++count);
//						if (msg.getId().equals("tabregister")) {
//							sendMsg(msg);
//							break;
//						}
//					}
					sendMsg(msg);

				} catch (Exception e) {
					e.printStackTrace();
					// 비정상 종료하여도 어떤 사람이 나가는지 확인 가능. //
					maps.remove(socket.getInetAddress().toString());
					System.out.println(socket.getInetAddress() + ":Exit");
					System.out.println("Visitor size : " + maps.size());
					break; // dis.readUTF 에 문제가 생기면 while roop 중단.
				} 
			}

			System.out.println("ois == null");
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

	class Sender extends Thread {
		Msg msg;

		public Sender(Msg msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			System.out.println("Sender run() msg.getId : " + msg.getId());
			System.out.println("Sender run() msg.getTxt : " + msg.getTxt());
			System.out.println("Sender run() msg.getTid : " + msg.getTid());
			// HashMap 에 있는 oos 를 꺼낸다음 for 문을 돌리면서 전송한다. //
			System.out.println("Sender run() entered");
			Collection<ObjectOutputStream> cols = maps.values();
			Iterator<ObjectOutputStream> its = cols.iterator();
			while (its.hasNext()) {
				try {
					its.next().writeObject(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// sender 2 생성 , 귓속말 기능//
	class Sender2 extends Thread {
		Msg msg;

		public Sender2(Msg msg) {
			this.msg = msg;
		}

		@Override
		public void run() {

			String ip = msg.getTid();
			System.out.println("Sender2 run() msg.getId : " + msg.getId());
			System.out.println("Sender2 run() msg.getTxt : " + msg.getTxt());
			System.out.println("Sender2 run() msg.getTid : " + msg.getTid());
			try {
//				maps.get("/192.168.43.180").writeObject(msg);
//				maps.get("/192.168.43.53").writeObject(msg);
//				maps.get("/192.168.43.111").writeObject(msg);
				maps.get(ip).writeObject(msg);
//				maps.get("/70.12.225.91").writeObject(msg);
			} catch (

			IOException e) {
				e.printStackTrace();
			}

		}

	}

	// 접속한 사용자에게 메시지 보내기.//
	public void sendMsg(Msg msg) {
		System.out.println("sendMsg(Msg msg) entered");
		String ip = msg.getTid();
		// null 을 항상 먼저 비교하자. //
		if (ip == null || ip.equals("")) {
//		 전체에 broadcast //
			Sender sender = new Sender(msg);
			sender.start();
		} else {
			// 한명에게 //
			System.out.println("Sender2");
			Sender2 sender2 = new Sender2(msg);
			sender2.start();
		}
	}

	public void serverStart() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("Input ip");
			String ip = sc.nextLine();
			System.out.println("Input Msg");
			String txt = sc.nextLine();
			Msg msg = null;
			if (txt.equals("q"))
				break;
			// 귓속말 기능을 넣기위해 ip도 입력받는다. constructor 에도 ip 추가
			if (ip == null || ip.equals("")) {
				msg = new Msg("Admin", txt, null);
			} else {
				msg = new Msg("Admin", txt, ip);
			}
			sendMsg(msg);
		}
		sc.close();
	}

	// 메인 쓰레드 //
	public static void main(String[] args) {

		Server server = null;
		try {
			// server 호출 = 쓰레드 실행 //
			server = new Server(8888);
			server.serverStart();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}