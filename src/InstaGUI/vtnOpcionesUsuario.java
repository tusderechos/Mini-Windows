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

public class vtnOpcionesUsuario extends JDialog implements ActionListener {
    private final Color COLOR_FONDO = new Color(18, 18, 18); 
    private final Color COLOR_TEXTO = Color.WHITE;
    private final Color COLOR_BOTON_PRIMARIO = new Color(38, 38, 38); 
    private final Color COLOR_BOTON_TEXTO = Color.WHITE;
    private final Color COLOR_BORDE_BOTON = new Color(50, 50, 50); 
    private final Color COLOR_BOTON_DESTRUCTIVO = new Color(255, 48, 48); 

    private final Usuario usuarioActual;
    private final vtnInstaPrincipal vtnP;
    private JButton btnBuscar, btnEntrar, btnDesactivar;

    public vtnOpcionesUsuario(Usuario usuario, vtnInstaPrincipal vtnP) {
        super(vtnP, "INSTA - Opciones de Perfil", true);
        this.usuarioActual = usuario;
        this.vtnP = vtnP;

        // Aplicar estilos a la ventana
        getContentPane().setBackground(COLOR_FONDO);
        
        inicializarComponentes();
        setSize(400, 300);
        
        setLocationRelativeTo(vtnP); 
    }

    private void inicializarComponentes() {
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        btnBuscar = new JButton("Buscar Cuentas");
        btnEntrar = new JButton("Entrar a una Cuenta Específica");
        String estado = usuarioActual.isActivo() ? "DESACTIVAR Cuenta" : "ACTIVAR Cuenta";
        btnDesactivar = new JButton(estado);

        // Aplicar estilos a los botones
        styleButton(btnBuscar, false);
        styleButton(btnEntrar, false);
        styleButton(btnDesactivar, usuarioActual.isActivo()); 

        btnBuscar.addActionListener(this);
        btnEntrar.addActionListener(this);
        btnDesactivar.addActionListener(this);

        add(Box.createVerticalGlue());
        add(wrapInPanel(btnBuscar));
        add(Box.createVerticalStrut(20)); 
        add(wrapInPanel(btnEntrar));
        add(Box.createVerticalStrut(20));
        add(wrapInPanel(btnDesactivar));
        add(Box.createVerticalGlue()); 
    }
    
    private void styleButton(JButton button, boolean isDestructive) {
        Color bgColor = isDestructive ? COLOR_BOTON_DESTRUCTIVO : COLOR_BOTON_PRIMARIO;
        Color fgColor = isDestructive ? Color.WHITE : COLOR_BOTON_TEXTO;
        
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(isDestructive ? bgColor.darker() : COLOR_BORDE_BOTON, 1));
        button.setPreferredSize(new Dimension(300, 40));
        button.setMaximumSize(new Dimension(300, 40));
    }

    private JPanel wrapInPanel(JComponent component) {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_FONDO); 
        panel.add(component);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBuscar) {
            manejarBusquedaCuentas();

        } else if (e.getSource() == btnEntrar) {
            manejarEntrarOtraCuenta();

        } else if (e.getSource() == btnDesactivar) {
            manejarDesactivacion();
        }
    }

    private void manejarBusquedaCuentas() {
        dispose();
        vtnBusquedaUsuarios b = new vtnBusquedaUsuarios(vtnP, false);
        b.setLocationRelativeTo(vtnP);
        b.setVisible(true);
    }

    private void manejarEntrarOtraCuenta() {
        dispose();
        vtnBusquedaUsuarios b = new vtnBusquedaUsuarios(vtnP, true);
        b.setLocationRelativeTo(vtnP);
        b.setVisible(true);
    }

    private void manejarDesactivacion() {
        if (usuarioActual.isActivo()) {
            int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Estás seguro? Su cuenta no aparecerá en búsquedas.", "Confirmar Desactivación", JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                try {
                    GestorInsta.actualizarEstadoCuenta(usuarioActual.getUsuario(), false);
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
            try {
                GestorInsta.actualizarEstadoCuenta(usuarioActual.getUsuario(), true);
                JOptionPane.showMessageDialog(this, "Cuenta activada exitosamente.");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error de E/S al activar la cuenta.");
            }
        }
        dispose();
    }
}
