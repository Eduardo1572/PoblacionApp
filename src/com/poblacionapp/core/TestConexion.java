package com.poblacionapp.core;

import java.util.List;
import java.util.Map;

/**
 * Clase temporal de prueba — se elimina después del Módulo 3.
 * Solo verifica que JPL conecta correctamente con Prolog.
 */
public class TestConexion {

    public static void main(String[] args) {
        
        System.out.println("=== PRUEBA DE CONEXION JPL ===\n");
        
        // 1. Inicializar el motor Prolog
        PrologEngine.inicializar();
        
        // 2. Prueba C1 — Listar continentes
        System.out.println("--- Continentes ---");
        List<Map<String, String>> continentes = PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : continentes) {
            System.out.println("  " + fila.get("Nombre") + 
                               " -> " + fila.get("Poblacion") + " millone de habitantes");
        }
        
        // 3. Prueba P4 — País más poblado de América
        System.out.println("\n--- Pais mas poblado de America ---");
        List<Map<String, String>> masPoblado = PrologEngine.paisMasPoblado("america");
        for (Map<String, String> fila : masPoblado) {
            System.out.println("  " + fila.get("Pais") + 
                               " -> " + fila.get("Poblacion") + " millones de habitantes");
        }
        
        // 4. Prueba E3 — Estado más poblado de México
        System.out.println("\n--- Estado mas poblado de Mexico ---");
        List<Map<String, String>> estado = PrologEngine.estadoMasPoblado("mexico");
        for (Map<String, String> fila : estado) {
            System.out.println("  " + fila.get("Estado") + 
                               " -> " + fila.get("Poblacion") + " miles de habitantes");
        }
        
        System.out.println("\n=== FIN DE PRUEBA ===");
    }
}