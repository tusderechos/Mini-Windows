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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class vtnOpcionesUsuario extends JDialog  implements ActionListener {
     private final Usuario usuarioActual;
    private final vtnInstaPrincipal vtnP;
    private JButton btnBuscar, btnEntrar, btnDesactivar;
    
    public vtnOpcionesUsuario(Usuario usuario, vtnInstaPrincipal vtnP) {
        super(vtnP, "INSTA - Opciones de Perfil", true);
        this.usuarioActual = usuario;
        this.vtnP = vtnP;
        
        inicializarComponentes();
        setSize(400, 300);
    }

    /*private void inicializarComponentes() {
        setLayout(new GridLayout(3, 1, 10, 10)); // 3 filas, 1 columna
        
        // 1. Opción: Buscar Cuenta / Entrar a Perfil
        JButton btnBuscar = new JButton("1. Buscar Cuentas y Entrar a Perfil");
        btnBuscar.addActionListener(e -> mostrarDialogoBusqueda());
        add(btnBuscar);
        
        // 2. Opción: Desactivar/Activar Cuenta
        JButton btnCambiarEstado = new JButton("2. Desactivar / Activar Mi Cuenta");
        btnCambiarEstado.addActionListener(e -> cambiarEstadoCuenta());
        add(btnCambiarEstado);
        
        // 3. Opción: Cerrar
        JButton btnCerrar = new JButton("Cerrar Menú");
        btnCerrar.addActionListener(e -> dispose());
        add(btnCerrar);
    }
    
    // --- LÓGICA DE FUNCIONALIDADES ---
    
    
    private void mostrarDialogoBusqueda() {
        new vtnBusquedaUsuarios(this, vtnP).setVisible(true); // Abre la nueva ventana de búsqueda
    }
    
   
    private void cambiarEstadoCuenta() {
        boolean estadoActual = usuarioActual.isActivo();
        String mensaje, titulo;
        int confirmacion = JOptionPane.YES_OPTION;

        if (estadoActual) {
            // Desactivar
            mensaje = "¿Estás seguro de que quieres desactivar tu cuenta? Dejarás de aparecer en búsquedas.";
            titulo = "Confirmar Desactivación";
            confirmacion = JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
            if (confirmacion == JOptionPane.NO_OPTION) return;
        } 
        // Si está inactivo, el ingeniero pide activarla automáticamente sin preguntar (confirmacion == YES_OPTION si está activo)

        try {
            // Llama al método de GestorInsta que maneja la lógica de cambio de estado y reescritura.
            boolean nuevoEstado = GestorInsta.actualizarEstadoCuenta(usuarioActual.getUsuario());
            
            if (nuevoEstado) {
                JOptionPane.showMessageDialog(this, "Cuenta ACTIVADA exitosamente.", "Activación", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Cuenta DESACTIVADA. Debes iniciar sesión de nuevo para reactivarla.", "Desactivación", JOptionPane.INFORMATION_MESSAGE);
                SesionManager.cerrarSesion();
                // Aquí deberías cerrar vtnP y mostrar la ventana de Login
                vtnP.dispose(); 
            }
            dispose(); // Cierra el JDialog de opciones
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error de I/O al guardar el estado de la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    
     private void inicializarComponentes() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        btnBuscar = new JButton("Buscar Cuentas");
        btnEntrar = new JButton("Entrar a una Cuenta Específica");
        String estado = usuarioActual.isActivo() ? "DESACTIVAR Cuenta" : "ACTIVAR Cuenta";
        btnDesactivar = new JButton(estado);
        
        btnBuscar.addActionListener(this);
        btnEntrar.addActionListener(this);
        btnDesactivar.addActionListener(this);
        
        // Añadir componentes con espacio
        add(Box.createVerticalStrut(10));
        add(wrapInPanel(btnBuscar));
        add(Box.createVerticalStrut(10));
        add(wrapInPanel(btnEntrar));
        add(Box.createVerticalStrut(10));
        add(wrapInPanel(btnDesactivar));
        add(Box.createVerticalStrut(10));
    }
    
    private JPanel wrapInPanel(JComponent component) {
        // Utilidad para centrar y darles un tamaño máximo
        JPanel panel = new JPanel();
        panel.add(component);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBuscar) {
            // DELEGA la acción a // Asumo que vtnInstaPrincipal tiene un método mostrarBusqueda()
            manejarBusquedaCuentas();

        } else if (e.getSource() == btnEntrar) {
            manejarEntrarOtraCuenta();
            
        } else if (e.getSource() == btnDesactivar) {
            manejarDesactivacion();
        }
    }
    
    private void manejarBusquedaCuentas() {
        dispose(); // Cierra el diálogo de opciones
        
        // Asumiendo que existe una clase llamada vtnBusquedaUsuarios (JDialog)
        // que maneja la búsqueda y muestra los resultados.
        // vtnP debe pasarse para que la ventana de búsqueda pueda llamar a vtnP.mostrarOtroPerfil().
        
        vtnBusquedaUsuarios b = new vtnBusquedaUsuarios(vtnP);
        b.setLocationRelativeTo(vtnP);
        b.setVisible(true);
    }
    
    private void manejarEntrarOtraCuenta() {
        String usernameBuscado = JOptionPane.showInputDialog(
                this,
                "Ingrese el nombre de usuario exacto del perfil:",
                "Acceder a Perfil",
                JOptionPane.QUESTION_MESSAGE
        );
        
        if (usernameBuscado != null && !usernameBuscado.trim().isEmpty()) {
            dispose(); // Cierra el diálogo de opciones
            // Aquí llamas al método de navegación que implementaste en vtnInstaPrincipal
            vtnP.mostrarOtroPerfil(usernameBuscado);
        }
    }
    
    private void manejarDesactivacion() {
        // Lógica de desactivación/activación (puedes copiarla del método eliminado en vtnPerfil)
        if (usuarioActual.isActivo()) {
            // ... (Lógica de confirmación y llamada a GestorInsta.actualizarEstadoCuenta(..., false)) ...
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Estás seguro? Su cuenta no aparecerá en búsquedas.", "Confirmar Desactivación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    GestorInsta.actualizarEstadoCuenta(usuarioActual.getUsuario(), false); // Marcar como inactivo
                    SesionManager.cerrarSesion();
                    JOptionPane.showMessageDialog(this, "Cuenta desactivada. Volviendo al login.");
                    dispose();
                    vtnP.dispose();
                    new vtnLogin().setVisible(true);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(this, "Error de E/S al desactivar la cuenta.");
                }
            }

        } else {
            // ... (Lógica de activación y llamada a GestorInsta.actualizarEstadoCuenta(..., true)) ...
            try {
                GestorInsta.actualizarEstadoCuenta(usuarioActual.getUsuario(), true); // Marcar como activo
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
                // No es necesario llamar a cargarDatosPerfil aquí, el método en vtnPerfil lo hará al cerrar.
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error de E/S al activar la cuenta.");
            }
        }
        dispose(); // Cierra el diálogo después de la acción.
    }
}
