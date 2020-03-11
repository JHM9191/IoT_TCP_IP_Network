//package controller;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import service.AndroidPushNotificationsService;
//
//@Controller
//public class SendNotificationController {
//
//	private final String TOPIC = "";
//
//	@Autowired
//	AndroidPushNotificationsService androidPushNotificationsService;
//
//	@RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<String> send() throws JSONException {
//
//		JSONObject body = new JSONObject();
////		body.put("to", "/topics/" + TOPIC);
//		body.put("priority", "high");
//
//		JSONObject notification = new JSONObject();
//		notification.put("title", "");
//		notification.put("body", "");
//
//		body.put("notification", notification);
//
//		HttpEntity<String> request = new HttpEntity<String>(body.toString());
//
//		CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
//		CompletableFuture.allOf(pushNotification).join();
//
//		try {
//			String firebaseResponse = pushNotification.get();
//
//			return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
//	}
//}
