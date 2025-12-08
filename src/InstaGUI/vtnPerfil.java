/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Compartidas.Usuario;
import Insta.Comentario;
import Insta.Insta;
import Insta.ManejoArchivosBinarios;
import Insta.SesionManager;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.swing.SwingUtilities;

/**
 *
 * @author HP
 */

public class vtnPerfil extends JPanel {

    private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO_TEXTO = new Color(150, 150, 150); 
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132); 
    private final Color COLOR_BOTON_FONDO = new Color(38, 38, 38); 
    private final Color COLOR_BORDE_POST = new Color(50, 50, 50); 
    
    private final Usuario usuarioActual;
    private JLabel labelFollowings, labelFollowers, labelPosts;
    private JPanel panelPostPropios;
    private JLabel labelFotoPerfil;
    private final vtnInstaPrincipal vtnP;

    public vtnPerfil(Usuario usuario, vtnInstaPrincipal vtnP) {
        this.usuarioActual = usuario;
        this.vtnP = vtnP;
        setSize(600, 800);

        setBackground(COLOR_FONDO);

        inicializarComponentes();
        cargarDatosPerfil();
    }

    private void volverALogin() {
        SesionManager.cerrarSesion(); 
        
        vtnP.dispose(); 
        
        SwingUtilities.invokeLater(() -> {
            vtnLogin login = new vtnLogin(); 
            login.setVisible(true);
        });
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelTopBar = new JPanel(new BorderLayout());
        panelTopBar.setBackground(COLOR_FONDO);
        panelTopBar.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JButton btnVolver = new JButton("<-");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 18));
        btnVolver.setForeground(COLOR_TEXTO);
        btnVolver.setBackground(COLOR_FONDO);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setToolTipText("Cerrar Sesión e ir al Login");

        btnVolver.addActionListener(e -> {
            volverALogin();
        });
        
        JPanel panelTituloWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTituloWrapper.setBackground(COLOR_FONDO);
        JLabel labelTituloPerfil = new JLabel("Mi Perfil");
        labelTituloPerfil.setFont(new Font("Arial", Font.BOLD, 20));
        labelTituloPerfil.setForeground(COLOR_TEXTO);
        panelTituloWrapper.add(labelTituloPerfil);
        
        panelTopBar.add(btnVolver, BorderLayout.WEST);
        panelTopBar.add(panelTituloWrapper, BorderLayout.CENTER);
        
        JPanel panelNorthWrapper = new JPanel();
        panelNorthWrapper.setLayout(new BoxLayout(panelNorthWrapper, BoxLayout.Y_AXIS));
        panelNorthWrapper.setBackground(COLOR_FONDO);
        panelNorthWrapper.add(panelTopBar);
        
        JPanel panelInfoPerfil = new JPanel();
        panelInfoPerfil.setLayout(new BoxLayout(panelInfoPerfil, BoxLayout.Y_AXIS)); 
        panelInfoPerfil.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        panelInfoPerfil.setBackground(COLOR_FONDO);

        JPanel panelFotoYDatos = new JPanel(new BorderLayout(15, 0));
        panelFotoYDatos.setBackground(COLOR_FONDO);

        labelFotoPerfil = new JLabel();
        labelFotoPerfil.setPreferredSize(new Dimension(100, 100));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST));
        labelFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setVerticalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setForeground(COLOR_SECUNDARIO_TEXTO);
        labelFotoPerfil.setAlignmentY(Component.TOP_ALIGNMENT);
        
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(COLOR_FONDO);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JLabel labelUsuario = new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + "; font-size: 14pt;'>@" + usuarioActual.getNombreUsuario() + "</b></html>");
        JLabel labelNombre = new JLabel("Nombre: " + usuarioActual.getUsuario()); 
        JLabel labelEdad = new JLabel("Edad: " + usuarioActual.getEdad());
        JLabel labelGenero = new JLabel("Genero: " + usuarioActual.getGenero());
        JLabel labelDesde = new JLabel("Desde: "+usuarioActual.getFechaEntrada());
        
        labelUsuario.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelEdad.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelGenero.setAlignmentX(Component.LEFT_ALIGNMENT);
        labelDesde.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        labelNombre.setForeground(COLOR_TEXTO);
        labelEdad.setForeground(COLOR_TEXTO);
        labelGenero.setForeground(COLOR_TEXTO);
        labelDesde.setForeground(COLOR_SECUNDARIO_TEXTO);
        
        panelDatos.add(labelUsuario);
        panelDatos.add(labelNombre);
        panelDatos.add(labelEdad);
        panelDatos.add(labelGenero);
        panelDatos.add(labelDesde);
        panelDatos.add(Box.createVerticalGlue());

        panelFotoYDatos.add(labelFotoPerfil, BorderLayout.WEST);
        panelFotoYDatos.add(panelDatos, BorderLayout.CENTER);
        
        JPanel panelStatsYContadores = new JPanel(new GridLayout(1, 3, 10, 0));
        panelStatsYContadores.setBackground(COLOR_FONDO);
        panelStatsYContadores.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); 

        labelPosts = crearContador("Posts", 0);
        labelFollowers = crearContador("Followers", 0);
        labelFollowings = crearContador("Following", 0); 

        panelStatsYContadores.add(labelPosts);
        panelStatsYContadores.add(labelFollowers);
        panelStatsYContadores.add(labelFollowings);
        
        panelInfoPerfil.add(panelFotoYDatos);
        panelInfoPerfil.add(panelStatsYContadores);

        panelNorthWrapper.add(panelInfoPerfil);
        add(panelNorthWrapper, BorderLayout.NORTH);
        
        JPanel panelContenidoCentral = new JPanel(new BorderLayout());
        panelContenidoCentral.setBackground(COLOR_FONDO);

        JPanel panelAccionYTitulo = new JPanel();
        panelAccionYTitulo.setLayout(new BoxLayout(panelAccionYTitulo, BoxLayout.Y_AXIS));
        panelAccionYTitulo.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 15)); 
        panelAccionYTitulo.setBackground(COLOR_FONDO);

        JButton btnAccion = new JButton("EDITAR PERFIL");
        btnAccion.addActionListener(e -> abrirOpciones(usuarioActual));
        btnAccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnAccion.setBackground(new Color(60, 60, 60)); 
        btnAccion.setForeground(COLOR_TEXTO);
        btnAccion.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 1));
        btnAccion.setFocusPainted(false);
        btnAccion.setPreferredSize(new Dimension(200, 30));
        btnAccion.setMaximumSize(new Dimension(500, 30)); 
        
        panelAccionYTitulo.add(btnAccion); 
        panelAccionYTitulo.add(Box.createVerticalStrut(15));
        
        JSeparator separadorPrincipal = new JSeparator(SwingConstants.HORIZONTAL);
        separadorPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);
        separadorPrincipal.setMaximumSize(new Dimension(500, 2));
        separadorPrincipal.setForeground(COLOR_BORDE_POST);
        separadorPrincipal.setBackground(COLOR_FONDO);
        panelAccionYTitulo.add(separadorPrincipal);
        panelAccionYTitulo.add(Box.createVerticalStrut(10));

        JLabel labelTituloPosts = new JLabel("Tus Posts:", SwingConstants.CENTER);
        labelTituloPosts.setFont(new Font("Arial", Font.BOLD, 16));
        labelTituloPosts.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelTituloPosts.setForeground(COLOR_TEXTO);
        
        panelAccionYTitulo.add(labelTituloPosts);
        panelAccionYTitulo.add(Box.createVerticalStrut(10));

        panelContenidoCentral.add(panelAccionYTitulo, BorderLayout.NORTH);

        panelPostPropios = new JPanel();
        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        panelPostPropios.setBackground(COLOR_FONDO);
        
        JScrollPane scrollPosts = new JScrollPane(panelPostPropios);
        scrollPosts.setBorder(BorderFactory.createEmptyBorder());
        scrollPosts.setBackground(COLOR_FONDO);

        panelContenidoCentral.add(scrollPosts, BorderLayout.CENTER);
        add(panelContenidoCentral, BorderLayout.CENTER);
    }
    
    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private void abrirOpciones(Usuario usuario){
        vtnOpcionesUsuario opc = new vtnOpcionesUsuario(usuario, vtnP);
        opc.setLocationRelativeTo(this);
        opc.setVisible(true);
        cargarDatosPerfil();
    }
    
    private JLabel crearContador(String titulo, int valor) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(COLOR_FONDO); 
        
        JLabel labelValor = new JLabel(String.valueOf(valor));
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setHorizontalAlignment(SwingConstants.CENTER);
        labelValor.setForeground(COLOR_TEXTO); 

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitulo.setForeground(COLOR_TEXTO);

        p.add(labelValor, BorderLayout.CENTER);
        p.add(labelTitulo, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(p, BorderLayout.CENTER);
        wrapper.setBackground(COLOR_FONDO);

        JLabel labelDisplay = new JLabel();
        labelDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        labelDisplay.setVerticalAlignment(SwingConstants.CENTER);
        labelDisplay.setForeground(COLOR_TEXTO);

        labelDisplay.setText("<html><p align='center'><b>" + valor + "</b><br>" + titulo + "</p></html>");

        return labelDisplay;
    }

    public void cargarDatosPerfil() {
        int followings = 0;
        int followers = 0;
        ArrayList<Insta> postPropios = new ArrayList<>();
        int numPosts = 0;
        try {
            followings = GestorInsta.contarFollows(usuarioActual.getNombreUsuario(), true); //true para labelFollowings
            followers = GestorInsta.contarFollows(usuarioActual.getNombreUsuario(), false); //false para los labelFollowers
            postPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual.getUsuario());
            numPosts = postPropios.size();

            String format = "<html><p align='center'><b>%d</b><br>%s</p></html>";

            if (labelPosts != null) {
                labelPosts.setText(String.format(format, numPosts, "Posts"));
            }
            
            if(labelFollowers!=null){
                labelFollowers.setText(String.format(format, followers, "Followers"));
            }
            
            if(labelFollowings!=null){
                labelFollowings.setText(String.format(format, followings, "Followings"));
            }
            

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar contadores o posts: " + e.getMessage());
        }

        panelPostPropios.removeAll();

        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        panelPostPropios.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPostPropios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final int anchoPost = 500;

        if (postPropios.isEmpty()) {
            JLabel labelNoPosts = new JLabel("Aún no tiene posts publicados.");
            labelNoPosts.setForeground(COLOR_SECUNDARIO_TEXTO);
            panelPostPropios.add(labelNoPosts);
        } else {
            for (Insta post : postPropios) {
                JPanel panelPostCompleto = crearPanelPost(post);
                panelPostCompleto.setMaximumSize(new Dimension(anchoPost, Integer.MAX_VALUE));
                panelPostCompleto.setPreferredSize(new Dimension(anchoPost, panelPostCompleto.getPreferredSize().height));
                panelPostCompleto.setAlignmentX(Component.CENTER_ALIGNMENT);

                panelPostPropios.add(panelPostCompleto);
                panelPostPropios.add(Box.createVerticalStrut(20));
            }
        }

        cargarFotoPerfil();
        panelPostPropios.revalidate();
        panelPostPropios.repaint();
        this.revalidate();
        this.repaint();
    }

    private void cargarFotoPerfil() {
        String rutaFoto = usuarioActual.getRutaFotoPerfil();

        if (rutaFoto != null && !rutaFoto.isEmpty()) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(rutaFoto);

                Image imagen = iconoOriginal.getImage();
                Image imagenRedimensionada = imagen.getScaledInstance(
                        labelFotoPerfil.getPreferredSize().width,
                        labelFotoPerfil.getPreferredSize().height,
                        Image.SCALE_SMOOTH);

                labelFotoPerfil.setIcon(new ImageIcon(imagenRedimensionada));
                labelFotoPerfil.setText(null);

            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo cargar la imagen de perfil: " + e.getMessage());
                labelFotoPerfil.setText("Sin foto");
            }
        } else {
            labelFotoPerfil.setText("Sin foto");
            labelFotoPerfil.setForeground(COLOR_SECUNDARIO_TEXTO);
        }
    }

    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_POST, 2));
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(COLOR_FONDO);

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_FONDO);

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierdo.setBackground(COLOR_FONDO);
        
        JLabel labelAutor = new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + ";'>@" + post.getAutorUsername() + " </b></html>");
        panelIzquierdo.add(labelAutor);
        
        JLabel labelFecha = new JLabel(" - " + post.getFechaPublicacion().toString());
        labelFecha.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelIzquierdo.add(labelFecha);

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);

        if (post.getAutorUsername().equals(usuarioActual.getUsuario())) {
            JButton btnEliminar = new JButton("X");
            btnEliminar.setForeground(COLOR_SECUNDARIO_TEXTO); 
            btnEliminar.setBackground(COLOR_FONDO);
            btnEliminar.setBorderPainted(false);
            btnEliminar.setToolTipText("Eliminar este post");

            btnEliminar.addActionListener(e -> eliminarPost(post));

            JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBoton.setBackground(COLOR_FONDO);
            panelBoton.add(btnEliminar);
            panelHeader.add(panelBoton, BorderLayout.EAST);
        }

        JPanel panelCuerpo = new JPanel();
        panelCuerpo.setLayout(new BoxLayout(panelCuerpo, BoxLayout.Y_AXIS));
        panelCuerpo.setBackground(COLOR_FONDO);

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
                labelImg.setForeground(Color.RED);
            }
        } else {
            labelImg.setText("IMAGEN NO DISPONIBLE");
            labelImg.setForeground(COLOR_SECUNDARIO_TEXTO);
        }
        labelImg.setBackground(COLOR_BOTON_FONDO);
        labelImg.setOpaque(true);
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

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(panelCuerpo, BorderLayout.CENTER);
        panel.add(panelInteraccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInteraccion(Insta post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        panel.setBackground(COLOR_FONDO);

        JTextArea areaComentarios = new JTextArea("Cargando comentarios...");
        areaComentarios.setEditable(false);
        areaComentarios.setWrapStyleWord(true);
        areaComentarios.setLineWrap(true);
        
        areaComentarios.setBackground(COLOR_FONDO);
        areaComentarios.setForeground(COLOR_SECUNDARIO_TEXTO);
        
        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        scrollComentarios.setPreferredSize(new Dimension(500, 80));
        scrollComentarios.setBorder(BorderFactory.createEmptyBorder()); 
        
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
            agregarComentario(post, txtComentario.getText());
            txtComentario.setText(""); 
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

    private void agregarComentario(Insta post, String texto) {
        if (!texto.trim().isEmpty()) {
            try {
                String autor = SesionManager.getUsuarioActual().getNombreUsuario();
                Comentario nuevoComentario = new Comentario(autor, texto);

                GestorInsta.guardarComentario(post, nuevoComentario);

                JOptionPane.showMessageDialog(this, "Comentario publicado.");

                cargarDatosPerfil();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar el comentario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "El comentario no puede estar vacío.");
        }
    }

    private void eliminarPost(Insta post) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro de que quieres eliminar este post?", "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                GestorInsta.eliminarInsta(post);
                JOptionPane.showMessageDialog(this, "Post eliminado exitosamente.");
                cargarDatosPerfil();
                vtnP.refrescarVistas(); 
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el post: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}
