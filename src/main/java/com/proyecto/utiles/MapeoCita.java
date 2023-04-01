package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.CitaDTO;
import com.proyecto.modelos.Cita;

@Mapper(componentModel = "spring")
public interface MapeoCita {
	Cita convertirAEntidadC(CitaDTO dTO);

	CitaDTO convertirADTOC(Cita entidad);


}
