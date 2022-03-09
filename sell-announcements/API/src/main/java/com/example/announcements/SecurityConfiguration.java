package com.example.announcements;

import javax.sql.DataSource;

import com.example.announcements.filter.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PasswordEncoder passwordEncoder;


	@Autowired
	private DataSource dataSource;

	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	@Autowired
	private CustomLoginSuccessHandler sucessHandler;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery).authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource).passwordEncoder(passwordEncoder);
	}

	@Bean(name = BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		config.applyPermitDefaultValues();

		http.cors().configurationSource(request -> config);

		http.authorizeRequests()
				// URLs matching for access rights
				.antMatchers("/").permitAll()
				.antMatchers("l").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/categories").permitAll()
				.antMatchers("/admin/statistics").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin/create_database").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/create_admin").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_auth").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_announcement").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_announcement/delete/{announcement_id}").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_category").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_category/create").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_category/delete/{category_id}").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/admin_priv").hasAnyAuthority("ADMIN_USER")
				.antMatchers("/home/**").hasAnyAuthority( "ADMIN_USER", "SITE_USER")
				.antMatchers("/home").permitAll()
				.antMatchers("/announcements/upload/{announcement_id}").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.antMatchers("/announcements").permitAll()
				.antMatchers("/announcements/category/{id}").permitAll()
				.antMatchers("/announcements/{id}").permitAll()
				.antMatchers("/announcements/image/{id}").permitAll()
				.antMatchers("/announcements/add").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.antMatchers("/announcements/user/{user_id}").permitAll()
				.antMatchers("/announcements/search/{keyword}").permitAll()
				.antMatchers("/announcements/hide/{announcement_id}").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.antMatchers("/announcements/delete/{id}").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.antMatchers("/announcements/{announcement_id}/messages/{user_id}").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.antMatchers("/announcements/{announcement_id}/messages").hasAnyAuthority("ADMIN_USER", "SITE_USER")
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				// logout
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and()
				.exceptionHandling()
				.accessDeniedPage("/access-denied");

		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}
