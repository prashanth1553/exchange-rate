package com.exchangerate.filters;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.exchangerate.service.MetricService;

public class CurrencyRequestCountFilter implements Filter {

	@Autowired
	private MetricService metricService;

	public CurrencyRequestCountFilter() {
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		metricService = (MetricService) WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext()).getBean("metricService");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws java.io.IOException, ServletException {
		chain.doFilter(request, response);
		metricService.increaseCount();
	}
}