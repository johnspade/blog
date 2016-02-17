package ru.johnspade.web;

import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;
import ru.johnspade.dao.Post;
import ru.johnspade.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("rss")
public class Rss extends AbstractRssFeedView {

	@Autowired
	PostService postService;
	@Value("${application.url}")
	private String baseUrl;
	@Value("${application.title}")
	private String title;
	@Value("${application.description}")
	private String description;

	@Override
	protected Channel newFeed() {
		Channel channel = new Channel("rss_2.0");
		channel.setLink(baseUrl + "/rss");
		channel.setTitle(title);
		channel.setDescription(description);
		channel.setPubDate(postService.findMostRecent().getDate());
		return channel;
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> map, HttpServletRequest httpServletRequest,
										HttpServletResponse httpServletResponse) throws Exception {
		List<Item> items = new ArrayList<>();
		Page<Post> page = postService.findRecent();
		for (Post post : page.getContent())
			items.add(createItem(post));
		return items;
	}

	private Item createItem(Post post) {
		Item item = new Item();
		item.setLink(baseUrl + "/posts/" + post.getId());
		item.setTitle(post.getTitle());
		Description description = new Description();
		description.setType(Content.HTML);
		description.setValue(post.getBody());
		item.setDescription(description);
		item.setPubDate(post.getDate());
		return item;
	}

}
