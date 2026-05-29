package com.poblacionapp.ui;

import com.poblacionapp.core.PrologEngine;
import java.awt.*;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class PaisWindow extends JFrame {

    private JComboBox<String> comboContinentes;
    private JTextArea areaResultados;

    public PaisWindow() {
        initComponents();
        cargarContinentes();
    }

    private void initComponents() {
        setTitle("Consulta por País");
        setSize(480, 460);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(30, 30, 47));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("  Consulta por País");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(240, 240, 255));

        JPanel panelControles = new JPanel(new GridLayout(4, 1, 0, 8));
        panelControles.setBackground(new Color(30, 30, 47));

        JLabel lblCombo = new JLabel("  Filtrar por continente:");
        lblCombo.setForeground(new Color(160, 160, 200));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboContinentes = new JComboBox<>();
        comboContinentes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboContinentes.setBackground(new Color(50, 50, 70));
        comboContinentes.setForeground(Color.WHITE);

        JButton btnListar      = MainWindow.crearBoton(
            "Listar países del continente",         new Color(52, 120, 212));
        JButton btnMasPoblado  = MainWindow.crearBoton(
            "País más poblado del continente",      new Color(72, 52, 212));
        JButton btnMenosPoblado = MainWindow.crearBoton(
            "País menos poblado del continente",    new Color(52, 140, 100));

        panelControles.add(lblCombo);
        panelControles.add(comboContinentes);
        panelControles.add(btnListar);
        panelControles.add(btnMasPoblado);

        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(20, 20, 35));
        areaResultados.setForeground(new Color(180, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(52, 120, 212)));
        scroll.setPreferredSize(new Dimension(440, 150));

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        panelInferior.setBackground(new Color(30, 30, 47));
        panelInferior.add(btnMenosPoblado);

        btnListar.addActionListener(e -> listarPaises());
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

    private void cargarContinentes() {
        List<Map<String, String>> continentes = PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : continentes) {
            comboContinentes.addItem(fila.get("Nombre"));
        }
    }

    private void listarPaises() {
        String continente = (String) comboContinentes.getSelectedItem();
        if (continente == null) return;
        List<Map<String, String>> paises = PrologEngine.paisesPorContinente(continente);
        if (paises.isEmpty()) {
            areaResultados.setText("Sin resultados para: " + continente);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Países en ").append(continente.toUpperCase()).append(":\n\n");
        for (Map<String, String> fila : paises) {
            sb.append("  • ").append(fila.get("Nombre"))
              .append("  ->  ").append(fila.get("Poblacion")).append(" millones de habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarMasPoblado() {
        String continente = (String) comboContinentes.getSelectedItem();
        if (continente == null) return;
        List<Map<String, String>> r = PrologEngine.paisMasPoblado(continente);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "Pais más poblado de " + continente.toUpperCase() + ":\n\n" +
                "  -> " + f.get("Pais") + "  con  " + f.get("Poblacion") + " millones de habitantes"
            );
        }
    }

    private void mostrarMenosPoblado() {
        String continente = (String) comboContinentes.getSelectedItem();
        if (continente == null) return;
        List<Map<String, String>> r = PrologEngine.paisMenosPoblado(continente);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "País menos poblado de " + continente.toUpperCase() + ":\n\n" +
                "  → " + f.get("Pais") + "  con  " + f.get("Poblacion") + " millones de habitantes"
            );
        }
    }
}