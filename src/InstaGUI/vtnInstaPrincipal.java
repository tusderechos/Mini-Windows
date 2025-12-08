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

public class vtnInstaPrincipal extends JFrame implements PostListener {

    private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BARRA_NAVEGACION = new Color(30, 30, 30); 
    private final Color COLOR_ICONO_ACTIVO = Color.WHITE;
    private final Color COLOR_ICONO_INACTIVO = new Color(150, 150, 150);
    private final Font FONT_LOGO = new Font("Arial", Font.BOLD, 28);

    private final Usuario usuarioActual;
    private JPanel panelContenidoCentral;
    private CardLayout cardLayout;
    private TimeLine timeLine;
    private Buscar buscar;
    private vtnPerfil perfil;

    public vtnInstaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("INSTA - @" + usuario.getNombreUsuario());
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        getContentPane().setBackground(COLOR_FONDO);

        inicializarVistas();
        inicializarComponentes();
        mostrarVista("Perfil"); 
    }

    private void inicializarVistas() {
        cardLayout = new CardLayout();
        panelContenidoCentral = new JPanel(cardLayout);
        panelContenidoCentral.setBackground(COLOR_FONDO);

        timeLine = new TimeLine(usuarioActual);
        buscar = new Buscar(usuarioActual, this);
        perfil = new vtnPerfil(usuarioActual, this);
        
        panelContenidoCentral.add(timeLine, "TimeLine");
        panelContenidoCentral.add(buscar, "Buscar");
        
        JLabel labelPublicar = new JLabel("PUBLICAR", SwingConstants.CENTER);
        labelPublicar.setForeground(COLOR_TEXTO);
        labelPublicar.setBackground(COLOR_FONDO);
        panelContenidoCentral.add(labelPublicar, "Publicar");
        
        panelContenidoCentral.add(perfil, "Perfil");
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());
        add(crearBarraSuperior(), BorderLayout.NORTH);

        add(panelContenidoCentral, BorderLayout.CENTER);

        add(crearBarraNavegacionInferior(), BorderLayout.SOUTH);
    }
    
    private JPanel crearBarraSuperior() {
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(COLOR_BARRA_NAVEGACION);
        panelHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 50))); 

        JLabel labelLogo = new JLabel("Instagram");
        labelLogo.setFont(FONT_LOGO);
        labelLogo.setForeground(new Color(255, 100, 180)); 
        labelLogo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0)); 

        panelHeader.add(labelLogo, BorderLayout.WEST);

        return panelHeader;
    }

    private JPanel crearBarraNavegacionInferior() {
        JPanel panelBarraNavegacion = new JPanel(new GridLayout(1, 4));
        panelBarraNavegacion.setBackground(COLOR_BARRA_NAVEGACION);
        panelBarraNavegacion.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(50, 50, 50))); // Separador

        JButton btnTimeLine = crearBotonNavegacion("🏠", "TimeLine");
        btnTimeLine.addActionListener(e -> {
            timeLine.cargarTimeLine();
            mostrarVista("TimeLine");
        });

        JButton btnBuscar = crearBotonNavegacion("🔍", "Buscar");
        btnBuscar.addActionListener(e -> {
            mostrarVista("Buscar");
        });

        JButton btnPublicar = crearBotonNavegacion("➕", "Publicar");
        btnPublicar.addActionListener(e -> {
            vtnCrearInsta crearInsta = new vtnCrearInsta(this);
            crearInsta.setVisible(true);
        });

        JButton btnPerfil = crearBotonNavegacion("👤", "Perfil");
        btnPerfil.addActionListener(e -> {
            perfil.cargarDatosPerfil();
            mostrarVista("Perfil");
        });

        panelBarraNavegacion.add(btnTimeLine);
        panelBarraNavegacion.add(btnBuscar);
        panelBarraNavegacion.add(btnPublicar);
        panelBarraNavegacion.add(btnPerfil);

        return panelBarraNavegacion;
    }
    
    private JButton crearBotonNavegacion(String icono, String comando) {
        JButton btn = new JButton(icono);
        btn.setFont(new Font("SansSerif", Font.BOLD, 24));
        btn.setBackground(COLOR_BARRA_NAVEGACION);
        btn.setForeground(COLOR_ICONO_INACTIVO);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setActionCommand(comando);
        
        btn.setOpaque(true); 
        
        return btn;
    }


    public void mostrarVista(String nombreVista) {
        cardLayout.show(panelContenidoCentral, nombreVista);
    }

    public void mostrarOtroPerfil(String username) {
        final String VISTA_PERFIL_OTRO = "PERFIL_AJENO_" + username.toUpperCase();

        try {
            System.out.println("Creando y mostrando perfil: " + username);

            vtnOtroPerfil panelOtroPerfil = new vtnOtroPerfil(username, this);

            panelContenidoCentral.add(panelOtroPerfil, VISTA_PERFIL_OTRO);

            cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);

        } catch (Exception e) {
            System.err.println("ERROR: No se pudo cargar el perfil.");
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Error al cargar el perfil: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        panelContenidoCentral.revalidate();
        panelContenidoCentral.repaint();
    }

    @Override
    public void postPublicadoExitosamente() {
        perfil.cargarDatosPerfil();
        mostrarVista("Perfil");
    }

    public void refrescarVistas() {
        SwingUtilities.invokeLater(() -> {
            this.perfil.cargarDatosPerfil();
            this.timeLine.cargarTimeLine();
            
            this.panelContenidoCentral.revalidate();
            this.panelContenidoCentral.repaint();

            this.revalidate();
            this.repaint();
        });
    }
}