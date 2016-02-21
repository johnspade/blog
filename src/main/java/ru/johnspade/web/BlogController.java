package ru.johnspade.web;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;
import ru.johnspade.service.PostService;
import ru.johnspade.service.TagService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class BlogController {

	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		return posts(1, null);
	}

	@RequestMapping(value = "/posts", method = RequestMethod.GET)
	public ModelAndView posts(@RequestParam(value = "page", required = false) Integer pageNumber,
							  @RequestParam(value = "tag", required = false) String tagName) {
		Tag tag = null;
		Page<Post> page;
		ModelAndView modelAndView = new ModelAndView("list");
		if (pageNumber == null)
			pageNumber = 1;
		if (tagName != null) {
			if (tagService.exists(tagName)) {
				tag = tagService.getOne(tagName);
				page = postService.findAllByTag(tag, pageNumber - 1);
			}
			else
				page = new PageImpl<>(Collections.<Post>emptyList());
		}
		else
			page = postService.findAll(pageNumber - 1);
		List<PostModel> resources = new ArrayList<>();
		for (Post post : page.getContent())
			resources.add(new PostModel(post));
		modelAndView.addObject("selectedTag", tag);
		modelAndView.addObject("posts", resources);
		modelAndView.addObject("currentPage", page.getNumber() + 1);
		modelAndView.addObject("hasNextPage", page.hasNext());
		modelAndView.addObject("hasPreviousPage", page.hasPrevious());
		return modelAndView;
	}

	@RequestMapping(value = "/contents", method = RequestMethod.GET)
	public String contents() {
		return "contents";
	}

	@RequestMapping(value = "/rss", method = RequestMethod.GET)
	public Rss rss() {
		Rss rss = null;
		Optional<Post> lastPost = postService.findMostRecent();
		if (lastPost.isPresent())
			rss = postService.getRss();
		return rss;
	}

}

