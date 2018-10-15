package miki.assignment.stockservice.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import miki.assignment.stockservice.dao.BaseDao;
import miki.assignment.stockservice.dao.IndexDao;
import miki.assignment.stockservice.model.Index;

public class IndexDaoImpl extends BaseDao implements IndexDao {

	public IndexDaoImpl(String endPoint, String dbName, String username, String password) {
		super(endPoint, dbName, username, password);
	}

	@Override
	public Set<String> getTargetStockIndex() throws SQLException {
		Set<String> targetStockIndex = new HashSet<String>();

		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			conn = createConnection();

			String sql = "SELECT index_name FROM index_list";

			statement = conn.createStatement();

			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				String indexName = resultSet.getString(1);

				targetStockIndex.add(indexName);
			}

		} catch (ClassNotFoundException classNotFound) {
			throw new SQLException("Cannot load mysql driver");

		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(conn);
		}

		return targetStockIndex;
	}

	@Override
	public void updateIndex(List<Index> indexList) throws SQLException {
		Connection conn = null;
		PreparedStatement statementInsert = null;
		PreparedStatement statementUpdate = null;

		try {
			conn = createConnection();
			conn.setAutoCommit(false);

			String sqlInsert = "INSERT INTO index_table (index_name, receive_date, last) VALUES (?,?,?)";
			String sqlUpdate = "UPDATE index_table SET last=? WHERE index_name=? AND receive_date=?";

			statementInsert = conn.prepareStatement(sqlInsert);
			statementUpdate = conn.prepareStatement(sqlUpdate);

			for (Index index : indexList) {
				Index existing = getIndex(index);

				if (existing != null) {
					statementUpdate.setBigDecimal(1, index.getLast());
					statementUpdate.setString(2, index.getCode());
					statementUpdate.setTimestamp(3, index.getReceiveDate());

					statementUpdate.addBatch();
				} else {
					statementInsert.setString(1, index.getCode());
					statementInsert.setTimestamp(2, index.getReceiveDate());
					statementInsert.setBigDecimal(3, index.getLast());

					statementInsert.addBatch();
				}
			}

			statementUpdate.executeBatch();
			statementInsert.executeBatch();

			conn.commit();

		} catch (ClassNotFoundException classNotFound) {
			throw new SQLException("Cannot load mysql driver");

		} finally {
			closeStatement(statementUpdate);
			closeStatement(statementInsert);
			closeConnection(conn);
		}
	}

	@Override
	public Index getIndex(Index index) throws SQLException {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		Index existing = null;
		try {
			conn = createConnection();

			String sql = "SELECT index_name, last, receive_date FROM index_table WHERE index_name=? AND receive_date=?";

			statement = conn.prepareStatement(sql);
			statement.setString(1, index.getCode());
			statement.setTimestamp(2, index.getReceiveDate());

			resultSet = statement.executeQuery();

			if (resultSet.next()) {
				existing = new Index();
				existing.setCode(resultSet.getString(1));
				existing.setLast(resultSet.getBigDecimal(2));
				existing.setReceiveDate(resultSet.getTimestamp(3));

			}

		} catch (ClassNotFoundException classNotFound) {
			throw new SQLException("Cannot load mysql driver");

		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(conn);
		}

		return existing;
	}

	@Override
	public List<Index> listIndexInDate(String indexCode, Date date) throws SQLException {
		Connection conn = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;

		List<Index> resultList = new ArrayList<Index>();
		try {
			conn = createConnection();

			String sql = "SELECT index_name, last, receive_date FROM index_table WHERE index_name=? AND receive_date BETWEEN CAST(? AS DATETIME) AND CAST(? AS DATETIME)";

			statement = conn.prepareStatement(sql);
			statement.setString(1, indexCode);
			setConditionForDateRange(statement, date);

			resultSet = statement.executeQuery();

			while (resultSet.next()) {
				Index index = new Index();
				index.setCode(resultSet.getString(1));
				index.setLast(resultSet.getBigDecimal(2));
				index.setReceiveDate(resultSet.getTimestamp(3));

				resultList.add(index);
			}

		} catch (ClassNotFoundException classNotFound) {
			throw new SQLException("Cannot load mysql driver");

		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
			closeConnection(conn);
		}

		return resultList;
	}

	private void setConditionForDateRange(PreparedStatement statement, Date date) throws SQLException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp startTimeStamp = new Timestamp(calendar.getTimeInMillis());

		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		Timestamp endTimeStamp = new Timestamp(calendar.getTimeInMillis());

		statement.setTimestamp(2, startTimeStamp);
		statement.setTimestamp(3, endTimeStamp);
	}
}
