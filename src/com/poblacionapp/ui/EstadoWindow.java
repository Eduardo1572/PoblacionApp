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
        setSize(500, 520); 
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(new Color(28, 28, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel lblTitulo = new JLabel("  Consulta por Estado");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(230, 230, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        // --- Etiqueta + ComboBox ---
        JLabel lblCombo = new JLabel("  Selecciona un país:");
        lblCombo.setForeground(new Color(180, 180, 220));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboPaises = new JComboBox<>();
        comboPaises.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboPaises.setBackground(Color.WHITE);
        comboPaises.setForeground(new Color(30, 30, 50));
        comboPaises.setMaximumRowCount(5);

        // --- Panel de Botones exclusivo para los 3 botones ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelBotones.setBackground(new Color(28, 28, 45));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnListar       = MainWindow.crearBoton(
            "  Listar estados del país",         new Color(52, 170, 140));
        JButton btnMasPoblado   = MainWindow.crearBoton(
            "  Estado más poblado del país",     new Color(72, 52, 212));
        JButton btnMenosPoblado = MainWindow.crearBoton(
            "  Estado menos poblado del país",   new Color(180, 100, 50));

        // Ahora los 3 botones comparten el mismo layout
        panelBotones.add(btnListar);
        panelBotones.add(btnMasPoblado);
        panelBotones.add(btnMenosPoblado);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(18, 18, 32));
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        areaResultados.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(52, 170, 140), 1));
        scroll.setPreferredSize(new Dimension(456, 150));

        btnListar.addActionListener(e -> listarEstados());
        btnMasPoblado.addActionListener(e -> mostrarMasPoblado());
        btnMenosPoblado.addActionListener(e -> mostrarMenosPoblado());

        JPanel panelSuperior = new JPanel(new BorderLayout(0, 6));
        panelSuperior.setBackground(new Color(28, 28, 45));
        
        JPanel panelCombo = new JPanel(new BorderLayout(0, 4));
        panelCombo.setBackground(new Color(28, 28, 45));
        panelCombo.add(lblCombo, BorderLayout.NORTH);
        panelCombo.add(comboPaises, BorderLayout.CENTER);
        
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelCombo, BorderLayout.CENTER);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelBotones,  BorderLayout.CENTER);
        panel.add(scroll,        BorderLayout.SOUTH);

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