package ru.johnspade.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@SuppressWarnings("unused")
@Entity
@Table(name = "posts")
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
	@Column
	private String description;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tag_post", joinColumns = @JoinColumn(name = "post_id"),
			inverseJoinColumns = @JoinColumn(name = "tag_name"))
	private List<Tag> tags;

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

	public List<Tag> getTags() {
		if (tags == null)
			tags = new ArrayList<>();
		return tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
