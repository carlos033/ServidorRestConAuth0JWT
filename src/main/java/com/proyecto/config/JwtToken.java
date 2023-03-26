/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.config;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.proyecto.modelos.Logable;
import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 *
 * @author ck
 */
@Component
public class JwtToken implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public static final long JWT_TOKEN_VALIDITY = 30 * 60 * 60;

	private final String secret;

	public JwtToken(@Value("${jwt.secret}") String secret) {
		this.secret = secret;
	}

	public String obtenerIdentificadorDelToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public Date ObtenerVencimientoDelToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean aExpiradoElToken(String token) {
		final Date expiration = ObtenerVencimientoDelToken(token);
		return expiration.before(new Date());
	}

	@SuppressWarnings("unchecked")
	public boolean esMedico(String token) {
		Claims claims = getAllClaimsFromToken(token);
		Map<String, Object> userMap = (HashMap<String, Object>) claims.get("usuario");
		return userMap.get("licencia") != null;
	}

	public String generarToken(UserDetails userDetails, Logable user) {
		Map<String, Object> claims = new HashMap<>();
		Map<String, Object> userMap = new HashMap<>();
		Medico medico = null;
		Paciente paciente = null;
		if (user instanceof Medico) {
			userMap.put("licencia", user.getIdentifier());
			medico = (Medico) user;
			userMap.put("nombre", medico.getNombre());
		} else if (user instanceof Paciente) {
			userMap.put("nss", user.getIdentifier());
			paciente = (Paciente) user;
			userMap.put("nombre", paciente.getNombre());
		}
		claims.put("usuario", userMap);
		return doGenerateToken(claims, userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String subject) {
		byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
		Key signingKey = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, signingKey).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String identifier = obtenerIdentificadorDelToken(token);
		return (identifier.equals(userDetails.getUsername()) && !aExpiradoElToken(token));
	}

}
