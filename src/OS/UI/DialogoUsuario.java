/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.awt.*;

import Compartidas.Usuario;
import OS.UI.util.TemaOscuro;

public class DialogoUsuario extends JDialog {
    
    private JTextField TxtNombre;
    private JTextField TxtUsuario;
    private JPasswordField TxtPass;
    private JTextField TxtEdad;
    private JComboBox<String> CBGenero;
    private JCheckBox CHKAdmin;
    private JCheckBox CHKActivo;
    
    private Usuario Resultado;
    
    public static Usuario mostrar(Window owner, Usuario base) {
        DialogoUsuario du = new DialogoUsuario(owner, base);
        du.setVisible(true);
        
        return du.Resultado;
    }
    
    private DialogoUsuario(Window owner, Usuario base) {
        super(owner, base == null ? "Nuevo Usuario" : "Editar Usuario", ModalityType.APPLICATION_MODAL);
        setSize(440, 360);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(8, 8));
        getContentPane().setBackground(TemaOscuro.BG);
        
        //Form
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(0, 2, 10, 10));
        form.setBackground(TemaOscuro.BAR);
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        
        JLabel LblNombre = new JLabel("Nombre Completo:");
        LblNombre.setForeground(TemaOscuro.TEXTO);
        
        JLabel LblUsuario = new JLabel("Usuario:");
        LblUsuario.setForeground(TemaOscuro.TEXTO);
        
        JLabel LblPass = new JLabel("Contraseña:");
        LblPass.setForeground(TemaOscuro.TEXTO);
        
        JLabel LblEdad = new JLabel("Edad:");
        LblEdad.setForeground(TemaOscuro.TEXTO);
        
        JLabel LblGenero = new JLabel("Genero:");
        LblGenero.setForeground(TemaOscuro.TEXTO);
        
        TxtNombre = new JTextField();
        Estilizar(TxtNombre);
        
        TxtUsuario = new JTextField();
        Estilizar(TxtUsuario);
        
        TxtPass = new JPasswordField();
        Estilizar(TxtPass);
        
        TxtEdad = new JTextField();
        Estilizar(TxtEdad);
        
        CBGenero = new JComboBox<>(new String[]{"M", "F"});
        Estilizar(CBGenero);
        
        CHKAdmin = new JCheckBox("Administrador");
        CHKAdmin.setOpaque(true);
        CHKAdmin.setBackground(TemaOscuro.BG);
        CHKAdmin.setForeground(TemaOscuro.TEXTO);
        
        CHKActivo = new JCheckBox("Activo");
        CHKActivo.setOpaque(true);
        CHKActivo.setBackground(TemaOscuro.BG);
        CHKActivo.setForeground(TemaOscuro.TEXTO);
        
        form.add(LblNombre);
        form.add(TxtNombre);
        
        form.add(LblUsuario);
        form.add(TxtUsuario);
        
        form.add(LblPass);
        form.add(TxtPass);
        
        form.add(LblEdad);
        form.add(TxtEdad);
        
        form.add(LblGenero);
        form.add(CBGenero);
        
        form.add(new JLabel());
        form.add(CHKAdmin);
        
        form.add(new JLabel());
        form.add(CHKActivo);
        
        add(form, BorderLayout.CENTER);
        
        //cosa de botones
        JPanel bajo = new JPanel();
        bajo.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bajo.setBackground(TemaOscuro.BAR);
        
        JButton BtnOk = BotonStyle("Guardar");
        JButton BtnCancelar = BotonStyle("Cancelar");
        
        bajo.add(BtnOk);
        bajo.add(BtnCancelar);
        
        add(bajo, BorderLayout.SOUTH);
        
        //Precarga si se edita
        if (base != null) {
            TxtNombre.setText(base.getNombreUsuario());
            TxtUsuario.setText(base.getUsuario());
            TxtUsuario.setEnabled(false); //Para que no se permita cambiar el username
            TxtEdad.setText(String.valueOf(base.getEdad()));
            CBGenero.setSelectedItem(String.valueOf(base.getGenero()));
            CHKAdmin.setSelected(base.isAdministrador());
            CHKActivo.setSelected(base.isActivo());
        } else {
            CHKActivo.setSelected(true);
        }
        
        BtnOk.addActionListener(e -> onGuardar(base));
        BtnCancelar.addActionListener(e -> dispose());
    }
    
    private void onGuardar(Usuario base) {
        String nombre = TxtNombre.getText().trim();
        String user = TxtUsuario.getText().trim();
        String pass =new String(TxtPass.getPassword()).trim();
        String edads = TxtEdad.getText().trim();
        String generos = (String) CBGenero.getSelectedItem();
        boolean admin = CHKAdmin.isSelected();
        boolean activo = CHKActivo.isSelected();
        
        if (nombre.isBlank() || user.isBlank()) {
            JOptionPane.showMessageDialog(this, "Nombre y usuario son obligatorios");
            return;
        }
        
        int edad;
        try {
            edad = Integer.parseInt(edads);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Edad invalida");
            return;
        }
        
        if (base == null) {
            if (pass.isBlank()) {
                JOptionPane.showMessageDialog(this, "Contraseña requerida");
                return;
            }
            
            Resultado = new Usuario(nombre, generos.charAt(0), user, pass, edad, admin);
            Resultado.setActivo(CHKActivo.isSelected());
        } else {
            //Actualizar sobre el mismo username
            base.setNombreUsuario(nombre);
            base.setGenero(generos.charAt(0));
            
            if (!pass.isBlank()) {
                base.setContrasena(pass);
            }
            
            base.setEdad(edad);
            base.setAdministrador(admin);
            base.setActivo(activo);
            
            Resultado = base;
        }
        
        dispose();
    }
    
    private JButton BotonStyle(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(TemaOscuro.CARD);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
        
        return boton;
    }
    
    private void Estilizar(JComponent component) {
        component.setBackground(TemaOscuro.CARD);
        component.setForeground(TemaOscuro.TEXTO);
        component.setBorder(BorderFactory.createLineBorder(TemaOscuro.LINEA));
    }
}
