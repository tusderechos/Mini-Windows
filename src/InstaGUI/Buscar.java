/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Insta;
import Compartidas.Usuario;
import Insta.Comentario;
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
/*public class Buscar extends JPanel{
    private final Usuario usuarioActual;
    private final vtnInstaPrincipal vtnP;
    
    private JRadioButton rbHashtag;
    private JRadioButton rbMenciones;
    private JTextField txtTerminoBusqueda;
    private JButton btnBuscar;
    
    private JPanel panelResultados;

    public Buscar(Usuario usuario, vtnInstaPrincipal principal) {
        this.usuarioActual = usuario;
        this.vtnP = principal;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelControl = new JPanel(new BorderLayout(10, 10));
        
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbHashtag = new JRadioButton("Buscar por Hashtag (#)", true);
        rbMenciones = new JRadioButton("Mis Menciones (@" + usuarioActual.getNombreUsuario() + ")");
        
        ButtonGroup grupoBusqueda = new ButtonGroup();
        grupoBusqueda.add(rbHashtag);
        grupoBusqueda.add(rbMenciones);
        
        panelOpciones.add(rbHashtag);
        panelOpciones.add(rbMenciones);
        
        JPanel panelAccion = new JPanel(new BorderLayout(5, 5));
        txtTerminoBusqueda = new JTextField("Ingrese #hashtag o @username");
        btnBuscar = new JButton("Buscar");
        
        panelAccion.add(txtTerminoBusqueda, BorderLayout.CENTER);
        panelAccion.add(btnBuscar, BorderLayout.EAST);
        
        panelControl.add(panelOpciones, BorderLayout.NORTH);
        panelControl.add(panelAccion, BorderLayout.SOUTH);
        
        add(panelControl, BorderLayout.NORTH);
        
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        add(scrollResultados, BorderLayout.CENTER);
        
        rbMenciones.addActionListener(e -> {
            txtTerminoBusqueda.setEnabled(false);
            btnBuscar.setText("Ver Menciones");
        });
        
        rbHashtag.addActionListener(e -> {
            txtTerminoBusqueda.setEnabled(true);
            btnBuscar.setText("Buscar");
        });
        
        btnBuscar.addActionListener(e -> {
            if (rbHashtag.isSelected()) {
                buscarHashtag();
            } else if (rbMenciones.isSelected()) {
                buscarMenciones();
            }
        });
        
        rbHashtag.setSelected(true);
        txtTerminoBusqueda.setEnabled(true);
        btnBuscar.setText("Buscar");
    }

    private void buscarHashtag() {
        String termino = txtTerminoBusqueda.getText().trim();
        if (termino.isEmpty() || !termino.startsWith("#")) {
            JOptionPane.showMessageDialog(this, "El término de búsqueda debe empezar con #.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ArrayList<Insta> resultados = GestorInsta.buscarPorHashtag(termino);
            mostrarResultados(resultados, "Resultados para: " + termino);

        } catch (IOException e) {
             JOptionPane.showMessageDialog(this, "Error de E/S al buscar posts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMenciones() {
        String miUsername = usuarioActual.getNombreUsuario();
        try {
            ArrayList<Insta> resultados = GestorInsta.buscarPorMencion(miUsername);
            mostrarResultados(resultados, "Posts que te mencionan (@" + usuarioActual.getNombreUsuario() + ")");

        } catch (IOException e) {
             JOptionPane.showMessageDialog(this, "Error de E/S al buscar menciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResultados(ArrayList<Insta> resultados, String titulo) {
    panelResultados.removeAll();
    
    panelResultados.add(new JLabel("<html><h2>" + titulo + "</h2></html>"));
    panelResultados.add(Box.createVerticalStrut(10));

    if (resultados.isEmpty()) {
        panelResultados.add(new JLabel("No se encontraron posts."));
    } else {
         for (Insta post : resultados) {
             
             try {
                 JPanel postPanel = crearPanelPost(post); 
                 
                 postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                 
                 panelResultados.add(postPanel);
                 panelResultados.add(Box.createVerticalStrut(15));
             } catch (Exception ex) {
                 JPanel errorPanel = new JPanel();
                 errorPanel.add(new JLabel("Error al cargar post de @" + post.getAutorUsername()));
                 panelResultados.add(errorPanel);
                 System.err.println("Error al cargar el post en BuscarPanel: " + ex.getMessage());
             }
         }
    }

    panelResultados.revalidate();
    panelResultados.repaint();
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

public class Buscar extends JPanel{
        private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO_TEXTO = new Color(150, 150, 150); 
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132); 
    private final Color COLOR_BOTON_FONDO = new Color(38, 38, 38); 
    private final Color COLOR_BORDE_POST = new Color(50, 50, 50); 
    
    private final Usuario usuarioActual;
    private final vtnInstaPrincipal vtnP;
    
    private JRadioButton rbHashtag;
    private JRadioButton rbMenciones;
    private JTextField txtTerminoBusqueda;
    private JButton btnBuscar;
    
    private JPanel panelResultados;

    public Buscar(Usuario usuario, vtnInstaPrincipal principal) {
        this.usuarioActual = usuario;
        this.vtnP = principal;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(COLOR_FONDO); 
        
        inicializarComponentes();
    }
    
    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private void inicializarComponentes() {
        JPanel panelControl = new JPanel(new BorderLayout(10, 10));
        panelControl.setBackground(COLOR_FONDO);
        
        JPanel panelOpciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelOpciones.setBackground(COLOR_FONDO);
        
        rbHashtag = new JRadioButton("Buscar por Hashtag (#)", true);
        rbMenciones = new JRadioButton("Mis Menciones (@" + usuarioActual.getNombreUsuario() + ")");
        
        rbHashtag.setBackground(COLOR_FONDO);
        rbHashtag.setForeground(COLOR_TEXTO);
        rbMenciones.setBackground(COLOR_FONDO);
        rbMenciones.setForeground(COLOR_TEXTO);
        
        ButtonGroup grupoBusqueda = new ButtonGroup();
        grupoBusqueda.add(rbHashtag);
        grupoBusqueda.add(rbMenciones);
        
        panelOpciones.add(rbHashtag);
        panelOpciones.add(rbMenciones);
        
        JPanel panelAccion = new JPanel(new BorderLayout(5, 5));
        panelAccion.setBackground(COLOR_FONDO);
        
        txtTerminoBusqueda = new JTextField("Ingrese #hashtag o @username");
        btnBuscar = new JButton("Buscar");
        
        txtTerminoBusqueda.setBackground(COLOR_BOTON_FONDO);
        txtTerminoBusqueda.setForeground(COLOR_TEXTO);
        txtTerminoBusqueda.setCaretColor(COLOR_TEXTO);
        txtTerminoBusqueda.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
        
        btnBuscar.setBackground(COLOR_BOTON_DOMINANTE);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        
        panelAccion.add(txtTerminoBusqueda, BorderLayout.CENTER);
        panelAccion.add(btnBuscar, BorderLayout.EAST);
        
        panelControl.add(panelOpciones, BorderLayout.NORTH);
        panelControl.add(panelAccion, BorderLayout.SOUTH);
        
        add(panelControl, BorderLayout.NORTH);
        
        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(COLOR_FONDO);
        
        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.getViewport().setBackground(COLOR_FONDO);
        scrollResultados.setBorder(BorderFactory.createEmptyBorder()); 
        add(scrollResultados, BorderLayout.CENTER);
        
        rbMenciones.addActionListener(e -> {
            txtTerminoBusqueda.setEnabled(false);
            txtTerminoBusqueda.setBackground(COLOR_BOTON_FONDO.darker()); 
            btnBuscar.setText("Ver Menciones");
        });
        
        rbHashtag.addActionListener(e -> {
            txtTerminoBusqueda.setEnabled(true);
            txtTerminoBusqueda.setBackground(COLOR_BOTON_FONDO);
            btnBuscar.setText("Buscar");
        });
        
        btnBuscar.addActionListener(e -> {
            if (rbHashtag.isSelected()) {
                buscarHashtag();
            } else if (rbMenciones.isSelected()) {
                buscarMenciones();
            }
        });
        
        rbHashtag.setSelected(true);
        txtTerminoBusqueda.setEnabled(true);
        btnBuscar.setText("Buscar");
    }

    private void buscarHashtag() {
        String termino = txtTerminoBusqueda.getText().trim();
        if (termino.isEmpty() || !termino.startsWith("#")) {
            JOptionPane.showMessageDialog(this, "El término de búsqueda debe empezar con #.", "Error de Búsqueda", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            ArrayList<Insta> resultados = GestorInsta.buscarPorHashtag(termino);
            mostrarResultados(resultados, "Resultados para: " + termino);

        } catch (IOException e) {
             JOptionPane.showMessageDialog(this, "Error de E/S al buscar posts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarMenciones() {
        String miUsername = usuarioActual.getNombreUsuario();
        try {
            ArrayList<Insta> resultados = GestorInsta.buscarPorMencion(miUsername);
            mostrarResultados(resultados, "Posts que te mencionan (@" + usuarioActual.getNombreUsuario() + ")");

        } catch (IOException e) {
             JOptionPane.showMessageDialog(this, "Error de E/S al buscar menciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarResultados(ArrayList<Insta> resultados, String titulo) {
        panelResultados.removeAll();
        panelResultados.setBackground(COLOR_FONDO);
        
        // Título de Resultados
        JLabel labelTitulo = new JLabel("<html><h2 style='color:" + toHex(COLOR_TEXTO) + ";'>" + titulo + "</h2></html>");
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelResultados.add(labelTitulo);
        panelResultados.add(Box.createVerticalStrut(10));

        if (resultados.isEmpty()) {
            JLabel labelNoResultados = new JLabel("No se encontraron posts.");
            labelNoResultados.setForeground(COLOR_SECUNDARIO_TEXTO);
            labelNoResultados.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelResultados.add(labelNoResultados);
        } else {
            final int anchoPost = 500;
             for (Insta post : resultados) {
                 
                 try {
                     JPanel postPanel = crearPanelPost(post); 
                     
                     postPanel.setMaximumSize(new Dimension(anchoPost, Integer.MAX_VALUE));
                     postPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                     
                     panelResultados.add(postPanel);
                     panelResultados.add(Box.createVerticalStrut(15));
                 } catch (Exception ex) {
                     JPanel errorPanel = new JPanel();
                     errorPanel.setBackground(COLOR_FONDO);
                     JLabel labelError = new JLabel("Error al cargar post de @" + post.getAutorUsername());
                     labelError.setForeground(Color.RED);
                     errorPanel.add(labelError);
                     panelResultados.add(errorPanel);
                     System.err.println("Error al cargar el post en BuscarPanel: " + ex.getMessage());
                 }
            }
        }

        panelResultados.revalidate();
        panelResultados.repaint();
    }
    
    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO); 

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
        labelImg.setBackground(COLOR_BOTON_FONDO);
        labelImg.setOpaque(true);


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
        btnComentar.setForeground(COLOR_BOTON_DOMINANTE);
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

