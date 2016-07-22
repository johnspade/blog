package ru.johnspade.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.Size

private const val SETTINGS_ID = "settings"

/**
 * Настройки блога
 */
@Suppress("unused")
@Entity
@Table(name = SETTINGS_ID)
class Settings {

	/**
	 * id – константа, настройки существуют в единственном экземпляре
	 */
	@Id
	private val id: String = SETTINGS_ID
	@Size(max = 255)
	/**
	 * Название блога
	 */
	@Column(nullable = false)
	var title: String = ""
	/**
	 * Краткое описание блога (в шапке)
	 */
	@Size(max = 255)
	@Column(nullable = false)
	var description: String = ""
	/**
	 * Описание блога (в сайдбаре)
	 */
	@Column(nullable = false, columnDefinition = "text")
	var about: String = ""

}

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
