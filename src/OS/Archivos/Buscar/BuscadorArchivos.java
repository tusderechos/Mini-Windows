/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos.Buscar;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.util.ArrayList;

import OS.Core.SesionActual;
import OS.Archivos.SistemaArchivo;

public class BuscadorArchivos {
    
    /*
        Busqueda principal
    */
    public static ResultadoBusqueda Buscar(String rutainicial, String patron, boolean recursivo) throws IllegalStateException {
        SesionRequerida();
        
        ArrayList<String> resultados = new ArrayList<>();
        
        if (rutainicial == null || patron == null || patron.isBlank()) {
            return new ResultadoBusqueda(resultados);
        }
        
        String patronlower = patron.toLowerCase();
        File inicio = new File(rutainicial);
        
        if (!inicio.exists() || !inicio.isDirectory()) {
            return new ResultadoBusqueda(resultados);
        }
        
        BuscarInterno(inicio, patronlower, recursivo, resultados);
        
        return new ResultadoBusqueda(resultados);
    }
    
    private static void BuscarInterno(File dir, String patronlower, boolean recursivo, ArrayList<String> salida) {
        File[] hijos = dir.listFiles();
        
        if (hijos == null) {
            return;
        }
        
        for (File hijo : hijos) {
            String nombre = hijo.getName().toLowerCase();
            
            //Coincidencia
            if (nombre.contains(patronlower)) {
                salida.add(hijo.getAbsolutePath());
            }
            
            //Recursividad
            if (recursivo && hijo.isDirectory()) {
                BuscarInterno(hijo, patronlower, true, salida);
            }
        }
    }
    
    /*
        Validacion centalizada de sesion
    */
    private static void SesionRequerida() throws IllegalStateException {
        if (!SesionActual.haySesion()) {
            throw new IllegalStateException("No hay sesion activa");
        }
    }
}
