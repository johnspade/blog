package ru.johnspade.service;

import com.google.common.base.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;
import ru.johnspade.repository.PostRepository;
import ru.johnspade.web.NodeState;
import ru.johnspade.web.Rss;
import ru.johnspade.web.Tree;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class PostService {

	private final static int PAGE_SIZE = 4;
	private final static String SORT_PROPERTY = "date";
	private final static Locale LOCALE = Locale.forLanguageTag("ru");
	@Autowired
	private CacheService cacheService;
	@Autowired
	PostRepository repository;
	@Autowired
	private Rss rss;

	@Cacheable(cacheNames = "postExists", keyGenerator = "cacheKeyGenerator")
	public boolean exists(int id) {
		return repository.exists(id);
	}

	@Cacheable(cacheNames = "post", keyGenerator = "cacheKeyGenerator")
	public Post get(int id) {
		return repository.getOne(id);
	}

	public Post save(Post post) {
		cacheService.setlastSaveTimestamp(new Date());
		return repository.save(post);
	}

	public void delete(Post post) {
		cacheService.setlastSaveTimestamp(new Date());
		repository.delete(post);
	}

	@Cacheable(cacheNames = "mostRecentPost", keyGenerator = "cacheKeyGenerator")
	public Optional<Post> findMostRecent() {
		Post post = null;
		Page<Post> result = repository.findAll(new PageRequest(0, 1, Sort.Direction.DESC, SORT_PROPERTY));
		if (result.getNumberOfElements() > 0)
			post = result.getContent().get(0);
		return Optional.fromNullable(post);
	}

	@Cacheable(cacheNames = "posts", keyGenerator = "cacheKeyGenerator")
	public List<Post> findAll() {
		List<Post> posts = repository.findAll();
		Collections.sort(posts, new Comparator<Post>() {
			@Override
			public int compare(Post o1, Post o2) {
				return o2.getDate().compareTo(o1.getDate());
			}
		});
		return Collections.unmodifiableList(posts);
	}

	@Cacheable(cacheNames = "postsPage", keyGenerator = "cacheKeyGenerator")
	public Page<Post> findAll(int pageNumber) {
		return repository.findAll(new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY));
	}

	public Page<Post> findAllByTag(Tag tag, int pageNumber) {
		return repository.findAllByTag(tag, new PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY));
	}

	public Page<Post> findRecent() {
		return repository.findAll(new PageRequest(0, 25, Sort.Direction.DESC, SORT_PROPERTY));
	}

	@Cacheable(cacheNames = "rss", keyGenerator = "cacheKeyGenerator")
	public Rss getRss() {
		return rss;
	}

	@Cacheable(cacheNames = "tree", keyGenerator = "cacheKeyGenerator")
	public List<Tree> getTree() {
		List<Tree> years = new ArrayList<>();
		List<Post> posts = findAll();
		for (Post post : posts) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(post.getDate());
			boolean yearFound = false;
			for (Tree year : years) {
				if (Integer.toString(cal.get(Calendar.YEAR)).equals(year.getText())) {
					boolean monthFound = false;
					List<Tree> months = year.getNodes();
					for (Tree month : months) {
						if (getMonthName(cal).equals(month.getText())) {
							addPostToMonth(post, month);
							monthFound = true;
							break;
						}
					}
					if (!monthFound) {
						Tree month = new Tree(getMonthName(cal));
						months.add(month);
						addPostToMonth(post, month);
					}
					yearFound = true;
					break;
				}
			}
			if (!yearFound) {
				Tree year = new Tree(Integer.toString(cal.get(Calendar.YEAR)));
				years.add(year);
				Tree month = new Tree(getMonthName(cal));
				years.get(years.size() - 1).getNodes().add(month);
				addPostToMonth(post, month);
			}
		}
		return years;
	}

	private String getMonthName(Calendar cal) {
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, LOCALE);
	}

	private void addPostToMonth(Post post, Tree month) {
		month.getNodes().add(new Tree(post.getTitle(), new NodeState(), null, "/posts/" + Integer.toString(post.getId())));
		if (month.getTags() == null)
			month.setTags(Collections.singletonList("0"));
		month.setTags(Collections.singletonList(Integer.toString(Integer.parseInt(month.getTags().get(0)) + 1)));
	}

}
