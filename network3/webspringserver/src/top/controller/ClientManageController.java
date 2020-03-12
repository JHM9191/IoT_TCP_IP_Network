package top.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import msg.Msg;
import top.model.Client;

@Controller
public class ClientManageController {

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
			res.sendRedirect("main.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
