/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos.Buscar;

/**
 *
 * @author Hp
 */

import java.util.ArrayList;

public class ResultadoBusqueda {
    
    private ArrayList<String> Coincidencias;
    
    public ResultadoBusqueda(ArrayList<String> Coincidencias) {
        this.Coincidencias = Coincidencias;
    }

    public ArrayList<String> getCoincidencias() {
        return Coincidencias;
    }   
}
