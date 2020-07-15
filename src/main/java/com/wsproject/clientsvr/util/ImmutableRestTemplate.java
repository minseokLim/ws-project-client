package com.wsproject.clientsvr.util;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

/**
 * RestTemplate Composition 구성을 위한 전달(Forwarding) 클래스<br>
 * 상속을 통한 재사용도 가능하나 이 자체로도 사용가능<br>
 * 이 클래스의 생성 의도는 Bean으로 생성되는 RestTemplate을 불변객체로 만들기 위함 <br>
 * @author mslim
 */
public class ImmutableRestTemplate implements RestOperations {
	
	private RestOperations restTemplate;
	
	public ImmutableRestTemplate(RestOperations restTemplate) {
		this.restTemplate = restTemplate;
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) throws RestClientException {
		return restTemplate.getForObject(url, responseType, uriVariables);
	}

	@Override
	public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		return restTemplate.getForObject(url, responseType, uriVariables);
	}

	@Override
	public <T> T getForObject(URI url, Class<T> responseType) throws RestClientException {
		return restTemplate.getForObject(url, responseType);
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		return restTemplate.getForEntity(url, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		return restTemplate.getForEntity(url, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) throws RestClientException {
		return restTemplate.getForEntity(url, responseType);
	}

	@Override
	public HttpHeaders headForHeaders(String url, Object... uriVariables) throws RestClientException {
		return restTemplate.headForHeaders(url, uriVariables);
	}

	@Override
	public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.headForHeaders(url, uriVariables);
	}

	@Override
	public HttpHeaders headForHeaders(URI url) throws RestClientException {
		return restTemplate.headForHeaders(url);
	}

	@Override
	public URI postForLocation(String url, Object request, Object... uriVariables) throws RestClientException {
		return restTemplate.postForLocation(url, request, uriVariables);
	}

	@Override
	public URI postForLocation(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.postForLocation(url, request, uriVariables);
	}

	@Override
	public URI postForLocation(URI url, Object request) throws RestClientException {
		return restTemplate.postForLocation(url, request);
	}

	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		return restTemplate.postForObject(url, request, responseType, uriVariables);
	}

	@Override
	public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		return restTemplate.postForObject(url, request, responseType, uriVariables);
	}

	@Override
	public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
		return restTemplate.postForObject(url, request, responseType);
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
			Object... uriVariables) throws RestClientException {
		return restTemplate.postForEntity(url, request, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType,
			Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.postForEntity(url, request, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType)
			throws RestClientException {
		return restTemplate.postForEntity(url, request, responseType);
	}

	@Override
	public void put(String url, Object request, Object... uriVariables) throws RestClientException {
		restTemplate.put(url, request, uriVariables);
	}

	@Override
	public void put(String url, Object request, Map<String, ?> uriVariables) throws RestClientException {
		restTemplate.put(url, request, uriVariables);
	}

	@Override
	public void put(URI url, Object request) throws RestClientException {
		restTemplate.put(url, request);
	}

	@Override
	public <T> T patchForObject(String url, Object request, Class<T> responseType, Object... uriVariables)
			throws RestClientException {
		return restTemplate.patchForObject(url, request, responseType, uriVariables);
	}

	@Override
	public <T> T patchForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables)
			throws RestClientException {
		return restTemplate.patchForObject(url, request, responseType, uriVariables);
	}

	@Override
	public <T> T patchForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
		return restTemplate.patchForObject(url, request, responseType);
	}

	@Override
	public void delete(String url, Object... uriVariables) throws RestClientException {
		restTemplate.delete(url, uriVariables);
	}

	@Override
	public void delete(String url, Map<String, ?> uriVariables) throws RestClientException {
		restTemplate.delete(url, uriVariables);
	}

	@Override
	public void delete(URI url) throws RestClientException {
		restTemplate.delete(url);
	}

	@Override
	public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) throws RestClientException {
		return restTemplate.optionsForAllow(url, uriVariables);
	}

	@Override
	public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.optionsForAllow(url, uriVariables);
	}

	@Override
	public Set<HttpMethod> optionsForAllow(URI url) throws RestClientException {
		return restTemplate.optionsForAllow(url);
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Object... uriVariables) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
			Class<T> responseType) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Object... uriVariables) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
	}

	@Override
	public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity,
			ParameterizedTypeReference<T> responseType) throws RestClientException {
		return restTemplate.exchange(url, method, requestEntity, responseType);
	}

	@Override
	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType)
			throws RestClientException {
		return restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType)
			throws RestClientException {
		return restTemplate.exchange(requestEntity, responseType);
	}

	@Override
	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Object... uriVariables) throws RestClientException {
		return restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables);
	}

	@Override
	public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) throws RestClientException {
		return restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables);
	}

	@Override
	public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {
		return restTemplate.execute(url, method, requestCallback, responseExtractor);
	}
}
