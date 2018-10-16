package miki.assignment.stockservice.lambda.model;

import org.junit.Assert;
import org.junit.Test;

public class AbstractResponseTest {
	public static final int SUCCESS_STATUS = 1;
	public static final int FAIL_STATUS = 0;

	public static final String SUCCESS_MESSAGE = "SUCCESS";
	public static final String FAIL_MESSAGE = "FAIL";

	@Test
	public void testInitial() {
		@SuppressWarnings("rawtypes")
		AbstractResponse response = new AbstractResponse();

		Assert.assertEquals(0, response.getStatus());
		Assert.assertNull(response.getMessage());
		Assert.assertNull(response.getResponseObject());

	}

	@Test
	public void testStatus() {
		@SuppressWarnings("rawtypes")
		AbstractResponse response = new AbstractResponse();

		response.setStatus(FAIL_STATUS);
		Assert.assertEquals(FAIL_STATUS, response.getStatus());

		response.setStatus(SUCCESS_STATUS);
		Assert.assertEquals(SUCCESS_STATUS, response.getStatus());
	}

	@Test
	public void testMessage() {
		@SuppressWarnings("rawtypes")
		AbstractResponse response = new AbstractResponse();

		response.setMessage(SUCCESS_MESSAGE);
		Assert.assertEquals(SUCCESS_MESSAGE, response.getMessage());

		response.setMessage(FAIL_MESSAGE);
		Assert.assertEquals(FAIL_MESSAGE, response.getMessage());

	}

	@Test
	public void testRequestObject() {
		@SuppressWarnings("rawtypes")
		AbstractResponse response = new AbstractResponse();
		response.setReponseObject(12);

		Assert.assertTrue(response.getResponseObject() instanceof Integer);

		response.setReponseObject(SUCCESS_MESSAGE);
		Assert.assertTrue(response.getResponseObject() instanceof String);
	}
}
