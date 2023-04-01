package com.proyecto.utiles;

import org.mapstruct.Mapper;

import com.proyecto.dto.HospitalDTO;
import com.proyecto.modelos.Hospital;

@Mapper(componentModel = "spring")
public interface MapeoHospital {

	Hospital convertirAEntidadH(HospitalDTO dTO);

	HospitalDTO convertirADTOH(Hospital entidad);

}
