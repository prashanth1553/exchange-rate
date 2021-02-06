package com.exchangerate;

import java.time.Duration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.exchangerate.filters.CurrencyRequestCountFilter;

@SpringBootApplication
public class ExchangeRateServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExchangeRateServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.setConnectTimeout(Duration.ofMillis(3000)).setReadTimeout(Duration.ofMillis(3000)).build();
	}

	@Bean
	public FilterRegistrationBean<CurrencyRequestCountFilter> loggingFilter() {
		FilterRegistrationBean<CurrencyRequestCountFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(currencyRequestCountFilter());
		registrationBean.addUrlPatterns("/exchange-rate/currencies");
		return registrationBean;
	}

	@Bean
	public CurrencyRequestCountFilter currencyRequestCountFilter() {
		return new CurrencyRequestCountFilter();
	}

}
