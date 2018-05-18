package com.despegar.sparkjava.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.ClassRule;
import org.junit.Test;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpResponse;

import spark.servlet.SparkApplication;

/**
 * The test class
 * @author fwasy
 *
 */
public class TestControllerTest {

	public static class TestContollerTestSparkApplication implements SparkApplication {
		@Override
		public void init() {
			System.out.println("Test application initialized");
			new TestController();
		}

		@Override
		public void destroy() {
			System.out.println("Test application stopped");
		}
		
		
	}
	
	@ClassRule
	public static SparkServer<TestContollerTestSparkApplication> testServer = new SparkServer<>(TestControllerTest.TestContollerTestSparkApplication.class, 4567);
	
	@Test
	public void test() throws Exception {
		GetMethod get = testServer.get("/test", false);
		get.addHeader("Test-Header", "test");
		HttpResponse httpResponse = testServer.execute(get);
		assertEquals(200, httpResponse.code());
		assertEquals("This works!", new String(httpResponse.body()));
		assertNotNull(testServer.getApplication());
	}

	@Test
	public void testDefaultFollowRedirect() throws Exception {
		SparkServer<TestControllerTest.TestContollerTestSparkApplication> serverDoFollow = new SparkServer<>(TestControllerTest.TestContollerTestSparkApplication.class, 4567, true);
		HttpResponse httpResponse = testServer.execute(serverDoFollow.get("/redirect"));
		assertEquals(200, httpResponse.code());
		assertEquals("This works!", new String(httpResponse.body()));

		SparkServer<TestControllerTest.TestContollerTestSparkApplication> serverDontFollow = new SparkServer<>(TestControllerTest.TestContollerTestSparkApplication.class, 4567, false);
		httpResponse = testServer.execute(serverDontFollow.get("/redirect"));
		assertEquals(302, httpResponse.code());
	}
	
}