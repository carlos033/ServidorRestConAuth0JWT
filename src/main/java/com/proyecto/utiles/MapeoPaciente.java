package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.PacienteDTO;
import com.proyecto.modelos.Paciente;

@Mapper(componentModel = "spring")
public interface MapeoPaciente {

	PacienteDTO convertirADTOP(Paciente entidad);

	Paciente convertirAEntidadP(PacienteDTO dto);

}
