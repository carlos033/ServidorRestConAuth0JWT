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

import com.proyecto.modelos.Informe;

/**
 *
 * @author ck
 */
public interface InformeRepository extends JpaRepository<Informe, String> {

    @Query("Select i from Informe i where i.paciente.nSS = :nSS")
    List<Informe> buscarInformeXPaciente(@Param("nSS") String nSS);

    @Query("Select i from Informe i where i.medico.nLicencia = :nLicencia")
    List<Informe> buscarInformeXMedico(@Param("nLicencia") String nLicencia);
}
