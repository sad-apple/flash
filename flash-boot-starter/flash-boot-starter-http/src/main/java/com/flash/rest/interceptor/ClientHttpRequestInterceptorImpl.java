
package com.flash.rest.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * restTemplate 请求响应信息拦截器
 */
@Slf4j
public class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
		HttpHeaders headers = request.getHeaders();
		long start = System.currentTimeMillis();

		ClientHttpResponse response = null;
		try {
			response = execution.execute(request, body);
		} finally {
			String respText = response == null ? "" : response.getStatusText();
			int status = response == null ? 400 : response.getStatusCode().value();
			log.info("请求地址: [{}], 请求头信息: [{}], 请求参数: [{}] => 请求状态: [{}], 返回结果: [{}]. 请求花费: [{}]毫秒.",
					 request.getURI(),
					new ObjectMapper().writeValueAsString(headers.entrySet()),
					new String(body),
					status,
					respText,
					System.currentTimeMillis() - start);
		}
		return response;
	}

}
