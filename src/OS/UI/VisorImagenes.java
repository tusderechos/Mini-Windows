/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class VisorImagenes extends JFrame {
    
    private JLabel LblImagen = new JLabel();
    private BufferedImage ImagenActual = null;
    private File ArchivoActual = null;
    
    private final Color Fondo = new Color(30, 30, 30);
    private final Color Barra = new Color(44, 44, 44);
    private final Color Texto = new Color(230, 230, 230);
    
    public VisorImagenes(File ArchivoActual) {
        this.ArchivoActual = ArchivoActual;
        
        setTitle("Mini-Windows - Visor de Imagenes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 400));
        
        AplicarModoOscuro();
        
        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBackground(Barra);
        
        JButton BtnZoomIn = CrearBoton("Zoom +");
        JButton BtnZoomOut = CrearBoton("Zoom -");
        JButton BtnAjustar = CrearBoton("Ajustar");
        JButton BtnOriginal = CrearBoton("Tamaño Original");
        
        top.add(BtnZoomIn);
        top.add(BtnZoomOut);
        top.add(BtnAjustar);
        top.add(BtnOriginal);
        
        JScrollPane scroll = new JScrollPane(LblImagen);
        scroll.getViewport().setBackground(Fondo);
        
        add(top, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        
        CargarImagen(ArchivoActual);
        
        BtnZoomIn.addActionListener(e -> Escalar(1.25));
        BtnZoomOut.addActionListener(e -> Escalar(0.8));
        BtnAjustar.addActionListener(e -> Ajustar());
        BtnOriginal.addActionListener(e -> Original());
    }
    
    /*
        Constantes para crear los botones (paja que lo volvere a usar)
    */
    private JButton CrearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(60, 60, 60));
        boton.setForeground(Texto);
        boton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boton.setBorder(BorderFactory.createEmptyBorder(6, 18, 6, 18));
        
        return boton;
    }
    
    /*
        Coso para el modo oscuro porque ya ratos se me estan quemando los ojos
    */
    private void AplicarModoOscuro() {
        getContentPane().setBackground(Fondo);
        LblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        LblImagen.setVerticalAlignment(SwingConstants.CENTER);
    }
    
    /*
        Carga la imagen
    */
    private void CargarImagen(File archivo) {
        try {
            ImagenActual = ImageIO.read(archivo);
            LblImagen.setIcon(new ImageIcon(ImagenActual));
            setTitle("Fotos - " + archivo.getName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error abriendo la imagen");
        }
    }
    
    /*
        Escala la imagen, ya sea acercando o alejando la imagen
    */
    private void Escalar(double factor) {
        if (ImagenActual == null) {
            return;
        }
        
        int nuevoancho = (int) (LblImagen.getIcon().getIconWidth() * factor);
        int nuevoalto = (int) (LblImagen.getIcon().getIconHeight()* factor);
        
        Image escalada = ImagenActual.getScaledInstance(nuevoancho, nuevoalto, Image.SCALE_SMOOTH);
        
        LblImagen.setIcon(new ImageIcon(escalada));
    }
    
    private void Ajustar() {
        if (ImagenActual == null) {
            return;
        }
        
        int maxw = getWidth() - 80;
        int maxh = getHeight()- 160;
        
        int imagenw = ImagenActual.getWidth();
        int imagenh = ImagenActual.getHeight();
        
        double factor = Math.min((double) maxw / imagenw, (double) maxh / imagenh);
        
        Escalar(factor);
    }
    
    /*
        Muestra la imagen original como estaba antes de escalar o ajustar o etc etc
    */
    private void Original() {
        if (ImagenActual == null) {
            return;
        }
        
        LblImagen.setIcon(new ImageIcon(ImagenActual));
    }
}
