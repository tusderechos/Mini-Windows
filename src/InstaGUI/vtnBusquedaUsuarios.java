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
        setSize(450, 400);
        inicializarComponentes();
        setLocationRelativeTo(vtnP);
    }

    private void inicializarComponentes() {
        JPanel panelInput = new JPanel(new BorderLayout(5, 5));
        txtBusqueda = new JTextField();
        JButton btnBuscar = new JButton("Buscar");

        panelInput.add(new JLabel("Buscar Username:"), BorderLayout.WEST);
        panelInput.add(txtBusqueda, BorderLayout.CENTER);
        panelInput.add(btnBuscar, BorderLayout.EAST);
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        add(panelInput, BorderLayout.NORTH);

        panelResultados = new JPanel();
        panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
        JScrollPane scrollResultados = new JScrollPane(panelResultados);
        add(scrollResultados, BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> ejecutarBusqueda());
        txtBusqueda.addActionListener(e -> ejecutarBusqueda());
    }

    private void ejecutarBusqueda() {
        String texto = txtBusqueda.getText().trim();
        panelResultados.removeAll();

        if (texto.isEmpty()) {
            panelResultados.add(new JLabel("Ingrese un texto para buscar."));
        } else {
            ArrayList<Usuario> resultados = GestorInsta.buscarPersonas(texto, usuarioActual.getNombreUsuario());

            if (resultados.isEmpty()) {
                panelResultados.add(new JLabel("No se encontraron usuarios activos que coincidan con '" + texto + "'."));
            } else {
                for (Usuario u : resultados) {
                    JPanel panelPrevio = vistaOtroUsuario(u);
                    
                    if(entrarC){
                        panelPrevio = entrarOtraCuenta(u);
                    }else{
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
        panelUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panelUsuario.setBackground(Color.WHITE);

        JPanel panelDatos = new JPanel();
        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        panelDatos.add(new JLabel("<html><b>@" + usuarioEncontrado.getNombreUsuario() + "</b></html>"));
        panelDatos.add(new JLabel("Nombre: " + usuarioEncontrado.getUsuario()));
        panelDatos.add(new JLabel("Edad: " + usuarioEncontrado.getEdad()));
        panelDatos.add(new JLabel("Genero: " + usuarioEncontrado.getGenero()));
        panelDatos.add(new JLabel("Fecha Ingreso: " + usuarioEncontrado.getFechaEntrada()));

        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAccion.setBackground(Color.WHITE);
        panelAccion.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JButton btnFollow = new JButton();
        actualizarBotonFollow(btnFollow, usuarioEncontrado.getNombreUsuario());

        btnFollow.addActionListener(e -> manejarFollow(usuarioEncontrado.getNombreUsuario(), btnFollow));

        panelAccion.add(btnFollow);

        panelUsuario.add(panelDatos, BorderLayout.WEST);
        panelUsuario.add(panelAccion, BorderLayout.EAST);

        panelDatos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                //vtnP.mostrarOtroPerfil(usuarioEncontrado.getNombreUsuario());
            }
        });

        return panelUsuario;
    }

    private void actualizarBotonFollow(JButton btnFollow, String usernameObjetivo) {
        boolean siguiendo = false;
        try {
            siguiendo = GestorInsta.estaSiguiendo(usuarioActual.getNombreUsuario(), usernameObjetivo);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al verificar el estado de seguimiento: " + e.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
        }
        if (siguiendo) {
            btnFollow.setText("SIGUIENDO");
            btnFollow.setBackground(Color.LIGHT_GRAY);
            btnFollow.setForeground(Color.BLACK);
        } else {
            btnFollow.setText("SEGUIR");
            btnFollow.setBackground(new Color(0, 150, 255));
            btnFollow.setForeground(Color.WHITE);
        }

    }

    private JPanel entrarOtraCuenta(Usuario usuarioEncontrado) {
        JPanel panelUsuario = new JPanel();
        panelUsuario.setLayout(new BorderLayout(10, 5));
        panelUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        panelUsuario.setBackground(Color.WHITE);

        // --- Panel de Datos Simplificado ---
        JPanel panelDatos = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelDatos.setBackground(Color.WHITE);
        panelDatos.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JLabel lblUsername = new JLabel("<html><b>@" + usuarioEncontrado.getNombreUsuario() + "</b></html>");
        panelDatos.add(lblUsername);

        // --- Panel de Acciones (Botones) ---
        JPanel panelAccion = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        panelAccion.setBackground(Color.WHITE);

        // 1. Botón de FOLLOW/UNFOLLOW
        JButton btnFollow = new JButton();
        actualizarBotonFollow(btnFollow, usuarioEncontrado.getNombreUsuario());
        btnFollow.addActionListener(e -> manejarFollow(usuarioEncontrado.getNombreUsuario(), btnFollow));

        // 2. Botón de ENTRAR
        JButton btnEntrar = new JButton("ENTRAR");
        btnEntrar.setBackground(new Color(20, 20, 20));
        btnEntrar.setForeground(Color.WHITE);

        // Lógica para entrar al perfil
        btnEntrar.addActionListener(e -> {
            dispose(); // Cierra la ventana actual
            //vtnP.mostrarOtroPerfil(usuarioEncontrado.getNombreUsuario());
        });

        panelAccion.add(btnFollow);
        panelAccion.add(btnEntrar);

        // --- Ensamblar el Panel de Resultado ---
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
