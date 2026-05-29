package com.poblacionapp.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jpl7.Atom;
import org.jpl7.JPL;
import org.jpl7.Query;
import org.jpl7.Term;

/**
 * PrologEngine — Clase puente entre Java y SWI-Prolog via JPL.
 * 
 */

public class PrologEngine {

    static {
        try {
            System.load("C:/Program Files/swipl/bin/libswipl.dll");
            System.out.println("✓ libswipl.dll cargada correctamente.");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("✗ No se pudo cargar libswipl.dll");
            System.err.println("  Detalle: " + e.getMessage());
        }
    }
    
    private static final String RUTA_BASE = 
        "C:/NetBeansProjects/PoblacionApp/prolog/";
    
    private static final String BASE_CONOCIMIENTOS = 
        RUTA_BASE + "base_de_conocimientos.pl";
    
    private static final String REGLAS = 
        RUTA_BASE + "reglas.pl";
    
    private static boolean inicializado = false;

    
    public static void inicializar() {
        if (!inicializado) {
            // Carga ambos archivos Prolog en la máquina virtual de SWI
            Query q1 = new Query("consult('" + BASE_CONOCIMIENTOS + "')");
            Query q2 = new Query("consult('" + REGLAS + "')");
            
            if (q1.hasSolution() && q2.hasSolution()) {
                System.out.println("✓ Base de conocimientos cargada correctamente.");
                inicializado = true;
            } else {
                System.err.println("✗ Error al cargar los archivos Prolog.");
                System.err.println("  Verifica las rutas en PrologEngine.java");
            }
        }
    }

    // ----------------------------------------------------------------
    // MÉTODO AUXILIAR — ejecuta una consulta y devuelve TODOS los
    // resultados como lista de mapas {variable → valor}.
    // ----------------------------------------------------------------
    private static List<Map<String, String>> ejecutarConsulta(String consulta) {
        List<Map<String, String>> resultados = new ArrayList<>();
        
        try {
            Query q = new Query(consulta);
            
            while (q.hasMoreSolutions()) {
                Map<String, Term> solucion = q.nextSolution();
                Map<String, String> fila = new HashMap<>();
                
                for (Map.Entry<String, Term> entrada : solucion.entrySet()) {
                    fila.put(entrada.getKey(), termAString(entrada.getValue()));
                }
                resultados.add(fila);
            }
        } catch (Exception e) {
            System.err.println("Error en consulta Prolog: " + consulta);
            System.err.println("Detalle: " + e.getMessage());
        }
        
        return resultados;
    }

    private static String termAString(Term t) {
        if (t.isAtom()) {
            return t.name();           // átomo → su nombre como String
        } else if (t.isInteger()) {
            return String.valueOf(t.longValue());   // entero → número
        } else if (t.isFloat()) {
            return String.valueOf(t.doubleValue()); // flotante → decimal
        } else {
            return t.toString();       // cualquier otro caso
        }
    }
    
    // ----------------------------------------------------------------
    // NORMALIZACIÓN — convierte entrada del usuario a formato Prolog.
    // Elimina acentos y convierte a minúsculas para búsqueda flexible.
    // ----------------------------------------------------------------
    public static String normalizar(String texto) {
        return texto.toLowerCase()
                    .replace("á", "a").replace("é", "e")
                    .replace("í", "i").replace("ó", "o")
                    .replace("ú", "u").replace("ñ", "n")
                    .replace(" ", "_");
    }

    // ================================================================
    // CONSULTAS — NIVEL CONTINENTE
    // ================================================================

    /** C1: Devuelve lista con todos los continentes y su población. */
    public static List<Map<String, String>> obtenerContinentes() {
        return ejecutarConsulta("continente(Nombre, Poblacion)");
    }

    /** C4: Devuelve el continente más poblado. */
    public static List<Map<String, String>> continenteMasPoblado() {
        return ejecutarConsulta("continente_mas_poblado(Continente, Poblacion)");
    }

    // ================================================================
    // CONSULTAS — NIVEL PAÍS
    // ================================================================

    /** P1: Devuelve todos los países con continente y población. */
    public static List<Map<String, String>> obtenerPaises() {
        return ejecutarConsulta("pais(Nombre, Continente, Poblacion)");
    }

    /** P3: Devuelve países filtrados por continente. */
    public static List<Map<String, String>> paisesPorContinente(String continente) {
        String c = normalizar(continente);
        return ejecutarConsulta("pais(Nombre, " + c + ", Poblacion)");
    }

    /** P4: País más poblado de un continente. */
    public static List<Map<String, String>> paisMasPoblado(String continente) {
        String c = normalizar(continente);
        return ejecutarConsulta(
            "pais_mas_poblado(" + c + ", Pais, Poblacion)");
    }

    /** P5: País menos poblado de un continente. */
    public static List<Map<String, String>> paisMenosPoblado(String continente) {
        String c = normalizar(continente);
        return ejecutarConsulta(
            "pais_menos_poblado(" + c + ", Pais, Poblacion)");
    }

    // ================================================================
    // CONSULTAS — NIVEL ESTADO
    // ================================================================

    /** E1: Estados de un país con su población. */
    public static List<Map<String, String>> estadosPorPais(String pais) {
        String p = normalizar(pais);
        return ejecutarConsulta("estado(Nombre, " + p + ", Poblacion)");
    }

    /** E3: Estado más poblado de un país. */
    public static List<Map<String, String>> estadoMasPoblado(String pais) {
        String p = normalizar(pais);
        return ejecutarConsulta(
            "estado_mas_poblado(" + p + ", Estado, Poblacion)");
    }

    /** E4: Estado menos poblado de un país. */
    public static List<Map<String, String>> estadoMenosPoblado(String pais) {
        String p = normalizar(pais);
        return ejecutarConsulta(
            "estado_menos_poblado(" + p + ", Estado, Poblacion)");
    }

    // ================================================================
    // CONSULTAS — NIVEL CIUDAD
    // ================================================================

    /** Cd1: Ciudades de un estado con su población. */
    public static List<Map<String, String>> ciudadesPorEstado(String estado) {
        String e = normalizar(estado);
        return ejecutarConsulta("ciudad(Nombre, " + e + ", Poblacion)");
    }

    /** Cd4: Ciudad más poblada de un estado. */
    public static List<Map<String, String>> ciudadMasPoblada(String estado) {
        String e = normalizar(estado);
        return ejecutarConsulta(
            "ciudad_mas_poblada(" + e + ", Ciudad, Poblacion)");
    }

    /** Cd3: País y continente al que pertenece una ciudad. */
    public static List<Map<String, String>> ubicacionCiudad(String ciudad) {
        String c = normalizar(ciudad);
        return ejecutarConsulta(
            "ciudad_en_pais(" + c + ", Pais)");
    }
}