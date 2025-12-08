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

    //perfil propio
    private final Usuario usuarioActual;
    private JLabel labelFollowings, labelFollowers, labelPosts;
    private JPanel panelPostPropios;
    private JLabel labelFotoPerfil;
    private final vtnInstaPrincipal vtnP;

    public vtnPerfil(Usuario usuario, vtnInstaPrincipal vtnP) {
        this.usuarioActual = usuario;
        this.vtnP = vtnP;
        setSize(600, 800);

        inicializarComponentes();
        cargarDatosPerfil();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        JPanel panelCabecera = new JPanel(new BorderLayout(30, 0));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));

        // ft perfil izquierda
        labelFotoPerfil = new JLabel();
        labelFotoPerfil.setPreferredSize(new Dimension(150, 150));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labelFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setVerticalAlignment(SwingConstants.CENTER);

        // panel datos
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        // Datos personales
        panelDatos.add(new JLabel("<html><h1>@" + usuarioActual.getNombreUsuario() + "</h1></html>"));
        panelDatos.add(new JLabel("Nombre: " + usuarioActual.getUsuario()));
        panelDatos.add(new JLabel("Edad: " + usuarioActual.getEdad()));
        panelDatos.add(new JLabel("Genero: " + usuarioActual.getGenero()));
        panelDatos.add(new JLabel("Desde: "+usuarioActual.getFechaEntrada()));

        labelFotoPerfil.setAlignmentY(Component.TOP_ALIGNMENT);
        panelDatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDatos.add(Box.createVerticalGlue());

        panelCabecera.add(labelFotoPerfil, BorderLayout.WEST);
        panelCabecera.add(panelDatos, BorderLayout.CENTER);

        add(panelCabecera, BorderLayout.NORTH);

        JPanel panelContenidoCentral = new JPanel(new BorderLayout());

        JPanel panelStatsYAccion = new JPanel();
        panelStatsYAccion.setLayout(new BoxLayout(panelStatsYAccion, BoxLayout.Y_AXIS));
        panelStatsYAccion.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Contadores
        JPanel panelContadores = new JPanel(new GridLayout(1, 3, 20, 0));
        labelPosts = crearContador("Posts", 0);
        labelFollowers = crearContador("Seguidores", 0);
        labelFollowings = crearContador("Siguiendo", 0);

        panelContadores.add(labelPosts);
        panelContadores.add(labelFollowers);
        panelContadores.add(labelFollowings);

        panelContadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelStatsYAccion.add(panelContadores);
        panelStatsYAccion.add(Box.createVerticalStrut(15));

        // Btn editar
        JButton btnAccion = new JButton("EDITAR PERFIL");
        btnAccion.addActionListener(e -> abrirOpciones(usuarioActual));
        btnAccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelStatsYAccion.add(btnAccion);
        panelStatsYAccion.add(Box.createVerticalStrut(15));
        JSeparator separadorPrincipal = new JSeparator(SwingConstants.HORIZONTAL);
        separadorPrincipal.setAlignmentX(Component.CENTER_ALIGNMENT);
        separadorPrincipal.setMaximumSize(new Dimension(500, 2));
        panelStatsYAccion.add(separadorPrincipal);
        panelStatsYAccion.add(Box.createVerticalStrut(10));

        JLabel labelTituloPosts = new JLabel("Tus Posts:", SwingConstants.CENTER);
        labelTituloPosts.setFont(new Font("Arial", Font.BOLD, 16));
        labelTituloPosts.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelStatsYAccion.add(labelTituloPosts);
        panelStatsYAccion.add(Box.createVerticalStrut(10));

        panelContenidoCentral.add(panelStatsYAccion, BorderLayout.NORTH);

        //Post Propios 
        panelPostPropios = new JPanel();
        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        JScrollPane scrollPosts = new JScrollPane(panelPostPropios);

        panelContenidoCentral.add(scrollPosts, BorderLayout.CENTER);
        add(panelContenidoCentral, BorderLayout.CENTER);
    }

    private void abrirOpciones(Usuario usuario){
        vtnOpcionesUsuario opc = new vtnOpcionesUsuario(usuario, vtnP);
        opc.setLocationRelativeTo(this);
        opc.setVisible(true);
        cargarDatosPerfil();
    }
    
    private JLabel crearContador(String titulo, int valor) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel labelValor = new JLabel(String.valueOf(valor));
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        p.add(labelValor, BorderLayout.CENTER);
        p.add(labelTitulo, BorderLayout.SOUTH);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(p, BorderLayout.CENTER);

        JLabel labelDisplay = new JLabel();
        labelDisplay.setHorizontalAlignment(SwingConstants.CENTER);
        labelDisplay.setVerticalAlignment(SwingConstants.CENTER);

        labelDisplay.setText("<html><p align='center'><b>" + valor + "</b><br>" + titulo + "</p></html>");

        return labelDisplay;
    }

    public void cargarDatosPerfil() {
        //Usuario us = SesionManager.getUsuarioActual();
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
                labelFollowers.setText(String.format(format, followers, "Seguidores"));
            }
            
            if(labelFollowings!=null){
                labelFollowings.setText(String.format(format, followings, "Siguiendo"));
            }
            
            //labelFollowers.setText(String.format(format, followers, "Seguidores"));
            //labelFollowings.setText(String.format(format, followings, "Siguiendo"));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar contadores o posts: " + e.getMessage());
        }

        panelPostPropios.removeAll();

        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        panelPostPropios.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPostPropios.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final int anchoPost = 500;

        if (postPropios.isEmpty()) {
            panelPostPropios.add(new JLabel("Aún no tiene posts publicados."));
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

            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo cargar la imagen de perfil: " + e.getMessage());
                labelFotoPerfil.setText("Sin foto");
            }
        } else {
            labelFotoPerfil.setText("Sin foto");
        }
    }

    private JPanel crearPanelPost(Insta post) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        panel.setLayout(new BorderLayout(10, 10));

        //encabezado: autor y fecha
        JPanel panelHeader = new JPanel(new BorderLayout());

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelAutor = new JLabel("<html><b>@" + post.getAutorUsername() + " </b></html>");
        panelIzquierdo.add(labelAutor);
        panelIzquierdo.add(new JLabel(" - " + post.getFechaPublicacion().toString()));

        panelHeader.add(panelIzquierdo, BorderLayout.WEST);

        if (post.getAutorUsername().equals(usuarioActual.getUsuario())) {
            JButton btnEliminar = new JButton("X");
            btnEliminar.setForeground(Color.RED);
            btnEliminar.setToolTipText("Eliminar este post");

            btnEliminar.addActionListener(e -> eliminarPost(post));

            JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            panelBoton.add(btnEliminar);
            panelHeader.add(panelBoton, BorderLayout.EAST);
        }

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

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(panelCuerpo, BorderLayout.CENTER);
        panel.add(panelInteraccion, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel crearPanelInteraccion(Insta post) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JTextArea areaComentarios = new JTextArea("Cargando comentarios...");
        areaComentarios.setEditable(false);
        areaComentarios.setWrapStyleWord(true);
        areaComentarios.setLineWrap(true);
        JScrollPane scrollComentarios = new JScrollPane(areaComentarios);
        scrollComentarios.setPreferredSize(new Dimension(500, 80));

        JPanel panelAgregar = new JPanel(new BorderLayout(5, 5));
        JTextField txtComentario = new JTextField();
        JButton btnComentar = new JButton("Comentar");

        btnComentar.addActionListener(e -> {
            agregarComentario(post, txtComentario.getText());
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
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el post: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JLabel crearLabelImagenPost(String rutaImg) {
        JLabel labelImg = new JLabel();
        labelImg.setPreferredSize(new Dimension(500, 400));
        labelImg.setHorizontalAlignment(SwingConstants.CENTER);
        labelImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        if (rutaImg != null && !rutaImg.isEmpty()) {
            try {
                ImageIcon iconoOriginal = new ImageIcon(rutaImg);
                Image imagen = iconoOriginal.getImage();

                Image imagenRedimensionada = imagen.getScaledInstance(
                        labelImg.getPreferredSize().width,
                        labelImg.getPreferredSize().height,
                        Image.SCALE_SMOOTH);

                labelImg.setIcon(new ImageIcon(imagenRedimensionada));
                labelImg.setText("");
            } catch (Exception e) {
                System.err.println("Advertencia: No se pudo cargar la imagen del post: " + e.getMessage());
                labelImg.setText("Error al cargar imagen: " + rutaImg);
            }
        } else {
            labelImg.setText("Post sin imagen");
        }
        return labelImg;
    }
    
    private void manejarDesactivacion(Usuario usuario) {
        try {
            if (usuario.isActivo()) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro? Su cuenta no aparecerá en búsquedas ni sus comentarios.", "Confirmar Desactivación", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), false); 
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");

                    if (vtnP != null) {
                        vtnP.dispose();
                        new vtnLogin().setVisible(true); 
                    }
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), true);
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                cargarDatosPerfil();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
    }

}
