package web;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class WebIotClientServlet
 */
@WebServlet({ "/WebIotClientServlet", "/iotclient" })
public class WebIotClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String id = request.getParameter("id");
		String txt = request.getParameter("txt");

		System.out.println("ID : " + id);
		System.out.println("Data : " + txt);

		response.sendRedirect("/web/send?id=" + id + "&txt=" + txt);

		URL url = new URL("https://fcm.googleapis.com/fcm/send");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		conn.setUseCaches(false);
		conn.setDoInput(true);
		conn.setDoOutput(true);

		conn.setRequestProperty("Authorization", "key=" + "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_");
		conn.setRequestProperty("Content-Type", "application/json");

		JSONObject json = new JSONObject();

		try {
			json.put("to", "e1Xt10qdk5I:APA91bFZkwR0p82c0aTQ07qaxutp-dKz8-3V_yQDpiDpsx8lOxw87DJ5AKVNSxu7vDlOTE_NG0HwjbB0Cmi11YuEf0gDCv4H1qkTMOgMA24QAuO8ZnGywaINSrvWQ6M4ErrRdUJWKlkm");

			JSONObject info = new JSONObject();
			info.put("title", id);
			info.put("body", txt);

			json.put("notification", info);

			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			out.write(json.toString());
			out.flush();
			conn.getInputStream();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
