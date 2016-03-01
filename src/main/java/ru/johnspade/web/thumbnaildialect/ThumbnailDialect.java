package ru.johnspade.web.thumbnaildialect;

import com.google.common.collect.Sets;
import org.thymeleaf.dialect.AbstractDialect;
import org.thymeleaf.processor.IProcessor;

import java.util.Set;

public class ThumbnailDialect extends AbstractDialect {

	public ThumbnailDialect() {
		super();
	}

	@Override
	public String getPrefix() {
		return "thmb";
	}

	@Override
	public Set<IProcessor> getProcessors() {
		return Sets.<IProcessor>newHashSet(new ThumbnailProcessor());
	}

}
