package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.InformeMedicoDTO;
import com.proyecto.modelos.Informe;

@Mapper(componentModel = "spring")
public interface MapeoInformeMedico {
	InformeMedicoDTO convertirADTOIM(Informe entidad);

}
