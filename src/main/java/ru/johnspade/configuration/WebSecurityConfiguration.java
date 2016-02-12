package ru.johnspade.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests().antMatchers("/new", "/save", "/*/*/edit", "/*/*/delete").authenticated()
				.anyRequest().permitAll()
				.and()
				.formLogin().loginPage("/login")
				.and()
				.logout().permitAll();
	}

	@Bean
	public DriverManagerDataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://" + System.getenv("OPENSHIFT_POSTGRESQL_DB_HOST") + ":"
				+ System.getenv("OPENSHIFT_POSTGRESQL_DB_PORT") + "/blog");
		dataSource.setUsername(System.getenv("OPENSHIFT_POSTGRESQL_DB_USERNAME"));
		dataSource.setPassword(System.getenv("OPENSHIFT_POSTGRESQL_DB_PASSWORD"));
		return dataSource;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource()).passwordEncoder(passwordEncoder())
				.usersByUsernameQuery("select username, password, enabled from users where username = ?")
				.authoritiesByUsernameQuery("select username, authority from authorities where username = ?");
	}

}
