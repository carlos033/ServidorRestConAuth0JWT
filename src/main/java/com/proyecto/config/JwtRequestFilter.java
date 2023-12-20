/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.config;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.proyecto.servicios.ServiciosJwtUsuarios;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

/**
 *
 * @author ck
 */
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private JwtToken jwtTokenUtil;
	private ApplicationContext applicationContext;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String identifier = null;
		String jwtToken = null;

		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);

			try {
				identifier = jwtTokenUtil.obtenerIdentificadorDelToken(jwtToken);
			} catch (JWTDecodeException exception) {
				logger.error("No se pudo obtener el token JWT o el token JWT ha expirado", exception);
			}
		}

		if (shouldAuthenticate(identifier, jwtToken)) {
			authenticateAndSetSecurityContext(request, identifier, jwtToken);
		}

		chain.doFilter(request, response);
	}

	private boolean shouldAuthenticate(String identifier, String jwtToken) {
		return identifier != null && SecurityContextHolder.getContext().getAuthentication() == null
				&& jwtTokenUtil.validateToken(jwtToken, identifier);
	}

	private void authenticateAndSetSecurityContext(HttpServletRequest request, String identifier, String jwtToken) {
		ServiciosJwtUsuarios jwtUserDetailsService = applicationContext.getBean(ServiciosJwtUsuarios.class);
		UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(identifier);

		if (isTokenValid(jwtToken, identifier)) {
			setAuthenticationInSecurityContext(request, userDetails);
		}
	}

	private boolean isTokenValid(String jwtToken, String identifier) {
		return jwtTokenUtil.validateToken(jwtToken, identifier);
	}

	private void setAuthenticationInSecurityContext(HttpServletRequest request, UserDetails userDetails) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
				null, userDetails.getAuthorities());
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
	}

}
