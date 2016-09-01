package ru.johnspade.web

import com.rometools.rome.feed.rss.Channel
import com.rometools.rome.feed.rss.Content
import com.rometools.rome.feed.rss.Description
import com.rometools.rome.feed.rss.Item
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.servlet.view.feed.AbstractRssFeedView
import ru.johnspade.service.PostService
import ru.johnspade.service.SettingsService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component("rss")
open class Rss : AbstractRssFeedView() {

	@Autowired
	private lateinit var postService: PostService
	@Autowired
	private lateinit var settingsService: SettingsService
	@Value("\${blog.url}")
	private lateinit var baseUrl: String

	override fun newFeed(): Channel {
		val feed = super.newFeed()
		feed.link = baseUrl + "/rss"
		val settings = settingsService.getSettings()
		feed.title = settings.title
		feed.description = settings.about
		val lastPost = postService.findMostRecent()
		if (lastPost.isPresent)
			feed.pubDate = lastPost.get().date
		return feed
	}

	override fun buildFeedItems(map: Map<String, Any>, request: HttpServletRequest, response: HttpServletResponse)
			: List<Item> {
		return postService.findRecent().content.map {
			val item = Item()
			item.title = it.title
			item.link = baseUrl + "/posts/" + it.id
			val description = Description()
			description.type = Content.HTML
			description.value = it.body
			item.description = description
			item.pubDate = it.date
			item
		}
	}

}
