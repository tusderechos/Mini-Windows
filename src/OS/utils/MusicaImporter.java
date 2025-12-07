/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.utils;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Hp
 */
public class MusicaImporter {
    
    public static ArrayList<File> Importar(JFrame padre) {
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("Musica (MP3)", "mp3"));
        
        ArrayList<File> copiados = new ArrayList<>();
        
        if (fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File[] sel = fc.getSelectedFiles();
            
            for (int i = 0; i < sel.length; i++) {
                try {
                    File destino = FileCopy.CopyTo(sel[i], RutasUsuario.dirMusica());
                    copiados.add(destino);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(padre, "No se pudo copiar: " + sel[i].getName());
                }
            }
        }
        
        return copiados;
    }
    
    public static void Exportar(JFrame padre, File mp3actual) {
        if (mp3actual == null) {
            return;
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setSelectedFile(new File(mp3actual.getName()));
        
        if (fc.showSaveDialog(padre) == JFileChooser.APPROVE_OPTION) {
            try {
                FileCopy.CopyTo(mp3actual, fc.getSelectedFile().getParentFile());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(padre, "No se pudo exportar");
            }
        }
    }
}
