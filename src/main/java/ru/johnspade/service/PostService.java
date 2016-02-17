package ru.johnspade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;
import ru.johnspade.repository.PostRepository;

@Service
public class PostService {

	private final static int PAGE_SIZE = 4;
	private final static String SORT_PROPERTY = "date";
	@Autowired
	PostRepository repository;

	public boolean exists(int id) {
		return repository.exists(id);
	}

	public Post get(int id) {
		return repository.getOne(id);
	}

	public Post save(Post post) {
		return repository.save(post);
	}

	public void delete(Post post) {
		repository.delete(post);
	}

	public Post findMostRecent() {
		return repository.findMostRecent().get(0);
	}

	public Page<Post> findAll(int pageNumber) {
		return repository.findAll(new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY));
	}

	public Page<Post> findAllByTag(Tag tag, int pageNumber) {
		return repository.findAllByTag(tag, new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY));
	}

	public Page<Post> findRecent() {
		return repository.findAll(new PageRequest(0, 25, Sort.Direction.DESC, SORT_PROPERTY));
	}

}
