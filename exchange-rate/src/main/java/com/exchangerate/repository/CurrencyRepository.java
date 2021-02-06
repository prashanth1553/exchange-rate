package com.exchangerate.repository;

import java.util.List;
import java.util.Map;

public interface CurrencyRepository {

	Double getReferenceRate(String currency);

	List<String> getAllCurrencies();

	void putCurrency(String symbol, double value);

	boolean isCurrencySupported(String currency);

	void refreshData(Map<String, Double> currencyMap);

}
