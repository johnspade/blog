package ru.johnspade.service

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.cache.interceptor.SimpleKeyGenerator

import java.lang.reflect.Method

class CacheKeyGenerator (private val cacheService: CacheService) : KeyGenerator {

	override fun generate(target: Any, method: Method, vararg params: Any): Any {
		return SimpleKeyGenerator.generateKey(params, cacheService.lastSaveTimestamp)
	}

}
