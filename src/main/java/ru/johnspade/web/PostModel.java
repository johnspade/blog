package ru.johnspade.web;

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

	public PostModel(Post post) {
		setId(post.getId());
		setTitle(post.getTitle());
		setDate(post.getDate());
		setBody(post.getBody());
		List<Tag> postTags = post.getTags();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < postTags.size(); i++) {
			sb.append(postTags.get(i).getName());
			if (i != postTags.size() - 1)
				sb.append(",");
		}
		setTags(sb.toString());
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
