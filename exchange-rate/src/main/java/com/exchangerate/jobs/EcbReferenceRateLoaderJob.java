package com.exchangerate.jobs;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.exchangerate.service.ExchangeRateService;

@Component
public class EcbReferenceRateLoaderJob {

	private ExchangeRateService exchangeRateService;
	private RestTemplate restTemplate;

	@Value("${jobs.cronSchedule}")
	private String SCHEDULE_EXPRESSION;

	private static final Logger log = LoggerFactory.getLogger(EcbReferenceRateLoaderJob.class);

	private static final String ECB_REF_URL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref.zip";

	@Autowired
	public EcbReferenceRateLoaderJob(RestTemplate restTemplate, ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
		this.restTemplate = restTemplate;
	}

	@Scheduled(cron = "${jobs.cronSchedule}")
	public void loadEcbReferenceRates() {
		try {
			Iterable<CSVRecord> records = downloadEcbReferenceRate();
			for (CSVRecord record : records) {
				Map<String, String> recordMap = record.toMap();
				recordMap.remove("Date");
				saveCurrencies(recordMap);
				log.info("Exchange rates loaded succesfully");
			}
		} catch (MalformedURLException e) {
			log.error(e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	@PostConstruct
	public void init() {
		// Load data when application is up
		CronExpression exp = CronExpression.parse(SCHEDULE_EXPRESSION);
		LocalDateTime next = exp.next(LocalDateTime.now());
		if (LocalDateTime.now().plusMinutes(3).isBefore(next)) {
			loadEcbReferenceRates();
		}
	}

	private void saveCurrencies(Map<String, String> recordMap) {
		Map<String, Double> currencyMap = new HashMap<>();
		for (Map.Entry<String, String> entry : recordMap.entrySet()) {
			if (StringUtils.isNotBlank(entry.getKey()) && StringUtils.isNotBlank(entry.getValue())) {
				try {
					currencyMap.put(entry.getKey().trim(), Double.parseDouble(entry.getValue()));
				} catch (NumberFormatException e) {
					log.error("Failed parsing the Currency:{} and Value:{}", entry.getKey(), entry.getValue());
				}
			}
		}
		// load entire data at once. So that we show every exchange rate which is
		// fetched at same time
		exchangeRateService.refreshData(currencyMap);
	}

	private Iterable<CSVRecord> downloadEcbReferenceRate() throws IOException {

		Iterable<CSVRecord> iterator = restTemplate.execute(ECB_REF_URL, HttpMethod.GET, null, clientHttpResponse -> {
			ZipInputStream zipInput = new ZipInputStream(clientHttpResponse.getBody());
			zipInput.getNextEntry();
			return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new InputStreamReader(zipInput));
		});

		return iterator;
	}

}