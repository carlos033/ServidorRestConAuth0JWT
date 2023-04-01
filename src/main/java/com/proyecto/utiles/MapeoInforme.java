package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.InformeCompletoDTO;
import com.proyecto.dto.InformeDTO;
import com.proyecto.dto.InformeMedicoDTO;
import com.proyecto.dto.InformePacienteDTO;
import com.proyecto.modelos.Informe;

@Mapper(componentModel = "spring")
public interface MapeoInforme {
	Informe convertirAEntidadI(InformePacienteDTO dTO);

	Informe convertirAEntidadI(InformeMedicoDTO dTO);

	Informe convertirAEntidadI(InformeCompletoDTO dTO);

	Informe convertirAEntidadI(InformeDTO dTO);

	InformeDTO convertirADTOI(Informe entidad);

}
