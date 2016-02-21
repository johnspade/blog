package ru.johnspade.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.johnspade.dao.Tag;
import ru.johnspade.service.PostService;
import ru.johnspade.service.TagService;

import java.util.List;

@ControllerAdvice
public class CommonAttributesAdvice {

	@Autowired
	private TagService tagService;
	@Autowired
	private PostService postService;
	@Value("${application.title}")
	private String title;
	@Value("${application.description}")
	private String description;

	@ModelAttribute("title")
	public String title() {
		return title;
	}

	@ModelAttribute("description")
	public String description() {
		return description;
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
