package ru.johnspade.dao;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "tags")
@SuppressWarnings("unused")
public class Tag {

	@Id
	@Column(name = "tag_name", nullable = false)
	private String name;
	@Column(columnDefinition = "text")
	private String description;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
	private List<Post> posts;

	public Tag(String name) {
		setName(name);
	}

	public Tag() {
		this("");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

}
