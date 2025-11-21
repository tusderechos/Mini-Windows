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

public class Archivo {
    
    private String Nombre;
    private String Extension;
    private String RutaAbsoluta;
    private long TamanoBytes;
    private LocalDate FechaCreacion;
    private TipoArchivo Tipo;
    
    /*
        Este es para crear un archivo a base de su nombre, su extension y la ruta padre
    */
    public Archivo(String Nombre, String Extension, String RutaPadre) {
        this.Nombre = Nombre;
        this.Extension = Extension != null ? Extension.toLowerCase() : "";
        RutaAbsoluta = ConstruirRuta(RutaPadre, Nombre, this.Extension);
        FechaCreacion = LocalDate.now();
        TamanoBytes = 0;
        Tipo = DeterminarTipo(this.Extension);
    }
    
    /*
        Este es para crear un archivo a base de una ruta absoluta que ya exista (enfasis en que ya exista porque es la quinta vez que hago esto)
    */
    public Archivo(String RutaAbsoluta) {
        this.RutaAbsoluta = RutaAbsoluta;
        
        File file = new File(RutaAbsoluta);
        
        String NombreCompleto = file.getName();
        int IndicePunto = NombreCompleto.lastIndexOf(".");
        
        if (IndicePunto == -1) {
            Nombre = NombreCompleto;
            Extension = "";
        } else {
            Nombre = NombreCompleto.substring(0, IndicePunto);
            Extension = NombreCompleto.substring(IndicePunto + 1).toLowerCase();
        }
        
        TamanoBytes = file.exists() ? file.length() : 0;
        FechaCreacion = LocalDate.now();
        Tipo = DeterminarTipo(Extension);
    }
    
    private String ConstruirRuta(String rutapadre, String nombre, String extension) {
        String separador = File.separator;
        
        if (rutapadre.endsWith("/") || rutapadre.endsWith("\\") || rutapadre.endsWith(separador)) {
            return rutapadre + nombre + (extension.isEmpty() ? "" : "." + extension);
        } else {
            return rutapadre + separador + nombre + (extension.isEmpty() ? "" : "." + extension);
        }
    }

    public String getNombre() {
        return Nombre;
    }

    public String getExtension() {
        return Extension;
    }

    public String getRutaAbsoluta() {
        return RutaAbsoluta;
    }

    public long getTamanoBytes() {
        return TamanoBytes;
    }

    public LocalDate getFechaCreacion() {
        return FechaCreacion;
    }

    public TipoArchivo getTipo() {
        return Tipo;
    }

    public void setExtension(String Extension) {
        this.Extension = Extension != null ? Extension.toLowerCase() : "";
        Tipo = DeterminarTipo(Extension);
    }

    public void setTamanoBytes(long TamanoBytes) {
        this.TamanoBytes = TamanoBytes;
    }
    
    public File toFile() {
        return new File(RutaAbsoluta);
    }
    
    /*
        Determinar el tipo de archivo segun la extension que tiene
        
        Ing y el resto del mundo que lea/vea/conjure esto, pero son las 1:41 y yo quiero dormir asi que toca hacer cosas prohibidas
        talvez lo cambio, talvez se presenta el proyecto asi, quien sabe, yo definitivamente no, si quieren la respuesta, todo esto
        en el siguiente episodio de Dragon Ball
    */
    private TipoArchivo DeterminarTipo(String ext) {
        if (ext == null) {
            ext = "";
        }
        
        ext = ext.toLowerCase();
        
        switch (ext) {
            case "txt":
            case "doc":
            case "docx":
            case "pdf":
                return TipoArchivo.DOCUMENTO;
            
            case "jpg":
            case "jpeg":
            case "png":
            case "gif":
            case "bmp":
                return TipoArchivo.IMAGEN;
                
            case "mp3":
            case "wav":
            case "flac":
                return TipoArchivo.MUSICA;
                
            case "mp4":
            case "avi":
            case "mkv":
                return TipoArchivo.VIDEO;
                
            default:
                return TipoArchivo.OTRO;
        }
    }
    
    @Override
    public String toString() {
        return "Archivo {" + "Nombre = '" + getNombre() + '\'' + ", Ruta = '" + RutaAbsoluta + '\'' + ", Tipo = '" + Tipo + "}";
    }
}
