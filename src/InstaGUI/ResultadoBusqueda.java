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
            // Verificar si el usuario actual sigue al objetivo. Usa el método existente.
            this.siguiendo = GestorInsta.estaSiguiendo(SesionManager.getUsuarioActual().getUsuario(), usuarioObjetivo.getUsuario());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al verificar estado de seguimiento: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
            this.siguiendo = false;
        }
        
        // ----------------- FORMATO -----------------
        setLayout(new BorderLayout(10, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // 1. Username (A la izquierda) - Actúa como el botón de "Entrar a un Perfil"
        JLabel labelUsername = new JLabel("<html><b>@" + usuarioObjetivo.getUsuario() + "</b></html>");
        labelUsername.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Listener para Opción 2: Entrar a un Perfil y mostrar detalles
        labelUsername.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mostrarDetallesPerfil();
            }
        });
        add(labelUsername, BorderLayout.WEST);

        // 2. Botón SEGUIR/DEJAR DE SEGUIR (A la derecha)
        btnFollow = new JButton();
        actualizarBotonFollow();
        btnFollow.addActionListener(e -> toggleFollow());
        
        add(btnFollow, BorderLayout.EAST);
        add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
    }
    
    // --- LÓGICA DE INTERACCIÓN ---

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
                // Dejar de seguir: Preguntar y marcar como inactivo (false)
                int confirmacion = JOptionPane.showConfirmDialog(this, 
                    "¿De verdad quieres dejar de seguir a @" + seguido + "?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                
                if (confirmacion == JOptionPane.YES_OPTION) {
                    GestorInsta.actualizarEstadoFollow(seguidor, seguido, false); // false = inactivo
                    siguiendo = false;
                }
            } else {
                // Seguir: Marcar como activo (true)
                GestorInsta.actualizarEstadoFollow(seguidor, seguido, true); // true = activo
                siguiendo = true;
            }
            actualizarBotonFollow();
            vtnP.mostrarVista("BUSCAR"); // Refrescar la vista o el TimeLine si es necesario
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado de seguimiento: " + e.getMessage(), "Error I/O", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Opción 2: Muestra un JDialog con los datos generales del usuario.
     * Al hacer click en "Entrar a Perfil" se navega a la vista principal.
     */
    private void mostrarDetallesPerfil() {
        // Crear un JDialog temporal para mostrar los datos.
        JDialog dialogoDatos = new JDialog(dialogBusqueda, "Datos de @" + usuarioObjetivo.getUsuario(), false);
        dialogoDatos.setLayout(new BorderLayout());
        
        JPanel panelInfo = new JPanel(new GridLayout(6, 1));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Agregar los datos generales
        panelInfo.add(new JLabel("Username: @" + usuarioObjetivo.getNombreUsuario()));
        panelInfo.add(new JLabel("Nombre: " + usuarioObjetivo.getUsuario()));
        panelInfo.add(new JLabel("Edad: " + usuarioObjetivo.getEdad()));
        panelInfo.add(new JLabel("Género: " + usuarioObjetivo.getGenero()));
        panelInfo.add(new JLabel("Fecha Ingreso: " + usuarioObjetivo.getFechaEntrada()));

        // Botón para la navegación
        JButton btnVerCompleto = new JButton("Entrar a Perfil Completo");
        btnVerCompleto.addActionListener(e -> {
            dialogoDatos.dispose(); // Cerrar el diálogo de datos
            dialogBusqueda.dispose(); // Cerrar la ventana de búsqueda
            // Llamar al método en vtnInstaPrincipal para cambiar la vista (navegación)
            vtnP.mostrarOtroPerfil(usuarioObjetivo.getUsuario()); 
        });
        
        dialogoDatos.add(panelInfo, BorderLayout.CENTER);
        dialogoDatos.add(btnVerCompleto, BorderLayout.SOUTH);
        
        dialogoDatos.pack();
        dialogoDatos.setLocationRelativeTo(dialogBusqueda);
        dialogoDatos.setVisible(true);
    }
}
