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

public class ResultadoBusqueda extends JPanel{
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
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel labelUsername = new JLabel("<html><b>@" + usuarioObjetivo.getUsuario() + "</b></html>");
        labelUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        labelUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarDetallesPerfil();
            }
        });
        add(labelUsername, BorderLayout.WEST);

        btnFollow = new JButton();
        actualizarBotonFollow();
        btnFollow.addActionListener(e -> toggleFollow());
        
        add(btnFollow, BorderLayout.EAST);
        add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
    }
    
    private void actualizarBotonFollow() {
        if (siguiendo) {
            btnFollow.setText("Dejar de Seguir");
            btnFollow.setBackground(Color.RED);
            btnFollow.setForeground(Color.WHITE);
        } else {
            btnFollow.setText("Seguir");
            btnFollow.setBackground(Color.GREEN);
            btnFollow.setForeground(Color.BLACK);
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
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, false); // false = inactivo
                    siguiendo = false;
                }
            } else {
                GestorInsta.actualizarEstadoFollow(seguidor, seguido, true); // true = activo
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
        
        JPanel panelInfo = new JPanel(new GridLayout(6, 1));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        panelInfo.add(new JLabel("Username: @" + usuarioObjetivo.getNombreUsuario()));
        panelInfo.add(new JLabel("Nombre: " + usuarioObjetivo.getUsuario()));
        panelInfo.add(new JLabel("Edad: " + usuarioObjetivo.getEdad()));
        panelInfo.add(new JLabel("Género: " + usuarioObjetivo.getGenero()));
        panelInfo.add(new JLabel("Fecha Ingreso: " + usuarioObjetivo.getFechaEntrada()));

        JButton btnVerCompleto = new JButton("Entrar a Perfil Completo");
        btnVerCompleto.addActionListener(e -> {
            dialogoDatos.dispose(); 
            dialogBusqueda.dispose(); 
            vtnP.mostrarOtroPerfil(usuarioObjetivo.getUsuario()); 
        });
        
        dialogoDatos.add(panelInfo, BorderLayout.CENTER);
        dialogoDatos.add(btnVerCompleto, BorderLayout.SOUTH);
        
        dialogoDatos.pack();
        dialogoDatos.setLocationRelativeTo(dialogBusqueda);
        dialogoDatos.setVisible(true);
    }
}
