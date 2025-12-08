/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.utils;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageImporter {
    
    public static ArrayList<File> Importar(JFrame padre) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Imagenes (jpg, jpeg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
        
        ArrayList<File> copiados = new ArrayList<>();
        
        if (fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File[] sel = fc.getSelectedFiles();
            
            File dirimagenes = RutasUsuario.dirImagenes();
            if (dirimagenes == null) {
                JOptionPane.showMessageDialog(padre, "Carpeta de imagenes no configurada");
                return copiados;
            }
            
            if (!dirimagenes.exists() && !dirimagenes.mkdirs()) {
                JOptionPane.showMessageDialog(padre, "No se pudo crear la carpeta de imagenes:\n " + dirimagenes.getAbsolutePath());
                return copiados;
            }
            
            for (File file : sel) {
                if (file == null || !file.isFile()) {
                    continue;
                }

                String ext = getExtensionLower(file.getName());

                if (!ext.matches("jpg|jpeg|png|gif|bmp")) {
                    continue;
                }
                    
                try {
                    Path destino = Paths.get(dirimagenes.getAbsolutePath(), file.getName());
                    destino = SiguienteDisponible(destino);
                    
                    Files.copy(file.toPath(), destino, StandardCopyOption.COPY_ATTRIBUTES);
                    
                    copiados.add(destino.toFile());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(padre, "No se pudo copiar: " + file.getName() + "\n" + e.getMessage());
                }
            }
        }
        
        return copiados;
    }
    
    public static void Exportar(JFrame padre, File imagenactual) {
        if (imagenactual == null || !imagenactual.isFile()) {
            JOptionPane.showMessageDialog(padre, "No hay imagen seleccionada para exportar");
            return;
        }
        
        File dirimagenes = RutasUsuario.dirImagenes();
        
        if (dirimagenes == null) {
            JOptionPane.showMessageDialog(padre, "Carpeta de imagenes no configurada");
            return;
        }
        if (!dirimagenes.exists()) {
            dirimagenes.mkdirs();
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Exportar Imagen");
        fc.setCurrentDirectory(dirimagenes);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter("Imagenes (jpg, jpeg, png, gif, bmp)", "jpg", "jpeg", "png", "gif", "bmp"));
        
        String sugerido = imagenactual.getName();
        fc.setSelectedFile(new File(dirimagenes, sugerido));
        
        if (fc.showSaveDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File elegido = fc.getSelectedFile();
            
            String extensionsrc = getExtensionLower(sugerido);
            String extensionsalida = getExtensionLower(elegido.getName());
            String nombrefinal = elegido.getName();
            
            if (extensionsalida.isEmpty()) {
                nombrefinal = elegido.getName() + (extensionsrc.isEmpty() ? ".png" : "." + extensionsrc);
            }
            
            File filesalida = new File(elegido.getParentFile(), nombrefinal);

            File parent = filesalida.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            
            try {
                Files.copy(imagenactual.toPath(), filesalida.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                
                JOptionPane.showMessageDialog(padre, "Exportado en: \n" + filesalida.getAbsolutePath());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(padre, "No se pudo exportar :\n" + e.getMessage());
            }
        }
    }
    
    private static Path SiguienteDisponible(Path path) {
        if (!Files.exists(path)) {
            return path;
        }
        
        String nombre = path.getFileName().toString();
        String base;
        String ext;
        
        if (nombre.lastIndexOf('.') >= 0) {
            base = nombre.substring(0, nombre.lastIndexOf('.'));
            ext = nombre.substring(nombre.lastIndexOf('.'));
        } else {
            base = nombre;
            ext = "";
        }
        
        int i = 1;
        Path padre = path.getParent();
        Path candidato;
        
        do {            
            candidato = padre.resolve(base + " (" + i + ")" + ext);
            i++;
        } while (Files.exists(candidato));
        
        return candidato;
    }
    
    private static String getExtensionLower(String nombre) {
        if (nombre.lastIndexOf('.') < 0 || nombre.lastIndexOf('.') == nombre.length() - 1) {
            return "";
        }
        
        return nombre.substring(nombre.lastIndexOf('.') + 1).toLowerCase();
    }
}
