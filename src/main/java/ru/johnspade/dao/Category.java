package ru.johnspade.dao;

import org.hibernate.annotations.OrderBy;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "categories")
@SuppressWarnings("unused")
public class Category {

	@Id
	@Column(name = "category_name", nullable = false)
	private String name;
	@Column(columnDefinition = "text")
	private String description;
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	@OrderBy(clause = "date")
	private List<Post> posts;

	public Category(String name) {
		setName(name);
	}

	public Category() {
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
