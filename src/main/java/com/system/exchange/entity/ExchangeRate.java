package com.system.exchange.entity;

import java.math.BigDecimal;

public class ExchangeRate {
	private String currencyName;
	private BigDecimal exchangeRate;

	public ExchangeRate(String currencyName, BigDecimal exchangeRate) {
		this.currencyName = currencyName;
		this.exchangeRate = exchangeRate;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public BigDecimal getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(BigDecimal exchangeRate) {
		this.exchangeRate = exchangeRate;
	}
}
