package com.system.exchange.service;

import org.json.JSONObject;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class OnlineExchangeRatesService {

	public JSONObject getOnlineCurrencyRates(String baseCurrency, String apiKey) throws IOException {
		URL url = new URL("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + baseCurrency);
		HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
		httpURLConnection.setRequestMethod("GET");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		String inputLine;
		StringBuilder currencyRateBuffer = new StringBuilder();

		while ((inputLine = bufferedReader.readLine()) != null) {
			currencyRateBuffer.append(inputLine);
		}

		bufferedReader.close();
		httpURLConnection.disconnect();

		return new JSONObject(currencyRateBuffer.toString());
	}

}
