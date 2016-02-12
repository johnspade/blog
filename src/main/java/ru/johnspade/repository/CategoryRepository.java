package ru.johnspade.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.johnspade.dao.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {}
