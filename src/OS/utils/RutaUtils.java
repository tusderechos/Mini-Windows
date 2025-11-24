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

public class RutaUtils {
    
    private static String separador() {
        return File.separator;
    }
    
    /*
        Normaliza separadores a los del sistema operativo
    */
    public static String Normalizar(String ruta) {
        if (ruta == null) {
            return null;
        }
        
        String s = ruta.replace("\\", separador()).replace("/", separador());
        
        //Eliminar los doblea separadores
        while (s.contains(separador() + separador())) {
            s = s.replace(separador() + separador(), separador());
        }
        
        return s;
    }
    
    /*
        Une segmentos de ruta con el separador correcto
    */
    public static String Unir(String... partes) { //Lo de 'String...' es porque no se que tantos parametros se van a unir, entonces ... es como decir que puede tener uno o multiples parametrs de tipo String
        if (partes == null || partes.length == 0) {
            return "";
        }
        
        String salida = "";
        
        for (String p : partes) {
            String n = Normalizar(p);
            
            if (salida.isEmpty()) {
                salida = n;
            } else {
                if (salida.endsWith(separador())) {
                    salida += n.startsWith(separador()) ? n.substring(1) : n;
                } else {
                    salida += n.startsWith(separador()) ? n : separador() + n;
                }
            }
        }
        
        return salida;
    }
    
    /*
        Devuelve la extension sin el punto
        si no hay pues devuelve ""
    */
    public static String getExtension(String nombre) {
        if (nombre == null) {
            return "";
        }
        
        int i = nombre.lastIndexOf('.');
        
        return (i == -1 || i == nombre.length() - 1) ? "" : nombre.substring(i + 1).toLowerCase();
    }
    
    /*
        Nombre de archivo sin extension
    */
    public static String getNombreBase(String nombre) {
        if (nombre == null) {
            return null;
        }
        
        int i = nombre.lastIndexOf('.');
        
        return (i == -1) ? nombre : nombre.substring(0, i);
    }
    
    public static boolean Existe(String ruta) {
        return new File(Normalizar(ruta)).exists();
    }
    
    public static boolean isDirectory(String ruta) {
        File file = new File(Normalizar(ruta));
        
        return file.exists() && file.isDirectory();
    }
    
    public static boolean isFile(String ruta) {
        File file = new File(Normalizar(ruta));
        
        return file.exists() && file.isFile();
    }
    
    /*
        Crea directorios (y los padres) si no existen
    */
    public static boolean AsegurarDir(String ruta) {
        File file = new File(Normalizar(ruta));
        
        return file.exists() ? file.isDirectory() : file.mkdirs();
    }
    
    /*
        Nombre seguro
    */
    public static String NombreSeguroParaArchivo(String nombre) {
        if (nombre == null) {
            return null;
        }
        
        return nombre.replaceAll("[\\\\/:*?\"<>|]", "_").trim();
    }
}
