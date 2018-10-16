package miki.assignment.stockservice.dao.impl;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UserDaoImplTest {
	private static UserDaoImpl userDaoImpl = null;

	private static final String USERNAME = "user01";
	private static final String PASSWORD = "pass01";
	private static final String INVALID_USER = "user02";
	private static final String INVALID_PASSWORD = "pass02";

	@BeforeClass
	public static void initDao() throws IOException {
		userDaoImpl = new UserDaoImpl(DBConfiguration.ENDPOINT, DBConfiguration.DBNAME, DBConfiguration.USERNAME,
				DBConfiguration.PASSWORD);
	}

	@Test
	public void testValidateUser() {
		try {
			Assert.assertTrue(userDaoImpl.validateUser(USERNAME, PASSWORD));
			Assert.assertFalse(userDaoImpl.validateUser(USERNAME, INVALID_PASSWORD));
			Assert.assertFalse(userDaoImpl.validateUser(INVALID_USER, PASSWORD));

		} catch (SQLException e) {
			Assert.fail(e.getMessage());
		}
	}

}
