package miki.assignment.stockservice.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import miki.assignment.stockservice.model.Index;

public interface IndexDao {
	public Set<String> getTargetStockIndex() throws SQLException;

	public void updateIndex(List<Index> indexList) throws SQLException;

	public Index getIndex(Index index) throws SQLException;

	public List<Index> listIndexInDate(String indexCode, Date date) throws SQLException;
}
