/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.proyecto.servicios.ServiciosJwtUsuarios;

/**
 *
 * @author ck
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private ServiciosJwtUsuarios jwtUserDetailsService;

	@Autowired
	private final JwtToken jwtTokenUtil;

	public JwtRequestFilter(JwtToken jwtTokenUtil) {
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("Authorization");
		String identifier = null;
		String jwtToken = null;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				System.out.println(
						"\n\n" + jwtToken + "\n\n");
				identifier = jwtTokenUtil.obtenerIdentificadorDelToken(jwtToken);
			} catch (JWTDecodeException exception) {
				logger.error("No se pudo obtener el token JWT o el token JWT ha expirado", exception);
			}
		}
		if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(identifier);
			if (jwtTokenUtil.validateToken(jwtToken, identifier)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
