package client;

public class ClientTest {

	static String CAN_ID = "engine";
	static String CAR_ID = "car1";

	public static void main(String[] args) {
//		Client client = new Client("192.168.43.1", 8888, "IoT_Client_1");
//		Client client = new Client("192.168.43.180", 8888, "IoT_Client_1");
//		Client client = new Client("192.168.43.53", 9999, CAN_ID, CAR_ID); // hotspot tabserver ip
		Client client = new Client("70.12.225.91", 9999, CAN_ID, CAR_ID); // MULTI_GUEST tabserver ip
//		Client client = new  Client("70.12.230.140", 8888, "JHM");
//		Client client = new  Client("192.168.43.2", 9999, "JHM");
//		Client client = new Client("192.168.43.111", 9999, CAN_ID, CAR_ID);
	}
}
