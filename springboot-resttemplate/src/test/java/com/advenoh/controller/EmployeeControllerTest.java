package com.advenoh.controller;

import com.advenoh.model.Address;
import com.advenoh.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest {
	final String BASE_URL = "http://localhost:8080";

	RestTemplate restTemplate = new RestTemplate();

	/**
	 * getEmployee() : 주어진 URL 주소로 HTTP GET 메서드로 객체로 결과를 반환받는다.
	 * getForEntity() : 주어진 URL 주소로 HTTP GET 메서드로 결과는 ResponseEntity로 반환받는다.
	 * exchange() : HTTP 헤더를 새로 만들 수 있고 어떤 HTTP 메서드도 사용가능하다.
	 * postForObject() : POST 요청을 보내고 객체로 결과를 반환받는다.
	 * postForEntity() : POST 요청을 보내고 결과로 ResponseEntity로 반환받는다
	 * postForLocation() : POST 요청을 보내고 결과로 헤더에 저장된 URI를 결과로 반환받는다
	 * headForHeaders() : 헤더의 모든 정보를 얻을 수 있으면 HTTP HEAD 메서드를 사용한다.
	 * delete() : 주어진 URL 주소로 HTTP DELETE 메서드를 실행한다.
	 * put() : 주어진 URL 주소로 HTTP PUT 메서드를 실행한다.
	 * execute():
	 */

	//GET
	@Test
	public void test_getForObject() {
		Employee employee = restTemplate.getForObject(BASE_URL + "/employee/{id}", Employee.class, 25);
		log.info("employee: {}", employee);
	}

	@Test
	public void test_getForEntity() {
		ResponseEntity<String> responseEntity = restTemplate.getForEntity(BASE_URL + "/employee/{id}", String.class, 25);
		log.info("statusCode: {}", responseEntity.getStatusCode());
		log.info("getBody: {}", responseEntity.getBody());
	}

	@Test
	public void test_getForEntity_여러_path_variables을_넘겨주는_경우() {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("name", "Frank Oh");
		params.add("country", "US");

		ResponseEntity<Employee> responseEntity = restTemplate.getForEntity(BASE_URL + "/employee/{name}/{country}", Employee.class, params);
		log.info("statusCode: {}", responseEntity.getStatusCode());
		log.info("getBody: {}", responseEntity.getBody());
	}

	//POST
	@Test
	public void testPostForObject_해더_포함해서_보내지_않기() {
		Employee newEmployee = Employee.builder()
				.name("Frank")
				.address(Address.builder()
						.country("US")
						.build())
				.build();

		Employee employee = restTemplate.postForObject(BASE_URL + "/employee", newEmployee, Employee.class);
		log.info("employee: {}", employee);
	}

	@Test
	public void testPostForObject_해데_포함해서_보내기() {
		Employee newEmployee = Employee.builder()
				.name("Frank")
				.address(Address.builder()
						.country("US")
						.build())
				.build();

		HttpHeaders headers = new HttpHeaders();
		headers.set("headerTest", "headerValue");

		HttpEntity<Employee> request = new HttpEntity<>(newEmployee, headers);

		Employee employee = restTemplate.postForObject(BASE_URL + "/employee", request, Employee.class);
		log.info("employee: {}", employee);
	}

	@Test
	public void testPostForEntity_스트링값으로_받기() {
		Employee newEmployee = Employee.builder()
				.name("Frank")
				.address(Address.builder()
						.country("US")
						.build())
				.build();

		ResponseEntity<String> responseEntity = restTemplate.postForEntity(BASE_URL + "/employee", newEmployee, String.class);
		log.info("statusCode: {}", responseEntity.getStatusCode());
		log.info("getBody: {}", responseEntity.getBody());
	}

	@Test
	public void testPostForLocation() {
		Employee newEmployee = Employee.builder()
				.name("Frank")
				.address(Address.builder()
						.country("US")
						.build())
				.build();

		HttpEntity<Employee> request = new HttpEntity<>(newEmployee);

		URI location = restTemplate.postForLocation(BASE_URL + "/employee/location", request);
		log.info("location: {}", location);
	}

	@Test
	public void testDelete() {
		Map<String, String> params = new HashMap<>();
		params.put("name", "Frank");
		restTemplate.delete(BASE_URL + "/employee/{name}", params);
	}

	@Test
	public void testPut() {
		Map<String, String> params = new HashMap<>();
		params.put("name", "Frank");
		Address address = Address.builder()
				.city("Columbus")
				.country("US")
				.build();
		restTemplate.put(BASE_URL + "/employee/{name}", address, params);
	}

	@Test
	public void test_exchange() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>("Hello World!", headers);
		log.info("request: {}", request);

		ResponseEntity<Employee> empEntity = restTemplate.exchange(BASE_URL + "/exchange/employee/{id}", HttpMethod.GET, request, Employee.class, 50);
		log.info("empEntity: {}", empEntity);
	}

	//	@Test
	//	public void testHeadForHeaders() {
	//		String url = "http://localhost:8080/data/fetch/{id}";
	//		HttpHeaders httpHeaders = restTemplate.headForHeaders(url, 100);
	//		System.out.println(httpHeaders.getDate());
	//		System.out.println(httpHeaders.getContentType());
	//	}



	@Test
	public void test_get_lists_of_objects() {
		ResponseEntity<List<Employee>> responseEntity = restTemplate
				.exchange(BASE_URL + "/employees", HttpMethod.GET, null, new ParameterizedTypeReference<List<Employee>>() {
				});
		log.info("responseEntity: {}", responseEntity);
	}
}