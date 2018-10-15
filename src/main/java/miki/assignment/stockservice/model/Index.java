package miki.assignment.stockservice.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Index {
	private String code;
	private BigDecimal last;
	private Timestamp receiveDate;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getLast() {
		return last;
	}

	public void setLast(BigDecimal last) {
		this.last = last;
	}

	public Timestamp getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Timestamp receiveDate) {
		this.receiveDate = receiveDate;
	}

}
