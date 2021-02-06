package com.exchangerate.service;

import static com.exchangerate.constants.ApplicationConstants.BASE_CURRENCY;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.exchangerate.exceptions.CurrencyNotSupported;
import com.exchangerate.repository.CurrencyRepository;

@Service
public class ExchangeRateService {

	private CurrencyRepository currencyRepository;
	@Value("${chatUrl}")
	private String chatUrl;

	@Autowired
	public ExchangeRateService(CurrencyRepository currencyRepository) {
		this.currencyRepository = currencyRepository;
	}

	public List<String> getAllCurrencis() {
		// TODO Sort based on name to make result consistent and readable
		return currencyRepository.getAllCurrencies();
	}

	public Double referenceRate(String currency) {
		if (isBaseCurrency(currency)) {
			return 1d;
		}
		Double rate = currencyRepository.getReferenceRate(currency);
		if (rate == null) {
			throw new CurrencyNotSupported();
		}
		return rate;
	}

	public Double getExchangeRate(String from, String to) {
		if (isBaseCurrency(from)) {
			return referenceRate(to);
		} else if (isBaseCurrency(to)) {
			return 1 / referenceRate(from);
		} else {
			return referenceRate(to) / referenceRate(from);
		}

	}

	public Double convertCurrency(String from, String to, int amount) {
		Double exchangeRate = getExchangeRate(from, to);
		return exchangeRate * amount;
	}

	public URI getChatUrl(String from, String to) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(chatUrl + "{from}/{to}").buildAndExpand(from, to);
		return uri.toUri();

	}
	
	public void refreshData(Map<String, Double> currencyMap) {
		currencyRepository.refreshData(currencyMap);
	}

	private boolean isBaseCurrency(String currency) {
		return BASE_CURRENCY.equals(currency);
	}
	
	

}
