package top.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import msg.Msg;
import top.model.Client;

@Controller
public class ClientManageController {

	@RequestMapping("/main.top")
	public ModelAndView viewMain(ModelAndView mv) {
		mv.setViewName("main");
		return mv;
	}

	@RequestMapping("/webclient.top")
	public void changeState(HttpServletRequest req, HttpServletResponse res) {
		Client client = null;
		try {
//	           client = new Client("70.12.113.191",8888);
//	           String ServerIp="70.12.113.191";
//			client = new Client("70.12.224.85", 8888);
//			client = new Client("15.165.163.102", 8888);
			client = new Client("192.168.43.2", 8888);
			String ServerIp = "192.168.43.2";

//	             client = new Client("70.12.224.85", 8888);
//	             String ServerIp="70.12.224.85";
			Msg msg = new Msg(ServerIp, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
		String tid = req.getParameter("ip");
		String txt = req.getParameter("state");

		System.out.println(tid + "--------servletCheck-------" + txt);
		Msg msg = new Msg("fromservlet", txt, tid);
		client.startClient(msg);

		try {
			res.sendRedirect("main.top");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping("/iotclient.top")
	public void sendNotiFromIoTClient(HttpServletRequest req, HttpServletResponse res) {
		String id = req.getParameter("id");
		String txt = req.getParameter("txt");

		if (Integer.parseInt(txt) >= 50) {
			try {
				res.sendRedirect("sendnotitoclient.top?id=" + id + "&txt=" + txt);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("ID : " + id);
		System.out.println("Data : " + txt);

		URL url;
		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Authorization", "key="
					+ "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_");
			conn.setRequestProperty("Content-Type", "application/json");

			JSONObject json = new JSONObject();
			json.put("to",
					"cmm9ME4d9Ss:APA91bGxP8xrtRCzEof13dArAAuJKGODYi7uejryVTxkdndEoUxC0NTw2LbNNhUizHS38syfGTmHRBRUzCXj5HLgkQcb2XYeE4eiyGG-kKHSU-OPbSet2AMU_yjv0gQMg0RDLhNy920d");
			JSONObject info = new JSONObject();
			info.put("title", id);
			info.put("body", txt);

			json.put("notification", info);

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(json.toString());
			out.flush();
			conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@RequestMapping("/sendnotitoclient.top")
	public void sendNotiFromManager(HttpServletRequest req) {
		String id = req.getParameter("id");
		String txt = req.getParameter("txt");
		
		URL url;
		

		/**
	    {
	       "notification": {
	          "title": "JSA Notification",
	          "body": "Happy Message!"
	       },
	       "data": {
	          "Key-1": "JSA Data 1",
	          "Key-2": "JSA Data 2"
	       },
	       "to": "/topics/JavaSampleApproach",
	       "priority": "high"
	    }
	*/

		try {
			url = new URL("https://fcm.googleapis.com/fcm/send");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);

			conn.setRequestProperty("Authorization", "key="
					+ "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_");
			conn.setRequestProperty("Content-Type", "application/json");

			JSONObject message = new JSONObject();
			
//			message.put("to",
//					"d5mywEIWuKg:APA91bEnl0jplW5jj6hYZq3pc3rzn_3HVvTiaayDg8m8CGikOxBS1_8LV43Yaz_qh_FwCulIzMYpniFzA-nrIfUKi_h4uCIJIrMshvpfW8d_QUaYgDjVV14wlnVhue2YWcjWkH5Yt6Sb");
			message.put("to",
					"/topics/temperature");
			message.put("priority", "high");
			
			JSONObject notification = new JSONObject();
			notification.put("title", id);
			notification.put("body", "WARNING! 온도가 50도 이상 입니다. 서버에 받은 값 : "+ txt);

			message.put("notification", notification);

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(message.toString());
			out.flush();
			conn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
