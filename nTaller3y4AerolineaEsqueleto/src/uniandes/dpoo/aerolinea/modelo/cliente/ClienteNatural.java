package uniandes.dpoo.aerolinea.modelo.cliente;

public class ClienteNatural extends Cliente{
	
	private String nombre;
	public static String NATURAL = "Natural";

	public ClienteNatural(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipoCliente() {
		return NATURAL;
	}

	public String getIdentificador() {
		return nombre;
	}


}
