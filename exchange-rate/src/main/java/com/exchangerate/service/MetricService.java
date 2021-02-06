package com.exchangerate.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class MetricService {

	private AtomicInteger count = new AtomicInteger();

	public MetricService() {
	}

	public void increaseCount() {
		count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}
}
