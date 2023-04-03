package com.proyecto.utiles;

import java.util.List;

import org.mapstruct.Mapper;

import com.proyecto.dto.CitaDTO;
import com.proyecto.dto.MedicoDTO;
import com.proyecto.modelos.Cita;
import com.proyecto.modelos.Medico;

@Mapper(componentModel = "spring")
public interface MapeoMedico {
	MedicoDTO convertirADTOM(Medico entidad);

	CitaDTO citaToCitaDTO(Cita cita);

	List<CitaDTO> citasToCitasDTO(List<Cita> citas);

	Medico convertirAEntidadM(MedicoDTO dTO);
}
