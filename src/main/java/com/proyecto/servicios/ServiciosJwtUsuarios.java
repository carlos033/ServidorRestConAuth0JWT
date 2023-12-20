/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;
import com.proyecto.repositorios.MedicoRepository;
import com.proyecto.repositorios.PacienteRepository;
import com.proyecto.serviciosI.ServicioJwtUsuario;

import lombok.AllArgsConstructor;

/**
 *
 * @author ck
 */
@AllArgsConstructor
@Service
public class ServiciosJwtUsuarios implements ServicioJwtUsuario {

	private MedicoRepository medicoRepository;

	private PacienteRepository pacienteRepository;

	private UserDetails cargarMedicoPorIdentificador(String identificador) {
		Optional<Medico> optMedico = medicoRepository.findById(identificador);
		if (optMedico.isPresent()) {
			Medico m = optMedico.get();
			List<GrantedAuthority> authorities = new ArrayList<>();
			return new User(m.getnLicencia(), m.getPassword(), authorities);
		}
		throw new UsernameNotFoundException("Usuario no encontrado con identificador: " + identificador);

	}

	private UserDetails cargarPacientePorIdentificador(String identificador) {
		Optional<Paciente> optPaciente = pacienteRepository.findById(identificador);
		if (optPaciente.isPresent()) {
			Paciente p = optPaciente.get();
			return new User(p.getNSS(), p.getPassword(), new ArrayList<>());
		}
		throw new UsernameNotFoundException("Usuario no encontrado con identificador: " + identificador);
	}

	@Override
	public UserDetails loadUserByUsername(String identificador) {
		if (identificador.startsWith("m")) {
			return cargarMedicoPorIdentificador(identificador);
		} else if (identificador.startsWith("es")) {
			return cargarPacientePorIdentificador(identificador);
		}
		return null;
	}
}
