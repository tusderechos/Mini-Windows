/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Compartidas.Usuario;
import Insta.Comentario;
import Insta.GestorInsta;
import Insta.Insta;
import Insta.SesionManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
/*public class TimeLine extends JPanel {

    private final Usuario usuarioActual;
    private final JPanel panelContenido;

    public TimeLine(Usuario usuario) {
        this.usuarioActual = usuario;
        setLayout(new BorderLayout());

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        
        JScrollPane scrollTimeLine = new JScrollPane(panelContenido);
        scrollTimeLine.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTimeLine.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollTimeLine, BorderLayout.CENTER);

        cargarTimeLine();
    }

    public void cargarTimeLine() {
        panelContenido.removeAll();
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 50));

        try {
            ArrayList<Insta> timeLine = GestorInsta.generarTimeLine(usuarioActual.getNombreUsuario());

            if (timeLine.isEmpty()) {
                panelContenido.add(new JLabel("Aun no sigues a nadie o no hay post para mostrar.", SwingConstants.CENTER));
            } else {
                for (int i = 0; i < timeLine.size(); i++) {
                    Insta post = timeLine.get(i);
                    JPanel postPanel = crearPanelPost(post);

                    postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panelContenido.add(postPanel);

                    if (i < timeLine.size() - 1) {
                        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                        separator.setMaximumSize(new Dimension(500, 2));
                        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
                        panelContenido.add(Box.createVerticalStrut(15));
                        panelContenido.add(separator);
                        panelContenido.add(Box.createVerticalStrut(15));
                    }
                }
            }
        } catch (IOException e) {
            panelContenido.add(new JLabel("Error de E/S al cargar el TimeLine: " + e.getMessage()));
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setLayout(new BorderLayout(10, 10));

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelAutor = new JLabel("<html><b>@" + post.getAutorUsername() + "</b></html>");
        panelHeader.add(labelAutor);
        panelHeader.add(new JLabel(" - " + post.getFechaPublicacion().toString()));
        panel.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));

        JLabel labelImg = new JLabel();
        String rutaImg = post.getRutaImg();
        if (rutaImg != null && !rutaImg.isEmpty() && new File(rutaImg).exists()) {
            try {
                ImageIcon icono = new ImageIcon(rutaImg);
                Image img = icono.getImage();
                int anchoMaximo = 490;

                Image imagenEscalada = img.getScaledInstance(anchoMaximo, -1, Image.SCALE_SMOOTH);
                labelImg.setIcon(new ImageIcon(imagenEscalada));
                labelImg.setAlignmentX(Component.CENTER_ALIGNMENT);

            } catch (Exception e) {
                labelImg.setText("Error al cargar imagen");
            }
        } else {
            labelImg.setText("IMAGEN NO DISPONIBLE");
        }
        panelCuerpo.add(labelImg);
        panelCuerpo.add(Box.createVerticalStrut(5));

        if (post.getTexto() != null && !post.getTexto().trim().isEmpty()) {
            JTextArea areaTexto = new JTextArea(post.getTexto());
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);
            areaTexto.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            areaTexto.setAlignmentX(Component.CENTER_ALIGNMENT); 
            areaTexto.setMaximumSize(new Dimension(480, areaTexto.getPreferredSize().height));
            panelCuerpo.add(areaTexto);
        }

        JPanel panelInteraccion = crearPanelInteraccion(post);

        panel.add(panelCuerpo, BorderLayout.CENTER);
        panel.add(panelInteraccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInteraccion(Insta post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JTextArea areaComentarios = new JTextArea();
        areaComentarios.setEditable(false);
        areaComentarios.setWrapStyleWord(true);
        areaComentarios.setLineWrap(true);
        areaComentarios.setFont(new Font("Arial", Font.PLAIN, 10));

        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        scrollComentarios.setMinimumSize(new Dimension(0, 70));
        scrollComentarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollComentarios.setPreferredSize(new Dimension(490, 70));

        JPanel panelAgregar = new JPanel(new BorderLayout(5, 5));
        JTextField txtComentario = new JTextField();
        JButton btnComentar = new JButton("Comentar");

        btnComentar.addActionListener(e -> {
            agregarComentario(post, txtComentario.getText(), areaComentarios, txtComentario);
        });

        panelAgregar.add(txtComentario, BorderLayout.CENTER);
        panelAgregar.add(btnComentar, BorderLayout.EAST);

        panel.add(scrollComentarios, BorderLayout.NORTH);
        panel.add(panelAgregar, BorderLayout.SOUTH);

        cargarComentariosEnArea(post, areaComentarios);

        return panel;
    }

    private void cargarComentariosEnArea(Insta post, JTextArea area) {
        ArrayList<Comentario> comentarios = GestorInsta.leerComentarios(post);

        StringBuilder sb = new StringBuilder();
        if (comentarios.isEmpty()) {
            sb.append("Aún no hay comentarios.");
        } else {
            for (Comentario c : comentarios) {
                sb.append("@").append(c.getAutorUsername())
                        .append(": ").append(c.getTexto()).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private void agregarComentario(Insta post, String texto, JTextArea areaComentarios, JTextField txtComentario) {
        if (!texto.trim().isEmpty()) {
            try {
                String autor = SesionManager.getUsuarioActual().getNombreUsuario();
                Comentario nuevoComentario = new Comentario(autor, texto);

                GestorInsta.guardarComentario(post, nuevoComentario);

                txtComentario.setText("");
                cargarComentariosEnArea(post, areaComentarios);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el comentario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
        }
    }
}*/

