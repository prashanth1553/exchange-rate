package com.exchangerate.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exchangerate.service.ExchangeRateService;
import com.exchangerate.service.MetricService;

@RestController
@RequestMapping("/exchange-rate")
public class ExchangeRateController {

	private ExchangeRateService exchangeRateService;
	private MetricService metricService;

	@Autowired
	public ExchangeRateController(ExchangeRateService exchangeRateService, MetricService metricService) {
		this.exchangeRateService = exchangeRateService;
		this.metricService = metricService;
	}

	@RequestMapping("/currencies")
	public List<String> currencies() {
		return exchangeRateService.getAllCurrencis();
	}

	@RequestMapping("/currencies_req_count")
	public Integer currenciesReqCount() {
		return metricService.getCount();
	}

	@RequestMapping("/ecb-reference-rate/{currency}")
	public Double ecbReferenceRate(@PathVariable String currency) {
		return exchangeRateService.referenceRate(currency);
	}

	@RequestMapping("/{from}/{to}")
	public Double exchangeRate(@PathVariable String from, @PathVariable String to) {
		return exchangeRateService.getExchangeRate(from, to);
	}

	@RequestMapping("/convert/{from}/{to}/{amount}")
	public Double convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable int amount) {
		return exchangeRateService.convertCurrency(from, to, amount);
	}

	@GetMapping(value = "/chat/{from}/{to}")
	public ResponseEntity<Void> chat(@PathVariable String from, @PathVariable String to) {
		return ResponseEntity.status(HttpStatus.FOUND).location(exchangeRateService.getChatUrl(from, to)).build();
	}
}
