package top.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import msg.Msg;
import top.model.Client;
import top.vo.Device;

@Controller
public class ClientManageController {

	public static ArrayList<Device> loginInfo = new ArrayList<>();

	@RequestMapping("/main.top")
	public ModelAndView viewMain(ModelAndView mv) {

		mv.setViewName("main");
		return mv;
	}

	@RequestMapping("/webclient.top")
	public void webclient(HttpServletRequest req, HttpServletResponse res) {
		Client client = null;
		try {
//	           client = new Client("70.12.113.191",8888);
//	           String ServerIp="70.12.113.191";
//			client = new Client("70.12.231.236", 8888);
//			client = new Client("15.165.163.102", 8888); // AWS donghyun
//			client = new Client("192.168.43.2", 8888); 
//			String serverIp = "15.165.163.102"; // AWS hyunmin
			String serverIp = "70.12.231.236"; // hyunmin
//			String serverIp = "192.168.43.2"; // Hotspot hyunmin
			client = new Client(serverIp, 8888); // AWS hyunmin

//	             client = new Client("70.12.224.85", 8888);
//	             String ServerIp="70.12.224.85";
			Msg msg = new Msg(serverIp, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		String carId = req.getParameter("carId");
		String control = req.getParameter("control");

//		String ip = "";
//		for (int i = 0; i < loginInfo.size(); i++) {
//			if ((loginInfo.get(i).getCarId()).equals(carId)) {
//				for (int j = 0; j < loginInfo.size(); j++) {
//					if ((loginInfo.get(j).getId()).equals(control)) {
//					ip = loginInfo.get(j).getIp();	
//					}
//				}
//			}
//		}
		String message = req.getParameter("message");

		System.out.println("================webclient.top=================");
		System.out.println(carId + " | " + message + " | " + control);
		Msg msg = new Msg(carId, message, control);
		client.startClient(msg);

		try {
			res.sendRedirect("main.top");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/iotclient.top")
	public void sendNotiFromIoTClient(HttpServletRequest req, HttpServletResponse res) {
		String id = req.getParameter("id");
		String txt = req.getParameter("txt");
		String carId = req.getParameter("carId");

		System.out.println(id + " " + txt + " " + carId);
		if (txt == null | txt.equals("null")) {
			System.out.println("txt == null | txt.equals(\"null\")");
			System.out.println("Pad connected");
			return;
		}

		// send notification to pad only when data is in some conditions
		int val = Integer.parseInt(txt);
		if (val < 77777 && val >= 90) { // if data is greater than 90, send 1 to pad
			val = 1;
			try {
				res.sendRedirect("sendnotitopad.top?id=" + id + "&txt=" + val);
			} catch (IOException e) {
				System.out.println("Error while redirecting to sendnotitopad.top when val >=90 | IOException");
				e.printStackTrace();
			}
		} else if (val <= 20) { // if data is less than 20, send 0 to pad
			val = 0;
			try {
				res.sendRedirect("sendnotitopad.top?id=" + id + "&txt=" + val);
			} catch (IOException e) {
				System.out.println("Error while redirecting to sendnotitopad.top when val <=20 | IOException");
				e.printStackTrace();
			}
		}

		// send notification to manageApp regardless of the said conditions above
		URL url = null;
		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
		} catch (MalformedURLException e) {
			System.out.println("Error while creating Firebase URL | MalformedURLException");
			e.printStackTrace();
		}
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			System.out.println("Error while createing connection with Firebase URL | IOException");
			e.printStackTrace();
		}
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");

		// set my firebase server key
		conn.setRequestProperty("Authorization", "key="
				+ "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_");

		// create notification message into JSON format
		JSONObject message = new JSONObject();
//			message.put("to",
//					"cmm9ME4d9Ss:APA91bGxP8xrtRCzEof13dArAAuJKGODYi7uejryVTxkdndEoUxC0NTw2LbNNhUizHS38syfGTmHRBRUzCXj5HLgkQcb2XYeE4eiyGG-kKHSU-OPbSet2AMU_yjv0gQMg0RDLhNy920d");
		message.put("to", "/topics/temperature_manage");
		message.put("priority", "high");
		JSONObject notification = new JSONObject();
		notification.put("title", carId);
		notification.put("body", id);
		message.put("notification", notification);
		JSONObject data = new JSONObject();
		data.put("control", id);
		data.put("data", txt);
		message.put("data", data);

//		{
//			  "message":{
//			    "token":"bk3RNwTe3H0:CI2k_HHwgIpoDKCIZvvDMExUdFQ3P1...",
//			    "notification":{
//			      "title":"Portugal vs. Denmark",
//			      "body":"great match!"
//			    },
//			    "data" : {
//			      "Nick" : "Mario",
//			      "Room" : "PortugalVSDenmark"
//			    }
//			  }
//			}

		// send data to firebase (http method)
		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(message.toString());
			out.flush();
			conn.getInputStream();
		} catch (IOException e) {
			System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
			e.printStackTrace();
		}

	}

	@RequestMapping("/sendnotitopad.top")
	public void sendNotiFromManager(HttpServletRequest req) {
		String id = req.getParameter("id");
		String txt = req.getParameter("txt");

		// send notification to pad
		URL url = null;
		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
		} catch (MalformedURLException e) {
			System.out.println("Error while creating Firebase URL | MalformedURLException");
			e.printStackTrace();
		} // firebase URL
		HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			System.out.println("Error while createing connection with Firebase URL | IOException");
			e.printStackTrace();
		} // create connection
		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");

