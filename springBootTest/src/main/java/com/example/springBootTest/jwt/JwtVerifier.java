package com.example.springBootTest.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtVerifier extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;
	private final SecretKey jwtSecretKey;

	public JwtVerifier(JwtConfig jwtConfig, SecretKey jwtSecretKey) {
		this.jwtConfig = jwtConfig;
		this.jwtSecretKey = jwtSecretKey;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest httpServletRequest,
									HttpServletResponse httpServletResponse,
									FilterChain filterChain) throws ServletException, IOException {

		String authorizationHeader = httpServletRequest.getHeader(jwtConfig.authHeader());

		if(Strings.isNullOrEmpty(authorizationHeader) ||
				!authorizationHeader.startsWith(jwtConfig.getTokenPrefix())){
			filterChain.doFilter(httpServletRequest,
					httpServletResponse);
			return;
		}

		String token = authorizationHeader.replace(jwtConfig.getTokenPrefix(), "");

		try {
			Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(jwtSecretKey)
					.build()
					.parseClaimsJws(token);

			Claims body = claimsJws.getBody();
			String username = body.getSubject();
			var authorities = (List<Map<String,String>>) body.get("authorities");

			Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream().map(
					m -> new SimpleGrantedAuthority(m.get("authority")))
					.collect(Collectors.toSet());

			Authentication authentication = new UsernamePasswordAuthenticationToken(
					username,
					null,
					simpleGrantedAuthorities
			);
			SecurityContextHolder.getContext().setAuthentication(authentication);

		}catch (JwtException e){
			throw new IllegalAccessError("Token cannot be trusted: "
			+token);
		}
		filterChain.doFilter(httpServletRequest,httpServletResponse);
	}
}
