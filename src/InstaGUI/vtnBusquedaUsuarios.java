/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

/**
 *
 * @author HP
 */
import Compartidas.Usuario;
import Insta.GestorInsta;
import Insta.SesionManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

public class vtnBusquedaUsuarios extends JDialog {
    private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_SECUNDARIO_TEXTO = new Color(150, 150, 150); 
    private final Color COLOR_BOTON_DOMINANTE = new Color(193, 53, 132); 
    private final Color COLOR_BOTON_FONDO = new Color(38, 38, 38); 
    private final Color COLOR_BORDE_SUAVE = new Color(50, 50, 50);
    private final Color COLOR_AZUL_SEGUIR = new Color(0, 150, 255); 


    private final Usuario usuarioActual;
    private final vtnInstaPrincipal vtnP;
    private JTextField txtBusqueda;
    private JPanel panelResultados;
    private final boolean entrarC; 

    public vtnBusquedaUsuarios(vtnInstaPrincipal vtnP, boolean entrarC) {
        super(vtnP, entrarC ? "Entrar a una Cuenta" : "Buscar Cuentas", true);
        this.usuarioActual = SesionManager.getUsuarioActual();
        this.vtnP = vtnP;
        this.entrarC = entrarC;

        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);
        getContentPane().setBackground(COLOR_FONDO);

