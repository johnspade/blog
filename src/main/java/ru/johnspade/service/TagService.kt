package ru.johnspade.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.johnspade.dao.Tag
import java.util.*

@Repository
interface TagRepository : JpaRepository<Tag, String>

@Service
open class TagService @Autowired constructor(private val repository: TagRepository) {

	open fun exists(tagName: String): Boolean {
		return repository.exists(tagName)
	}

	open fun getOne(tagName: String): Tag {
		return repository.getOne(tagName)
	}

	open fun save(tag: Tag): Tag {
		return repository.save(tag)
	}

	@Cacheable(cacheNames = arrayOf("tags"), keyGenerator = "cacheKeyGenerator")
	open fun findAll(): List<Tag> {
		val tags = repository.findAll()
		Collections.sort(tags) { o1, o2 -> o2.posts.size.compareTo(o1.posts.size) }
		return tags
	}

}
