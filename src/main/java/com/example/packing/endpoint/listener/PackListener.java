package com.example.packing.endpoint.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import com.example.packing.dto.converter.PickConfirmToPackRequestConverter;
import com.example.packing.service.PackingService;
import com.example.packing.streams.PackingStreams;
import com.example.picking.dto.events.PickConfirmationEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PackListener {
	@Autowired
	PackingService packService;

	@StreamListener(target = PackingStreams.PICK_OUTPUT, 
			condition = "headers['eventName']=='PickConfirmationEvent'")
	public void handlePickConfirmationEvent(PickConfirmationEvent pickConfirmEvent) { 
		log.info("Received PickConfirmationEvent for: {}" + ": at :" + new java.util.Date(), pickConfirmEvent);
		long startTime = System.currentTimeMillis();
		try {
			packService.createPack(PickConfirmToPackRequestConverter.createPackCreationRequest(pickConfirmEvent));
			long endTime = System.currentTimeMillis();
			log.info("Completed Processing PickConfirmationEvent for: " + pickConfirmEvent + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs");
		} catch (Exception e) {
			e.printStackTrace();
			long endTime = System.currentTimeMillis();
			log.error("Error Completing PickConfirmationEvent for: " + pickConfirmEvent + ": at :"
					+ new java.util.Date() + " : total time:" + (endTime - startTime) / 1000.00 + " secs", e);
		}
	}
}
