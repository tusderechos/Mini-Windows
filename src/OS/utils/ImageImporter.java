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
import java.util.ArrayList;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageImporter {
    
    public static ArrayList<File> Importar(JFrame padre) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("Imagenes", "jpg", "jpeg", "png", "gif", "bmp"));
        
        ArrayList<File> copiados = new ArrayList<>();
        
        if (fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File[] sel = fc.getSelectedFiles();
            
            for (int i = 0; i < sel.length; i++) {
                try {
                    File destino = FileCopy.CopyTo(sel[i], RutasUsuario.dirImagenes());
                    copiados.add(destino);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(padre, "No se pudo copiar: " + sel[i].getName());
                }
            }
        }
        
        return copiados;
    }
    
    public static void Exportar(JFrame padre, File imagenactual) {
        if (imagenactual == null) {
            return;
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(imagenactual.getName()));
        
        if (fc.showSaveDialog(padre) == JFileChooser.APPROVE_OPTION) {
            try {
                FileCopy.CopyTo(imagenactual, fc.getSelectedFile().getParentFile());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(padre, "No se pudo exportar");
            }
        }
    }
}
