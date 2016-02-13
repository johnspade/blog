package ru.johnspade.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import ru.johnspade.dao.Category;
import ru.johnspade.dao.Post;
import ru.johnspade.repository.CategoryRepository;
import ru.johnspade.repository.PostRepository;

import java.util.Collections;

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
		return posts(1, null);
	}

	@RequestMapping(value = "/posts", method = RequestMethod.GET)
	public ModelAndView posts(@RequestParam(value = "page", required = false) Integer pageNumber,
							  @RequestParam(value = "category", required = false) String categoryName) {
		Category category = null;
		Page<Post> page;
		ModelAndView modelAndView = new ModelAndView("list");
		if (pageNumber == null)
			pageNumber = 1;
		PageRequest pageRequest = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "date");
		if (categoryName != null) {
			if (categoryRepository.exists(categoryName)) {
				category = categoryRepository.getOne(categoryName);
				page = postRepository.findAllByCategory(category, pageRequest);
			}
			else
				page = new PageImpl<>(Collections.<Post>emptyList());
		}
		else
			page = postRepository.findAll(pageRequest);
		modelAndView.addObject("selectedCategory", category);
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
		if (postRepository.exists(id))
			model.addAttribute("post", postRepository.getOne(id));
		else
			throw new ResourceNotFoundException("Пост не найден");
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

