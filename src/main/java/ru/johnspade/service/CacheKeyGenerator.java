package ru.johnspade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;

import java.lang.reflect.Method;

public class CacheKeyGenerator implements KeyGenerator {

	@Autowired
	private CacheService cacheService;

	@Override
	public Object generate(Object target, Method method, Object... params) {
		return SimpleKeyGenerator.generateKey(params, cacheService.getLastSaveTimestamp());
	}

}
