package ru.johnspade.web.controller;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.rometools.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;
import ru.johnspade.service.PostService;
import ru.johnspade.service.TagService;
import ru.johnspade.web.PostModel;
import ru.johnspade.web.ResourceNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class PostController {

	private static final int RELATED_COUNT = 4;
	@Autowired
	private PostService postService;
	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("post", new PostModel());
		return "edit";
	}

	@RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
	public String read(@PathVariable int id, Model model) {
		if (postService.exists(id)) {
			final Post post = postService.get(id);
			List<Post> posts = new ArrayList<>(postService.findAll());
			Iterables.removeIf(posts, new Predicate<Post>() {
				@Override
				public boolean apply(Post postObject) {
					return post.getId() == postObject.getId();
				}
			});
			List<Post> related = new ArrayList<>(RELATED_COUNT);
			Random random = new Random();
			int count = RELATED_COUNT < posts.size() ? RELATED_COUNT : posts.size();
			for (int i = 0; i < count; i++) {
				int n = random.nextInt(posts.size());
				related.add(posts.get(n));
				posts.remove(n);
			}
			model.addAttribute("post", new PostModel(post));
			model.addAttribute("related", related);
		}
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

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=save")
	public String save(@ModelAttribute("post") PostModel resource) {
		int id = resource.getId();
		Post post = postService.exists(id) ? postService.get(id) : new Post();
		update(post, resource);
		post = postService.save(post);
		return "redirect:/posts/" + Integer.toString(post.getId());
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=preview")
	public String preview(@ModelAttribute("post") PostModel resource, Model model) {
		model.addAttribute("post", resource);
		return "post";
	}

	private Post update(Post post, PostModel resource) {
		post.setTitle(resource.getTitle());
		post.setBody(resource.getBody());
		String[] tagsArray = resource.getTags().split(",");
		List<Tag> postTags = post.getTags();
		postTags.clear();
		for (String tag : tagsArray) {
			String tagName = tag.trim();
			if (Strings.isNotEmpty(tagName) && !tagService.exists(tagName))
				tagService.save(new Tag(tagName));
			if (tagService.exists(tagName)) {
				Tag foundTag = tagService.getOne(tagName);
				if (!postTags.contains(foundTag))
					postTags.add(foundTag);
			}
		}
		post.setDescription(resource.getDescription());
		return post;
	}

}
