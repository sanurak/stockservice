package miki.assignment.stockservice.view;

import java.math.BigDecimal;

import org.junit.Assert;
import org.junit.Test;

public class IndexViewTest {
	public static final String CODE = "CODE1";
	public static final BigDecimal LAST = new BigDecimal("1200.21");
	public static final String RECEIVE_DATE = "2018-10-16  12:59";

	@Test
	public void testInitialIndex() {
		IndexView indexView = new IndexView();

		Assert.assertNull(indexView.getCode());
		Assert.assertNull(indexView.getLast());
		Assert.assertNull(indexView.getReceiveDate());
	}

	@Test
	public void testIndexCode() {
		IndexView indexView = new IndexView();
		indexView.setCode(CODE);

		Assert.assertEquals(CODE, indexView.getCode());
	}

	@Test
	public void testIndexLast() {
		IndexView indexView = new IndexView();
		indexView.setLast(LAST);

		Assert.assertEquals(LAST, indexView.getLast());
	}

	@Test
	public void testIndexReceiveDate() {
		IndexView indexView = new IndexView();
		indexView.setReceiveDate(RECEIVE_DATE);

		Assert.assertEquals(RECEIVE_DATE, indexView.getReceiveDate());
	}
}
