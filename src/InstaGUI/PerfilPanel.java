/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.DatosPerfil;
import Insta.GestorInsta;
import Insta.Insta;
import Insta.PerfilNoEncontrado;
import Insta.SesionManager;
import Compartidas.Usuario;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class PerfilPanel extends JPanel {

    private String usernamePerfil;
    private JLabel username, nombre, edad, genero, contadorPost, contadorFollowers, ContadorFollowing;
    private JButton btnAccion;

    public PerfilPanel(String usernamePerfil) {
        this.usernamePerfil = usernamePerfil;
        setLayout(new BorderLayout());
        cargarDatosYRenderizar();
    }

    private void cargarDatosYRenderizar() {
        removeAll();
        try {
            String usuarioLogueado = SesionManager.getUsuarioActual().getUsuario();
            DatosPerfil datos = GestorInsta.obtenerPefilCompleto(usernamePerfil, usuarioLogueado);
            add(crearEncabezadoPerfil(datos, usuarioLogueado), BorderLayout.NORTH);
            add(crearGridPublicaciones(datos.getInstasPropios()), BorderLayout.CENTER);
        } catch (PerfilNoEncontrado e) {
            add(new JLabel("<html><h1>Error: Perfil no encontrado<h1><p>" + e.getMessage() + "<p></html>"), BorderLayout.CENTER);
        } catch (IOException e) {
            add(new JLabel("<html><h1>Error de Archivos<h1><p>No se pudieron cargar los datos del perfil<p></html>"));
        } catch (Exception e) {
            add(new JLabel("Error: " + e.getMessage()), BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    private JPanel crearEncabezadoPerfil(DatosPerfil datos, String usuarioLogueado) {

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelInfoSuperior = new JPanel(new BorderLayout(30, 0));

        // 1.1. Foto de Perfil (IZQUIERDA - BorderLayout.WEST)
        JLabel labelFotoPerfil = new JLabel();
        // Aquí deberías cargar la foto del perfil del usuario (usando datos.getDatosGenerales().getRutaFotoPerfil())
        ImageIcon iconoDefault = new ImageIcon("default_profile.png");
        // Redimensionar icono si es necesario
        labelFotoPerfil.setIcon(iconoDefault);
        labelFotoPerfil.setPreferredSize(new Dimension(150, 150));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        panelInfoSuperior.add(labelFotoPerfil, BorderLayout.WEST);

        JPanel panelDatosGenerales = new JPanel();
        panelDatosGenerales.setLayout(new BoxLayout(panelDatosGenerales, BoxLayout.Y_AXIS));

        // Username
        username = new JLabel("<html><h1>@" + datos.getDatosGenerales().getUsuario() + "</h1></html>");
        panelDatosGenerales.add(username);
        panelDatosGenerales.add(Box.createVerticalStrut(5));

        // Datos Personales
        nombre = new JLabel("Nombre: " + datos.getDatosGenerales().getNombreUsuario());
        edad = new JLabel("Edad: " + datos.getDatosGenerales().getEdad());
        genero = new JLabel("Genero: " + datos.getDatosGenerales().getGenero());

        panelDatosGenerales.add(nombre);
        panelDatosGenerales.add(edad);
        panelDatosGenerales.add(genero);
        panelDatosGenerales.add(Box.createVerticalGlue()); 

        panelDatosGenerales.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfoSuperior.add(panelDatosGenerales, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        panelCentral.add(separador);
        panelCentral.add(Box.createVerticalStrut(20)); 

        JPanel panelContadores = new JPanel(new GridLayout(1, 3, 20, 0));
        panelContadores.add(crearContador("Posts", datos.getInstasPropios().size()));
        panelContadores.add(crearContador("Followers", datos.getTotalSeguidores()));
        panelContadores.add(crearContador("Following", datos.getTotalSeguidos()));

        panelContadores.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(panelContadores);
        panelCentral.add(Box.createVerticalStrut(20)); 

        if (usernamePerfil.equalsIgnoreCase(usuarioLogueado)) {
            btnAccion = new JButton("EDITAR PERFIL");
            btnAccion.addActionListener(e -> mostrarOpcionesEdicion(datos.getDatosGenerales()));
        } else {
            if (datos.getloSigueElUsuarioActual()) {
                btnAccion = new JButton("DEJAR DE SEGUIR");
            } else {
                btnAccion = new JButton("SEGUIR");
            }
            btnAccion.addActionListener(e -> manejarFollow(usuarioLogueado, usernamePerfil, datos.getloSigueElUsuarioActual()));
        }

        btnAccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCentral.add(btnAccion);
        panelCentral.add(Box.createVerticalStrut(20)); // Espacio al final
        panelCentral.add(separador);

        panelPrincipal.add(panelInfoSuperior, BorderLayout.NORTH);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        JPanel contenedorFinal = new JPanel(new BorderLayout());
        contenedorFinal.add(panelPrincipal, BorderLayout.NORTH);

        return contenedorFinal;
    }

    private JPanel crearContador(String titulo, int valor) {
        JPanel p = new JPanel(new BorderLayout());
        JLabel labelValor = new JLabel(String.valueOf(valor));
        labelValor.setFont(new Font("Arial", Font.BOLD, 18));
        labelValor.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel labelTitulo = new JLabel(titulo);
        labelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(labelValor, BorderLayout.CENTER);
        p.add(labelTitulo, BorderLayout.SOUTH);
        return p;
    }

    private JScrollPane crearGridPublicaciones(ArrayList<Insta> instas) {
        JPanel grid = new JPanel(new GridLayout(0, 3, 5, 5));

        for (Insta i : instas) {
            JPanel postPanel = new JPanel(new BorderLayout());
            postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel labelImg = new JLabel(i.getRutaImg().isEmpty() ? "TEXTO" : "IMAGEN");
            labelImg.setPreferredSize(new Dimension(100, 200));
            labelImg.setHorizontalAlignment(SwingConstants.CENTER);
            postPanel.add(labelImg, BorderLayout.CENTER);

            JLabel labelContenido = new JLabel("<html><p> style='width: 90px'>" + i.getContenido() + "</p></html>");
            postPanel.add(labelContenido, BorderLayout.SOUTH);
            grid.add(postPanel);
        }
        return new JScrollPane(grid);
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

                    Window vtnActual = SwingUtilities.getWindowAncestor(this);
                    if (vtnActual != null) {
                        vtnActual.dispose();
                    }
                    new vtnLogin().setVisible(true);
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsuario());
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                cargarDatosYRenderizar();
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de la cuenta: " + e.getMessage());
        }
    }

    private void manejarFollow(String seguidor, String seguido, boolean esSiguiendo) {
        try {
            if (esSiguiendo) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Desea dejar de seguir a " + seguido + "?", "Confirmar Unfollow", JOptionPane.YES_NO_OPTION);

                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, false);
                    JOptionPane.showMessageDialog(this, "Has dejado de seguir a " + seguido + ".");
                    cargarDatosYRenderizar();
                }
            } else {
                GestorInsta.actualizarEstadoFollow(seguidor, seguido, true);
                JOptionPane.showMessageDialog(this, "Ahora sigues a " + seguido + ".");
                cargarDatosYRenderizar();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de seguimiento");
        }
    }
}
