/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Compartidas.Usuario;
import Insta.Insta;
import Insta.ManejoArchivosBinarios;
import Insta.SesionManager;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author HP
 */
public class vtnPerfil extends JDialog {

    //perfil propio
    private final Usuario usuarioActual;
    private JLabel labelFollowings, labelFollowers, labelPosts;
    private JPanel panelPostPropios;
    private JLabel labelFotoPerfil;

    public vtnPerfil(Usuario usuario) {
        setTitle("INSTA - Perfil de @" + usuario.getNombreUsuario());
        this.usuarioActual = usuario;
        setSize(550, 800);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarDatosPerfil();
    }

    private void inicializarComponentes() {
        /*setLayout(new BorderLayout(10, 10));

        JPanel panelCabecera = new JPanel();
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //foto de Perfil - Izquierda
        labelFotoPerfil = new JLabel();
        labelFotoPerfil.setPreferredSize(new Dimension(150, 150));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        labelFotoPerfil.setHorizontalAlignment(SwingConstants.CENTER);
        labelFotoPerfil.setVerticalAlignment(SwingConstants.CENTER);

        //panel de Datos - Derecha
        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        // Datos personales
        panelDatos.add(new JLabel("<html><h1>@" + usuarioActual.getNombreUsuario() + "</h1></html>"));
        panelDatos.add(new JLabel("Nombre: " + usuarioActual.getUsuario()));
        panelDatos.add(new JLabel("Edad: " + usuarioActual.getEdad()));
        panelDatos.add(new JLabel("Genero: " + usuarioActual.getGenero()));

        //contadores
        JPanel panelContadores = new JPanel(new GridLayout(1, 3, 20, 0));
        labelPosts = new JLabel("Posts: 0", SwingConstants.CENTER);
        labelFollowings = new JLabel("Siguiendo: 0");
        labelFollowers = new JLabel("Seguidores: 0");
        panelContadores.add(labelPosts);
        panelContadores.add(labelFollowings);
        panelContadores.add(labelFollowers);

        panelDatos.add(Box.createVerticalStrut(15));
        panelDatos.add(panelContadores);

        labelFotoPerfil.setAlignmentY(Component.TOP_ALIGNMENT);
        panelDatos.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelDatos.add(Box.createVerticalGlue());

        panelCabecera.add(labelFotoPerfil, BorderLayout.WEST);
        panelCabecera.add(panelDatos, BorderLayout.CENTER);

        add(panelCabecera, BorderLayout.NORTH);

        JPanel panelContenidoCentral = new JPanel(new BorderLayout());
        panelContenidoCentral.add(new JLabel("<html><hr><h2>Tus Posts:</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);

        //post propios
        panelPostPropios = new JPanel();
        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        JScrollPane scrollPosts = new JScrollPane(panelPostPropios);
        scrollPosts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelContenidoCentral.add(scrollPosts, BorderLayout.CENTER);
        add(panelContenidoCentral, BorderLayout.CENTER);
        add(crearPanelAccion(), BorderLayout.SOUTH);*/

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
        panelDatos.add(new JLabel("<html><h1>@" + usuarioActual.getUsuario() + "</h1></html>")); 
        panelDatos.add(new JLabel("Nombre: " + usuarioActual.getNombreUsuario())); 
        panelDatos.add(new JLabel("Edad: " + usuarioActual.getEdad()));
        panelDatos.add(new JLabel("Genero: " + usuarioActual.getGenero()));

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

        // Botón de Edición 
        JButton btnAccion = new JButton("EDITAR PERFIL");
        btnAccion.addActionListener(e -> mostrarOpcionesEdicion(usuarioActual));
        btnAccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelStatsYAccion.add(btnAccion);
        panelStatsYAccion.add(Box.createVerticalStrut(15)); // Espacio antes de los posts
        panelStatsYAccion.add(new JLabel("<html><hr><h2>Tus Posts:</h2></html>", SwingConstants.CENTER));

        panelContenidoCentral.add(panelStatsYAccion, BorderLayout.NORTH); // Añadir Stats y Botón al Norte del Central

        //Post Propios 
        panelPostPropios = new JPanel();
        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        JScrollPane scrollPosts = new JScrollPane(panelPostPropios);
        scrollPosts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelContenidoCentral.add(scrollPosts, BorderLayout.CENTER);
        add(panelContenidoCentral, BorderLayout.CENTER);
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

    private void cargarDatosPerfil() {
        int followings = 0;
        int followers = 0;
        ArrayList<Insta> postPropios = new ArrayList<>();
        int numPosts = 0;
        try {
            followings = GestorInsta.contarFollows(usuarioActual.getUsuario(), true); //true para labelFollowings
            followers = GestorInsta.contarFollows(usuarioActual.getUsuario(), false); //false para los labelFollowers
            postPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual.getUsuario());
            numPosts = postPropios.size();

            String format = "<html><p align='center'><b>%d</b><br>%s</p></html>";

            if (labelPosts != null) {
                labelPosts.setText(String.format(format, numPosts, "Posts"));
            }
            labelFollowers.setText(String.format(format, followers, "Seguidores"));
            labelFollowings.setText(String.format(format, followings, "Siguiendo"));

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar contadores o posts: " + e.getMessage());
        }

        panelPostPropios.removeAll();

        if (postPropios.isEmpty()) {
            panelPostPropios.add(new JLabel("Aun no tienes post publicados."));
        } else {
            for (Insta post : postPropios) {
                JPanel postPanel = crearPanelPost(post);
                panelPostPropios.add(postPanel);
                panelPostPropios.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }
        cargarFotoPerfil();
        panelPostPropios.revalidate();
        panelPostPropios.repaint();
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setLayout(new BorderLayout(10, 10));

        //encabezado: autor y fecha
        JPanel panelHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel labelAutor = new JLabel("<html><b>@" + post.getAutorUsername() + "</b></html>");
        panelHeader.add(labelAutor);

        //cuerpo: img y texto
        JPanel panelCuerpo = new JPanel(new BorderLayout());

        //simulacionde la img
        JLabel labelImg = new JLabel("img aqui: " + post.getRutaImg());
        labelImg.setPreferredSize(new Dimension(550, 400));
        labelImg.setHorizontalAlignment(SwingConstants.CENTER);
        labelImg.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        //texto
        JTextArea txtCaption = new JTextArea(post.getTexto());
        txtCaption.setWrapStyleWord(true);
        txtCaption.setLineWrap(true);
        txtCaption.setEditable(false);
        txtCaption.setBackground(panel.getBackground());

        panelCuerpo.add(labelImg, BorderLayout.NORTH);
        panelCuerpo.add(txtCaption, BorderLayout.CENTER);

        panel.add(panelHeader, BorderLayout.NORTH);
        panel.add(panelCuerpo, BorderLayout.CENTER);

        return panel;
    }

    private void mostrarOpcionesEdicion(Usuario usuario) {
        String[] opciones = {"Buscar Personas", "Desactivar/Activar Cuenta", "Agregar Foto de Perfil"};

        String estadoActual = usuario.isActivo() ? "(ACTIVA)" : "(DESACTIVA)";
        opciones[1] = opciones[1] + estadoActual;

        String seleccion = (String) JOptionPane.showInputDialog(
                this,
                "Seleccione una opcion de Edicion:",
                "Editar Profile",
                JOptionPane.PLAIN_MESSAGE,
                null,
                opciones,
                opciones[0]);

        if (seleccion != null) {
            if (seleccion.contains("Buscar Personas")) {
                System.out.println("abrir vtn de busqueda personas");
            } else if (seleccion.contains("Desactivar/Activar Cuenta")) {
                manejarDesactivacion(usuario);
            } else if (seleccion.contains("Agregar Foto de Perfil")) {
                System.out.println("abrir JFilechooser y llamar a guardarImagenEnSistema()");
            }
        }
    }

    private void manejarDesactivacion(Usuario usuario) {
        try {
            if (usuario.isActivo()) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estas seguro? Su cuenta no aparecera en busquedas.", "Confirmar Desactivacion", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsuario());
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");

                    this.dispose();
                    new vtnLogin().setVisible(true);
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsuario());
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                cargarDatosPerfil();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
    }

}
