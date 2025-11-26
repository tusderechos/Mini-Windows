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
import OS.Archivos.TipoArchivo;

public class ComparadorTipoArchivo implements Comparator<Archivo> {
    
    private final boolean asc;
    
    public ComparadorTipoArchivo(boolean asc) {
        this.asc = asc;
    }
    
    /*
        El orden logico aqui seria tipo:
        0. Documento
        1. Imagen
        2. Musica
        3. Video
        4. Otro
    */
    private static int Rango(TipoArchivo tipo) {
        switch (tipo) {
            case DOCUMENTO:
                return 0;
            case IMAGEN:
                return 1;
            case MUSICA:
                return 2;
            case VIDEO:
                return 3;
            default:
                return 4;
        }
    }
    
    @Override
    public int compare(Archivo a, Archivo b) {
        int cmp = Integer.compare(Rango(a.getTipo()), Rango(b.getTipo()));
        
        if (cmp == 0) {
            //tie-break por nombre para que visualmente se mire estable
            cmp = a.getNombre().compareToIgnoreCase(b.getNombre());
        }
        
        return asc ? cmp : -cmp;
    }
}
