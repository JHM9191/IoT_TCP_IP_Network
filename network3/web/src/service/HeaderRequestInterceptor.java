//package service;
//
//import java.io.IOException;
//
//import org.springframework.http.HttpRequest;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.http.client.support.HttpRequestWrapper;
//
//public class HeaderRequestInterceptor implements ClientHttpRequestInterceptor {
//
//	String headerName;
//	String headerValue;
//
//	public HeaderRequestInterceptor(String headerName, String headerValue) {
//		this.headerName = headerName;
//		this.headerValue = headerValue;
//	}
//
//	@Override
//	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
//			throws IOException {
//
//		HttpRequest wrapper = new HttpRequestWrapper(request);
//		wrapper.getHeaders().set(headerName, headerValue);
//		return execution.execute(wrapper, body);
//	}
//
//}
