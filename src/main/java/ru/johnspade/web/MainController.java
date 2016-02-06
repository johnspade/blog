package ru.johnspade.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ru.johnspade.dao.Post;
import ru.johnspade.repository.PostRepository;

@Controller
public class MainController {

	private final static int PAGE_SIZE = 4;
	@Autowired
	private PostRepository repository;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = {"/", "/pages", "/posts"}, method = RequestMethod.GET)
	public ModelAndView index() {
		return getPosts(1);
	}

	@RequestMapping(value = "/pages/{pageNumber}", method = RequestMethod.GET)
	public ModelAndView getPosts(@PathVariable int pageNumber) {
		PageRequest pageRequest = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "date");
		Page<Post> page = repository.findAll(pageRequest);
		ModelAndView modelAndView = new ModelAndView("list");
		modelAndView.addObject("posts", page);
		modelAndView.addObject("currentPage", page.getNumber() + 1);
		modelAndView.addObject("hasNextPage", page.hasNext());
		modelAndView.addObject("hasPreviousPage", page.hasPrevious());
		return modelAndView;
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("post", new Post());
		return "edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("post") Post editedPost) {
		int id = editedPost.getId();
		Post post = repository.exists(id) ? repository.getOne(id) : new Post();
		post.setTitle(editedPost.getTitle());
		post.setBody(editedPost.getBody());
		post = repository.save(post);
		return new ModelAndView("redirect:/posts/" + Integer.toString(post.getId()));
	}

	@RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
	public String read(@PathVariable int id, Model model) {
		model.addAttribute("post", repository.getOne(id));
		return "post";
	}

	@RequestMapping(value = "/posts/{id}/edit", method = RequestMethod.GET)
	public String update(@PathVariable int id, Model model) {
		model.addAttribute("post", repository.getOne(id));
		return "edit";
	}

	@RequestMapping(value = "/posts/{id}/delete", method = RequestMethod.DELETE)
	public String delete(@PathVariable int id) {
		repository.delete(repository.getOne(id));
		return "redirect:/";
	}

}
