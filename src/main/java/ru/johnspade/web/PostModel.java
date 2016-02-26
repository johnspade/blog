package ru.johnspade.web;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;

import java.util.Date;
import java.util.List;

public class PostModel {

	private int id;
	private String title;
	private Date date;
	private String body;
	private String tags;

	public PostModel(Post post, boolean isShort) {
		setId(post.getId());
		setTitle(post.getTitle());
		setDate(post.getDate());
		String body = post.getBody();
		if (isShort) {
			Document doc = Jsoup.parse(body);
			Elements cuts = doc.select("body > a[name=more]");
			if (!cuts.isEmpty())
				body = doc.body().html().split(cuts.first().toString())[0];
		}
		setBody(body);
		List<Tag> postTags = post.getTags();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < postTags.size(); i++) {
			sb.append(postTags.get(i).getName());
			if (i != postTags.size() - 1)
				sb.append(",");
		}
		setTags(sb.toString());
	}

	public PostModel(Post post) {
		this(post, false);
	}

	public PostModel() {
		setId(0);
		setTitle("");
		setDate(new Date());
		setBody("");
		setTags("");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

}
