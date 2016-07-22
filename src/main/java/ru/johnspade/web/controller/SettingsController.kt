package ru.johnspade.web.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import ru.johnspade.dao.Settings
import ru.johnspade.dao.SettingsService

@Controller
class SettingsController {

	@Autowired
	private lateinit var settingsService: SettingsService

	@RequestMapping(value = "/settings", method = arrayOf(RequestMethod.GET))
	fun read(model: Model): String {
		model.addAttribute("settings", settingsService.getSettings())
		return "settings"
	}

	@RequestMapping(value = "/settings", method = arrayOf(RequestMethod.POST))
	fun update(settings: Settings): String {
		settingsService.saveSettings(settings)
		return "redirect:/"
	}

}
