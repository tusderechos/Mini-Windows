/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos;

/**
 *
 * @author Hp
 */

import java.util.ArrayList;

public class ResultadoListado {
    
    private ArrayList<Carpeta> Carpetas;
    private ArrayList<Archivo> Archivos;

    public ResultadoListado(ArrayList<Carpeta> Carpetas, ArrayList<Archivo> Archivos) {
        this.Carpetas = Carpetas;
        this.Archivos = Archivos;
    }

    public ArrayList<Carpeta> getCarpetas() {
        return Carpetas;
    }


    public ArrayList<Archivo> getArchivos() {
        return Archivos;
    }
}
