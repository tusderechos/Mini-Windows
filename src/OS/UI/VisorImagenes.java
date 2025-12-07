/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import OS.UI.util.GradientWallpaper;
import OS.utils.ImageImporter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class VisorImagenes extends JFrame {
    
    private final JLabel LblImagen = new JLabel();
    private BufferedImage ImagenActual = null;
    private File ArchivoActual = null;
    
    private final Color Texto = new Color(230, 230, 230);
    private final Color Sel = new Color(76, 141, 255);
    
    private final ArrayList<File> Imagenes = new ArrayList<>();
    private int Indice = -1;
    
    private final int THUMB = 92;
    private final int VISIBLE_SLOTS = 7;
    
    private final JPanel PanelCarrusel = new JPanel(new GridLayout(1, VISIBLE_SLOTS, 12, 0));
    private final JLabel[] Slots = new JLabel[VISIBLE_SLOTS];
    private int Inicio = 0;
    
    private final JButton BtnPrev = CrearBoton("<-", false, 15);
    private final JButton BtnNext = CrearBoton("->", false, 15);
        
    public VisorImagenes(File ArchivoActual) {
        this.ArchivoActual = ArchivoActual;
        
        setTitle("Mini-Windows - Visor de Imagenes");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 420));        
        setLayout(new BorderLayout());
        
        GradientWallpaper top = new GradientWallpaper();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        top.setBorder(new EmptyBorder(6, 8, 6, 8));
        
        JButton BtnZoomIn = CrearBoton("Zoom +", false, 14);
        JButton BtnZoomOut = CrearBoton("Zoom -", false, 14);
        JButton BtnAjustar = CrearBoton("Ajustar", false, 14);
        JButton BtnOriginal = CrearBoton("Tamaño Original", false, 14);
        JButton BtnImportar = CrearBoton("Importar", true, 14);
        JButton BtnExportar = CrearBoton("Exportar", false, 14);
        
        top.add(BtnZoomIn);
        top.add(BtnZoomOut);
        top.add(BtnAjustar);
        top.add(BtnOriginal);
        top.add(BtnImportar);
        top.add(BtnExportar);
        
        add(top, BorderLayout.NORTH);
        
        LblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        
        JScrollPane centro = new JScrollPane(LblImagen);
        centro.getViewport().setBackground(new Color(30, 30, 30));
        centro.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, new Color(60, 60, 60)));
        
        add(centro, BorderLayout.CENTER);
        
        GradientWallpaper bottom = new GradientWallpaper();
        bottom.setLayout(new BorderLayout());
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        
        PanelCarrusel.setOpaque(false);
        
        bottom.add(PanelCarrusel, BorderLayout.CENTER);
        
        JPanel nav = new JPanel();
        nav.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 8));
        nav.setOpaque(false);
        
        nav.add(BtnPrev);
        nav.add(BtnNext);
        
        bottom.add(nav, BorderLayout.SOUTH);
        
        add(bottom, BorderLayout.SOUTH);
        
        //Slots iniciales
        for (int i = 0; i < VISIBLE_SLOTS; i++) {
            JLabel lbl = new JLabel("", SwingConstants.CENTER);
            lbl.setOpaque(false);
            lbl.setBorder(new LineBorder(new Color(255, 255, 55, 30), 2, true));
            lbl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            
            final int indiceenslot = i;
            
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (Imagenes.isEmpty()) {
                        return;
                    }
                    
                    int real = (Inicio + indiceenslot) % Imagenes.size();
                    Indice = real;
                    
                    MostrarActual();
                    RefrescarCarrusel();
                }
            });
            
            Slots[i] = lbl;
            PanelCarrusel.add(lbl);
        }
        
        PanelCarrusel.addMouseWheelListener(e -> mover(e.getWheelRotation()));
        
        BtnZoomIn.addActionListener(e -> Escalar(1.25));
        BtnZoomOut.addActionListener(e -> Escalar(0.8));
        BtnAjustar.addActionListener(e -> Ajustar());
        BtnOriginal.addActionListener(e -> Original());
        
        BtnPrev.addActionListener(e -> Anterior());
        BtnNext.addActionListener(e -> Siguiente());
        
        BtnImportar.addActionListener(e -> {
            ArrayList<File> nuevos = ImageImporter.Importar(this);
            
            if (!nuevos.isEmpty()) {
                CargarColeccionDesde(nuevos.get(0));
                MostrarActual();
            }
        });
        
        BtnExportar.addActionListener(e -> {
            ArrayList<File> nuevos = ImageImporter.Importar(this);
            
            if (!nuevos.isEmpty()) {
                CargarColeccionDesde(nuevos.get(0));
                MostrarActual();
            }
        });
        
        CargarColeccionDesde(ArchivoActual);
        MostrarActual();
        setVisible(true);
    }
    
    private void CargarColeccionDesde(File archivo) {
        Imagenes.clear();
        
        File dir = archivo.getParentFile();
        File[] arr = dir.listFiles();
        
        if (arr != null) {
            for (File file : arr) {
                if (esImagen(file)) {
                    Imagenes.add(file);
                }
            }
        }
        
        Imagenes.sort((a, b) -> a.getName().compareTo(b.getName()));
        
        Indice = Imagenes.indexOf(archivo);
        
        if (Indice < 0) {
            Indice = 0;
        }
        
        Inicio = Math.max(0, Indice - VISIBLE_SLOTS / 2);
        RefrescarCarrusel();
    }
    
    private void RefrescarCarrusel() {
        if (Imagenes.isEmpty()) {
            for (JLabel slot : Slots) {
                slot.setIcon(null);
                return;
            }
        }
        
        int n = Imagenes.size();
        
        for (int i = 0; i < VISIBLE_SLOTS; i++) {
            int real = (Inicio + i) % n;
            File file = Imagenes.get(real);
            
            Slots[i].setIcon(CrearMiniatura(file, THUMB));
            Slots[i].setToolTipText(file.getName());
            
            Color borde = (real == Indice) ? Sel : new Color(255, 255, 255, 30);
            Slots[i].setBorder(new LineBorder(borde, 2, true));
        }
        
        PanelCarrusel.revalidate();
        PanelCarrusel.repaint();
    }
    
    private void mover(int delta) {
        if (Imagenes.isEmpty()) {
            return;
        }
        
        int n = Imagenes.size();
        Inicio = ((Inicio + delta) % n + n) % n;
        
        RefrescarCarrusel();
    }
    
    private void MostrarActual() {
        try {
            ArchivoActual = Imagenes.get(Indice);
            ImagenActual = ImageIO.read(ArchivoActual);
            
            LblImagen.setIcon(new ImageIcon(ImagenActual));
            setTitle("Fotos - " + ArchivoActual.getName());
            Ajustar();
            RefrescarCarrusel();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error abriendo la imagen");
        }
    }
    
    /*
        Constantes para crear los botones (paja que lo volvere a usar)
        soy daniel de como 2 semanas depues de escribir el mensaje de arriba, tremendo pajero la misma funcion estoy usando en todas las clases
    */
    private JButton CrearBoton(String texto, boolean primario, int radio) {
        //Colores base segun el tipo
        Color BG = primario ? new Color(84, 36, 122) : new Color(44, 44, 50);
        Color hover = primario ? new Color(110, 50, 150) : new Color(60, 60, 68);
        Color presionado = primario ? new Color(60, 20, 95) : new Color(24, 24, 28);
        Color textoC = primario ? Color.WHITE : new Color(230, 230, 230);
        
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                int w = getWidth();
                int h = getHeight();
                
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                //Estado actual
                ButtonModel modelo = getModel();
                Color fill = !isEnabled() ? BG.darker().darker() : modelo.isPressed() ? presionado : modelo.isRollover() ? hover : BG;
                
                //Fondo redondeado
                Shape rr = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radio, radio);
                
                //Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(2, 3, w - 4, h - 5, radio + 2, radio + 2);
                
                //Relleno
                g2d.setColor(fill);
                g2d.fill(rr);
                
                //Borde
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.draw(new RoundRectangle2D.Float(2, 3, w - 1, h - 1, radio, radio));
                
                g2d.setClip(rr);
                super.paintComponent(g);
                g2d.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                return rr.contains(x, y);
            }
        };
        
        //Baseline de estilo
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(textoC);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setRolloverEnabled(true);
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14f));
        boton.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return boton;
    }
    
    /*
        Escala la imagen, ya sea acercando o alejando la imagen
    */
    private void Escalar(double factor) {
        if (ImagenActual == null) {
            return;
        }
        
        ImageIcon actual = (ImageIcon) LblImagen.getIcon();
        int w = actual.getIconWidth();
        int h = actual.getIconHeight();
        
        Image escalada = ImagenActual.getScaledInstance((int)(w * factor), (int)(h * factor), Image.SCALE_SMOOTH);
        
        LblImagen.setIcon(new ImageIcon(escalada));
    }
    
    private void Ajustar() {
        if (ImagenActual == null) {
            return;
        }
        
        int maxw = getWidth() - 80;
        int maxh = getHeight() - 220;
        
        double fw = (double) maxw / ImagenActual.getWidth();
        double fh = (double) maxh / ImagenActual.getHeight();
        
        double factor = Math.min(fw, fh);
        
        int nw = (int)(ImagenActual.getWidth() * factor);
        int nh = (int)(ImagenActual.getHeight() * factor);
        
        Image escalada = ImagenActual.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
        
        LblImagen.setIcon(new ImageIcon(escalada));
    }
    
    /*
        Muestra la imagen original como estaba antes de escalar o ajustar o etc etc
    */
    private void Original() {
        if (ImagenActual != null) {
            LblImagen.setIcon(new ImageIcon(ImagenActual));
        }
    }
    
    private void Anterior() {
        if (Imagenes.isEmpty()) {
            return;
        }
        
        Indice = (Indice - 1 + Imagenes.size()) % Imagenes.size();
        
        if (!estaVisible(Indice)) {
            mover(-1);
        }
        
        MostrarActual();
    }
    
    private void Siguiente() {
        if (Imagenes.isEmpty()) {
            return;
        }
        
        Indice = (Indice + 1) % Imagenes.size();
        
        if (!estaVisible(Indice)) {
            mover(+1);
        }
        
        MostrarActual();
    }
    
    private boolean estaVisible(int indice) {
        for (int i = 0; i < VISIBLE_SLOTS; i++) {
            if ((Inicio + i) % Imagenes.size() == indice) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean esImagen(File file) {
        String n = file.getName().toLowerCase();
        return file.isFile() && (n.endsWith(".jpg") || n.endsWith(".jpeg") || n.endsWith(".png") || n.endsWith(".gif") || n.endsWith(".bmp"));
    } 
    
    private Icon CrearMiniatura(File file, int tamano) {
        try {
            BufferedImage bi = ImageIO.read(file);
            int w = bi.getWidth();
            int h = bi.getHeight();
            
            double s = Math.min((double) tamano / w, (double) tamano / h);
            int nw = (int)(w * s);
            int nh = (int)(h * s);
            
            Image escalada = bi.getScaledInstance(nw, nh, Image.SCALE_SMOOTH);
            
            //Centrado
            BufferedImage lienzo = new BufferedImage(tamano, tamano, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = lienzo.createGraphics();
            
            g2d.setColor(new Color(255, 255, 255, 24));
            g2d.fillRoundRect(0, 0, tamano, tamano, 12, 12);
            
            int x = (tamano - nw) / 2;
            int y = (tamano - nh) / 2;
            
            g2d.drawImage(escalada, x, y, null);
            g2d.dispose();
            
            return new ImageIcon(lienzo);
        } catch (Exception e) {
            return null;
        }
    }
}
