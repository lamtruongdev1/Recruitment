package com.poly.Recruitment.config;

import com.poly.Recruitment.service.CustomUserDetailsService;
import com.poly.Recruitment.util.RoleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

	private final CustomUserDetailsService customUserDetailsService;

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(customUserDetailsService);
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return authenticationProvider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable).cors(cr -> cr.configurationSource(corsConfigurationSource()))
				.headers(headers -> headers.frameOptions().sameOrigin()

				).authorizeHttpRequests(auth -> {

					auth.requestMatchers("/resources/**", "/static/**", "/templates/**", "/templates/NguoiTimViec/**",
							"/static/uploads/**","/admin/cv/**").permitAll();
					auth.requestMatchers("/auth/**", "/index").permitAll();
					auth.requestMatchers("/admin/**").hasAuthority(RoleEnum.ADMIN.name());
					auth.requestMatchers("/api/**").permitAll();
					auth.requestMatchers("/api/files/**", "/api/reviews", "/api/reviews/add", "/public/**",
							"/api/files/cv/**", "/api/cart/**", "/api/cart","/admin/cv/upload","/admin/cv/delete/**","/api/provinces/**","/job-postings/create").permitAll();
					auth.anyRequest().authenticated();
				})
				.formLogin(login -> login.loginPage("/auth/login/form").loginProcessingUrl("/auth/login")
						.defaultSuccessUrl("/auth/login/success", true).failureUrl("/auth/login/failure")
						.usernameParameter("username").passwordParameter("password"))
				.rememberMe(remember -> remember.rememberMeParameter("remember").tokenValiditySeconds(86400))
				.logout(httpSecurityLogoutConfigurer -> httpSecurityLogoutConfigurer.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID").clearAuthentication(true)
						.logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout"))
						.logoutSuccessUrl("/auth/logout/success").addLogoutHandler(new SecurityContextLogoutHandler()))
				.exceptionHandling(ex -> ex.accessDeniedPage("/auth/access-denied"))
				.authenticationProvider(authenticationProvider());
		return http.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		log.info("Config Cors API");
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(Collections.singletonList("*"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setExposedHeaders(Collections.singletonList("Authorization"));
		configuration.addAllowedOrigin("http://localhost:8080");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}