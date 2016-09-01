package ru.johnspade.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import ru.johnspade.dao.SETTINGS_ID
import ru.johnspade.dao.Settings

/**
 * Весь репозиторий не нужен, но мне лень самому доставать сущность из Hibernate
 */
@Repository
private interface SettingsRepository: CrudRepository<Settings, String> {}

@Service
class SettingsService {

	@Autowired
	private lateinit var settingsRepository: SettingsRepository

	fun getSettings(): Settings {
		return settingsRepository.findOne(SETTINGS_ID) ?: Settings()
	}

	fun saveSettings(settings: Settings) {
		settingsRepository.save(settings)
	}

}
