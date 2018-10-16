package miki.assignment.stockservice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import org.junit.Assert;
import org.junit.Test;

public class IndexTest {

	public static final String CODE = "CODE1";
	public static final BigDecimal LAST = new BigDecimal("1200.21");
	public static final Timestamp RECEIVE_DATE = new Timestamp(Calendar.getInstance().getTimeInMillis());

	@Test
	public void testInitialIndex() {
		Index index = new Index();

		Assert.assertNull(index.getCode());
		Assert.assertNull(index.getLast());
		Assert.assertNull(index.getReceiveDate());
	}

	@Test
	public void testIndexCode() {
		Index index = new Index();
		index.setCode(CODE);

		Assert.assertEquals(CODE, index.getCode());
	}

	@Test
	public void testIndexLast() {
		Index index = new Index();
		index.setLast(LAST);

		Assert.assertEquals(LAST, index.getLast());
	}

	@Test
	public void testIndexReceiveDate() {
		Index index = new Index();
		index.setReceiveDate(RECEIVE_DATE);

		Assert.assertEquals(RECEIVE_DATE, index.getReceiveDate());
	}

}
