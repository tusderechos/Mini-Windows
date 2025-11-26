/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.apps.Imagenes;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.util.ArrayList;

public class VisorImagenesCore {
    
    private static final String[] EXTENSIONES = {"jpg", "jpeg", "png", "gif", "bmp", "webp"};
    
    public static ArrayList<String> ListarImagenes(String carpeta) {
        File dir = new File(carpeta);
        ArrayList<String> salida = new ArrayList<>();
        
        if (!dir.exists() || !dir.isDirectory()) {
            return salida;
        }
        
        File[] hijos = dir.listFiles();
        
        if (hijos == null) {
            return salida;
        }
        
        for (File hijo : hijos) {
            if (hijo.isFile() && esImagen(hijo.getName())) {
                salida.add(hijo.getAbsolutePath());
            }
        }
        
        return salida;
    }
    
    public static boolean esImagen(String nombre) {
        String n = nombre.toLowerCase();
        
        for (String ext : EXTENSIONES) {
            if (n.endsWith("." + ext)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static int IndiceSiguiente(int actual, int total) {
        if (total <= 0) {
            return -1;
        }
        
        return (actual + 1) % total;
    }
    
    public static int IndicePrevio(int actual, int total) {
        if (total <= 0) {
            return -1;
        }
        
        return (actual - 1 + total) % total;
    }
}
