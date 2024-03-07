package uniandes.dpoo.aerolinea.persistencia;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import org.json.JSONArray;
import org.json.JSONObject;

import uniandes.dpoo.aerolinea.modelo.Aerolinea;
import uniandes.dpoo.aerolinea.modelo.Aeropuerto;
import uniandes.dpoo.aerolinea.modelo.Avion;
import uniandes.dpoo.aerolinea.modelo.Ruta;
import uniandes.dpoo.aerolinea.modelo.Vuelo;
import uniandes.dpoo.aerolinea.tiquetes.Tiquete;

public class PersistenciaAerolineaJson implements IPersistenciaAerolinea{
	
	private static final String NOMBRE_AVION = "nombre";
	private static final String CAPACIDAD_AVION = "capacidad";
	
//	LLAVES RUTA
	private static final String HORA_SALIDA = "horaSalida";
	private static final String HORA_LLEGADA = "horaLlegada";
	private static final String CODIGO_RUTA = "codigoRuta";
	private static final String DESTINO = "destino";
	private static final String ORIGEN = "origen";
	
//	LLAVES VUELOS
	private static final String FECHA_VUELO = "fecha";
	private static final String AVION_VUELO = "nombreAvion";
	private static final String RUTA_VUELO = "codigoRuta";
	
//	LLAVES AEROPUERTO
	private static final String NOMBRE_AEROPUERTO = "nombre";
	private static final String CODIGO = "codigo";
	private static final String NOMBRE_CIUDAD = "nombreCiudad";
	private static final String LATITUD = "latitud";
	private static final String LONGITUD = "longitud";
	
	@Override
    public void cargarAerolinea(String archivo, Aerolinea aerolinea) throws Exception{
    	String jsonCompleto = new String( Files.readAllBytes( new File( archivo ).toPath( ) ) );
        JSONObject raiz = new JSONObject( jsonCompleto );
        JSONArray aviones = raiz.getJSONArray("aviones");
        JSONArray rutas = raiz.getJSONArray("rutas");
        JSONArray vuelos = raiz.getJSONArray("vuelos");
        cargarAviones(aerolinea, aviones);
        cargarRutas(aerolinea, rutas);
        cargarVuelos(aerolinea, vuelos);
        
	}
    
	@Override
	public void salvarAerolinea(String archivo, Aerolinea aerolinea) throws IOException {
		JSONObject jAerolinea = new JSONObject( );
		salvarAviones(aerolinea, jAerolinea);
		salvarRutas(aerolinea, jAerolinea);
		salvarVuelos(aerolinea, jAerolinea);
		
		PrintWriter pw = new PrintWriter( archivo );
		jAerolinea.write( pw, 2, 0 );
		pw.close( );
	}
	
    public void cargarAviones(Aerolinea aerolinea, JSONArray jAviones ) {
    	for (int i = 0; i < jAviones.length(); i++) {
			JSONObject jAvion = jAviones.getJSONObject(i);
			Avion avion = cargarAvion(jAvion);
			aerolinea.agregarAvion(avion);
		}
    }
    
    public void cargarRutas(Aerolinea aerolinea, JSONArray jRutas) {
    	for (int i = 0; i < jRutas.length(); i++) {
			JSONObject jRuta = jRutas.getJSONObject(i);
			Ruta ruta = cargarRuta(jRuta);
			aerolinea.agregarRuta(ruta);
		}
    }
    
    public void cargarVuelos(Aerolinea aerolinea, JSONArray jVuelos) throws Exception{
    	for (int i = 0; i < jVuelos.length(); i++) {
			JSONObject jVuelo = jVuelos.getJSONObject(i);
			String fecha = jVuelo.getString(FECHA_VUELO);
			String nombreAvion = jVuelo.getString(AVION_VUELO);
			String codigoRuta = jVuelo.getString(RUTA_VUELO);
			aerolinea.programarVuelo(fecha, codigoRuta, nombreAvion);
			
		}
    }
    
    public Avion cargarAvion(JSONObject jAvion) {
    	String nombre = jAvion.getString(NOMBRE_AVION);
    	int capacidad = jAvion.getInt(CAPACIDAD_AVION);
    	Avion avion = new Avion(nombre, capacidad);
    	return avion;
    }
    
