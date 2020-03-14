//package serial;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.TooManyListenersException;
//
//import gnu.io.CommPort;
//import gnu.io.CommPortIdentifier;
//import gnu.io.NoSuchPortException;
//import gnu.io.PortInUseException;
//import gnu.io.SerialPort;
//import gnu.io.SerialPortEvent;
//import gnu.io.SerialPortEventListener;
//import gnu.io.UnsupportedCommOperationException;
//
//public class SerialConnect implements SerialPortEventListener {
//	CommPortIdentifier commPortIdentifier;
//	CommPort commPort;
//
//	private BufferedInputStream bin;
//	private InputStream in;
//	private OutputStream out;
//
//	public SerialConnect() {
//	}
//
//	public SerialConnect(String portName) throws Exception {
//
//		// 1.È®ÀÎÇÏ±â
//		commPortIdentifier = CommPortIdentifier.getPortIdentifier(portName);
//		System.out.println("Com Port Identified!");
//
//		// 2. Á¢¼ÓÇÏ±â
//		connect();
//		System.out.println("Com Port Connected!");
//		// 3. ¼ö½Å ½ÃÀÛÇÏ±â
//		new Thread(new SerialWriter()).start(); // SerialWriter() »ý¼ºÀÚ´Â ¼ö½Å½ÃÀÛ ¸Þ¼¼Áö¸¦ msgº¯¼ö¿¡ ¼³Á¤ÇØÁØ´Ù.
//		System.out.println("CAN network started");
//	}
//
//	private void connect()
//			throws PortInUseException, UnsupportedCommOperationException, IOException, TooManyListenersException {
//		if (commPortIdentifier.isCurrentlyOwned()) {
//			// ´©±º°¡°¡ »ç¿ëÇÏ°í ÀÖÀ¸¸é ³»°¡ »ç¿ëÇÒ ¼ö ¾ø°Ô µÈ´Ù.
//			System.out.println("Identifier currently in use");
//		} else {
//			commPort = commPortIdentifier.open(this.getClass().getName(), 5000); // 5ÃÊ µ¿¾È Ã£´Â´Ù. ¸ø Ã£À¸¸é Exception
//
//			if (commPort instanceof SerialPort) {
//				SerialPort serialPort = (SerialPort) commPort;
//				serialPort.addEventListener(this);
//				serialPort.notifyOnDataAvailable(true);
//				serialPort.setSerialPortParams(921600, // Åë½Å¼Óµµ
//						SerialPort.DATABITS_8, // µ¥ÀÌÅÍ ºñÆ®
//						SerialPort.STOPBITS_1, // stop ºñÆ®
//						SerialPort.PARITY_NONE); // ÆÐ¸®Æ¼
//				// socketÀ» ¿¬°áÇÏ´Â °Í°ú µ¿ÀÏ.
//
//				in = serialPort.getInputStream();
//				bin = new BufferedInputStream(in);
//				out = serialPort.getOutputStream();
//			} else {
//				System.out.println("this is port is not SerialPort");
//				System.out.println("Error: Only serial ports are handled by this example.");
//			}
//		}
//	}
//
//	@Override
//	public void serialEvent(SerialPortEvent event) {
//		switch (event.getEventType()) {
//		case SerialPortEvent.BI:
//		case SerialPortEvent.OE:
//		case SerialPortEvent.FE:
//		case SerialPortEvent.PE:
//		case SerialPortEvent.CD:
//		case SerialPortEvent.CTS:
//		case SerialPortEvent.DSR:
//		case SerialPortEvent.RI:
//		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
//			break;
//		case SerialPortEvent.DATA_AVAILABLE:
//			byte[] readBuffer = new byte[128];
//			try {
//				while (bin.available() > 0) {
//					int numBytes = bin.read(readBuffer);
//				}
//				String ss = new String(readBuffer);
//				System.out.println("Receive Low Data:" + ss + "||");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			break;
//		}
//	}
//
//	// Sending message
//	public void send(String msg) {
//		new Thread(new SerialWriter(msg)).start();
//	}
//
//	class SerialWriter implements Runnable {
//		String data;
//
//		public SerialWriter() {
//			this.data = ":G11A9\r";
//		}
//
//		public SerialWriter(String msg) {
//			this.data = convertData(msg);
//		}
//
//		public String convertData(String msg) {
//			msg = msg.toUpperCase();
//			msg = "W28" + msg;
//			char charArray[] = msg.toCharArray();
//			int checksum = 0;
//			for (char ch : charArray) {
//				checksum += ch;
//			}
//			checksum = (checksum & 0xFF);
//			String result = ":";
//
//			result += msg + Integer.toHexString(checksum).toUpperCase();
//			result += "\r";
//			return result;
//		}
//
//		public void run() {
//			try {
//
//				byte[] outputData = data.getBytes();
//
//				out.write(outputData);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//	}
//
//	public static void main(String[] args) {
//		SerialConnect serialConnect = null;
//		String id = "00000000";
//		String data = "0000000000000055";
//		String msg = id + data;
//
//		try {
//			serialConnect = new SerialConnect("COM8");
//			serialConnect.send(msg);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//}
