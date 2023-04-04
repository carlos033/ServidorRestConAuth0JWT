/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.modelos.Medico;
import com.proyecto.modelos.Paciente;

/**
 *
 * @author ck
 */
public interface MedicoRepository extends JpaRepository<Medico, String> {

	String BuscarPacientesXMedico = "SELECT DISTINCT p FROM Paciente p JOIN FETCH p.listaCitas c JOIN FETCH c.medico m WHERE m.nLicencia = :nLicencia";

	String BuscarMedicoXEspecialidad = "SELECT m FROM Medico m JOIN FETCH m.hospital h WHERE m.especialidad = :especialidad AND h.nombreHos = :nombreHospital";

	String Hospital = "SELECT m FROM Medico m JOIN FETCH m.hospital h WHERE h.nombreHos = :nombreHospital";

	@Query(value = BuscarPacientesXMedico)
	public List<Paciente> BuscarPacientesXMedico(@Param("nLicencia") String nLicencia);

	@Query(value = BuscarMedicoXEspecialidad)
	public List<Medico> BuscarMedicoXEspecialidad(@Param("especialidad") String especialidad,
			@Param("nombreHospital") String hospital);

	@Query(value = Hospital)
	public List<Medico> BuscarMedicosXHospital(@Param("nombreHospital") String hospitalNombre);

}
