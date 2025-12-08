/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.LineBorder;

public class BarraTareas extends JPanel {
    
    private final JPanel Izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
    private final JPanel Centro = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 6));
    private final JPanel Derecha = new JPanel();
    private final JPanel DerechaWrap = new JPanel(new BorderLayout());
    private final JLabel Reloj = new JLabel("");
    private final JLabel LblUsuario = new JLabel();
    
    public BarraTareas() {
        setLayout(new BorderLayout());
        setOpaque(true);
        setBackground(new Color(24, 24, 29, 210)); //Semi transparente
        setBorder(new EmptyBorder(4, 8, 4, 8));
        
        Izquierda.setOpaque(true);
        Izquierda.setBackground(new Color(22, 22, 26));
        
        Centro.setOpaque(true);
        Centro.setBackground(new Color(22, 22, 26));
        
        Derecha.setOpaque(false);
        Derecha.setLayout(new BoxLayout(Derecha, BoxLayout.Y_AXIS));
        
        //Reloj simple
        Reloj.setForeground(TemaOscuro.TEXTO);
        Reloj.setFont(new Font("Segoe UI", Font.BOLD, 14));
        Reloj.setAlignmentX(1.0f);
        
        LblUsuario.setForeground(TemaOscuro.TEXTO);
        LblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        LblUsuario.setAlignmentX(1.0f);
                
        Derecha.add(Reloj);
        Derecha.add(Box.createVerticalStrut(2));
        Derecha.add(LblUsuario);
        
        DerechaWrap.setOpaque(true);
        DerechaWrap.setBackground(new Color(22, 22, 26));
        DerechaWrap.add(Derecha, BorderLayout.SOUTH);
        DerechaWrap.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        add(Izquierda, BorderLayout.WEST);
        add(Centro, BorderLayout.CENTER);
        add(DerechaWrap, BorderLayout.EAST);
        
        //Timer para el reloj (cada 30 segundos)
        new Timer(30_000, e -> RefrescarReloj()).start();
        RefrescarReloj();
    }
    
    public void AnadirApp(BotonesBarra boton) {
        Izquierda.add(boton);
        revalidate();
        repaint();
    }
    
    public void setBotonCentro(JButton boton) {
        boton.setForeground(new Color(255, 230, 230));
        boton.setBackground(new Color(60, 18, 22));
        boton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(150, 50, 60)), new EmptyBorder(6, 10, 6, 10)));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        Centro.removeAll();
        Centro.add(boton);
        revalidate();
        repaint();
    }
    
    private void RefrescarReloj() {
        String texto = new SimpleDateFormat("HH:mm   dd/MM/yyyy").format(new Date());
        Reloj.setText(texto);
    }
    
    public void setUsuario(String texto) {
        LblUsuario.setText(texto);
    }
}
