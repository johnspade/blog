package ru.johnspade.web;

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

import java.util.List;

@Controller
public class PostController {

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
			if (tagService.exists(tagName))
				postTags.add(tagService.getOne(tagName));
		}
		return post;
	}

}