        setSize(450, 400);
        inicializarComponentes();
        setLocationRelativeTo(vtnP);
    }
    
    private String toHex(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    private void inicializarComponentes() {
        JPanel panelInput = new JPanel(new BorderLayout(5, 5));
        panelInput.setBackground(COLOR_FONDO);
        
        txtBusqueda = new JTextField();
        JButton btnBuscar = new JButton("Buscar");
        
        JLabel lblBuscar = new JLabel("Buscar Username:");
        lblBuscar.setForeground(COLOR_TEXTO);
        
        txtBusqueda.setBackground(COLOR_BOTON_FONDO);
        txtBusqueda.setForeground(COLOR_TEXTO);
        txtBusqueda.setCaretColor(COLOR_TEXTO);
        txtBusqueda.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE));
        
        btnBuscar.setBackground(COLOR_BOTON_DOMINANTE);
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        panelInput.add(lblBuscar, BorderLayout.WEST);
        panelInput.add(txtBusqueda, BorderLayout.CENTER);
        panelInput.add(btnBuscar, BorderLayout.EAST);
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(panelInput, BorderLayout.NORTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        panelResultados.setBackground(COLOR_FONDO);
        
        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        scrollResultados.getViewport().setBackground(COLOR_FONDO);
        scrollResultados.setBorder(BorderFactory.createEmptyBorder());
        add(scrollResultados, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBusqueda.addActionListener(e -> ejecutarBusqueda());
    }

    private void ejecutarBusqueda() {
        // LÓGICA ORIGINAL
        String texto = txtBusqueda.getText().trim();
        panelResultados.removeAll();

        if (texto.isEmpty()) {
            JLabel labelVacio = new JLabel("Ingrese un texto para buscar.");
            labelVacio.setForeground(COLOR_SECUNDARIO_TEXTO);
            panelResultados.add(labelVacio);
        } else {
            ArrayList<Usuario> resultados = GestorInsta.buscarPersonas(texto, usuarioActual.getNombreUsuario());

            if (resultados.isEmpty()) {
                JLabel labelNoEncontrado = new JLabel("No se encontraron usuarios activos que coincidan con '" + texto + "'.");
                labelNoEncontrado.setForeground(COLOR_SECUNDARIO_TEXTO);
                panelResultados.add(labelNoEncontrado);
            } else {
                for (Usuario u : resultados) {
                    JPanel panelPrevio;

                    if (entrarC) {
                        panelPrevio = entrarOtraCuenta(u);
                    } else {
                        panelPrevio = vistaOtroUsuario(u);
                    }
                    
                    panelPrevio.setAlignmentX(Component.CENTER_ALIGNMENT);
                    panelPrevio.setMaximumSize(new Dimension(420, panelPrevio.getPreferredSize().height));

                    panelResultados.add(panelPrevio);
                    panelResultados.add(Box.createVerticalStrut(10));
                }
            }
        }
        panelResultados.revalidate();
        panelResultados.repaint();
    }

    private JPanel vistaOtroUsuario(Usuario usuarioEncontrado) {
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BorderLayout(10, 5));
        panelUsuario.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panelUsuario.setBackground(COLOR_BOTON_FONDO); 

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(COLOR_BOTON_FONDO);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelDatos.add(new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + ";'>@" + usuarioEncontrado.getNombreUsuario() + "</b></html>"));
        JLabel lblNombre = new JLabel("Nombre: " + usuarioEncontrado.getUsuario());
        lblNombre.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelDatos.add(lblNombre);
        JLabel lblEdad = new JLabel("Edad: " + usuarioEncontrado.getEdad());
        lblEdad.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelDatos.add(lblEdad);
        JLabel lblGenero = new JLabel("Genero: " + usuarioEncontrado.getGenero());
        lblGenero.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelDatos.add(lblGenero);
        JLabel lblFecha = new JLabel("Fecha Ingreso: " + usuarioEncontrado.getFechaEntrada());
        lblFecha.setForeground(COLOR_SECUNDARIO_TEXTO);
        panelDatos.add(lblFecha);

        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAccion.setBackground(COLOR_BOTON_FONDO);
        panelAccion.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton btnFollow = new JButton();
        actualizarBotonFollow(btnFollow, usuarioEncontrado.getNombreUsuario());

        btnFollow.addActionListener(e -> manejarFollow(usuarioEncontrado.getNombreUsuario(), btnFollow));

        panelAccion.add(btnFollow);

        panelUsuario.add(panelDatos, BorderLayout.WEST);
        panelUsuario.add(panelAccion, BorderLayout.EAST);

        return panelUsuario;
    }

    private void actualizarBotonFollow(JButton btnFollow, String usernameObjetivo) {
        boolean siguiendo = false;
        try {
            siguiendo = GestorInsta.estaSiguiendo(usuarioActual.getNombreUsuario(), usernameObjetivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al verificar el estado de seguimiento: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
        
        btnFollow.setFocusPainted(false);
        btnFollow.setBorderPainted(false);
        
        if (siguiendo) {
            btnFollow.setText("SIGUIENDO");
            btnFollow.setBackground(COLOR_BORDE_SUAVE.brighter()); 
            btnFollow.setForeground(COLOR_TEXTO);
        } else {
            btnFollow.setText("SEGUIR");
            btnFollow.setBackground(COLOR_AZUL_SEGUIR); 
            btnFollow.setForeground(Color.WHITE);
        }
    }

    private JPanel entrarOtraCuenta(Usuario usuarioEncontrado) {
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BorderLayout(10, 5));
        panelUsuario.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE, 1));
        panelUsuario.setBackground(COLOR_BOTON_FONDO);

        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDatos.setBackground(COLOR_BOTON_FONDO);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblUsername = new JLabel("<html><b style='color:" + toHex(COLOR_TEXTO) + ";'>@" + usuarioEncontrado.getNombreUsuario() + "</b></html>");
        panelDatos.add(lblUsername);

        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelAccion.setBackground(COLOR_BOTON_FONDO);

        JButton btnFollow = new JButton();
        actualizarBotonFollow(btnFollow, usuarioEncontrado.getNombreUsuario());
        btnFollow.addActionListener(e -> manejarFollow(usuarioEncontrado.getNombreUsuario(), btnFollow));

        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(COLOR_BOTON_DOMINANTE);
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFocusPainted(false);
        btnEntrar.setBorderPainted(false);

        btnEntrar.addActionListener(e -> {
            String otroUser = usuarioEncontrado.getNombreUsuario();
            vtnP.mostrarOtroPerfil(otroUser);
            dispose();
        });

        panelAccion.add(btnFollow);
        panelAccion.add(btnEntrar);

        panelUsuario.add(panelDatos, BorderLayout.CENTER);
        panelUsuario.add(panelAccion, BorderLayout.EAST);

        return panelUsuario;
    }

    private void manejarFollow(String usernameObjetivo, JButton btnFollow) {
        boolean siguiendo;
        boolean nuevoEstado;
        try {
            siguiendo = GestorInsta.estaSiguiendo(this.usuarioActual.getNombreUsuario(), usernameObjetivo);
            nuevoEstado = !siguiendo;
            GestorInsta.actualizarEstadoFollow(usuarioActual.getNombreUsuario(), usernameObjetivo, nuevoEstado);
            actualizarBotonFollow(btnFollow, usernameObjetivo);

            if (vtnP != null) {
                vtnP.refrescarVistas();
            }

            String mensaje = nuevoEstado ? "¡Has empezado a seguir a @" + usernameObjetivo + "!" : "¡Has dejado de seguir a @" + usernameObjetivo + "!";
            JOptionPane.showMessageDialog(this, mensaje, "Actualizacion de seguimiento", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el seguimiento: " + ex.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
    }
}
