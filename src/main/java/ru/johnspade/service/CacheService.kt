package ru.johnspade.service

import org.springframework.stereotype.Service
import java.util.*

@Service
open class CacheService {

	@Volatile internal var lastSaveTimestamp = Date()
		private set

	open @Synchronized internal fun setlastSaveTimestamp(date: Date) {
		lastSaveTimestamp = date
	}

}
