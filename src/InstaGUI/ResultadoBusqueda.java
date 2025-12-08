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
import java.io.IOException;
import javax.swing.*;

public class ResultadoBusqueda extends JPanel { 
    private final Color COLOR_FONDO_ITEM = new Color(38, 38, 38); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BORDE_SUAVE = new Color(50, 50, 50);
    private final Color COLOR_AZUL_SEGUIR = new Color(0, 150, 255);
    private final Color COLOR_ROJO_DEJAR_SEGUIR = new Color(237, 73, 86); 
    private final Color COLOR_FONDO_DIALOGO = new Color(25, 25, 25);
    
    private final Usuario usuarioObjetivo;
    private final vtnInstaPrincipal vtnP;
    private final JDialog dialogBusqueda;
    private JButton btnFollow;
    private boolean siguiendo;

    public ResultadoBusqueda(Usuario usuarioObjetivo, vtnInstaPrincipal vtnP, JDialog dialogBusqueda) {
        this.usuarioObjetivo = usuarioObjetivo;
        this.vtnP = vtnP;
        this.dialogBusqueda = dialogBusqueda;

        try {
            this.siguiendo = GestorInsta.estaSiguiendo(SesionManager.getUsuarioActual().getUsuario(), usuarioObjetivo.getUsuario());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar estado de seguimiento: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
            this.siguiendo = false;
        }
        
        setLayout(new BorderLayout(10, 5));
        setBackground(COLOR_FONDO_ITEM);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); 

        JLabel labelUsername = new JLabel("<html><b style='color:#FFFFFF;'>@" + usuarioObjetivo.getUsuario() + "</b></html>");
        labelUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        labelUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarDetallesPerfil();
            }
        });
        add(labelUsername, BorderLayout.WEST);

        btnFollow = new JButton();
        actualizarBotonFollow();
        
        btnFollow.setFont(new Font("Arial", Font.BOLD, 12));
        btnFollow.setFocusPainted(false);
        btnFollow.setBorderPainted(false);
        
        btnFollow.addActionListener(e -> toggleFollow());
        
        add(btnFollow, BorderLayout.EAST);
        
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(COLOR_BORDE_SUAVE);
        separator.setBackground(COLOR_FONDO_ITEM);
        add(separator, BorderLayout.SOUTH);
    }
    
    private void actualizarBotonFollow() {
        if (siguiendo) {
            btnFollow.setText("Dejar de Seguir");
            btnFollow.setBackground(COLOR_FONDO_ITEM.brighter()); 
            btnFollow.setForeground(COLOR_ROJO_DEJAR_SEGUIR); 
            btnFollow.setBorder(BorderFactory.createLineBorder(COLOR_BORDE_SUAVE));
        } else {
            btnFollow.setText("Seguir");
            btnFollow.setBackground(COLOR_AZUL_SEGUIR);
            btnFollow.setForeground(Color.WHITE);
            btnFollow.setBorder(BorderFactory.createEmptyBorder());
        }
    }

    private void toggleFollow() {
        String seguidor = SesionManager.getUsuarioActual().getUsuario();
        String seguido = usuarioObjetivo.getUsuario();
        
        try {
            if (siguiendo) {
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿De verdad quieres dejar de seguir a @" + seguido + "?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, false); 
                    siguiendo = false;
                }
            } else {
                GestorInsta.actualizarEstadoFollow(seguidor, seguido, true); 
                siguiendo = true;
            }
            actualizarBotonFollow();
            
            vtnP.mostrarVista("BUSCAR"); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de seguimiento: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void mostrarDetallesPerfil() {
        JDialog dialogoDatos = new JDialog(dialogBusqueda, "Datos de @" + usuarioObjetivo.getUsuario(), false);
        dialogoDatos.setLayout(new BorderLayout());
        dialogoDatos.getContentPane().setBackground(COLOR_FONDO_DIALOGO);
        
        JPanel panelInfo = new JPanel(new GridLayout(6, 1));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelInfo.setBackground(COLOR_FONDO_DIALOGO);
        
        Font font = new Font("Arial", Font.PLAIN, 14);
        Color textColor = Color.WHITE;
        
        JLabel lblUsername = new JLabel("Username: @" + usuarioObjetivo.getNombreUsuario());
        lblUsername.setForeground(textColor);
        lblUsername.setFont(font);
        panelInfo.add(lblUsername);
        
        JLabel lblNombre = new JLabel("Nombre: " + usuarioObjetivo.getUsuario());
        lblNombre.setForeground(textColor);
        lblNombre.setFont(font);
        panelInfo.add(lblNombre);
        
        JLabel lblEdad = new JLabel("Edad: " + usuarioObjetivo.getEdad());
        lblEdad.setForeground(textColor);
        lblEdad.setFont(font);
        panelInfo.add(lblEdad);
        
        JLabel lblGenero = new JLabel("Género: " + usuarioObjetivo.getGenero());
        lblGenero.setForeground(textColor);
        lblGenero.setFont(font);
        panelInfo.add(lblGenero);
        
        JLabel lblFecha = new JLabel("Fecha Ingreso: " + usuarioObjetivo.getFechaEntrada());
        lblFecha.setForeground(textColor);
        lblFecha.setFont(font);
        panelInfo.add(lblFecha);

        JButton btnVerCompleto = new JButton("Entrar a Perfil Completo");
        btnVerCompleto.setBackground(COLOR_AZUL_SEGUIR);
        btnVerCompleto.setForeground(Color.WHITE);
        btnVerCompleto.setFocusPainted(false);
        
        btnVerCompleto.addActionListener(e -> {
            dialogoDatos.dispose(); 
            dialogBusqueda.dispose(); 
            vtnP.mostrarOtroPerfil(usuarioObjetivo.getUsuario()); 
        });
        
        dialogoDatos.add(panelInfo, BorderLayout.CENTER);
        
        JPanel panelBoton = new JPanel();
        panelBoton.setBackground(COLOR_FONDO_DIALOGO);
        panelBoton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelBoton.add(btnVerCompleto);
        
        dialogoDatos.add(panelBoton, BorderLayout.SOUTH);
        
        dialogoDatos.pack();
        dialogoDatos.setLocationRelativeTo(dialogBusqueda);
        dialogoDatos.setVisible(true);
    }
}
