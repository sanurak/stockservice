package miki.assignment.stockservice.dao.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import miki.assignment.stockservice.model.Index;

public class IndexDaoImplTest {
	private static IndexDaoImpl indexDaoImpl = null;

	private static final String SET = "SET";

	private static final BigDecimal INITIAL_LAST = new BigDecimal(100);
	private static final BigDecimal UPDATE_LAST = new BigDecimal(200);

	@BeforeClass
	public static void initDao() throws IOException {
		indexDaoImpl = new IndexDaoImpl(DBConfiguration.ENDPOINT, DBConfiguration.DBNAME, DBConfiguration.USERNAME,
				DBConfiguration.PASSWORD);
	}

	@Test
	public void testGetTargetStockIndex() {
		try {
			Set<String> indexSet = indexDaoImpl.getTargetStockIndex();

			Assert.assertTrue(!indexSet.isEmpty());

		} catch (SQLException sql) {
			Assert.fail("Exception with db :" + sql.getMessage());
		}
	}

	@Test
	public void testAllIndexFunction() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.MILLISECOND, 0);
			Timestamp currentDate = new Timestamp(calendar.getTimeInMillis());

			Index index = new Index();
			index.setCode(SET);
			index.setLast(INITIAL_LAST);
			index.setReceiveDate(currentDate);

			Index existing = indexDaoImpl.getIndex(index);

			// New create data should not exist
			Assert.assertNull(existing);

			List<Index> indexList = new ArrayList<Index>();
			indexList.add(index);

			// This case will create index data
			indexDaoImpl.updateIndex(indexList);

			existing = indexDaoImpl.getIndex(index);

			// Index should exist
			Assert.assertNotNull(existing);
			Assert.assertEquals(SET, existing.getCode());
			Assert.assertTrue(INITIAL_LAST.doubleValue() == existing.getLast().doubleValue());
			Assert.assertEquals(currentDate, existing.getReceiveDate());

			// update last and call dao to update
			index.setLast(UPDATE_LAST);
			indexDaoImpl.updateIndex(indexList);

			existing = indexDaoImpl.getIndex(index);

			// Index should exist
			Assert.assertNotNull(existing);
			Assert.assertTrue(UPDATE_LAST.doubleValue() == existing.getLast().doubleValue());

			// check listIndexInDate function
			List<Index> indexInDateList = indexDaoImpl.listIndexInDate(SET, calendar.getTime());
			Assert.assertTrue(indexInDateList.size() > 0);

		} catch (SQLException sql) {
			Assert.fail("Exception with db :" + sql.getMessage());
		}
	}

}
