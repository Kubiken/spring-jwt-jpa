package com.example.springBootTest.security;

import com.example.springBootTest.auth.ApplicationUserService;
import com.example.springBootTest.jwt.JwtConfig;
import com.example.springBootTest.jwt.JwtUsernameAndPasswordFilter;
import com.example.springBootTest.jwt.JwtVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecureConfig extends WebSecurityConfigurerAdapter {


	private final PasswordEncoder passwordEncoder;
	private final ApplicationUserService applicationUserService;
	private final JwtConfig jwtConfig;
	private final SecretKey jwtSecretKey;

	@Autowired
	public AppSecureConfig(PasswordEncoder passwordEncoder,
						   ApplicationUserService applicationUserService, JwtConfig jwtConfig, SecretKey jwtSecretKey) {
		this.passwordEncoder = passwordEncoder;
		this.applicationUserService = applicationUserService;
		this.jwtConfig = jwtConfig;
		this.jwtSecretKey = jwtSecretKey;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(new JwtUsernameAndPasswordFilter(authenticationManager(), jwtConfig, jwtSecretKey))
				.addFilterAfter(new JwtVerifier(jwtConfig, jwtSecretKey), JwtUsernameAndPasswordFilter.class)
				.authorizeRequests()
				.antMatchers("/","index","/css/*, /js/*").permitAll()
				.anyRequest()
				.authenticated();

//				.and()
//				.httpBasic(); Basic authentication
//				.formLogin() form-based auth
//				.loginPage("/login").permitAll()
//				.defaultSuccessUrl("/api/v1/student/get", true)
//				.and()
//				.rememberMe()
//					.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(14))
//					.key("vakakobatrucci")
//				.and()
//				.logout()
//					.logoutUrl("/logout")
//					.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//					.clearAuthentication(true)
//					.invalidateHttpSession(true)
//					.deleteCookies("JSESSIONID", "remember-me")
//					.logoutSuccessUrl("/login");
	}

	//straight in-memory auth
//	@Override
//	@Bean
//	protected UserDetailsService userDetailsService() {
//		UserDetails vaskez = User.builder()
//				.username("Vaskez")
//				.password(passwordEncoder.encode("samurai"))
//				.authorities(STUDENT.getGrantedAuthority())
//				.build();
//
//		UserDetails zhmyh = User.builder()
//				.username("Zhmyshenko Valeriy")
//				.password(passwordEncoder.encode("vamban"))
//				.authorities(ADMIN.getGrantedAuthority())
//				.build();
//
//		UserDetails silverhand = User.builder()
//				.username("Jonny")
//				.password(passwordEncoder.encode("diearasaka"))
//				.authorities(ADMINTRAINEE.getGrantedAuthority())
//				.build();
//
//		return new InMemoryUserDetailsManager(vaskez, zhmyh, silverhand);
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(daoAuthenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider(){
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder);
		provider.setUserDetailsService(applicationUserService);
		return provider;
	}
}
