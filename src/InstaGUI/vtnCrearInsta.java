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

    private static final int ANCHO_VENTANA = 600;
    private static final int ALTO_VENTANA = 800;
    private static final int ANCHO_IMAGEN_MAX = ANCHO_VENTANA - 40;

    public vtnCrearInsta(PostListener listener) {
        setTitle("INSTA - Crear Nuevo Post");
        this.listener = listener;
        setSize(ANCHO_VENTANA, ALTO_VENTANA);
        setResizable(false);
        setLocationRelativeTo(null);
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        labelImagen = new JLabel("<html><center>Selecciona una imagen</center></html>", JLabel.CENTER);
        labelImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelImagen.setPreferredSize(new Dimension(ANCHO_VENTANA - 40, 300));
        labelImagen.setMinimumSize(new Dimension(ANCHO_VENTANA - 40, 300));
        labelImagen.setMaximumSize(new Dimension(ANCHO_VENTANA - 40, 300));
        labelImagen.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        
        panelContenido.add(labelImagen);
        panelContenido.add(Box.createVerticalStrut(15));

        JButton btnSelec = new JButton("Seleccionar Imagen");
        btnSelec.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSelec.setMaximumSize(new Dimension(200, btnSelec.getPreferredSize().height));
        btnSelec.addActionListener(e -> seleccionarFoto());

        panelContenido.add(btnSelec);
        panelContenido.add(Box.createVerticalStrut(15));

        JLabel labelDesc = new JLabel("Escribe una descripción");
        labelDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelContenido.add(labelDesc);
        panelContenido.add(Box.createVerticalStrut(5));

        txtCaption = new JTextArea(5, 30);
        txtCaption.setLineWrap(true);
        txtCaption.setWrapStyleWord(true);
        JScrollPane scrollCaption = new JScrollPane(txtCaption);
        scrollCaption.setAlignmentX(Component.CENTER_ALIGNMENT);// Usar el mismo ancho máx

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
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        int resultado = fileChooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            rutaFotoSeleccionada = archivo.getAbsolutePath();

            try {
                ImageIcon icono = new ImageIcon(rutaFotoSeleccionada);
                Image img = icono.getImage();

                int anchoOriginal = img.getWidth(null);
                int altoOriginal = img.getHeight(null);

                int nuevoAncho;
                int nuevaAltura;

                int max_w = ANCHO_IMAGEN_MAX; 
                int max_h = 450; 

                if (anchoOriginal > max_w || altoOriginal > max_h) {
                    double ratioAncho = (double) max_w / anchoOriginal;
                    double ratioAlto = (double) max_h / altoOriginal;

                    double ratio = Math.min(ratioAncho, ratioAlto);

                    nuevoAncho = (int) (anchoOriginal * ratio);
                    nuevaAltura = (int) (altoOriginal * ratio);
                } else {
                    nuevoAncho = anchoOriginal;
                    nuevaAltura = altoOriginal;
                }

                Image imagenEscalada = img.getScaledInstance(nuevoAncho, nuevaAltura, Image.SCALE_SMOOTH);

                labelImagen.setIcon(new ImageIcon(imagenEscalada));
                labelImagen.setText(null);

                labelImagen.setPreferredSize(new Dimension(nuevoAncho, nuevaAltura));

                labelImagen.getParent().revalidate();

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
