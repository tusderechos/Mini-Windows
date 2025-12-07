/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.Desktop;

/**
 *
 * @author Hp
 */

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.util.Properties;

public class PosicionGuardar {
    
    private final Path Archivo;
    private final Properties Props = new Properties();
    
    public PosicionGuardar(String RutaUsuario) {
        Path dir = Paths.get(RutaUsuario, ".desktop");
        Archivo = dir.resolve("shorcuts.properties");
        
        try {
            Files.createDirectories(dir);
            
            if (Files.exists(Archivo)) {
                try (InputStream entrada = Files.newInputStream(Archivo)) {
                    Props.load(entrada);
                }
            }
        } catch (IOException ignorar) {
        }
    }
    
    public Point get(String ID) {
        String v = Props.getProperty(ID);
        
        if (v == null) {
            return null;
        }
        
        String[] p = v.split(",");
        
        if (p.length != 2) {
            return null;
        }
        
        try {
            return new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    public void put(String ID, Point punto) {
        Props.setProperty(ID, punto.x + "," + punto.y);
        Guardar();
    }
    
    public void remove(String ID) {
        Props.remove(ID);
        Guardar();
    }
    
    private void Guardar() {
        try {
            OutputStream salida = Files.newOutputStream(Archivo);
            Props.store(salida, "Desktop shortcuts positions");
        } catch (IOException ignorar) {
        }
    }
}
