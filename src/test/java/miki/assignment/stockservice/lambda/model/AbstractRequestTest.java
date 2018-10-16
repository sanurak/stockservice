package miki.assignment.stockservice.lambda.model;

import org.junit.Assert;
import org.junit.Test;

public class AbstractRequestTest {
	public static final String USERNAME = "USER01";
	public static final String PASSWORD = "PASS01";

	@Test
	public void testInitial() {
		@SuppressWarnings("rawtypes")
		AbstractRequest request = new AbstractRequest();

		Assert.assertNull(request.getUsername());
		Assert.assertNull(request.getPassword());
		Assert.assertNull(request.getRequestObject());

	}

	@Test
	public void testUsername() {
		@SuppressWarnings("rawtypes")
		AbstractRequest request = new AbstractRequest();

		request.setUsername(USERNAME);

		Assert.assertEquals(USERNAME, request.getUsername());
	}

	@Test
	public void testPassword() {
		@SuppressWarnings("rawtypes")
		AbstractRequest request = new AbstractRequest();

		request.setPassword(PASSWORD);

		Assert.assertEquals(PASSWORD, request.getPassword());
	}

	@Test
	public void testRequestObject() {
		@SuppressWarnings("rawtypes")
		AbstractRequest request = new AbstractRequest();
		request.setRequestObject(12);

		Assert.assertTrue(request.getRequestObject() instanceof Integer);

		request.setRequestObject(USERNAME);
		Assert.assertTrue(request.getRequestObject() instanceof String);
	}
}