		// set my firebase server key
		conn.setRequestProperty("Authorization", "key="
				+ "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_");

		// create notification message into JSON format
		JSONObject message = new JSONObject();
//			message.put("to",
//					"d5mywEIWuKg:APA91bEnl0jplW5jj6hYZq3pc3rzn_3HVvTiaayDg8m8CGikOxBS1_8LV43Yaz_qh_FwCulIzMYpniFzA-nrIfUKi_h4uCIJIrMshvpfW8d_QUaYgDjVV14wlnVhue2YWcjWkH5Yt6Sb");
		message.put("to", "/topics/temperature"); // send to pad with topic subscribed to temperature
		message.put("priority", "high");
		JSONObject notification = new JSONObject();
		notification.put("title", id);
		notification.put("body", txt);
		message.put("notification", notification);

		// send data to firebase (http method)
		try {
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			out.write(message.toString());
			out.flush();
			conn.getInputStream();
		} catch (IOException e) {
			System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
			e.printStackTrace();
		}
	}

	@RequestMapping("/iotclientloginstatus.top")
	public void iotclientloginstatus(HttpServletRequest req, HttpServletResponse res) {
		String ip = req.getParameter("ip");
		String id = req.getParameter("id");
		String carId = req.getParameter("carId");

		if (ip != null) {
			if (id == null | id.equals("")) {
				for (int i = 0; i < loginInfo.size(); i++) {
					int cnt = 0;
					if (ip.equals(loginInfo.get(i).getIp())) {
						System.out.println(loginInfo.get(i).getId() + "님 로그아웃");
						try {
							res.sendRedirect(
									"iotclient.top?id=" + loginInfo.get(i).getId() + "&txt=88888&carId=" + carId);
						} catch (IOException e) {
							e.printStackTrace();
						}
						loginInfo.remove(i);
						cnt++;
						if (cnt == 1)
							break;
					} else {
						System.out.println("No login info found.");
					}
				}
			} else {
				Device device = new Device();
				device.setIp(ip);
				device.setId(id);
				device.setCarId(carId);
				loginInfo.add(device);
				System.out.println(id + "님 로그인");
				try {
					res.sendRedirect("iotclient.top?id=" + id + "&txt=77777&carId=" + carId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {

		}
		System.out.println("Login List");
		for (int i = 0; i < loginInfo.size(); i++) {
			System.out.println("ip : " + loginInfo.get(i).getIp() + " | id : " + loginInfo.get(i).getId()
					+ " | carId : " + loginInfo.get(i).getCarId());
//			loginInfo.add(loginInfo.get(i).getId());
		}

//		strings.add("2");
//		
//		mv.addObject("loginInfo", strings);
//		mv.setViewName("main");
//		if (loginInfo.size() == 0) {
//			return null;
//		}
	}

	@RequestMapping("/getIoTClientLoginData.top")
	@ResponseBody
	public ArrayList<Device> getIoTClientLoginData(HttpServletRequest req, HttpServletResponse res, ModelAndView mv) {
//		mv.addObject("loginInfo", loginInfo);
//		mv.setViewName("main");
		return loginInfo;

	}

	@RequestMapping("/checkCarState.top")
	public void checkCarState(HttpServletRequest req, HttpServletResponse res) {
		String id = req.getParameter("control");
		String carId = req.getParameter("carId");
		String txt = "88888";

//		for (int i = 0; i < loginInfo.size(); i++) {
//			if ((loginInfo.get(i).getCarId()).equals(carId)) {
//				for (int j = 0; j < loginInfo.size(); j++) {
//					if ((loginInfo.get(j).getId()).equals(id)) {
//						txt = "77777";
//						if (j == 0) {
//							try {
//								res.sendRedirect("iotclient.top?id=" + "login" + "&txt=" + (j + 1) + "&carId=" + carId);
//							} catch (IOException e) {
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//			}
//		}

		System.out.println("checkCarState() : " + id + " " + carId + " " + txt);

		try {
			res.sendRedirect("iotclient.top?id=" + id + "&txt=" + txt + "&carId=" + carId);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@RequestMapping("/checkServerConnection.top")
	public void checkServerConnection(HttpServletRequest req) {
		String managerId = req.getParameter("managerId");
		System.out.println("managerId : " + managerId);
	}

}
