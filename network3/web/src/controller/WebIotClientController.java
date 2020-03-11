//package controller;
//
//import java.io.IOException;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class WebIotClientController {
//
//	@RequestMapping("/iotclient")
//	protected void fromIotClient(HttpServletRequest request, HttpServletResponse response) {
//
//		String id = request.getParameter("id");
//		String txt = request.getParameter("txt");
//
//		System.out.println("ID : " + id);
//		System.out.println("Data : " + txt);
//
//		try {
//			response.sendRedirect("");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
