package com.system.exchange.service;

import com.system.exchange.dao.ExchangeRateDao;
import com.system.exchange.entity.ExchangeRate;
import com.system.exchange.request.ExchangeRequest;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

@Service("exchangeService")
public class ExchangeService {

	@Value("${baseCurrency}")
	private String baseCurrency;

	@Value("${apiKey}")
	private String apiKey;

	private final ExchangeRateDao exchangeRateDao;

	@Autowired
	private OnlineExchangeRatesService onlineExchangeRatesService;

	@Autowired
	public ExchangeService(@Qualifier("exchangeRateDB") ExchangeRateDao exchangeRateDao) {
		this.exchangeRateDao = exchangeRateDao;
	}

	public int uploadExchangeRates(MultipartFile exchangeInfoFile) {
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(exchangeInfoFile.getInputStream(), StandardCharsets.UTF_8));
			CSVParser csvParser = new CSVParser(bufferedReader, CSVFormat.DEFAULT);
			Iterable<CSVRecord> csvRecords = csvParser.getRecords();
			for (CSVRecord csvRecord : csvRecords) {
				ExchangeRate exchangeRate = new ExchangeRate(csvRecord.get(0), new BigDecimal(csvRecord.get(1)));
				exchangeRateDao.addExchangeRate(exchangeRate);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}

	public BigDecimal getExchangedCurrency(ExchangeRequest exchangeRequest) {
		if (exchangeRequest.getCurrencyQuantity().signum() <= 0) {
			return new BigDecimal(-1);
		} else if (exchangeRateDao.getExchangeRateByCurrencyName(exchangeRequest.getCurrencyFrom()) == null
				|| exchangeRateDao.getExchangeRateByCurrencyName(exchangeRequest.getCurrencyTo()) == null) {
			return new BigDecimal(-2);
		} else if (exchangeRequest.getCurrencyFrom().equals(exchangeRequest.getCurrencyTo())) {
			return exchangeRequest.getCurrencyQuantity();
		} else if (exchangeRequest.getCurrencyTo().equals(baseCurrency)) {
			return convertToBaseCurrency(exchangeRequest);
		} else if (exchangeRequest.getCurrencyFrom().equals(baseCurrency)) {
			return convertFromBaseCurrency(exchangeRequest);
		} else {
			return convertToBaseCurrency(exchangeRequest)
					.divide(exchangeRateDao.getExchangeRateByCurrencyName(exchangeRequest.getCurrencyTo()).getExchangeRate(),
							18, RoundingMode.HALF_UP);
		}
	}

	public BigDecimal convertToBaseCurrency(ExchangeRequest exchangeRequest) {
		return exchangeRequest.getCurrencyQuantity()
				.multiply(exchangeRateDao.getExchangeRateByCurrencyName(exchangeRequest
						.getCurrencyFrom()).getExchangeRate());
	}

	public BigDecimal convertFromBaseCurrency(ExchangeRequest exchangeRequest) {
		return exchangeRequest.getCurrencyQuantity()
				.divide(exchangeRateDao.getExchangeRateByCurrencyName(exchangeRequest
						.getCurrencyTo()).getExchangeRate(), 18, RoundingMode.HALF_UP);
	}

	public void clearExchangeRates() {
		exchangeRateDao.clearExchangeRates();
	}

	public List<ExchangeRate> getExchangeRates() {
		return exchangeRateDao.getExchangeRates();
	}

	public void uploadOnlineExchangeRates() throws IOException {
		JSONObject exchangeRatesJson = onlineExchangeRatesService.getOnlineCurrencyRates(baseCurrency, apiKey)
				.getJSONObject("conversion_rates");
		Iterator<String> iterator = exchangeRatesJson.keys();
		while (iterator.hasNext()) {
			String key = iterator.next();
			exchangeRateDao.addExchangeRate(new ExchangeRate(key,
					BigDecimal.valueOf(1 / Double.parseDouble(exchangeRatesJson.get(key).toString()))));
		}
	}
}
