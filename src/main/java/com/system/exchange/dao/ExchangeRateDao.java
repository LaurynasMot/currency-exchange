package com.system.exchange.dao;

import com.system.exchange.entity.ExchangeRate;

import java.util.List;

public interface ExchangeRateDao {

	void addExchangeRate(ExchangeRate exchangeRate);

	ExchangeRate getExchangeRateByCurrencyName(String currencyName);

	List<ExchangeRate> getExchangeRates();

	void clearExchangeRates();
}
