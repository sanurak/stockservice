package miki.assignment.stockservice.dao;

import java.sql.SQLException;

public interface UserDao {
	public boolean validateUser(String username, String password) throws SQLException;
}
