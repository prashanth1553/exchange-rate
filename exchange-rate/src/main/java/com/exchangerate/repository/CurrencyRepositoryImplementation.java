package com.exchangerate.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class CurrencyRepositoryImplementation implements CurrencyRepository {

	private Map<String, Double> currencyMap = new HashMap<>();

	public CurrencyRepositoryImplementation() {
	}

	@Override
	public Double getReferenceRate(String currency) {
		return currencyMap.get(currency);
	}

	@Override
	public List<String> getAllCurrencies() {
		return Arrays.asList(currencyMap.keySet().toArray(new String[0]));
	}

	@Override
	public void putCurrency(String symbol, double value) {
		currencyMap.put(symbol, value);
	}

	@Override
	public boolean isCurrencySupported(String currency) {
		return currencyMap.containsKey(currency);
	}

	@Override
	public void refreshData(Map<String, Double> currencyMap) {
		this.currencyMap = currencyMap;
	}

}