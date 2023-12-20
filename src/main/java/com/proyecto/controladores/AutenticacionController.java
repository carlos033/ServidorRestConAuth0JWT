/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.controladores;

import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.proyecto.config.JwtToken;
import com.proyecto.dto.jwt.JwtRequest;
import com.proyecto.dto.jwt.JwtResponse;
import com.proyecto.servicios.ServiciosJwtUsuarios;
import com.proyecto.servicios.ServiciosMedico;
import com.proyecto.servicios.ServiciosPaciente;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 *
 * @author ck
 */
@RestController
@CrossOrigin
@RequestMapping("/autenticacion")
@AllArgsConstructor
public class AutenticacionController {

	private AuthenticationManager authenticationManager;

	private JwtToken jwtToken;

	private ServiciosJwtUsuarios jwtUserDetailsService;

	private ServiciosMedico sMedico;

	private ServiciosPaciente sPaciente;

	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
		authenticationRequest.setIdentificador(authenticationRequest.getIdentificador().toLowerCase());
		final UserDetails userDetails = jwtUserDetailsService
				.loadUserByUsername(authenticationRequest.getIdentificador());
		authenticate(authenticationRequest.getIdentificador(), authenticationRequest.getPassword(),
				userDetails.getAuthorities());

		String token = generateToken(authenticationRequest.getIdentificador());
		return ResponseEntity.ok(new JwtResponse(token));
	}

	private String generateToken(String identificador) throws Exception {
		if (identificador.toUpperCase().startsWith("M")) {
			return sMedico.buscarMedico(identificador).map(jwtToken::generarToken)
					.orElseThrow(() -> new Exception("No se encontró el médico con identificador " + identificador));
		} else if (identificador.toUpperCase().startsWith("ES")) {
			return sPaciente.buscarPaciente(identificador).map(jwtToken::generarToken)
					.orElseThrow(() -> new Exception("No se encontró el paciente con identificador " + identificador));
		} else {
			throw new Exception("Tipo de identificador no soportado");
		}
	}

	@Transactional
	private void authenticate(String username, String password, Collection<? extends GrantedAuthority> authorities)
			throws Exception {
		try {
			authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(username, password, authorities));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credenciales invalidas");
		}
	}
}
