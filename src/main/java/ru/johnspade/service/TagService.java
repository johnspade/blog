package ru.johnspade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.johnspade.dao.Tag;
import ru.johnspade.repository.TagRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class TagService {

	@Autowired
	private TagRepository repository;

	public boolean exists(String tagName) {
		return repository.exists(tagName);
	}

	public Tag getOne(String tagName) {
		return repository.getOne(tagName);
	}

	public Tag save(Tag tag) {
		return repository.save(tag);
	}

	@Cacheable(cacheNames = "tags", keyGenerator = "cacheKeyGenerator")
	public List<Tag> findAll() {
		List<Tag> tags = repository.findAll();
		Collections.sort(tags, new Comparator<Tag>() {
			@Override
			public int compare(Tag o1, Tag o2) {
				return ((Integer) o2.getPosts().size()).compareTo(o1.getPosts().size());
			}
		});
		return tags;
	}

}
