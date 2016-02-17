package ru.johnspade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.johnspade.dao.Tag;

@Repository
public interface TagRepository extends JpaRepository<Tag, String> {}
