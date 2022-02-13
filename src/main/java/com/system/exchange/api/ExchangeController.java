package com.system.exchange.api;

import com.system.exchange.entity.ExchangeRate;
import com.system.exchange.request.ExchangeRequest;
import com.system.exchange.service.ExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
	private final ExchangeService exchangeService;

	public ExchangeController(ExchangeService exchangeService) {
		this.exchangeService = exchangeService;
	}

	@PostMapping("rates")
	public void uploadExchangeRatesCsv(@RequestParam(value = "file") MultipartFile exchangeInfoFile) {
		if (exchangeService.uploadExchangeRates(exchangeInfoFile) == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file is empty or of wrong format");
		}
	}

	@PostMapping("ratesOnline")
	public void uploadOnlineExchangeRates() throws IOException {
		exchangeService.uploadOnlineExchangeRates();
	}

	@GetMapping("rates")
	public List<ExchangeRate> getExchangeRates() {
		return exchangeService.getExchangeRates();
	}

	@PostMapping("clear")
	public void clearExchangeRates() {
		exchangeService.clearExchangeRates();
	}

	@GetMapping("convert")
	public BigDecimal getExchangedCurrency(@RequestBody ExchangeRequest exchangeRequest) {
		BigDecimal currencyResult = exchangeService.getExchangedCurrency(exchangeRequest);
		if (currencyResult.equals(BigDecimal.valueOf(-1))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The quantity can not be less or equal to 0");
		} else if (currencyResult.equals(BigDecimal.valueOf(-2))) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The conversion rates of these currencies are not available");
		} else return exchangeService.getExchangedCurrency(exchangeRequest);
	}
}
