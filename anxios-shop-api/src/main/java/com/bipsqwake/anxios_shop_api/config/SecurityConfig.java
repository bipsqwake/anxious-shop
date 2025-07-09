package com.bipsqwake.anxios_shop_api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Value("${appconfig.cors.origins}")
    String[] corsOrigins;
    @Value("${appconfig.cors.methods}")
    String[] corsMethods;
    @Value("${appconfig.cors.headers}")
    String[] corsHeaders;
	

	@Bean
    public UserDetailsService userDetailsService(@Value("${appconfig.adminpass}") String adminPass) {
		log.info(adminPass);
        UserDetails user = User
            .withUsername("user")
            .password("{noop}" + adminPass)
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.authorizeHttpRequests((requests) -> requests
				.requestMatchers("/admin/**").authenticated()
				.requestMatchers("/**").permitAll()
			).httpBasic(httpBasic -> {})
            .cors((cors) -> cors.configurationSource(apiConfigurationSource()))
            .csrf((csrf) -> csrf.disable());
		return http.build();
	}

    UrlBasedCorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList(corsOrigins));
        // configuration.setAllowedOrigins(Arrays.asList(allowedOrigin));
		configuration.setAllowedMethods(Arrays.asList(corsMethods));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
