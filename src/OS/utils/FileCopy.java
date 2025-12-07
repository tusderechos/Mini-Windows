/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.utils;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileCopy {
    
    public static File CopyTo(File origen, File carpetadestino) throws IOException {
        if (!carpetadestino.exists()) {
            carpetadestino.mkdirs();
        }
        
        String nombre = origen.getName();
        String base = nombre;
        String ext = "";
        
        int i = nombre.lastIndexOf('.');
        
        if (i >= 0) {
            base = nombre.substring(0, i);
            ext = nombre.substring(i);
        }
        
        File destino = new File(carpetadestino, nombre);
        int k = 1;
        
        while(destino.exists()) {
            destino = new File(carpetadestino, base + " (" + k + ")" + ext);
            k++;
        }
        
        Copiar(origen, destino);
        return destino;
    }
    
    private static void Copiar(File src, File destino) throws IOException {
        FileChannel entrada = null;
        FileChannel salida = null;
        
        try {
            entrada = new FileInputStream(src).getChannel();
            salida = new FileInputStream(destino).getChannel();
            long tamano = entrada.size();
            long pos = 0;
            
            while(pos < tamano) {
                long sent = entrada.transferTo(pos, Math.min(16 * 1024 * 1024, tamano - pos), salida);
                
                if (sent <= 0) {
                    break;
                }
                
                pos += sent;
            }
        } finally {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
        }
    }
}
