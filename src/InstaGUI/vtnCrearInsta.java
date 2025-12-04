/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package InstaGUI;

import Insta.GestorInsta;
import Insta.Insta;
import Insta.LongitudInstaInvalida;
import Insta.SesionManager;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

/**
 *
 * @author HP
 */
public class vtnCrearInsta extends JDialog {

    private JTextArea txtCaption;
    private JLabel rutaFoto;
    private String rutaFotoSeleccionada = "";

    public vtnCrearInsta(JFrame parent) {
        setTitle("INSTA - Crear Nuevo Post");
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        //panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(3, 1, 5, 5));

        //aqui va para escibir el caption
        panelForm.add(new JLabel("Escribe uan descripcion:"));
        txtCaption = new JTextArea(5, 40);
        txtCaption.setLineWrap(true);
        txtCaption.setWrapStyleWord(true);
        JScrollPane scrollCaption = new JScrollPane(txtCaption);
        panelForm.add(scrollCaption);

        //seleccion de img
        JPanel panelFoto = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnSelec = new JButton("Seleccionar imagen");
        rutaFoto = new JLabel("Ninguna imagen seleccionada");

        btnSelec.addActionListener(e -> seleccionarFoto());

        panelFoto.add(btnSelec);
        panelFoto.add(rutaFoto);
        panelForm.add(panelFoto);

        //btn publicar
        JButton btnPublicar = new JButton("PUBLICAR INSTA");
        btnPublicar.addActionListener(e -> publicarInsta());

        add(panelForm, BorderLayout.CENTER);
        add(btnPublicar, BorderLayout.SOUTH);
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();
            rutaFoto.setText(archivo.getName());
        }
    }

    private void publicarInsta() {
        String caption = txtCaption.getText();

        if (rutaFotoSeleccionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar uan imagen para publicar.");
            return;
        }

        try {
            String autor = SesionManager.getUsuarioActual().getNombreUsuario();
            Insta nuevoPost = new Insta(autor, caption, rutaFotoSeleccionada);

            GestorInsta.crearInsta(nuevoPost);
            JOptionPane.showMessageDialog(this, "¡Post publicado exitosamente!");
            
            /*Refrescar el TimeLine de la ventana principal
            if (getParent() instanceof vtnInstaPrincipal) {
            ((vtnInstaPrincipal) getParent()).cargarTimeLine();*/
            
            dispose();

        } catch(LongitudInstaInvalida e) {
            JOptionPane.showMessageDialog(this, e.getMessage()+" Maximo: "+e.getLongitudMaxima()+" caractere");
        } catch(IOException e){
            JOptionPane.showMessageDialog(this, "Error de E/S al guardar el post: "+e.getMessage());
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, "Ocurrio un error: "+e.getMessage());
        }
    }
}
