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
import Insta.Comentario;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class PerfilPanel extends JPanel {

    //logica para el otro perfil
    private String usernamePerfil;
    private JLabel username, nombre, edad, genero, fecha;
    private JButton btnAccion;
    private JPanel panelPosts;

    public PerfilPanel(String usernamePerfil) {
        this.usernamePerfil = usernamePerfil;
        setLayout(new BorderLayout());
         try {
        cargarDatosYRenderizar();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Fallo al crear PerfilPanel: " + ex.getMessage(), "Error Crítico de Carga", JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace();
    }
    }

    public void cargarDatosYRenderizar() {
        removeAll();
        try {
            String usuarioLogueado = SesionManager.getUsuarioActual().getNombreUsuario();
            DatosPerfil datos = GestorInsta.obtenerPefilCompleto(usernamePerfil, usuarioLogueado);
            add(crearEncabezadoPerfil(datos, usuarioLogueado), BorderLayout.NORTH);
            //add(crearGridPublicaciones(datos.getInstasPropios()), BorderLayout.CENTER);
            add(crearListaPublicaciones(datos.getInstasPropios()), BorderLayout.CENTER);
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

        JPanel panelDatosGenerales = new JPanel();

        JLabel labelFotoPerfil = new JLabel();
        labelFotoPerfil.setPreferredSize(new Dimension(150, 150));
        labelFotoPerfil.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        String rutaFoto = datos.getDatosGenerales().getRutaFotoPerfil();

        if (rutaFoto != null && !rutaFoto.isEmpty() && new java.io.File(rutaFoto).exists()) {
            try {
                ImageIcon icono = new ImageIcon(rutaFoto);
                Image img = icono.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                labelFotoPerfil.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                labelFotoPerfil.setText("Error cargando foto");
            }
        } else {
            ImageIcon iconoDefault = new ImageIcon("default_profile.png");
            labelFotoPerfil.setIcon(iconoDefault);
        }
        panelInfoSuperior.add(labelFotoPerfil, BorderLayout.WEST);

        panelDatosGenerales.setLayout(new BoxLayout(panelDatosGenerales, BoxLayout.Y_AXIS));

        // Username
        username = new JLabel("<html><h1>@" + datos.getDatosGenerales().getUsuario() + "</h1></html>");
        panelDatosGenerales.add(username);
        panelDatosGenerales.add(Box.createVerticalStrut(5));

        // Datos Personales
        nombre = new JLabel("Nombre: " + datos.getDatosGenerales().getNombreUsuario());
        edad = new JLabel("Edad: " + datos.getDatosGenerales().getEdad());
        genero = new JLabel("Genero: " + datos.getDatosGenerales().getGenero());
        fecha = new JLabel("Desde: "+datos.getDatosGenerales().getFechaEntrada());

        panelDatosGenerales.add(nombre);
        panelDatosGenerales.add(edad);
        panelDatosGenerales.add(genero);
        panelDatosGenerales.add(fecha);
        panelDatosGenerales.add(Box.createVerticalGlue());

        panelDatosGenerales.setAlignmentX(Component.LEFT_ALIGNMENT);

        panelInfoSuperior.add(panelDatosGenerales, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));

        JSeparator separador = new JSeparator(SwingConstants.HORIZONTAL);
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
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
        panelCentral.add(Box.createVerticalStrut(20));
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

    private JScrollPane crearListaPublicaciones(ArrayList<Insta> instas) {
        panelPosts = new JPanel();
        panelPosts.setLayout(new BoxLayout(panelPosts, BoxLayout.Y_AXIS));
        panelPosts.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelPosts.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        final int anchoPost = 500;
        
        if (instas.isEmpty()) {
            panelPosts.add(new JLabel("Aún no tiene posts publicados."));
        } else {
            for (Insta post : instas) {
                JPanel panelPostCompleto = crearPanelPost(post);
                panelPostCompleto.setMaximumSize(new Dimension(anchoPost, Integer.MAX_VALUE));
                panelPostCompleto.setPreferredSize(new Dimension(anchoPost, panelPostCompleto.getPreferredSize().height));
                panelPostCompleto.setAlignmentX(Component.CENTER_ALIGNMENT);

                panelPosts.add(panelPostCompleto);
                panelPosts.add(Box.createVerticalStrut(20));
            }
        }

        JScrollPane scrollPane = new JScrollPane(panelPosts);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
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

    private JScrollPane crearGridPublicaciones(ArrayList<Insta> instas) {
        JPanel grid = new JPanel(new GridLayout(0, 3, 5, 5));

        for (Insta i : instas) {
            JPanel postPanel = new JPanel(new BorderLayout());
            postPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel labelImg = new JLabel();
            labelImg.setPreferredSize(new Dimension(100, 200));
            labelImg.setHorizontalAlignment(SwingConstants.CENTER);

            String rutaImg = i.getRutaImg();
            if (rutaImg != null && !rutaImg.isEmpty() && new java.io.File(rutaImg).exists()) {
                try {
                    ImageIcon icono = new ImageIcon(rutaImg);
                    Image img = icono.getImage().getScaledInstance(100, 200, Image.SCALE_SMOOTH);
                    labelImg.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    labelImg.setText("IMG ERROR");
                }
            } else {
                labelImg.setText("Sin Imagen");
            }

            postPanel.add(labelImg, BorderLayout.CENTER);

            JLabel labelContenido = new JLabel("<html><p style='width: 90px'>" + i.getTexto() + "</p></html>");
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
                    GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), false);
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");

                    Window vtnActual = SwingUtilities.getWindowAncestor(this);
                    if (vtnActual != null) {
                        vtnActual.dispose();
                    }
                    new vtnLogin().setVisible(true);
                }
            } else {
                GestorInsta.actualizarEstadoCuenta(usuario.getUsuario(), true);
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
