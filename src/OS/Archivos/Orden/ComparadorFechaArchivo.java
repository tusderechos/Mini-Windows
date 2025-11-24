/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos.Orden;

/**
 *
 * @author Hp
 */

import java.util.Comparator;

import OS.Archivos.Archivo;

public class ComparadorFechaArchivo implements Comparator<Archivo> {
    
    private final boolean asc;
    
    public ComparadorFechaArchivo(boolean asc) {
        this.asc = asc;
    }
    
    @Override
    public int compare(Archivo a, Archivo b) {
        int cmp = a.getFechaCreacion().compareTo(b.getFechaCreacion());
        
        return asc ? cmp : -cmp;
    }
}
