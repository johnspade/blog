package ru.johnspade.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import ru.johnspade.dao.SettingsService
import ru.johnspade.service.PostService
import ru.johnspade.service.TagService

data class PostInfo(val id: Int, val title: String, val recent: Boolean)
data class TagInfo(val name: String, val posts: List<PostInfo>)

@ControllerAdvice
class CommonAttributesAdvice {

	@Autowired
	private lateinit var tagService: TagService
	@Autowired
	private lateinit var postService: PostService
	@Autowired
	private lateinit var settingsService: SettingsService

	@ModelAttribute
	fun getAttributes(model: Model) {
		val settings = settingsService.getSettings()
		model.addAttribute("title", settings.title)
		model.addAttribute("description", settings.description)
		model.addAttribute("about", settings.about)
		model.addAttribute("tree", postService.tree)
		val recentPosts = postService.findAll(0)
		model.addAttribute("tags", tagService.findAll().map {
			TagInfo(it.name, it.posts.map { p -> PostInfo(p.id, p.title, recentPosts.any { it.id == p.id }) })
		})
	}

}