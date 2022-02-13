package com.system.exchange.request;

import java.math.BigDecimal;

public class ExchangeRequest {

	private BigDecimal currencyQuantity;
	private String currencyFrom;
	private String currencyTo;

	public ExchangeRequest(BigDecimal currencyQuantity, String currencyFrom, String currencyTo) {
		this.currencyQuantity = currencyQuantity;
		this.currencyFrom = currencyFrom;
		this.currencyTo = currencyTo;
	}

	public BigDecimal getCurrencyQuantity() {
		return currencyQuantity;
	}

	public void setCurrencyQuantity(BigDecimal currencyQuantity) {
		this.currencyQuantity = currencyQuantity;
	}

	public String getCurrencyFrom() {
		return currencyFrom;
	}

	public void setCurrencyFrom(String currencyFrom) {
		this.currencyFrom = currencyFrom;
	}

	public String getCurrencyTo() {
		return currencyTo;
	}

	public void setCurrencyTo(String currencyTo) {
		this.currencyTo = currencyTo;
	}
}
