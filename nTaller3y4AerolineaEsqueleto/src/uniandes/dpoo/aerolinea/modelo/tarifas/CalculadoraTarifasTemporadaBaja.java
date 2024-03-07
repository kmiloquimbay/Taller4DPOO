package uniandes.dpoo.aerolinea.modelo.tarifas;

import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.modelo.cliente.Cliente;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteCorporativo;
import uniandes.dpoo.aerolinea.modelo.cliente.ClienteNatural;

public class CalculadoraTarifasTemporadaBaja extends CalculadoraTarifas{
	
	protected static int COSTO_POR_KM_NATURAL = 600;
	protected static int COSTO_POR_KM_CORPORATIVO = 900;
	protected static double DESCUENTO_PEQ = 0.02;
	protected static double DESCUENTO_MEDIANAS = 0.1;
	protected static double DESCUENTO_GRANDES = 0.2;
	
	// Costo = costo_por_km * distancia

	@Override
	protected int calcularCostoBase(Vuelo vuelo, Cliente cliente) {

		int costoBase;
		Ruta ruta = vuelo.getRuta();
		int distancia = calcularDistanciaVuelo(ruta);
		if (cliente.getTipoCliente() == ClienteNatural.NATURAL){
			costoBase = COSTO_POR_KM_NATURAL * distancia;	
		}
		else {
			costoBase = COSTO_POR_KM_CORPORATIVO * distancia;
		}
		return costoBase;
	}

	@Override
	protected double calcularPorcentajeDescuento(Cliente cliente) {
		double descuento;
		if(cliente.getTipoCliente() == ClienteCorporativo.CORPORATIVO) {
			ClienteCorporativo copiaCliente = (ClienteCorporativo) cliente;
			int tamanio = copiaCliente.getTamanoEmpresa();
			// Grande
			if (tamanio == 1) {
				descuento = DESCUENTO_GRANDES;
			} else if (tamanio == 2){ // Mediana
				descuento = DESCUENTO_MEDIANAS;				
			} else { //Pequeña
				descuento = DESCUENTO_PEQ;
			}
		}
		else {
			descuento = 0;
		}
		return descuento;
	}

}
