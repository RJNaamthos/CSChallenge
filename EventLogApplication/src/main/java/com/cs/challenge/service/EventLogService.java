package com.cs.challenge.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.challenge.entity.Event;
import com.cs.challenge.entity.EventEntity;
import com.cs.challenge.repo.EventRepository;
import com.google.gson.Gson;

@Service
public class EventLogService {

	private static Logger logger = LoggerFactory.getLogger(EventLogService.class);
	
	@Autowired
	EventRepository eventRepo;
	
	public EventLogService() {
	}
	
	public void parseAndSaveFileContents(String fileName) {
		logger.info("Reading file from path " + fileName);
		Map<String, Event> uniqueEvents = new HashMap<String, Event>();
		Gson gson = new Gson();
		try {
			Stream<String> stream = Files.lines(Paths.get(fileName));
			if(logger.isDebugEnabled()) {
				logger.debug("Iterating over each log event");
			}
			stream.forEach(eventsStream -> {
				long duration = 0L;
				Event event = gson.fromJson(eventsStream, Event.class);
				String eventId = event.getId();
				if(eventId != null) {
					
					if(uniqueEvents.containsKey(eventId)) {
						Event existingEvent = uniqueEvents.remove(eventId);
						duration = Math.abs(event.getTimestamp() - existingEvent.getTimestamp());
						EventEntity eventEntity = new EventEntity();
						eventEntity.setEventId(eventId);
						eventEntity.setDuration(duration);
						eventEntity.setHost(event.getHost());
						eventEntity.setType(event.getType());
						eventEntity.setAlert(duration > 4 ? true: false);
						logger.info("Saving record in DB");
						insertEventInDB(eventEntity);
					} else {
						// Add Event if it does not exists
						uniqueEvents.put(eventId, event);
					}
				}
			});
		} catch (IOException e) {
			logger.error("Error while reading file" + e.getMessage());
		}
		
		
	}

	private void insertEventInDB(EventEntity eventEntity) {
		if(eventRepo.save(eventEntity) != null) {
			logger.info("Event" + eventEntity.getEventId() + " inserted into DB ");
		} else {
			logger.error("Error while inserting event " + eventEntity.getEventId());
		}
	}
	
	public List<EventEntity> getAllEvents(){
		List<EventEntity> events = eventRepo.findAll();
		return events;
	}

}
