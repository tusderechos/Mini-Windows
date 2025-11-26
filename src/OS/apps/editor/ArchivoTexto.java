/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.apps.editor;

/**
 *
 * @author Hp
 */

import java.io.Serializable;
import java.awt.Color;
import java.time.LocalDateTime;

public class ArchivoTexto implements Serializable {
    
    private String Nombre;
    private String RutaAbsoluta;
    private String Contenido;
    private String Fuente;
    private int Tamano;
    private Color color;
    private LocalDateTime FechaCreacion;
    private LocalDateTime FechaModificacion;
    
    public ArchivoTexto(String Nombre, String RutaAbsoluta) {
        this.Nombre = Nombre;
        this.RutaAbsoluta = RutaAbsoluta;
        Contenido = "";
        Fuente = "Consola";
        Tamano = 14;
        color = Color.BLACK;
        FechaCreacion = LocalDateTime.now();
        FechaModificacion = FechaCreacion;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getRutaAbsoluta() {
        return RutaAbsoluta;
    }

    public String getContenido() {
        return Contenido;
    }

    public String getFuente() {
        return Fuente;
    }

    public int getTamano() {
        return Tamano;
    }

    public Color getColor() {
        return color;
    }

    public LocalDateTime getFechaCreacion() {
        return FechaCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return FechaModificacion;
    }

    public void setContenido(String Contenido) {
        this.Contenido = Contenido != null ? Contenido : "";
        FechaModificacion = LocalDateTime.now();
    }

    public void setFuente(String Fuente) {
        this.Fuente = Fuente;
        FechaModificacion = LocalDateTime.now();
    }

    public void setTamano(int Tamano) {
        this.Tamano = Tamano;
        FechaModificacion = LocalDateTime.now();
    }

    public void setColor(Color color) {
        this.color = color;
        FechaModificacion = LocalDateTime.now();
    }
    
    
}
