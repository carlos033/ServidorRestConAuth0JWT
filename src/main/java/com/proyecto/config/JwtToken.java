package com.proyecto.config;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.proyecto.modelos.Logable;
import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;

@Component
public class JwtToken {

	private final String secret;

	public JwtToken(@Value("${jwt.secret}") String secret) {
		this.secret = secret;
	}

	public String obtenerIdentificadorDelToken(String token) {
		return getClaimFromToken(token, DecodedJWT::getSubject);
	}

	public Instant obtenerVencimientoDelToken(String token) {
		return getClaimFromToken(token, DecodedJWT::getExpiresAt).toInstant();
	}

	public <T> T getClaimFromToken(String token, Function<DecodedJWT, T> claimsResolver) {
		final DecodedJWT decodedJWT = decodeToken(token);
		return claimsResolver.apply(decodedJWT);
	}

	private DecodedJWT decodeToken(String token) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC512(secret)).build();
			DecodedJWT jwt = verifier.verify(token);
			return jwt;
		} catch (JWTDecodeException exception) {
			throw new RuntimeException("Error decodificando el token", exception);
		}
	}

	private Boolean haExpiradoElToken(String token) {
		final Instant expiration = obtenerVencimientoDelToken(token);
		return expiration.isBefore(Instant.now());
	}

	public boolean esMedico(String token) {
		final DecodedJWT decodedJWT = decodeToken(token);
		final Map<String, Object> userMap = decodedJWT.getClaim("usuario").asMap();
		return userMap.get("licencia") != null;
	}

	public String generarToken(Logable user) {
		final Map<String, Object> claims = new HashMap<>();
		final Map<String, Object> userMap = new HashMap<>();
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
		claims.put("usuario", new HashMap<>(userMap));
		return doGenerateToken(claims);
	}

	private String doGenerateToken(Map<String, Object> claims) {
		final Algorithm algorithm = Algorithm.HMAC512(secret);
		final Instant now = Instant.now();
		final Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
		Map<String, Object> usuarioClaims = new HashMap<>();
		Map<String, Object> originalUsuarioClaims = (Map<String, Object>) claims.get("usuario");
		usuarioClaims.putAll(originalUsuarioClaims);
		return JWT.create().withIssuedAt(now).withExpiresAt(expirationTime).withClaim("usuario", usuarioClaims)
				.sign(algorithm);
	}

	public Boolean validateToken(String token, String expectedSubject) {
		try {
			final String identifier = obtenerIdentificadorDelToken(token);
			return (identifier.equals(expectedSubject) && !haExpiradoElToken(token));
		} catch (Exception e) {
			throw new RuntimeException("Error al validar el token JWT", e);
		}
	}
}