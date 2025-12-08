/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileNameExtensionFilter("Musica (MP3)", "mp3"));
        
        ArrayList<File> copiados = new ArrayList<>();
        
        if (fc.showOpenDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File[] sel = fc.getSelectedFiles();
            
            File dirmusica = RutasUsuario.dirMusica();
            if (dirmusica == null) {
                JOptionPane.showMessageDialog(padre, "Carpeta de musica no configurada");
                return copiados;
            }
            
            if (!dirmusica.exists() && !dirmusica.mkdirs()) {
                JOptionPane.showMessageDialog(padre, "No se pudo crear la carpeta de musica:\n " + dirmusica.getAbsolutePath());
                return copiados;
            }
            
            for (File file : sel) {
                try {
                    if (file == null || !file.isFile()) {
                        continue;
                    }
                    
                    String nombre = file.getAbsolutePath().toLowerCase();
                    
                    if (!nombre.endsWith(".mp3")) {
                        continue;
                    }
                    
                    Path destino = Paths.get(dirmusica.getAbsolutePath(), file.getName());
                    
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
    
    public static void Exportar(JFrame padre, File mp3actual) {
        if (mp3actual == null || !mp3actual.isFile()) {
            JOptionPane.showMessageDialog(padre, "No hay cancion seleccionada para exportar");
            return;
        }
        
        File dirmusica = RutasUsuario.dirMusica();
        
        if (dirmusica == null) {
            JOptionPane.showMessageDialog(padre, "Carpeta de musica no configurada");
            return;
        }
        if (!dirmusica.exists()) {
            dirmusica.mkdirs();
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Exportar MP3");
        fc.setCurrentDirectory(dirmusica);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new FileNameExtensionFilter("Musica (MP3)", "mp3"));
        
        String sugerido = mp3actual.getName().toLowerCase().endsWith(".mp3") ? mp3actual.getName() : mp3actual.getName() + ".mp3";
        fc.setSelectedFile(new File(dirmusica, sugerido));
        
        if (fc.showSaveDialog(padre) == JFileChooser.APPROVE_OPTION) {
            File destino = fc.getSelectedFile();
            
            String nombresalida = destino.getName().toLowerCase().endsWith(".mp3") ? destino.getName() : destino.getName() + ".mp3";
            File filesalida = new File(destino.getParentFile(), nombresalida);

            File parent = filesalida.getParentFile();

            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            
            try {
                Files.copy(mp3actual.toPath(), filesalida.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
                
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
 }
