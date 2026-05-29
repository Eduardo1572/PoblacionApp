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
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(new Color(28, 28, 45));
        panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

        // Título 
        JLabel lblTitulo = new JLabel("  Consulta por Continente");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(230, 230, 255));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        comboContinentes = new JComboBox<>();
        comboContinentes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboContinentes.setBackground(Color.WHITE);
        comboContinentes.setForeground(new Color(30, 30, 50));
        comboContinentes.setMaximumRowCount(5);
        comboContinentes.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        // Botones ubicados en GridLayout de forma uniforme (4 botones = 4 filas)
        JPanel panelBotones = new JPanel(new GridLayout(4, 1, 0, 10));
        panelBotones.setBackground(new Color(28, 28, 45));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton btnPoblacion   = MainWindow.crearBoton(
            "  Ver población del continente",    new Color(79, 58, 210));
        JButton btnPaises      = MainWindow.crearBoton(
            "  Ver países del continente",       new Color(52, 120, 212));
        JButton btnMasPoblado  = MainWindow.crearBoton(
            "  Continente más poblado",          new Color(130, 60, 180));
        JButton btnTodos       = MainWindow.crearBoton(
            "  Ver todos los continentes",       new Color(40, 140, 110));

        panelBotones.add(btnPoblacion);
        panelBotones.add(btnPaises);
        panelBotones.add(btnMasPoblado);
        panelBotones.add(btnTodos);

        // Área de resultados
        areaResultados = new JTextArea();
        areaResultados.setEditable(false);
        areaResultados.setFont(new Font("Monospaced", Font.PLAIN, 13));
        areaResultados.setBackground(new Color(18, 18, 32));
        areaResultados.setForeground(new Color(140, 255, 180));
        areaResultados.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        areaResultados.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(areaResultados);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(79, 58, 210), 1));
        scroll.setPreferredSize(new Dimension(456, 150));

        //  Acciones 
        btnPoblacion.addActionListener(e  -> mostrarPoblacionContinente());
        btnPaises.addActionListener(e     -> mostrarPaisesContinente());
        btnMasPoblado.addActionListener(e -> mostrarContinenteMasPoblado());
        btnTodos.addActionListener(e      -> mostrarTodosContinentes());

        //  Panel superior: título + combo 
        JPanel panelSuperior = new JPanel(new BorderLayout(0, 8));
        panelSuperior.setBackground(new Color(28, 28, 45));
        panelSuperior.add(lblTitulo, BorderLayout.NORTH);
        panelSuperior.add(comboContinentes, BorderLayout.CENTER);

        panel.add(panelSuperior,  BorderLayout.NORTH);
        panel.add(panelBotones,   BorderLayout.CENTER);
        panel.add(scroll,         BorderLayout.SOUTH);

        add(panel);
    }

    private void cargarContinentes() {
        List<Map<String, String>> continentes = PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : continentes) {
            comboContinentes.addItem(fila.get("Nombre"));
        }
    }

    private void mostrarPoblacionContinente() {
        String sel = (String) comboContinentes.getSelectedItem();
        if (sel == null) return;
        List<Map<String, String>> lista = PrologEngine.obtenerContinentes();
        for (Map<String, String> fila : lista) {
            if (fila.get("Nombre").equals(sel)) {
                areaResultados.setText(
                    "Continente : " + sel.toUpperCase() + "\n" +
                    "Población  : " + fila.get("Poblacion") + " millones de habitantes"
                );
                return;
            }
        }
    }

    private void mostrarPaisesContinente() {
        String sel = (String) comboContinentes.getSelectedItem();
        if (sel == null) return;
        List<Map<String, String>> paises = PrologEngine.paisesPorContinente(sel);
        if (paises.isEmpty()) {
            areaResultados.setText("No se encontraron países para: " + sel);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Países en ").append(sel.toUpperCase()).append(":\n\n");
        for (Map<String, String> f : paises) {
            sb.append("  • ").append(f.get("Nombre"))
              .append("  →  ").append(f.get("Poblacion")).append(" millones de habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }

    private void mostrarContinenteMasPoblado() {
        List<Map<String, String>> r = PrologEngine.continenteMasPoblado();
        if (!r.isEmpty()) {
            Map<String, String> f = r.get(0);
            areaResultados.setText(
                "Continente más poblado del mundo:\n\n" +
                "  → " + f.get("Continente").toUpperCase() +
                "  con  " + f.get("Poblacion") + " millones de habitantes"
            );
        }
    }

    private void mostrarTodosContinentes() {
        List<Map<String, String>> lista = PrologEngine.obtenerContinentes();
        StringBuilder sb = new StringBuilder("Todos los continentes:\n\n");
        for (Map<String, String> f : lista) {
            sb.append("  • ").append(f.get("Nombre").toUpperCase())
              .append("  →  ").append(f.get("Poblacion")).append(" millones de habitantes\n");
        }
        areaResultados.setText(sb.toString());
    }
}