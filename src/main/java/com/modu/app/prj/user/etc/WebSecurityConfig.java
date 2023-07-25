package com.modu.app.prj.user.etc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
		@Bean   //== @Component
	   PasswordEncoder passwordEncoder() {
	      return new BCryptPasswordEncoder();
	   }
	   
	   @Bean
	   CustomFailureHandler authenticationFailureHandler() {
	      return new CustomFailureHandler();
	   }
	   
	   @Bean
	   CustomSuccessHandler authenticationSuccessHandler() {
	      return new CustomSuccessHandler();
	   }
	   
	   @Bean
	   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	       http
	           .csrf().disable()
	           .authorizeHttpRequests()
	           .antMatchers("/assets/**", "/signup/**", "/sms/**", "/modu/**").permitAll()
	           .anyRequest().authenticated()
	           .and()
	           .formLogin()
	           .passwordParameter("pwd")
	           .successHandler(authenticationSuccessHandler())
	           .failureHandler(authenticationFailureHandler())
	           .loginPage("/login")
	           .failureUrl("/login?error=true") // 로그인 실패(비밀번호 틀림)
	           .permitAll()
	           .and()
	           .logout((logout) -> logout.permitAll());

	       return http.build();
	   }

}