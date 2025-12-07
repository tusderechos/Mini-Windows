/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

/**
 *
 * @author HP
 */
import Insta.GestorInsta;
import Insta.SesionManager;
import Insta.UsernameYaExiste;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.io.IOException;
import Compartidas.Usuario;

public class vtnRegistro extends JFrame {

    //1 vtn
    private JTextField txtNombre, txtUsername, txtPassword, txtEdad;
    private JRadioButton rbMasculino, rbFemenino;
    private JLabel rutaFoto;
    private String rutaFotoSeleccionada = "";
    private String extensionFoto = "";

    public vtnRegistro() {
        setTitle("INSTA - Crear Cuenta");
        setSize(600, 800);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        JPanel panelFormulario = new JPanel(new GridLayout(8, 2, 5, 5));

        panelFormulario.add(new JLabel("Nombre :"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Username:"));
        txtUsername = new JTextField();
        panelFormulario.add(txtUsername);

        panelFormulario.add(new JLabel("Password:"));
        txtPassword = new JPasswordField();
        panelFormulario.add(txtPassword);

        panelFormulario.add(new JLabel("Edad:"));
        txtEdad = new JTextField();
        panelFormulario.add(txtEdad);

        panelFormulario.add(new JLabel("Género (M/F):"));
        JPanel panelGenero = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rbMasculino = new JRadioButton("M");
        rbFemenino = new JRadioButton("F");
        ButtonGroup grupoGenero = new ButtonGroup();
        grupoGenero.add(rbMasculino);
        grupoGenero.add(rbFemenino);
        panelGenero.add(rbMasculino);
        panelGenero.add(rbFemenino);
        panelFormulario.add(panelGenero);

        panelFormulario.add(new JLabel("Foto de Perfil:"));
        JButton btnSeleccionarFoto = new JButton("Seleccionar Foto");
        rutaFoto = new JLabel("Ninguna seleccionada");
        btnSeleccionarFoto.addActionListener(e -> seleccionarFoto());

        JPanel panelFoto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFoto.add(btnSeleccionarFoto);
        panelFoto.add(rutaFoto);
        panelFormulario.add(panelFoto);

        JButton btnRegistrar = new JButton("REGISTRAR CUENTA");
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });

        add(panelFormulario, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }
    
    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();
            rutaFoto.setText(archivo.getName());
            
             String nombreArchivoOriginal = archivo.getName();
        int indicePunto = nombreArchivoOriginal.lastIndexOf('.');
        if (indicePunto > 0) {
            extensionFoto = nombreArchivoOriginal.substring(indicePunto); 
        } else {
            extensionFoto = ""; 
        }
        }
    }

    private void registrarUsuario() {

        String nombre = txtNombre.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String edad = txtEdad.getText();

        char genero = 'N'; // N = No especificado
        if (rbMasculino.isSelected()) {
            genero = 'M';
        } else if (rbFemenino.isSelected()) {
            genero = 'F';
        }

        if (nombre.isEmpty() || username.isEmpty() || password.isEmpty() || genero == 'N' || edad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int Edad = Integer.parseInt(edad);
            Usuario nuevoUsuario = new Usuario(
                    username, genero, nombre, password, Edad, false
            );
            if (!rutaFotoSeleccionada.isEmpty()) {
                String rutaPermanente = GestorInsta.copiarFotoPerfil(username, rutaFotoSeleccionada, extensionFoto);

                if (rutaPermanente != null) {
                    nuevoUsuario.setRutaFotoPerfil(rutaPermanente);
                }
            }

            GestorInsta.crearNuevaCuenta(nuevoUsuario);
            SesionManager.setUsuarioActual(nuevoUsuario);
            JOptionPane.showMessageDialog(this, "Usuario " + username + " creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            dispose();
            System.out.println("Cuenta creada, debe abrir la ventana principal de INSTA.");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La Edad debe ser un número válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (UsernameYaExiste e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error de Username", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al crear archivos o copar ft: " + e.getMessage(), "Error de Sistema", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
