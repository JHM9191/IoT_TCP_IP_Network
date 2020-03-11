//package service;
//
//import java.util.ArrayList;
//import java.util.concurrent.CompletableFuture;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class AndroidPushNotificationsService {
//
//	private static final String FIREBASE_SERVER_KEY = "AAAAVdP8m2Q:APA91bEBunCsGlFxxvUpSebrMvCsChSic-hCVKSwRipRB84Whar5gJNafydQc_PQSP6JLfflxeynTJ8zHO2ZJM2M_WZcrZPYIHMYpPgqah7xS7-wpU-ES5iG3RCYnGdkp6X_Eu5VboJ_";
//	private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";
//
//	public CompletableFuture<String> send(HttpEntity<String> entity) {
//		RestTemplate restTemplate = new RestTemplate();
//
//		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
//		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
//		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
//		restTemplate.setInterceptors(interceptors);
//
//		String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);
//
//		return CompletableFuture.completedFuture(firebaseResponse);
//	}
//}
