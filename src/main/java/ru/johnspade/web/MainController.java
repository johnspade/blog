package ru.johnspade.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.johnspade.dao.Category;
import ru.johnspade.dao.Post;
import ru.johnspade.repository.CategoryRepository;
import ru.johnspade.repository.PostRepository;

import java.util.Collections;
import java.util.Optional;

@Controller
public class MainController {

	private final static int PAGE_SIZE = 4;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private CategoryRepository categoryRepository;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView index() {
		return posts(Optional.of(1), Optional.empty());
	}

	@RequestMapping(value = "/posts", method = RequestMethod.GET)
	public ModelAndView posts(@RequestParam(value = "page", required = false) Optional<Integer> pageNumber,
							  @RequestParam(value = "category", required = false) Optional<String> categoryName) {
		Category category = null;
		Page<Post> page;
		ModelAndView modelAndView = new ModelAndView("list");
		if (!pageNumber.isPresent())
			pageNumber = Optional.of(1);
		PageRequest pageRequest = new PageRequest(pageNumber.get() - 1, PAGE_SIZE, Sort.Direction.DESC, "date");
		if (categoryName.isPresent()) {
			if (categoryRepository.exists(categoryName.get())) {
				category = categoryRepository.getOne(categoryName.get());
				page = postRepository.findAllByCategory(category, pageRequest);
			}
			else
				page = new PageImpl<>(Collections.emptyList());
		}
		else
			page = postRepository.findAll(pageRequest);
		modelAndView.addObject("category", category);
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

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=save")
	public String save(@ModelAttribute("post") Post editedPost) {
		int id = editedPost.getId();
		Post post = postRepository.exists(id) ? postRepository.getOne(id) : new Post();
		post.setTitle(editedPost.getTitle());
		post.setBody(editedPost.getBody());
		post.setCategory(editedPost.getCategory());
		post = postRepository.save(post);
		return "redirect:/posts/" + Integer.toString(post.getId());
	}

	@RequestMapping(value = "/posts/{id}", method = RequestMethod.GET)
	public String read(@PathVariable int id, Model model) {
		model.addAttribute("post", postRepository.getOne(id));
		return "post";
	}

	@RequestMapping(value = "/posts/{id}/edit", method = RequestMethod.GET)
	public String update(@PathVariable int id, Model model) {
		Post post = postRepository.getOne(id);
		model.addAttribute("post", post);
		return "edit";
	}

	@RequestMapping(value = "/posts/{id}/delete", method = RequestMethod.DELETE)
	public String delete(@PathVariable int id) {
		postRepository.delete(postRepository.getOne(id));
		return "redirect:/";
	}

	@RequestMapping(value = "/contents", method = RequestMethod.GET)
	public String contents() {
		return "contents";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, params = "action=preview")
	public String preview(@ModelAttribute("post") Post editedPost, Model model) {
		model.addAttribute(editedPost);
		return "post";
	}

}

@ControllerAdvice
class CategoriesAdvice {

	@Autowired
	private CategoryRepository categoryRepository;

	@ModelAttribute
	public String categories(Model model) {
		model.addAttribute("categories", categoryRepository.findAll());
		return "categories";
	}

}

