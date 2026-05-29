package com.poblacionapp.ui;

import com.poblacionapp.core.PrologEngine;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class EstadoWindow extends JFrame {

    private JComboBox<String> comboPaises;
    private JTextArea areaResultados;

    public EstadoWindow() {
        initComponents();
        cargarPaises();
    }

    private void initComponents() {
        setTitle("Consulta por Estado");
        setSize(480, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 47));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("📍  Consulta por Estado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(240, 240, 255));

        JPanel panelControles = new JPanel(new GridLayout(4, 1, 0, 8));
        panelControles.setBackground(new Color(30, 30, 47));

        JLabel lblCombo = new JLabel("  Selecciona un país:");
        lblCombo.setForeground(new Color(160, 160, 200));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboPaises = new JComboBox<>();
        comboPaises.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboPaises.setBackground(new Color(50, 50, 70));
        comboPaises.setForeground(Color.WHITE);

        JButton btnListar       = MainWindow.crearBoton(
            "Listar estados del país",          new Color(52, 170, 140));
        JButton btnMasPoblado   = MainWindow.crearBoton(
            "Estado más poblado del país",      new Color(72, 52, 212));
        JButton btnMenosPoblado = MainWindow.crearBoton(
            "Estado menos poblado del país",    new Color(180, 100, 50));

        panelControles.add(lblCombo);
        panelControles.add(comboPaises);
        panelControles.add(btnListar);
        panelControles.add(btnMasPoblado);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(20, 20, 35));
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 140)));
        scroll.setPreferredSize(new Dimension(440, 150));

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelInferior.setBackground(new Color(30, 30, 47));
        panelInferior.add(btnMenosPoblado);

        btnListar.addActionListener(e -> listarEstados());
        btnMasPoblado.addActionListener(e -> mostrarMasPoblado());
        btnMenosPoblado.addActionListener(e -> mostrarMenosPoblado());

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelControles, BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(new Color(30, 30, 47));
        panelSur.add(panelInferior, BorderLayout.NORTH);
        panelSur.add(scroll, BorderLayout.CENTER);
        panel.add(panelSur, BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarPaises() {
        List<Map<String, String>> paises = PrologEngine.obtenerPaises();
        for (Map<String, String> fila : paises) {
            comboPaises.addItem(fila.get("Nombre"));
        }
    }

    private void listarEstados() {
        String pais = (String) comboPaises.getSelectedItem();
        if (pais == null) return;
        List<Map<String, String>> estados = PrologEngine.estadosPorPais(pais);
        if (estados.isEmpty()) {
            areaResultados.setText("No hay estados registrados para: " + pais);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Estados de ").append(pais.toUpperCase()).append(":\n\n");
        for (Map<String, String> fila : estados) {
            sb.append("  • ").append(fila.get("Nombre"))
              .append("  →  ").append(fila.get("Poblacion")).append(" mil habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarMasPoblado() {
        String pais = (String) comboPaises.getSelectedItem();
        if (pais == null) return;
        List<Map<String, String>> r = PrologEngine.estadoMasPoblado(pais);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "Estado más poblado de " + pais.toUpperCase() + ":\n\n" +
                "  → " + f.get("Estado") + "  con  " + f.get("Poblacion") + " mil habitantes"
            );
        } else {
            areaResultados.setText("No hay datos de estados para: " + pais);
        }
    }

    private void mostrarMenosPoblado() {
        String pais = (String) comboPaises.getSelectedItem();
        if (pais == null) return;
        List<Map<String, String>> r = PrologEngine.estadoMenosPoblado(pais);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "Estado menos poblado de " + pais.toUpperCase() + ":\n\n" +
                "  → " + f.get("Estado") + "  con  " + f.get("Poblacion") + " mil habitantes"
            );
        } else {
            areaResultados.setText("No hay datos de estados para: " + pais);
        }
    }
}