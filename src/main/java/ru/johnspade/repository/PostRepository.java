package ru.johnspade.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.johnspade.dao.Post;
import ru.johnspade.dao.Tag;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Query("select p from Post p join p.tags t where t = ?1")
	Page<Post> findAllByTag(Tag tag, Pageable pageable);

}
