/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Carpeta {
    
    private String Nombre;
    private String RutaAbsoluta;
    private LocalDate FechaCreacion;
    
    private ArrayList<Carpeta> Subcarpetas;
    private ArrayList<Archivo> Archivos;
    
    
    /*
        Este es para crear una carpeta pero indicando el nombre y la ruta padre \('_')/
    */
    public Carpeta(String Nombre, String RutaPadre) {
        this.Nombre = Nombre;
        if (RutaPadre.endsWith(File.separator) || RutaPadre.endsWith("/") || RutaPadre.endsWith("\\")) {
            this.RutaAbsoluta = RutaPadre + Nombre;
        } else {
            this.RutaAbsoluta = RutaPadre + File.separator + Nombre;
        }
        
        FechaCreacion = LocalDate.now();
        Subcarpetas = new ArrayList<>();
        Archivos = new ArrayList<>();
    }
    
    /*
        Este es para crear una carpeta pero indicando diunsolo la ruta absoluta
    */
    public Carpeta(String RutaAbsoluta) {
        this.RutaAbsoluta = RutaAbsoluta;
        Nombre = ExtraerNombreDesdeRuta(RutaAbsoluta);
        FechaCreacion = LocalDate.now();
        Subcarpetas = new ArrayList<>();
        Archivos = new ArrayList<>();
    }
    
    private String ExtraerNombreDesdeRuta(String ruta) {
        ruta = ruta.replace("\\", "/");
        
        int indice = ruta.lastIndexOf("/");
        
        if (indice == -1) {
            return ruta; //Es para cuando no hay separadores, osea que la ruta es el mismo nombre
        }
        
        return ruta.substring(indice + 1);
    }

    public String getNombre() {
        return Nombre;
    }

    public String getRutaAbsoluta() {
        return RutaAbsoluta;
    }

    public LocalDate getFechaCreacion() {
        return FechaCreacion;
    }

    public ArrayList<Carpeta> getSubcarpetas() {
        return Subcarpetas;
    }

    public ArrayList<Archivo> getArchivos() {
        return Archivos;
    }
    
    
    public void AgregarSubcarpeta(Carpeta carpetahija) {
        if (carpetahija != null && !Subcarpetas.contains(carpetahija)) {
            Subcarpetas.add(carpetahija);
        }
    }
    
    public boolean EliminarSubcarpeta(Carpeta carpetahija) {
        return Subcarpetas.remove(carpetahija);
    }
    
    public Carpeta BuscarSubcarpetaPorNombre(String nombre) {
        for (Carpeta carpeta : Subcarpetas) {
            if (carpeta.getNombre().equalsIgnoreCase(nombre)) {
                return carpeta;
            }
        }
        
        return null;
    }
    
    
    public void AgregarArchivo(Archivo archivo) {
        if (archivo != null && !Archivos.contains(archivo)) {
            Archivos.add(archivo);
        }
    }
    
    public boolean EliminarArchivo(Archivo archivo) {
        return Archivos.remove(archivo);
    }
    
    public Archivo BuscarArchivoPorNombre(String nombre) {
        for (Archivo archivo : Archivos) {
            if (archivo.getNombre().equalsIgnoreCase(nombre)) {
                return archivo;
            }
        }
        
        return null;
    }
    
    
    public File toFile() {
        return new File(RutaAbsoluta);
    }
    
    @Override
    public String toString() {
        return "Carpeta {" + "Nombre = '" + Nombre + '\'' + ", Ruta = '" + RutaAbsoluta + '\'' + '}';
    }
}