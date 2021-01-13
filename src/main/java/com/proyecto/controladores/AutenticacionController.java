/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.controladores;

import com.proyecto.config.JwtToken;
import com.proyecto.dto.jwt.JwtRequest;
import com.proyecto.dto.jwt.JwtResponse;
import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;
import com.proyecto.servicios.ServiciosJwtUsuarios;
import com.proyecto.servicios.ServiciosMedico;
import com.proyecto.servicios.ServiciosPaciente;
import java.util.Collection;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ck
 */
@RestController
@CrossOrigin
@RequestMapping("/autenticacion")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtToken jwtToken;

    @Autowired
    private ServiciosJwtUsuarios jwtUserDetailsService;

    @Autowired
    private ServiciosMedico sMedico;

    @Autowired
    private ServiciosPaciente sPaciente;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        authenticationRequest.setIdentificador(authenticationRequest.getIdentificador().toLowerCase());
        final UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(authenticationRequest.getIdentificador());
        authenticate(authenticationRequest.getIdentificador(), authenticationRequest.getPassword(), userDetails.getAuthorities());
        String token = "";
        if (authenticationRequest.getIdentificador().toUpperCase().startsWith("M")) {
            Optional<Medico> optMedico = sMedico.buscarMedico(authenticationRequest.getIdentificador());
            token = jwtToken.generarToken(userDetails, optMedico.get());
        } else if (authenticationRequest.getIdentificador().toUpperCase().startsWith("ES")) {
            Optional<Paciente> optPaciente = sPaciente.buscarPaciente(authenticationRequest.getIdentificador());
            token = jwtToken.generarToken(userDetails, optPaciente.get());
        }
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @Transactional
    private void authenticate(String username, String password, Collection<? extends GrantedAuthority> authorities) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password, authorities));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Credenciales invalidas");
        }
    }
}
