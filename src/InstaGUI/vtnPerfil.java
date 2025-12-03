/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Usuario;
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

    private final Usuario usuarioActual;
    private JLabel labelFollowings, labelFollowers;
    private JPanel panelPostPropios;

    public vtnPerfil(Usuario usuario) {
        setTitle("INSTA - Perfil de @ " + usuario.getUsername());
        this.usuarioActual = usuario;
        setSize(700, 700);
        setLocationRelativeTo(null);

        inicializarComponentes();
        cargarDatosPerfil();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //datos personales
        panelInfo.add(new JLabel("<html><h1>@" + usuarioActual.getUsername() + "</h1></html>"));
        panelInfo.add(new JLabel("Nombre: " + usuarioActual.getNombre()));
        panelInfo.add(new JLabel("Edad: " + usuarioActual.getEdad()));
        panelInfo.add(new JLabel("Genero: " + usuarioActual.getGenero()));

        //contadores
        JPanel panelContadores = new JPanel(new GridLayout(1, 2, 20, 0));
        labelFollowings = new JLabel("Siguiendo: 0");
        labelFollowers = new JLabel("Seguidores: 0");
        panelContadores.add(labelFollowings);
        panelContadores.add(labelFollowers);

        panelInfo.add(Box.createVerticalStrut(15));
        panelInfo.add(panelContadores);

        add(panelInfo, BorderLayout.NORTH);

        JPanel panelContenidoCentral = new JPanel(new BorderLayout());
        panelContenidoCentral.add(new JLabel("<html><hr><h2>Tus Posts:</h2></html>", SwingConstants.CENTER), BorderLayout.NORTH);
        
        //post propios
        panelPostPropios = new JPanel();
        panelPostPropios.setLayout(new BoxLayout(panelPostPropios, BoxLayout.Y_AXIS));
        JScrollPane scrollPosts = new JScrollPane(panelPostPropios);
        scrollPosts.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panelContenidoCentral.add(scrollPosts, BorderLayout.CENTER);
        add(panelContenidoCentral, BorderLayout.CENTER);
        add(crearPanelAccion(), BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelAccion(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAccion = new JButton("EDITAR PERFIL");
        
        btnAccion.addActionListener(e -> mostrarOpcionesEdicion(usuarioActual));
        
        panel.add(btnAccion);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));
        return panel;
    }

    private void cargarDatosPerfil() {
        try {
            int followings = GestorInsta.contarFollows(usuarioActual.getUsername(), true); //true para labelFollowings
            int followers = GestorInsta.contarFollows(usuarioActual.getUsername(), false); //false para los labelFollowers

            labelFollowings.setText("Siguiendo: " + followings);
            labelFollowers.setText("Seguidores: " + followers);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al csrgar contadores: " + e.getMessage());
        }

        panelPostPropios.removeAll();

        try {
            ArrayList<Insta> postPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual.getUsername());

            if (postPropios.isEmpty()) {
                panelPostPropios.add(new JLabel("Aun no tienes post publicados."));
            } else {
                for (Insta post : postPropios) {
                    JPanel postPanel = crearPanelPost(post);
                    panelPostPropios.add(postPanel);
                    panelPostPropios.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al cargar tus post: " + e.getMessage());
        }
        panelPostPropios.revalidate();
        panelPostPropios.repaint();
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
                // Si tienes una vtnBusqueda: new vtnBusqueda().setVisible(true);
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
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsername());
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");

                    this.dispose(); 
                    new vtnLogin().setVisible(true); 
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsername());
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                cargarDatosPerfil();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
    }

}
