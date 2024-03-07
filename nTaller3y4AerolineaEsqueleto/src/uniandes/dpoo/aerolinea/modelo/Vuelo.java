package uniandes.dpoo.aerolinea.modelo;

import java.util.Collection;
import java.util.Map;

import uniandes.dpoo.aerolinea.exceptions.VueloSobrevendidoException;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.tarifas.CalculadoraTarifas;
import uniandes.dpoo.aerolinea.tiquetes.GeneradorTiquetes;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class Vuelo {
	private String fecha;
	private Avion avion;
	private Ruta ruta;
	private Map<String, Tiquete> tiquetes;
	
	public Vuelo(Ruta ruta, String fecha, Avion avion) {
		this.fecha = fecha;
		this.avion = avion;
		this.ruta = ruta;
	}

	public String getFecha() {
		return fecha;
	}

	public Avion getAvion() {
		return avion;
	}

	public Ruta getRuta() {
		return ruta;
	}

	public Collection<Tiquete> getTiquetes() {
		return tiquetes.values();
	}

	public int venderTiquetes(Cliente cliente, CalculadoraTarifas calculadora, int cantidad) throws Exception{
		int precio_unitario = calculadora.calcularTarifa(this, cliente);
		int precio_total = precio_unitario * cantidad;
		
		int capacidad_avion = avion.getCapacidad();
		int vendidos = tiquetes.size();
		int capacidad_actual = capacidad_avion - vendidos;
		
		if (cantidad <= capacidad_actual) {
			for (int i = 0; i < cantidad; i++) {
				Tiquete nuevoTiquete = GeneradorTiquetes.generarTiquete(this, cliente, precio_unitario);
				GeneradorTiquetes.registrarTiquete(nuevoTiquete);
				tiquetes.put(nuevoTiquete.getCodigo(), nuevoTiquete);
			}
		} else {
			throw new VueloSobrevendidoException(this); 
		}
		
		return precio_total;
	}
	
	public boolean equals(Object obj) {
		return obj.equals(obj);
	}
	
}
