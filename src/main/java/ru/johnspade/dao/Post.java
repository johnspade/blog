package ru.johnspade.dao;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "posts")
@SuppressWarnings("unused")
public class Post {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id", nullable = false)
	private int id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private Date date;
	@Column(nullable = false, columnDefinition = "text")
	private String body;

	@ManyToOne
	@JoinColumn(name = "category_name")
	private Category category;

	@SuppressWarnings("unused")
	public Post(String title, String body) {
		setTitle(title);
		setDate(new Date());
		setBody(body);
	}

	public Post() {
		this("", "");
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

}
