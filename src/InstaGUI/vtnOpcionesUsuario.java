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
        JPanel panel = new JPanel();
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
