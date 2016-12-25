package ru.johnspade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import ru.johnspade.service.CacheKeyGenerator;
import ru.johnspade.service.CacheService;
import ru.johnspade.web.thumbnaildialect.ThumbnailDialect;

@SpringBootApplication
@EnableCaching
public class BlogApplication extends SpringBootServletInitializer {

	@Autowired
	private CacheService cacheService;

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(BlogApplication.class);
	}

	@Bean
	public ThumbnailDialect thumbnailDialect() {
		return new ThumbnailDialect();
	}

	@Bean
	public CacheKeyGenerator cacheKeyGenerator() {
		return new CacheKeyGenerator(cacheService);
	}

}
