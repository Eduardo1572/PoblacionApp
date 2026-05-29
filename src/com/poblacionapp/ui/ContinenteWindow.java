package com.poblacionapp.ui;

import com.poblacionapp.core.PrologEngine;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class ContinenteWindow extends JFrame {

    private JComboBox<String> comboContinentes;
    private JTextArea areaResultados;

    public ContinenteWindow() {
        initComponents();
        cargarContinentes();
    }

    private void initComponents() {
        setTitle("Consulta por Continente");
        setSize(480, 420);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 47));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- Título ---
        JLabel lblTitulo = new JLabel("  Consulta por Continente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(240, 240, 255));

        // --- Panel de controles ---
        JPanel panelControles = new JPanel(new GridLayout(3, 1, 0, 8));
        panelControles.setBackground(new Color(30, 30, 47));

        comboContinentes = new JComboBox<>();
        comboContinentes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboContinentes.setBackground(new Color(50, 50, 70));
        comboContinentes.setForeground(Color.WHITE);

        JButton btnPoblacion  = MainWindow.crearBoton(
            "Ver población del continente", new Color(72, 52, 212));
        JButton btnPaises     = MainWindow.crearBoton(
            "Ver países del continente",    new Color(52, 120, 212));
        JButton btnMasPoblado = MainWindow.crearBoton(
            "Continente más poblado",       new Color(140, 60, 180));

        panelControles.add(comboContinentes);
        panelControles.add(btnPoblacion);
        panelControles.add(btnPaises);

        // --- Área de resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(20, 20, 35));
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(72, 52, 212)));

        // --- Panel inferior con botón extra ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelInferior.setBackground(new Color(30, 30, 47));
        panelInferior.add(btnMasPoblado);

        // --- Acciones ---
        btnPoblacion.addActionListener(e -> mostrarPoblacionContinente());
        btnPaises.addActionListener(e -> mostrarPaisesContinente());
        btnMasPoblado.addActionListener(e -> mostrarContinenteMasPoblado());

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(panelControles, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);
        scroll.setPreferredSize(new Dimension(440, 160));

        add(panel);
    }

    private void cargarContinentes() {
        List<Map<String, String>> continentes = PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : continentes) {
            comboContinentes.addItem(fila.get("Nombre"));
        }
    }

    private void mostrarPoblacionContinente() {
        String seleccion = (String) comboContinentes.getSelectedItem();
        if (seleccion == null) return;
        List<Map<String, String>> resultados =
            PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : resultados) {
            if (fila.get("Nombre").equals(seleccion)) {
                areaResultados.setText(
                    "Continente : " + seleccion.toUpperCase() + "\n" +
                    "Población  : " + fila.get("Poblacion") + " millones de habitantes"
                );
                return;
            }
        }
    }

    private void mostrarPaisesContinente() {
        String seleccion = (String) comboContinentes.getSelectedItem();
        if (seleccion == null) return;
        List<Map<String, String>> paises =
            PrologEngine.paisesPorContinente(seleccion);
        if (paises.isEmpty()) {
            areaResultados.setText("No se encontraron países para: " + seleccion);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Países en ").append(seleccion.toUpperCase()).append(":\n\n");
        for (Map<String, String> fila : paises) {
            sb.append("  • ").append(fila.get("Nombre"))
              .append("  →  ").append(fila.get("Poblacion"))
              .append(" millones de habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarContinenteMasPoblado() {
        List<Map<String, String>> resultado =
            PrologEngine.continenteMasPoblado();
        if (!resultado.isEmpty()) {
            Map<String, String> fila = resultado.get(0);
            areaResultados.setText(
                "Continente más poblado:\n\n" +
                "  → " + fila.get("Continente").toUpperCase() +
                "  con  " + fila.get("Poblacion") + " millones de habitantes"
            );
        }
    }
}