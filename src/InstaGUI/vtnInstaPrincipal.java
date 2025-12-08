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
    private JPanel panelContenidoCentral;
    private CardLayout cardLayout;
    private TimeLine timeLine;
    private Buscar buscar;
    private vtnPerfil perfil;
    //private JPanel panelContenedor;

    public vtnInstaPrincipal(Usuario usuario) {
        this.usuarioActual = usuario;
        setTitle("INSTA - Perfil de @" + usuario.getNombreUsuario());
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
        mostrarVista("TimeLine");
        });

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

    public void mostrarVista(String nombreVista) {
        cardLayout.show(panelContenidoCentral, nombreVista);
    }

    /*public void mostrarOtroPerfil(String username){
         final String VISTA_PERFIL_OTRO = "PERFIL_AJENO_" + username.toUpperCase();
        
        try {
            cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);
            System.out.println("Navegando a vista de perfil existente: " + username);
            
            Component componenteExistente = null;
        for (Component comp : panelContenidoCentral.getComponents()) {
            // Comprobamos si el nombre del componente coincide con la vista
            if (VISTA_PERFIL_OTRO.equals(panelContenidoCentral.getLayout().getConstraints(comp))) {
                 componenteExistente = comp;
                 break;
            }
        }
        
        if (componenteExistente instanceof vtnOtroPerfil) {
            ((vtnOtroPerfil) componenteExistente).recargarPerfil();
        }

        } catch (IllegalArgumentException e) {
            
            vtnOtroPerfil panelOtroPerfil = new vtnOtroPerfil(username, this); 
            
            panelContenidoCentral.add(panelOtroPerfil, VISTA_PERFIL_OTRO);
            cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);
            
            System.out.println("Creando y mostrando nuevo perfil: " + username);
        }
    }*/
    
    /*public void mostrarOtroPerfil(String username){
    final String VISTA_PERFIL_OTRO = "PERFIL_AJENO_" + username.toUpperCase();
    
    try {
        cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);
        System.out.println("Navegando a vista de perfil existente: " + username);

        Component componenteExistente = null;
        for (Component comp : panelContenidoCentral.getComponents()) {
            if (VISTA_PERFIL_OTRO.equals(cardLayout.getConstraints(comp))) {
                 componenteExistente = comp;
                 break;
            }
        }
        
        if (componenteExistente instanceof vtnOtroPerfil) {
            ((vtnOtroPerfil) componenteExistente).recargarPerfil();
            System.out.println("Perfil existente recargado: " + username);
        }

    } catch (Exception e) { 
        
        try {
            vtnOtroPerfil panelOtroPerfil = new vtnOtroPerfil(username, this); 
            
            panelContenidoCentral.add(panelOtroPerfil, VISTA_PERFIL_OTRO);
            
            cardLayout.show(panelContenidoCentral, VISTA_PERFIL_OTRO);
            
            System.out.println("Creando y mostrando nuevo perfil: " + username);
            
        } catch (Exception exCreacion) {
            System.err.println("--- ERROR CRÍTICO AL CARGAR PERFIL ---");
            exCreacion.printStackTrace();
            
            JOptionPane.showMessageDialog(this, 
                "Error al cargar el perfil de @" + username + ". Verifique la consola.", 
                "Error de Navegación", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    panelContenidoCentral.revalidate();
    panelContenidoCentral.repaint();
}*/
    
    @Override
    public void postPublicadoExitosamente() {
        perfil.cargarDatosPerfil();
        mostrarVista("Perfil");
    }
    
    public void refrescarVistas(){
        SwingUtilities.invokeLater(() -> {
        this.perfil.cargarDatosPerfil(); 
        this.timeLine.cargarTimeLine(); 
        this.panelContenidoCentral.invalidate();
        this.panelContenidoCentral.validate();
        this.panelContenidoCentral.repaint();
        
        Dimension actTam = this.getSize();
        this.setSize(actTam);
        
        this.revalidate();
        this.repaint();
    });
    }
}
