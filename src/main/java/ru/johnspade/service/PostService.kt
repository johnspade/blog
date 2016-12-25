package ru.johnspade.service

import com.google.common.base.Optional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.johnspade.dao.Post
import ru.johnspade.dao.Tag
import ru.johnspade.web.NodeState
import ru.johnspade.web.Tree
import java.util.*

@Repository
interface PostRepository : JpaRepository<Post, Int> {

	@Query("select p from Post p join p.tags t where t = ?1")
	fun findAllByTag(tag: Tag, pageable: Pageable): Page<Post>

}

@Service
open class PostService @Autowired constructor(
		private val cacheService: CacheService,
		private val repository: PostRepository
) {

	@Cacheable(cacheNames = arrayOf("postExists"), keyGenerator = "cacheKeyGenerator")
	open fun exists(id: Int): Boolean {
		return repository.exists(id)
	}

	@Cacheable(cacheNames = arrayOf("post"), keyGenerator = "cacheKeyGenerator")
	open operator fun get(id: Int): Post {
		return repository.getOne(id)
	}

	open fun save(post: Post): Post {
		cacheService.setlastSaveTimestamp(Date())
		return repository.save(post)
	}

	open fun delete(post: Post) {
		cacheService.setlastSaveTimestamp(Date())
		repository.delete(post)
	}

	@Cacheable(cacheNames = arrayOf("mostRecentPost"), keyGenerator = "cacheKeyGenerator")
	open fun findMostRecent(): Optional<Post> {
		var post: Post? = null
		val result = repository.findAll(PageRequest(0, 1, Sort.Direction.DESC, SORT_PROPERTY))
		if (result.numberOfElements > 0)
			post = result.content[0]
		return Optional.fromNullable(post)
	}

	@Cacheable(cacheNames = arrayOf("posts"), keyGenerator = "cacheKeyGenerator")
	open fun findAll(): List<Post> {
		val posts = repository.findAll()
		Collections.sort(posts) { o1, o2 -> o2.date.compareTo(o1.date) }
		return Collections.unmodifiableList(posts)
	}

	@Cacheable(cacheNames = arrayOf("postsPage"), keyGenerator = "cacheKeyGenerator")
	open fun findAll(pageNumber: Int): Page<Post> {
		return repository.findAll(PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY))
	}

	open fun findAllByTag(tag: Tag, pageNumber: Int): Page<Post> {
		return repository.findAllByTag(tag, PageRequest(pageNumber, PAGE_SIZE, Sort.Direction.DESC, SORT_PROPERTY))
	}

	open fun findRecent(): Page<Post> {
		return repository.findAll(PageRequest(0, 25, Sort.Direction.DESC, SORT_PROPERTY))
	}

	@Cacheable(cacheNames = arrayOf("tree"), keyGenerator = "cacheKeyGenerator")
	open fun getTree(postDate: Calendar?): List<Tree> {
		val years = ArrayList<Tree>()
		val posts = findAll()
		posts.forEach { post ->
			val cal = Calendar.getInstance()
			cal.time = post.date
			val expandYear = postDate != null && postDate.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
			val expandMonth = expandYear && postDate!!.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
			var year = years.firstOrNull { it.text == cal.get(Calendar.YEAR).toString() }
			if (year == null) {
				year = Tree(cal.get(Calendar.YEAR).toString(), expandYear)
				years.add(year)
			}
			var month = year.nodes.firstOrNull { it.text == getMonthName(cal) }
			if (month == null) {
				month = Tree(getMonthName(cal), expandMonth)
				year.nodes.add(month)
			}
			addPostToMonth(post, month, expandMonth)
		}
		return years
	}

	private fun getMonthName(cal: Calendar): String {
		return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, LOCALE)
	}

	private fun addPostToMonth(post: Post, month: Tree, expanded: Boolean) {
		month.expand(expanded)
		month.nodes.add(Tree(post.title, NodeState(false), null, "/posts/" + Integer.toString(post.id)))
		if (month.tags == null)
			month.tags = listOf("0")
		month.tags = listOf(Integer.toString(Integer.parseInt(month.tags[0]) + 1))
	}

	companion object {
		private val PAGE_SIZE = 4
		private val SORT_PROPERTY = "date"
		private val LOCALE = Locale.forLanguageTag("ru")
	}

}
