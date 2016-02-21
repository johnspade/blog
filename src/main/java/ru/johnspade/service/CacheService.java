package ru.johnspade.service;

import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CacheService {

	private volatile Date lastSaveTimestamp = new Date();

	public synchronized void setlastSaveTimestamp(Date date) {
		lastSaveTimestamp = date;
	}

	public Date getLastSaveTimestamp() {
		return lastSaveTimestamp;
	}

}