    public Ruta cargarRuta(JSONObject jRuta) {
    	String horaSalida = jRuta.getString(HORA_SALIDA);
    	String horaLlegada = jRuta.getString(HORA_LLEGADA);
    	String codigoRuta = jRuta.getString(CODIGO_RUTA);
    	Aeropuerto origen = cargarAeropuerto(jRuta.getJSONObject(ORIGEN));
    	Aeropuerto destino = cargarAeropuerto(jRuta.getJSONObject(DESTINO));
    	Ruta ruta = new Ruta(horaSalida, horaLlegada, codigoRuta, origen, destino);
    	return ruta;
    }
    
    public Aeropuerto cargarAeropuerto(JSONObject jAeropuerto) {
    	Aeropuerto aeropuerto;
    	String nombre = jAeropuerto.getString(NOMBRE_AEROPUERTO);
    	String codigo = jAeropuerto.getString(CODIGO);
    	String nombreCiudad = jAeropuerto.getString(NOMBRE_CIUDAD);
    	double latitud = jAeropuerto.getDouble(LATITUD);
    	double longitud = jAeropuerto.getDouble(LONGITUD);
    	aeropuerto = new Aeropuerto(nombre, codigo, nombreCiudad, latitud, longitud);
    	return aeropuerto;
    }
    
    public void salvarAviones(Aerolinea aerolinea, JSONObject jobject) {
    	JSONArray jAviones = new JSONArray();
    	for (Avion avion : aerolinea.getAviones()) {
    		jAviones.put(salvarAvion(avion));
		}
    	
    	jobject.put("aviones", jAviones);
    	
    }
    
    public void salvarRutas(Aerolinea aerolinea, JSONObject jObject) {
    	JSONArray jRutas = new JSONArray();
    	for (Ruta ruta : aerolinea.getRutas()) {
			JSONObject jRuta = salvarRuta(ruta);
			jRutas.put(jRuta);
		}
    	
    	jObject.put("rutas", jRutas);
    }
    
    public void salvarVuelos(Aerolinea aerolinea, JSONObject jObject) {
    	JSONArray jVuelos= new JSONArray();
    	for (Vuelo vuelo: aerolinea.getVuelos()) {
			JSONObject jVuelo = new JSONObject();
			jVuelo.put(FECHA_VUELO, vuelo.getFecha());
			jVuelo.put(RUTA_VUELO, vuelo.getRuta().getCodigoRuta());
			jVuelo.put(AVION_VUELO, vuelo.getAvion().getNombre());
			
			jVuelos.put(jVuelo);
		}
    	jObject.put("vuelos", jVuelos);
    }
    
    public JSONObject salvarRuta(Ruta ruta) {
    	JSONObject jRuta = new JSONObject();
    	jRuta.put(HORA_SALIDA, ruta.getHoraSalida());
    	jRuta.put(HORA_LLEGADA, ruta.getHoraLlegada());
    	jRuta.put(CODIGO_RUTA, ruta.getCodigoRuta());
    	jRuta.put(ORIGEN, salvarAeropuerto(ruta.getOrigen()));
    	jRuta.put(DESTINO, salvarAeropuerto(ruta.getDestino()));
    	return jRuta;
    	
    }
    
    public JSONObject salvarAeropuerto(Aeropuerto aeropuerto) {
    	JSONObject jAeropuerto = new JSONObject();
    	jAeropuerto.put(NOMBRE_AEROPUERTO, aeropuerto.getNombre());
    	jAeropuerto.put(CODIGO, aeropuerto.getCodigo());
    	jAeropuerto.put(NOMBRE_CIUDAD, aeropuerto.getNombreCiudad());
    	jAeropuerto.put(LATITUD, aeropuerto.getLatitud());
    	jAeropuerto.put(LONGITUD, aeropuerto.getLongitud());
    	
    	return jAeropuerto;
    }
    
    public JSONObject salvarAvion(Avion avion) {
    	JSONObject jAvion = new JSONObject( );
		jAvion.put(NOMBRE_AVION, avion.getNombre());
		jAvion.put(CAPACIDAD_AVION, avion.getCapacidad());
		
		return jAvion;
    }
    
}
