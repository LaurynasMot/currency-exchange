package com.system.exchange;

import com.system.exchange.dao.ExchangeRateDao;
import com.system.exchange.dao.ExchangeRateDaoService;
import com.system.exchange.entity.ExchangeRate;
import com.system.exchange.request.ExchangeRequest;
import com.system.exchange.service.ExchangeService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.*;

@SpringBootTest
public class ExchangeServiceTests {

	private static ExchangeRateDao exchangeRateDao = new ExchangeRateDaoService();

	private ExchangeService exchangeService = new ExchangeService(exchangeRateDao);

	@BeforeClass
	public static void populateDb() {
		if (exchangeRateDao.getExchangeRates().size() == 0) {
			exchangeRateDao.addExchangeRate(new ExchangeRate("EUR", new BigDecimal("1")));
			exchangeRateDao.addExchangeRate(new ExchangeRate("USD", new BigDecimal("0.89")));
			exchangeRateDao.addExchangeRate(new ExchangeRate("GBP", new BigDecimal("1.12")));
		}
	}

	@Test
	public void testGetExchangedCurrency() {
		assertEquals(1, exchangeService
				.getExchangedCurrency(new ExchangeRequest(BigDecimal.valueOf(50), "EUR", "USD"))
				.compareTo(new BigDecimal(50 / 0.89, new MathContext(18, RoundingMode.HALF_UP))));

		assertEquals(1, exchangeService
				.getExchangedCurrency(new ExchangeRequest(BigDecimal.valueOf(30), "USD", "EUR"))
				.compareTo(new BigDecimal(30 * 0.89, new MathContext(18, RoundingMode.HALF_UP))));

		assertEquals(1, exchangeService
				.getExchangedCurrency(new ExchangeRequest(BigDecimal.valueOf(100), "USD", "GBP"))
				.compareTo(new BigDecimal(0.89 * 100 / 1.12, new MathContext(18, RoundingMode.HALF_UP))));
	}

	@Test
	public void testUploadExchangeRates() {
		try {
			byte[] content = Files.readAllBytes(Paths.get("src/test/java/com/system/exchange/testData/exchangeRates.csv"));
			MultipartFile exchangeRatesFile =
					new MockMultipartFile("exchangeRates.csv", "exchangeRates.csv", "text/plain", content);
			exchangeService.uploadExchangeRates(exchangeRatesFile);
			assertEquals(BigDecimal.valueOf(11.11), exchangeRateDao.getExchangeRateByCurrencyName("TEST").getExchangeRate());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testClearDb() {
		exchangeRateDao.addExchangeRate(new ExchangeRate("EUR", new BigDecimal("1")));
		exchangeService.clearExchangeRates();
		assertTrue(exchangeRateDao.getExchangeRates().isEmpty());
	}
}
