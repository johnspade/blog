package ru.johnspade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.johnspade.dao.Category;
import ru.johnspade.dao.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	Page<Post> findAllByCategory(Category category, Pageable pageable);

}
