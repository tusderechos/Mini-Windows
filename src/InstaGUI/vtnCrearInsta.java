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
    private JLabel labelImagen;
    private String rutaFotoSeleccionada = "";
    private final PostListener listener;

    public vtnCrearInsta(PostListener listener) {
        setTitle("INSTA - Crear Nuevo Post");
        this.listener = listener;
        setSize(600, 800);
        setResizable(false);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        labelImagen = new JLabel("<html><center>Sin imagen</center></html>", JLabel.CENTER);
        labelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelImagen.setPreferredSize(new Dimension(500 - 40, 400));
        labelImagen.setMinimumSize(new Dimension(500 - 40, 400));
        labelImagen.setMaximumSize(new Dimension(500 - 40, 400));
        labelImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        panelContenido.add(labelImagen);
        panelContenido.add(Box.createVerticalStrut(15)); 

        JButton btnSelec = new JButton("Seleccionar Imagen");
        btnSelec.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSelec.setMaximumSize(new Dimension(200, btnSelec.getPreferredSize().height));
        btnSelec.addActionListener(e -> seleccionarFoto());

        panelContenido.add(btnSelec);
        panelContenido.add(Box.createVerticalStrut(30)); 

        JLabel labelDesc = new JLabel("Añade una descripción");
        labelDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(labelDesc);
        panelContenido.add(Box.createVerticalStrut(10));

        txtCaption = new JTextArea(5, 30);
        txtCaption.setLineWrap(true);
        txtCaption.setWrapStyleWord(true);
        JScrollPane scrollCaption = new JScrollPane(txtCaption);
        scrollCaption.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollCaption.setMaximumSize(new Dimension(500 - 40, 100)); 

        panelContenido.add(scrollCaption);
        panelContenido.add(Box.createVerticalStrut(25));

        JButton btnPublicar = new JButton("PUBLICAR INSTA");
        btnPublicar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPublicar.addActionListener(e -> publicarInsta());

        panelContenido.add(btnPublicar);

        add(panelContenido, BorderLayout.CENTER);
    }

    private void seleccionarFoto() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG, JPEG)", "jpg", "jpeg", "png"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();

            try {
                ImageIcon icono = new ImageIcon(rutaFotoSeleccionada);
                Image img = icono.getImage();

                int anchoMaximo = labelImagen.getWidth();
                int altoMaximo = labelImagen.getHeight();

                double ratioX = (double) anchoMaximo / img.getWidth(null);
                double ratioY = (double) altoMaximo / img.getHeight(null);
                double ratio = Math.min(ratioX, ratioY);

                int nuevoAncho = (int) (img.getWidth(null) * ratio);
                int nuevaAltura = (int) (img.getHeight(null) * ratio);

                Image imagenEscalada = img.getScaledInstance(nuevoAncho, nuevaAltura, Image.SCALE_SMOOTH);

                labelImagen.setIcon(new ImageIcon(imagenEscalada));
                labelImagen.setText(null); 

            } catch (Exception e) {
                labelImagen.setIcon(null);
                labelImagen.setText("Error al cargar imagen: " + archivo.getName());
                rutaFotoSeleccionada = "";
                JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen seleccionada.", "Error de Imagen", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void publicarInsta() {
        String caption = txtCaption.getText();

        if (caption.trim().isEmpty()) {
            caption = "";
        }

        if (rutaFotoSeleccionada.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una imagen para publicar.", "Error de Publicación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String autor = SesionManager.getUsuarioActual().getUsuario();
            Insta nuevoPost = new Insta(autor, caption, rutaFotoSeleccionada);

            GestorInsta.crearInsta(nuevoPost);

            if (listener != null) {
                listener.postPublicadoExitosamente();
            }
            JOptionPane.showMessageDialog(this, "¡Post publicado exitosamente!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();

        } catch (LongitudInstaInvalida e) {
            JOptionPane.showMessageDialog(this, e.getMessage() + ". Máximo: " + e.getLongitudMaxima() + " caracteres", "Error de Post", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error de E/S al guardar el post: " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
