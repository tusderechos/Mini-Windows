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
import Insta.Usuario;
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
    private JLabel username, nombre, contadorPost, contadorFollowers, ContadorFollowing;
    private JButton btnAccion;

    public PerfilPanel(String usernamePerfil) {
        this.usernamePerfil = usernamePerfil;
        setLayout(new BorderLayout());
        cargarDatosYRenderizar();
    }

    private void cargarDatosYRenderizar() {
        removeAll();
        try {
            String usuarioLogueado = SesionManager.getUsuarioActual().getUsername();
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
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //panel izquierdo: el username y ft
        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelIzquierdo.add(new JLabel(new ImageIcon("default_profile.png")));
        username = new JLabel(datos.getDatosGenerales().getUsername());
        username.setFont(new Font("Arial", Font.BOLD, 24));
        panelIzquierdo.add(username);
        panel.add(panelIzquierdo, BorderLayout.WEST);

        //panelcentral: contadores de post, followers y following
        JPanel panelContadores = new JPanel(new GridLayout(1, 3));
        panelContadores.add(crearContador("Posts", datos.getInstasPropios().size()));
        panelContadores.add(crearContador("Followers", datos.getTotalSeguidores()));
        panelContadores.add(crearContador("Following", datos.getTotalSeguidos()));
        panel.add(panelContadores, BorderLayout.CENTER);

        //panel inferior: nombre y btn de accion
        JPanel panelInferior = new JPanel(new BorderLayout());
        nombre = new JLabel("Nombre: " + datos.getDatosGenerales().getNombre());
        panelInferior.add(nombre, BorderLayout.WEST);

        //logica del btn de accion
        if (usernamePerfil.equalsIgnoreCase(usuarioLogueado)) {
            //aqui es que el user esta viendo su propio perfil
            btnAccion = new JButton("EDITAR PERFIL");
            btnAccion.addActionListener(e -> mostrarOpcionesEdicion(datos.getDatosGenerales()));
        } else {
            //aqui el user esta viendo otro perfil
            if (datos.getloSigueElUsuarioActual()) { //isLoSigueElUsuarioActual
                btnAccion = new JButton("DEJAR DE SEGUIR");
            } else {
                btnAccion = new JButton("SEGUIR");
            }
            btnAccion.addActionListener(e -> manejarFollow(usuarioLogueado, usernamePerfil, datos.getloSigueElUsuarioActual()));
        }
        panelInferior.add(btnAccion, BorderLayout.EAST);
        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
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
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsername());
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");
                    
                    Window vtnActual = SwingUtilities.getWindowAncestor(this);
                    if(vtnActual != null){
                        vtnActual.dispose();
                    }
                    new vtnLogin().setVisible(true);
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsername());
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