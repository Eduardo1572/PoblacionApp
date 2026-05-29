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
        setSize(480, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 47));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("🏙  Consulta por Ciudad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(240, 240, 255));

        JPanel panelControles = new JPanel(new GridLayout(4, 1, 0, 8));
        panelControles.setBackground(new Color(30, 30, 47));

        JLabel lblCombo = new JLabel("  Selecciona un estado:");
        lblCombo.setForeground(new Color(160, 160, 200));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboEstados = new JComboBox<>();
        comboEstados.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboEstados.setBackground(new Color(50, 50, 70));
        comboEstados.setForeground(Color.WHITE);

        JButton btnListar     = MainWindow.crearBoton(
            "Listar ciudades del estado",       new Color(180, 100, 50));
        JButton btnMasPoblada = MainWindow.crearBoton(
            "Ciudad más poblada del estado",    new Color(72, 52, 212));
        JButton btnUbicacion  = MainWindow.crearBoton(
            "Ver país de la primera ciudad",    new Color(52, 140, 100));

        panelControles.add(lblCombo);
        panelControles.add(comboEstados);
        panelControles.add(btnListar);
        panelControles.add(btnMasPoblada);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(20, 20, 35));
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(180, 100, 50)));
        scroll.setPreferredSize(new Dimension(440, 150));

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelInferior.setBackground(new Color(30, 30, 47));
        panelInferior.add(btnUbicacion);

        btnListar.addActionListener(e -> listarCiudades());
        btnMasPoblada.addActionListener(e -> mostrarMasPoblada());
        btnUbicacion.addActionListener(e -> mostrarUbicacion());

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelControles, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(new Color(30, 30, 47));
        panelSur.add(panelInferior, BorderLayout.NORTH);
        panelSur.add(scroll, BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

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