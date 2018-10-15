package miki.assignment.stockservice.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseDao {
	private String endPoint;
	private String dbName;
	private String username;
	private String password;

	public BaseDao(String endPoint, String dbName, String username, String password) {
		this.endPoint = endPoint;
		this.dbName = dbName;
		this.username = username;
		this.password = password;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Connection createConnection() throws SQLException, ClassNotFoundException {
		if (this.endPoint == null) {
			throw new SQLException("Missing db endpoint");
		}

		if (this.dbName == null) {
			throw new SQLException("Missing db name");
		}

		if (this.username == null) {
			throw new SQLException("Missing db username");
		}

		if (this.password == null) {
			throw new SQLException("Missing db password");
		}

		Class.forName("com.mysql.jdbc.Driver");

		Connection connection = DriverManager
				.getConnection("jdbc:mysql://" + endPoint + ":3306/" + dbName + "?&useSSL=false", username, password);

		return connection;
	}

	protected void closeResultSet(ResultSet resultSet) {
		try {
			if (resultSet != null) {
				resultSet.close();
			}

		} catch (SQLException sql) {

		}
	}

	protected void closeStatement(Statement statemenet) {
		try {
			if (statemenet != null) {
				statemenet.close();
			}

		} catch (SQLException sql) {

		}
	}

	protected void closeConnection(Connection conn) {
		try {
			if (conn != null) {
				conn.close();
			}

		} catch (SQLException sql) {

		}
	}

}
