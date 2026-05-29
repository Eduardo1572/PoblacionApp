package com.poblacionapp.ui;

import com.poblacionapp.core.PrologEngine;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class CiudadWindow extends JFrame {

    private JComboBox<String> comboEstados;
    private JTextArea areaResultados;

    public CiudadWindow() {
        initComponents();
        cargarEstados();
    }

    private void initComponents() {
        setTitle("Consulta por Ciudad");
        setSize(500, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 12)); // Ajustado a 10, 12
        panel.setBackground(new Color(28, 28, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        // --- Título ---
        JLabel lblTitulo = new JLabel("  Consulta por Ciudad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(240, 240, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0)); // Margen inferior

        // --- Etiqueta + ComboBox ---
        JLabel lblCombo = new JLabel("  Selecciona un estado:");
        lblCombo.setForeground(new Color(160, 160, 200));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboEstados = new JComboBox<>();
        comboEstados.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Ajustado a 14
        comboEstados.setBackground(Color.WHITE);
        comboEstados.setForeground(new Color(30, 30, 50));
        comboEstados.setMaximumRowCount(5); // Límite de despliegue

        // --- Panel de Botones ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10)); // Ajustado gap a 10
        panelBotones.setBackground(new Color(28, 28, 45));
        // Se agregó el margen que faltaba para separar los botones arriba y abajo
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Se agregaron los espacios en blanco iniciales en los textos de los botones
        JButton btnListar     = MainWindow.crearBoton(
            "  Listar ciudades del estado",       new Color(180, 100, 50));
        JButton btnMasPoblada = MainWindow.crearBoton(
            "  Ciudad más poblada del estado",    new Color(72, 52, 212));
        JButton btnUbicacion  = MainWindow.crearBoton(
            "  Ver país de la primera ciudad",    new Color(52, 140, 100));

        panelBotones.add(btnListar);
        panelBotones.add(btnMasPoblada);
        panelBotones.add(btnUbicacion);
        
        // --- Área de resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(18, 18, 32)); // Color estandarizado
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        areaResultados.setLineWrap(true); // Ajuste automático de línea

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 100, 50), 1));
        scroll.setPreferredSize(new Dimension(456, 150)); // Dimensión estandarizada

        // --- Acciones ---
        btnListar.addActionListener(e -> listarCiudades());
        btnMasPoblada.addActionListener(e -> mostrarMasPoblada());
        btnUbicacion.addActionListener(e -> mostrarUbicacion());
        
        // --- Ensamblaje Estructural ---
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 6));
        panelSuperior.setBackground(new Color(28, 28, 45));
        
        JPanel panelCombo = new JPanel(new BorderLayout(0, 4));
        panelCombo.setBackground(new Color(28, 28, 45));
        panelCombo.add(lblCombo, BorderLayout.NORTH);
        panelCombo.add(comboEstados, BorderLayout.CENTER);
        
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelCombo, BorderLayout.CENTER);

        // AQUÍ ESTABA EL ERROR: Se estaba agregando lblTitulo en lugar de panelSuperior
        panel.add(panelSuperior, BorderLayout.NORTH); 
        panel.add(panelBotones,  BorderLayout.CENTER);
        panel.add(scroll,        BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarEstados() {
        // Cargamos estados de los 3 países que tienen datos
        String[] paises = {"mexico", "brasil", "alemania"};
        for (String pais : paises) {
            List<Map<String, String>> estados = PrologEngine.estadosPorPais(pais);
            for (Map<String, String> fila : estados) {
                comboEstados.addItem(fila.get("Nombre"));
            }
        }
    }

    private void listarCiudades() {
        String estado = (String) comboEstados.getSelectedItem();
        if (estado == null) return;
        List<Map<String, String>> ciudades = PrologEngine.ciudadesPorEstado(estado);
        if (ciudades.isEmpty()) {
            areaResultados.setText("No hay ciudades registradas para: " + estado);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Ciudades en ").append(estado.toUpperCase()).append(":\n\n");
        for (Map<String, String> fila : ciudades) {
            sb.append("  • ").append(fila.get("Nombre"))
              .append("  →  ").append(fila.get("Poblacion")).append(" mil habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarMasPoblada() {
        String estado = (String) comboEstados.getSelectedItem();
        if (estado == null) return;
        List<Map<String, String>> r = PrologEngine.ciudadMasPoblada(estado);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "Ciudad más poblada de " + estado.toUpperCase() + ":\n\n" +
                "  → " + f.get("Ciudad") + "  con  " + f.get("Poblacion") + " mil habitantes"
            );
        } else {
            areaResultados.setText("No hay ciudades registradas para: " + estado);
        }
    }

    private void mostrarUbicacion() {
        String estado = (String) comboEstados.getSelectedItem();
        if (estado == null) return;
        List<Map<String, String>> ciudades = PrologEngine.ciudadesPorEstado(estado);
        if (ciudades.isEmpty()) {
            areaResultados.setText("No hay ciudades para consultar en: " + estado);
            return;
        }
        String primeraCiudad = ciudades.get(0).get("Nombre");
        List<Map<String, String>> r = PrologEngine.ubicacionCiudad(primeraCiudad);
        if (!r.isEmpty()) {
            areaResultados.setText(
                "Jerarquía geográfica de: " + primeraCiudad.toUpperCase() + "\n\n" +
                "  Ciudad  → " + primeraCiudad + "\n" +
                "  Estado  → " + estado + "\n" +
                "  País    → " + r.get(0).get("Pais")
            );
        }
    }
}