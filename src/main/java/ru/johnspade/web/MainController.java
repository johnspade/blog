package ru.johnspade.web;

import com.rometools.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;
import ru.johnspade.repository.TagRepository;
import ru.johnspade.service.PostService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class MainController {

	@Autowired
	Rss rss;
	@Autowired
	private PostService postService;
	@Autowired
	private TagRepository tagRepository;

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
			if (tagRepository.exists(tagName)) {
				tag = tagRepository.getOne(tagName);
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

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("post", new PostModel());
		return "edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=save")
	public String save(@ModelAttribute("post") PostModel resource) {
		int id = resource.getId();
		Post post = postService.exists(id) ? postService.get(id) : new Post();
		update(post, resource);
		post = postService.save(post);
		return "redirect:/posts/" + Integer.toString(post.getId());
	}

	@RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
	public String read(@PathVariable int id, Model model) {
		if (postService.exists(id))
			model.addAttribute("post", new PostModel(postService.get(id)));
		else
			throw new ResourceNotFoundException("Пост не найден");
		return "post";
	}

	@RequestMapping(value = "/posts/{id}/edit", method = RequestMethod.GET)
	public String update(@PathVariable int id, Model model) {
		if (postService.exists(id))
			model.addAttribute("post", new PostModel(postService.get(id)));
		else
			throw new ResourceNotFoundException("Пост не найден");
		return "edit";
	}

	@RequestMapping(value = "/posts/{id}/delete", method = RequestMethod.DELETE)
	public String delete(@PathVariable int id) {
		postService.delete(postService.get(id));
		return "redirect:/";
	}

	@RequestMapping(value = "/contents", method = RequestMethod.GET)
	public String contents() {
		return "contents";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=preview")
	public String preview(@ModelAttribute("post") PostModel resource, Model model) {
		model.addAttribute("post", resource);
		return "post";
	}

	@RequestMapping(value = "/rss", method = RequestMethod.GET)
	public Rss rss() {
		return rss;
	}

	private Post update(Post post, PostModel resource) {
		post.setTitle(resource.getTitle());
		post.setBody(resource.getBody());
		String[] tagsArray = resource.getTags().split(",");
		List<Tag> postTags = post.getTags();
		postTags.clear();
		for (String tag : tagsArray) {
			String tagName = tag.trim();
			if (Strings.isNotEmpty(tagName) && !tagRepository.exists(tagName))
				tagRepository.save(new Tag(tagName));
			if (tagRepository.exists(tagName))
				postTags.add(tagRepository.getOne(tagName));
		}
		return post;
	}

}

@ControllerAdvice
class CommonAttributesAdvice {

	@Autowired
	private TagRepository tagRepository;
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
		List<Tag> tags = tagRepository.findAll();
		Collections.sort(tags, new Comparator<Tag>() {
			@Override
			public int compare(Tag o1, Tag o2) {
				return ((Integer) o2.getPosts().size()).compareTo(o1.getPosts().size());
			}
		});
		return tags;
	}

}

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException extends RuntimeException {

	@SuppressWarnings("unused")
	public ResourceNotFoundException() {
		super();
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

}

