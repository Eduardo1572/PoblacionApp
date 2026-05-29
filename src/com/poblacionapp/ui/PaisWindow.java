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
        setSize(500, 520);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(new Color(28, 28, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        JLabel lblTitulo = new JLabel("  Consulta por País");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(230, 230, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0));

        // --- Etiqueta + ComboBox ---
        JLabel lblCombo = new JLabel("  Filtrar por continente:");
        lblCombo.setForeground(new Color(180, 180, 220));
        lblCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        comboContinentes = new JComboBox<>();
        comboContinentes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboContinentes.setBackground(Color.WHITE);
        comboContinentes.setForeground(new Color(30, 30, 50));
        comboContinentes.setMaximumRowCount(5);

        // --- Los 3 botones en GridLayout(3,1) — TODOS iguales ---
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelBotones.setBackground(new Color(28, 28, 45));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnListar       = MainWindow.crearBoton(
            "  Listar países del continente",        new Color(52, 120, 212));
        JButton btnMasPoblado   = MainWindow.crearBoton(
            "  País más poblado del continente",     new Color(79, 58, 210));
        JButton btnMenosPoblado = MainWindow.crearBoton(
            "  País menos poblado del continente",   new Color(40, 140, 110));

        panelBotones.add(btnListar);
        panelBotones.add(btnMasPoblado);
        panelBotones.add(btnMenosPoblado);

        // --- Área de resultados ---
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(18, 18, 32));
        areaResultados.setForeground(new Color(140, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        areaResultados.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(52, 120, 212), 1));
        scroll.setPreferredSize(new Dimension(456, 150));

        // --- Acciones ---
        btnListar.addActionListener(e       -> listarPaises());
        btnMasPoblado.addActionListener(e   -> mostrarMasPoblado());
        btnMenosPoblado.addActionListener(e -> mostrarMenosPoblado());

        // --- Panel superior ---
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 6));
        panelSuperior.setBackground(new Color(28, 28, 45));
        JPanel panelCombo = new JPanel(new BorderLayout(0, 4));
        panelCombo.setBackground(new Color(28, 28, 45));
        panelCombo.add(lblCombo, BorderLayout.NORTH);
        panelCombo.add(comboContinentes, BorderLayout.CENTER);
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(panelCombo, BorderLayout.CENTER);

        panel.add(panelSuperior, BorderLayout.NORTH);
        panel.add(panelBotones,  BorderLayout.CENTER);
        panel.add(scroll,        BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarContinentes() {
        List<Map<String, String>> lista = PrologEngine.obtenerContinentes();
        for (Map<String, String> f : lista) {
            comboContinentes.addItem(f.get("Nombre"));
        }
    }

    private void listarPaises() {
        String c = (String) comboContinentes.getSelectedItem();
        if (c == null) return;
        List<Map<String, String>> paises = PrologEngine.paisesPorContinente(c);
        if (paises.isEmpty()) {
            areaResultados.setText("Sin resultados para: " + c);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Países en ").append(c.toUpperCase()).append(":\n\n");
        for (Map<String, String> f : paises) {
            sb.append("  • ").append(f.get("Nombre"))
              .append("  →  ").append(f.get("Poblacion")).append(" millones de habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarMasPoblado() {
        String c = (String) comboContinentes.getSelectedItem();
        if (c == null) return;
        List<Map<String, String>> r = PrologEngine.paisMasPoblado(c);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "País más poblado de " + c.toUpperCase() + ":\n\n" +
                "  → " + f.get("Pais") + "  con  " + f.get("Poblacion") + " millones de habitantes"
            );
        }
    }

    private void mostrarMenosPoblado() {
        String c = (String) comboContinentes.getSelectedItem();
        if (c == null) return;
        List<Map<String, String>> r = PrologEngine.paisMenosPoblado(c);
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "País menos poblado de " + c.toUpperCase() + ":\n\n" +
                "  → " + f.get("Pais") + "  con  " + f.get("Poblacion") + " millones de habitantes"
            );
        }
    }
}