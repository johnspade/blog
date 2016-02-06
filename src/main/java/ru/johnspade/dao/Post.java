package ru.johnspade.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "posts")
@SuppressWarnings("unused")
public class Post {

	@Id
	@GeneratedValue
	@Column(nullable = false)
	private int id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private Date date;
	@Column(nullable = false, columnDefinition = "TEXT")
	private String body;

	@SuppressWarnings("unused")
	public Post(String title, String body) {
		setTitle(title);
		setDate(new Date());
		setBody(body);
	}

	public Post() {
		setTitle("");
		setBody("");
		setDate(new Date());
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

}
