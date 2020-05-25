package com.lukaslt1993.diary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.lukaslt1993.diary.models.Author;
import com.lukaslt1993.diary.repositories.AuthorsRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	AuthorsRepository repo;

	private class MyUserDetailsService implements UserDetailsService {
		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			if (repo.existsById(username)) {
				Author author = repo.findById(username).orElse(null);
				if (author != null) {
					String password = author.getPassword();
					return User.builder().username(username).password(password).roles("USER").build();
				}
			}
			throw new UsernameNotFoundException("User not found");
		}
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", EndpointNames.REGISTER).permitAll().antMatchers(EndpointNames.AUTHORS)
				.denyAll().anyRequest().authenticated().and().httpBasic().and().csrf().disable();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return new MyUserDetailsService();
	}

}