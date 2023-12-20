/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.utiles;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.proyecto.dto.CitaDTO;
import com.proyecto.dto.HospitalDTO;
import com.proyecto.dto.InformeCompletoDTO;
import com.proyecto.dto.InformeDTO;
import com.proyecto.dto.InformeMedicoDTO;
import com.proyecto.dto.InformePacienteDTO;
import com.proyecto.dto.MedicoDTO;
import com.proyecto.dto.PacienteDTO;
import com.proyecto.modelos.Cita;
import com.proyecto.modelos.Hospital;
import com.proyecto.modelos.Informe;
import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;

import lombok.AllArgsConstructor;

/**
 *
 * @author ck
 */
@Component
@AllArgsConstructor
public class Transformadores {

	private ModelMapper modelMapper;

	public Cita convertirAEntidadC(CitaDTO dTO) {
		return modelMapper.map(dTO, Cita.class);
	}

	public CitaDTO convertirADTOC(Cita entidad) {
		return modelMapper.map(entidad, CitaDTO.class);

	}

	public Hospital convertirAEntidadH(HospitalDTO dTO) {
		return modelMapper.map(dTO, Hospital.class);

	}

	public HospitalDTO convertirADTOH(Hospital entidad) {
		return modelMapper.map(entidad, HospitalDTO.class);

	}

	public Informe convertirAEntidadI(InformeDTO dTO) {
		return modelMapper.map(dTO, Informe.class);
	}

	public InformeDTO convertirADTOI(Informe entidad) {
		return modelMapper.map(entidad, InformeDTO.class);

	}

	public Medico convertirAEntidadM(MedicoDTO dTO) {
		return modelMapper.map(dTO, Medico.class);
	}

	public MedicoDTO convertirADTOM(Medico entidad) {
		return modelMapper.map(entidad, MedicoDTO.class);
	}

	public Paciente convertirAEntidadP(PacienteDTO dto) {
		return modelMapper.map(dto, Paciente.class);
	}

	public PacienteDTO convertirADTOP(Paciente entidad) {
		return modelMapper.map(entidad, PacienteDTO.class);
	}

	public Informe convertirAEntidadI(InformePacienteDTO dTO) {
		return modelMapper.map(dTO, Informe.class);
	}

	public InformePacienteDTO convertirADTOIP(Informe entidad) {
		return modelMapper.map(entidad, InformePacienteDTO.class);
	}

	public Informe convertirAEntidadI(InformeMedicoDTO dTO) {
		return modelMapper.map(dTO, Informe.class);
	}

	public InformeMedicoDTO convertirADTOIM(Informe entidad) {
		return modelMapper.map(entidad, InformeMedicoDTO.class);
	}

	public Informe convertirAEntidadI(InformeCompletoDTO dTO) {
		return modelMapper.map(dTO, Informe.class);
	}
}