public class TimeLine extends JPanel {
    private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO_TEXTO = new Color(150, 150, 150); 
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132); 
    private final Color COLOR_BOTON_FONDO = new Color(38, 38, 38); 
    private final Color COLOR_BORDE_POST = new Color(50, 50, 50);

    private final Usuario usuarioActual;
    private final JPanel panelContenido;

    public TimeLine(Usuario usuario) {
        this.usuarioActual = usuario;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO); 

        panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBackground(COLOR_FONDO); 
        
        JScrollPane scrollTimeLine = new JScrollPane(panelContenido);
        scrollTimeLine.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTimeLine.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        scrollTimeLine.setBorder(BorderFactory.createEmptyBorder());
        scrollTimeLine.getViewport().setBackground(COLOR_FONDO);

        add(scrollTimeLine, BorderLayout.CENTER);

        cargarTimeLine();
    }
    
    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }


    public void cargarTimeLine() {
        // LÓGICA ORIGINAL
        panelContenido.removeAll();
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50)); 

        try {
            ArrayList<Insta> timeLine = GestorInsta.generarTimeLine(usuarioActual.getNombreUsuario());

            if (timeLine.isEmpty()) {
                JLabel labelVacio = new JLabel("Aun no sigues a nadie o no hay post para mostrar.", SwingConstants.CENTER);
                labelVacio.setForeground(COLOR_SECUNDARIO_TEXTO);
                labelVacio.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelContenido.add(labelVacio);
            } else {
                for (int i = 0; i < timeLine.size(); i++) {
                    Insta post = timeLine.get(i);
                    JPanel postPanel = crearPanelPost(post);
                    
                    final int anchoPost = 500; 
                    postPanel.setMaximumSize(new Dimension(anchoPost, Integer.MAX_VALUE));

                    postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panelContenido.add(postPanel);

                    if (i < timeLine.size() - 1) {
                        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                        separator.setBackground(COLOR_FONDO);
                        separator.setForeground(COLOR_BORDE_POST); // Separador sutil
                        separator.setMaximumSize(new Dimension(500, 1));
                        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
                        
                        panelContenido.add(Box.createVerticalStrut(15));
                        panelContenido.add(separator);
                        panelContenido.add(Box.createVerticalStrut(15));
                    }
                }
            }
        } catch (IOException e) {
            JLabel labelError = new JLabel("Error de E/S al cargar el TimeLine: " + e.getMessage());
            labelError.setForeground(Color.RED);
            panelContenido.add(labelError);
        }

        panelContenido.revalidate();
        panelContenido.repaint();
    }

    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO); // Fondo oscuro

        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelHeader.setBackground(COLOR_FONDO);
        
        JLabel labelAutor = new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + ";'>@" + post.getAutorUsername() + "</b></html>");
        panelHeader.add(labelAutor);
        
        JLabel labelFecha = new JLabel(" - " + post.getFechaPublicacion().toString());
        labelFecha.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelHeader.add(labelFecha);
        
        panel.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setBackground(COLOR_FONDO);

        JLabel labelImg = new JLabel();
        labelImg.setForeground(COLOR_SECUNDARIO_TEXTO);

        String rutaImg = post.getRutaImg();
        if (rutaImg != null && !rutaImg.isEmpty() && new File(rutaImg).exists()) {
            try {
                ImageIcon icono = new ImageIcon(rutaImg);
                Image img = icono.getImage();
                int anchoMaximo = 490; 

                Image imagenEscalada = img.getScaledInstance(anchoMaximo, -1, Image.SCALE_SMOOTH);
                labelImg.setIcon(new ImageIcon(imagenEscalada));
                labelImg.setAlignmentX(Component.CENTER_ALIGNMENT);

            } catch (Exception e) {
                labelImg.setText("Error al cargar imagen");
                labelImg.setForeground(Color.RED);
            }
        } else {
            labelImg.setText("IMAGEN NO DISPONIBLE");
            labelImg.setBackground(COLOR_BOTON_FONDO);
            labelImg.setOpaque(true);
        }
        panelCuerpo.add(labelImg);
        panelCuerpo.add(Box.createVerticalStrut(5));

        if (post.getTexto() != null && !post.getTexto().trim().isEmpty()) {
            JTextArea areaTexto = new JTextArea(post.getTexto());
            areaTexto.setEditable(false);
            areaTexto.setLineWrap(true);
            areaTexto.setWrapStyleWord(true);
            
            areaTexto.setBackground(COLOR_FONDO);
            areaTexto.setForeground(COLOR_TEXTO);
            
            areaTexto.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            areaTexto.setAlignmentX(Component.CENTER_ALIGNMENT);
            areaTexto.setMaximumSize(new Dimension(480, areaTexto.getPreferredSize().height));
            panelCuerpo.add(areaTexto);
        }

        JPanel panelInteraccion = crearPanelInteraccion(post);

        panel.add(panelCuerpo, BorderLayout.CENTER);
        panel.add(panelInteraccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInteraccion(Insta post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panel.setBackground(COLOR_FONDO);

        JTextArea areaComentarios = new JTextArea();
        areaComentarios.setEditable(false);
        areaComentarios.setWrapStyleWord(true);
        areaComentarios.setLineWrap(true);
        areaComentarios.setFont(new Font("Arial", Font.PLAIN, 10));
        
        areaComentarios.setBackground(COLOR_FONDO);
        areaComentarios.setForeground(COLOR_SECUNDARIO_TEXTO);

        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        scrollComentarios.setMinimumSize(new Dimension(0, 70));
        scrollComentarios.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        scrollComentarios.setPreferredSize(new Dimension(490, 70));
        scrollComentarios.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
        scrollComentarios.getViewport().setBackground(COLOR_FONDO);

        JPanel panelAgregar = new JPanel(new BorderLayout(5, 5));
        panelAgregar.setBackground(COLOR_FONDO);
        
        JTextField txtComentario = new JTextField();
        JButton btnComentar = new JButton("Comentar");

        txtComentario.setBackground(COLOR_BOTON_FONDO);
        txtComentario.setForeground(COLOR_TEXTO);
        txtComentario.setCaretColor(COLOR_TEXTO);
        txtComentario.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
        
        btnComentar.setBackground(COLOR_FONDO);
        btnComentar.setForeground(COLOR_BOTON_DOMINANTE); // Color rosa/púrpura
        btnComentar.setBorderPainted(false);
        
        btnComentar.addActionListener(e -> {
            agregarComentario(post, txtComentario.getText(), areaComentarios, txtComentario);
        });

        panelAgregar.add(txtComentario, BorderLayout.CENTER);
        panelAgregar.add(btnComentar, BorderLayout.EAST);

        panel.add(scrollComentarios, BorderLayout.NORTH);
        panel.add(panelAgregar, BorderLayout.SOUTH);

        cargarComentariosEnArea(post, areaComentarios);

        return panel;
    }

    private void cargarComentariosEnArea(Insta post, JTextArea area) {
        ArrayList<Comentario> comentarios = GestorInsta.leerComentarios(post);

        StringBuilder sb = new StringBuilder();
        if (comentarios.isEmpty()) {
            sb.append("Aún no hay comentarios.");
        } else {
            for (Comentario c : comentarios) {
                sb.append("@").append(c.getAutorUsername())
                        .append(": ").append(c.getTexto()).append("\n");
            }
        }
        area.setText(sb.toString());
    }

    private void agregarComentario(Insta post, String texto, JTextArea areaComentarios, JTextField txtComentario) {
        if (!texto.trim().isEmpty()) {
            try {
                String autor = SesionManager.getUsuarioActual().getNombreUsuario();
                Comentario nuevoComentario = new Comentario(autor, texto);

                GestorInsta.guardarComentario(post, nuevoComentario);

                txtComentario.setText("");
                cargarComentariosEnArea(post, areaComentarios);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el comentario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
        }
    }
}
