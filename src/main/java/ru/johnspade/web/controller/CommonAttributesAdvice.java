package ru.johnspade.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.johnspade.dao.Tag;
import ru.johnspade.service.PostService;
import ru.johnspade.service.TagService;
import ru.johnspade.web.Tree;

import java.util.List;

@ControllerAdvice
public class CommonAttributesAdvice {

	@Autowired
	private Environment env;
	@Autowired
	private TagService tagService;
	@Autowired
	private PostService postService;

	@ModelAttribute("title")
	public String title() {
		return env.getProperty("blog.title");
	}

	@ModelAttribute("description")
	public String description() {
		return env.getProperty("blog.description");
	}

	@ModelAttribute("tags")
	public List<Tag> tags() {
		return tagService.findAll();
	}

	@ModelAttribute("tree")
	public List<Tree> tree() {
		return postService.getTree();
	}

}
