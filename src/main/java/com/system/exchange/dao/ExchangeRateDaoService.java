package com.system.exchange.dao;

import com.system.exchange.entity.ExchangeRate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("exchangeRateDB")
public class ExchangeRateDaoService implements ExchangeRateDao {
	private static List<ExchangeRate> DB = new ArrayList<>();

	@Override
	public void addExchangeRate(ExchangeRate exchangeRate) {
		DB.add(exchangeRate);
	}

	@Override
	public ExchangeRate getExchangeRateByCurrencyName(String currencyName) {
		return DB.stream().filter(exchangeRate -> exchangeRate.getCurrencyName().equals(currencyName))
				.findFirst().orElse(null);
	}

	@Override
	public List<ExchangeRate> getExchangeRates() {
		return DB;
	}

	@Override
	public void clearExchangeRates() {
		DB.clear();
	}
}
