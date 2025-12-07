/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Insta;
import Compartidas.Usuario;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class vtnInstaPrincipal extends JFrame implements PostListener{

    //3 vtn
    private final Usuario usuarioActual;
    private JTextField txtBusqueda;
    private JPanel panelContenidoCentral;
    private CardLayout cardLayout;
    private TimeLine timeLine;
    private Buscar buscar;
    private vtnPerfil perfil;

    public vtnInstaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("INSTA - TimeLine de @" + usuario.getNombreUsuario());
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarVistas();
        inicializarComponentes();
        mostrarVista("Perfil");
    }

    private void inicializarVistas() {
        cardLayout = new CardLayout();
        panelContenidoCentral = new JPanel(cardLayout);

        timeLine = new TimeLine(usuarioActual);
        buscar = new Buscar(usuarioActual, this);
        perfil = new vtnPerfil(usuarioActual, this);

        panelContenidoCentral.add(timeLine, "TimeLine");
        panelContenidoCentral.add(buscar, "Buscar");
        panelContenidoCentral.add(new JLabel("PUBLICAR", SwingConstants.CENTER), "Publicar"); 
        panelContenidoCentral.add(perfil, "Perfil");
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        add(panelContenidoCentral, BorderLayout.CENTER);

        JPanel panelBarraNavegacion = new JPanel(new GridLayout(1, 4, 10, 0));
        panelBarraNavegacion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnTimeLine = new JButton("TimeLine");
        btnTimeLine.addActionListener(e -> {
        timeLine.cargarTimeLine();
        mostrarVista("TimeLine");});

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> {
            mostrarVista("Buscar");
        });

        JButton btnPublicar = new JButton("Publicar");
        btnPublicar.addActionListener(e -> {
            vtnCrearInsta crearInsta = new vtnCrearInsta(this);
            crearInsta.setVisible(true);
        });

        JButton btnPerfil = new JButton("Perfil");
        btnPerfil.addActionListener(e -> {
            perfil.cargarDatosPerfil();
            mostrarVista("Perfil");
        });

        panelBarraNavegacion.add(btnTimeLine);
        panelBarraNavegacion.add(btnBuscar);
        panelBarraNavegacion.add(btnPublicar);
        panelBarraNavegacion.add(btnPerfil);

        add(panelBarraNavegacion, BorderLayout.SOUTH);
    }

    private void mostrarVista(String nombreVista) {
        cardLayout.show(panelContenidoCentral, nombreVista);
    }

    private void buscarUsuarios() {
        String texto = txtBusqueda.getText().trim();
        if (texto.isEmpty() || texto.equals("Buscar usuario...")) {
            JOptionPane.showMessageDialog(this, "Ingrese un termino de busqueda valido.");
            return;
        }

        try {
            ArrayList<Usuario> resultados = GestorInsta.buscarPersonas(texto, usuarioActual.getNombreUsuario());
            JDialog rd = new JDialog(this, "Resultados de Busqueda", true);
            rd.setSize(400, 500);
            rd.setLayout(new BorderLayout());

            JPanel panelLista = new JPanel();
            panelLista.setLayout(new BoxLayout(panelLista, BoxLayout.Y_AXIS));

            if (resultados.isEmpty()) {
                panelLista.add(new JLabel("No se encontraron usuarios que coincidan con su busqueda."));
            } else {
                for (Usuario u : resultados) {
                    JPanel userPanel = crearPanelUsuario(u, rd);
                    panelLista.add(userPanel);
                    panelLista.add(new JSeparator(SwingConstants.HORIZONTAL));
                }
            }
            rd.add(new JScrollPane(panelLista), BorderLayout.CENTER);
            rd.setLocationRelativeTo(this);
            rd.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al buscar usuarios: " + e.getMessage());
        }
    }

    private JPanel crearPanelUsuario(Usuario usuarioEncontrado, JDialog dialogoBusqueda) throws IOException {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel labelUsername = new JLabel("<html><b><a href='#'>@" + usuarioEncontrado.getUsuario() + "</a></b> (" + usuarioEncontrado.getUsuario() + ")</html>");
        labelUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        labelUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dialogoBusqueda.dispose();
                //abrirPerfilUsuario(usuarioEncontrado.getUsuario());
            }
        });

        panel.add(labelUsername, BorderLayout.WEST);

        JButton btnFollow = new JButton();
        boolean esSiguiendo = GestorInsta.estaSiguiendo(usuarioActual.getUsuario(), usuarioEncontrado.getUsuario());
        btnFollow.setText(esSiguiendo ? "Dejar de Seguir" : "Seguir");

        btnFollow.addActionListener(e -> {
            manejarFollow(usuarioEncontrado.getUsuario(), !esSiguiendo, btnFollow, dialogoBusqueda);
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(btnFollow);
        panel.add(btnPanel, BorderLayout.EAST);

        return panel;
    }

    /*private void abrirPerfilUsuario(String username) {
        if (username.equalsIgnoreCase(usuarioActual.getUsuario())) {
            vtnPerfil p = new vtnPerfil(usuarioActual);
            p.setVisible(true);
        } else {
            new vtnOtroPerfil(username, this);
        }
    }*/

    private void manejarFollow(String seguido, boolean nuevoEstado, JButton btnFollow, JDialog dialogoBusqueda) {
        String seguidor = usuarioActual.getUsuario();

        try {
            if (!nuevoEstado) {
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Desea dejar de seguir a @" + seguido + "?",
                        "Confirmar Unfollow", JOptionPane.YES_NO_OPTION);

                if (confirmacion != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            GestorInsta.actualizarEstadoFollow(seguidor, seguido, nuevoEstado);
            dialogoBusqueda.dispose();
            btnFollow.setText(nuevoEstado ? "Dejar de Seguir" : "Seguir");

            JOptionPane.showMessageDialog(this,
                    (nuevoEstado ? "¡Ahora sigues a @" : "Has de jado de seguir a @") + seguido,
                    "Actualizacion de Follow", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al actualizar el estado: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error: " + e.getMessage());
        }
    }

    @Override
    public void postPublicadoExitosamente() {
        perfil.cargarDatosPerfil();
        mostrarVista("Perfil");
    }
}
