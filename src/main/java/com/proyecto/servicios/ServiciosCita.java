/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.proyecto.excepciones.ExcepcionServicio;
import com.proyecto.modelos.Cita;
import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;
import com.proyecto.repositorios.CitaRepository;
import com.proyecto.repositorios.MedicoRepository;
import com.proyecto.repositorios.PacienteRepository;
import com.proyecto.serviciosI.ServiciosCitaI;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/**
 *
 * @author ck
 */
@AllArgsConstructor
@Service
@Transactional
public class ServiciosCita implements ServiciosCitaI {

	private CitaRepository repositorioC;

	private MedicoRepository repositorioM;

	private PacienteRepository repositorioP;

	@Override
	public void save(Cita cita) {
		repositorioC.save(cita);
	}

	@Override
	public Cita crearCita(Cita cita) throws ExcepcionServicio {
		Date fechaDHoy = new Date();
		Date fecha = cita.getFHoraCita();
		String nss = cita.getPaciente().getNSS();
		String nLicencia = cita.getMedico().getnLicencia();
		List<Cita> citasPacienteEsaHora = repositorioC.buscarCitaXPacienteYHora(nss, fecha);
		List<Cita> citasMedicoEsaHora = repositorioC.buscarCitaXMedicoYHora(nLicencia, fecha);
		if (citasPacienteEsaHora.isEmpty()) {
			throw new ExcepcionServicio("Usted ya tiene una cita en esa fecha y hora");
		}
		if (citasMedicoEsaHora.isEmpty()) {
			throw new ExcepcionServicio("El medico no tiene hueco a esa hora ese dia");
		}
		if (fecha.before(fechaDHoy)) {
			throw new ExcepcionServicio("La fecha debe ser posterior a hoy");
		}
		Optional<Medico> m = repositorioM.findById(nLicencia);
		Optional<Paciente> p = repositorioP.findById(nss);
		if (!m.isPresent()) {
			throw new ExcepcionServicio("El numero de licencia no existe");
		}
		if (!p.isPresent()) {
			throw new ExcepcionServicio("El numero de SS no existe");
		}
		cita.setPaciente(p.get());
		cita.setMedico(m.get());
		return this.repositorioC.save(cita);
	}

	@Override
	public List<Cita> buscarTodasC() {
		return repositorioC.findAll();
	}

	@Override
	public void eliminarCita(int id) throws ExcepcionServicio {
		try {
			buscarXId(id);
			repositorioC.deleteById(id);
		} catch (ExcepcionServicio ex) {
			Logger.getLogger(ServiciosCita.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public List<Cita> buscarXPaciente(String nSS) throws ExcepcionServicio {
		List<Cita> listaCitas = repositorioC.buscarCitaXPaciente(nSS);
		if (listaCitas.isEmpty()) {
			throw new ExcepcionServicio("el nSS no existe");
		}
		return listaCitas;
	}

	@Override
	public List<Cita> buscarXMedico(String nLicencia) throws ExcepcionServicio {
		List<Cita> listaCitas = repositorioC.buscarCitaXMedico(nLicencia);
		if (listaCitas.isEmpty()) {
			throw new ExcepcionServicio("el numero de licencia no existe");
		}
		return listaCitas;
	}

	@Override
	public void eliminarTodasXPaciente(String nSS) throws ExcepcionServicio {
		List<Cita> listaCitas = buscarXPaciente(nSS);
		if (listaCitas.isEmpty()) {
			throw new ExcepcionServicio("El numero de SS no existe");
		}
		repositorioC.deleteAllInBatch(listaCitas);
	}

	@Override
	public Medico buscarMiMedico(String nSS) throws ExcepcionServicio {
		Optional<Medico> optMedico = repositorioC.buscarMmedico(nSS);
		if (optMedico.isPresent()) {
			return optMedico.get();
		}
		throw new ExcepcionServicio("Paciente con nSS no existe o no tiene citas");

	}

	@Override
	public Cita buscarXId(int id) throws ExcepcionServicio {
		Optional<Cita> optCita = repositorioC.findById(id);
		if (optCita.isPresent()) {
			return optCita.get();
		}
		throw new ExcepcionServicio("La id no existe");

	}
}
