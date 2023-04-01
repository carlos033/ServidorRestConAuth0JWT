package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.InformePacienteDTO;
import com.proyecto.modelos.Informe;

@Mapper(componentModel = "spring")
public interface MapeoInformePaciente {

	InformePacienteDTO convertirADTOIP(Informe entidad);

}
