package miki.assignment.stockservice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import miki.assignment.stockservice.dao.BaseDao;
import miki.assignment.stockservice.dao.UserDao;

public class UserDaoImpl extends BaseDao implements UserDao {

	public UserDaoImpl(String endPoint, String dbName, String username, String password) {
		super(endPoint, dbName, username, password);
	}

	@Override
	public boolean validateUser(String username, String password) throws SQLException {
		boolean found = false;

		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		try {
			conn = createConnection();

			String sql = "SELECT * FROM user WHERE login=? AND password=?";

			statement = conn.prepareStatement(sql);
			statement.setString(1, username);
			statement.setString(2, password);

			resultSet = statement.executeQuery();

			found = resultSet.next();

		} catch (ClassNotFoundException classNotFound) {
			throw new SQLException("Cannot load mysql driver");

		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(conn);
		}

		return found;
	}

}
