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
import com.proyecto.servicios.ServiciosMedico;

/**
 *
 * @author ck
 */
@Component
public class PrecargaBD {

	@Autowired
	private ServiciosMedico sMedico;

	@Transactional
	public void precargarBaseDeDatos() {
		try {
			// lanza excepcion si no lo encuentra, entonces en ese caso precargamos
			sMedico.buscarMedico("M1");
		} catch (ExcepcionServicio ex) {
			List<Cita> listaCitas = new ArrayList<>();
			List<Informe> listaInformes = new ArrayList<>();

			Medico m = new Medico("M1", "Admin", "Administrador", 0, "1234", null, listaCitas, listaInformes);
			sMedico.saveMedico(m);
		}
	}

}
