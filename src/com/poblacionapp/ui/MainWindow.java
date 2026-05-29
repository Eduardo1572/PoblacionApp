package com.poblacionapp.ui;

import com.poblacionapp.core.PrologEngine;
import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow() {
        initComponents();
    }

    private void initComponents() {
        // --- Configuración de la ventana principal ---
        setTitle("App de Consulta: Población Mundial");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 380);
        setLocationRelativeTo(null);
        setResizable(false);

        // --- Panel principal con fondo oscuro ---
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBackground(new Color(30, 30, 47));

        // --- Panel del título ---
        JPanel panelTitulo = new JPanel();
        panelTitulo.setBackground(new Color(30, 30, 47));
        panelTitulo.setBorder(BorderFactory.createEmptyBorder(25, 20, 10, 20));

        JLabel lblTitulo = new JLabel("Consulta de Población");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(240, 240, 255));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblSubtitulo = new JLabel("Selecciona un nivel geográfico");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(160, 160, 200));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);

        panelTitulo.setLayout(new GridLayout(2, 1, 0, 5));
        panelTitulo.add(lblTitulo);
        panelTitulo.add(lblSubtitulo);

        // --- Panel de botones ---
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(new Color(30, 30, 47));
        panelBotones.setLayout(new GridLayout(4, 1, 0, 12));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 50, 30, 50));

        JButton btnContinente = crearBoton("   Por Continente", new Color(72, 52, 212));
        JButton btnPais       = crearBoton("   Por País",       new Color(52, 120, 212));
        JButton btnEstado     = crearBoton("   Por Estado",     new Color(52, 170, 140));
        JButton btnCiudad     = crearBoton("   Por Ciudad",     new Color(180, 100, 50));

        // --- Acciones de los botones ---
        btnContinente.addActionListener(e -> new ContinenteWindow().setVisible(true));
        btnPais.addActionListener(e -> new PaisWindow().setVisible(true));
        btnEstado.addActionListener(e -> new EstadoWindow().setVisible(true));
        btnCiudad.addActionListener(e -> new CiudadWindow().setVisible(true));

        panelBotones.add(btnContinente);
        panelBotones.add(btnPais);
        panelBotones.add(btnEstado);
        panelBotones.add(btnCiudad);

        panelPrincipal.add(panelTitulo, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        add(panelPrincipal);
    }

    // --- Método auxiliar para crear botones con estilo uniforme ---
    public static JButton crearBoton(String texto, Color color) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            Color original = color;
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(original.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    // --- Punto de entrada de la aplicación ---
    public static void main(String[] args) {
        PrologEngine.inicializar();
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}