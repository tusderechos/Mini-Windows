/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos.Orden;

/**
 *
 * @author Hp
 */

import java.util.Collections;
import java.util.ArrayList;

import OS.Archivos.Archivo;
import OS.Archivos.Carpeta;
import OS.Archivos.ResultadoListado;

public class Ordenadores {
    
    /*
        Carpetas por nombre
    */
    public static void OrdenarCarpetasPorNombre(ArrayList<Carpeta> Carpetas, boolean asc) {
        Collections.sort(Carpetas, new ComparadorNombreCarpeta(asc));
    }
    
    /*
        Archivos por nombre
    */
    public static void OrdenarArchivosPorNombre(ArrayList<Archivo> Archivos, boolean asc) {
        Collections.sort(Archivos, new ComparadorNombreArchivo(asc));
    }
    
    /*
        Carpetas por fecha
    */
    public static void OrdenarArchivoPorFecha(ArrayList<Archivo> Archivos, boolean asc) {
        Collections.sort(Archivos, new ComparadorFechaArchivo(asc));
    }
    
    /*
        Carpetas por tipo logico
    */
    public static void OrdenarArchivoPorTipo(ArrayList<Archivo> Archivos, boolean asc) {
        Collections.sort(Archivos, new ComparadorTipoArchivo(asc));
    }
    
    /*
        Carpetas por tamano
    */
    public static void OrdenarArchivoPorTamano(ArrayList<Archivo> Archivos, boolean asc) {
        Collections.sort(Archivos, new ComparadorTamanoArchivo(asc));
    }
    
    
    /*
        Ordenar un ResultadoListado completo
        primero las carpetas, despues los archivos
    */
    public static void Ordenar(ResultadoListado listado, CriterioOrden criterio, boolean asc) {
        OrdenarCarpetasPorNombre(listado.getCarpetas(), true);
        
        switch (criterio) {
            case NOMBRE:
                OrdenarArchivosPorNombre(listado.getArchivos(), asc);
                break;
            case FECHA:
                OrdenarArchivoPorFecha(listado.getArchivos(), asc);
                break;
            case TIPO:
                OrdenarArchivoPorTipo(listado.getArchivos(), asc);
                break;
            case TAMANO:
                OrdenarArchivoPorTamano(listado.getArchivos(), asc);
                break;
        }
    }
}
