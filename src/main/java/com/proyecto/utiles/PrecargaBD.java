/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.proyecto.utiles;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.proyecto.excepciones.ExcepcionServicio;
import com.proyecto.modelos.Cita;
import com.proyecto.modelos.Hospital;
import com.proyecto.modelos.Informe;
import com.proyecto.modelos.Medico;
import com.proyecto.servicios.ServiciosHospital;
import com.proyecto.servicios.ServiciosMedico;

/**
 *
 * @author ck
 */
@Component
public class PrecargaBD {

	@Autowired
	private ServiciosMedico sMedico;
	@Autowired
	private ServiciosHospital sHos;

	@Transactional
	public void precargarBaseDeDatos() {
		try {
			// lanza excepcion si no lo encuentra, entonces en ese caso precargamos
			sMedico.buscarMedico("M1");
		} catch (ExcepcionServicio ex) {
			List<Medico> listaAdmin = new ArrayList<>();
			List<Cita> listaCitas = new ArrayList<>();
			List<Informe> listaInformes = new ArrayList<>();
			Hospital administracion = new Hospital("administracion", "Madrdid", "0", listaAdmin);
			Medico m = new Medico("M1", "Admin", "Administrador", 0, "1234", administracion, listaCitas, listaInformes);
			listaAdmin.add(m);
			sHos.save(administracion);
			sMedico.saveMedico(m);
		}
	}

}
