package com.cs.challenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.cs.challenge.service.EventLogService;
import com.cs.challenge.ApplicationContextProvider;
import com.cs.challenge.entity.EventEntity;

@SpringBootApplication
public class EventLogApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(EventLogApplication.class);

	@Autowired
	ApplicationContextProvider applicationContextProvider;

	public static void main(String[] args) {
		SpringApplication.run(EventLogApplication.class, args);
	}

	@Override
	public void run(String... args) {
		if (args == null || args.length != 1) {
			throw new IllegalArgumentException("Please provide file path");
		}
		EventLogService eventLogService = applicationContextProvider.getApplicationContext()
				.getBean(EventLogService.class);
		eventLogService.parseAndSaveFileContents(args[0]);
		for (EventEntity event : eventLogService.getAllEvents()) {
			logger.info(event.toString());
		}
		System.exit(0);
	}

}
