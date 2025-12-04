/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Core;

/**
 *
 * @author Hp
 */

import java.io.File;
import Compartidas.Constantes;

public class GestorCarpetasUsuario {
    
    public static void AsegurarBase() {
        File base = new File(Constantes.RUTA_BASE);
        if (!base.exists()) {
            base.mkdirs();
        }
    }
    
    /*
        Aqui se crea Z:/username y las demas carpetas basicas
    */
    public static void CrearEstructuraUsuario(String username) {
        //Carpetas del usuario: Z:/username
        File baseusuario = new File(Constantes.RUTA_BASE, username);
        
        if (!baseusuario.exists()) {
            baseusuario.mkdirs();
        }
        
        //Carpetas - Mis Documentos, Musica, Mis Imagenes
        for (String NombreCarpeta : Constantes.CARPETAS_PREDETERMINADAS) {
            new File(baseusuario, NombreCarpeta).mkdirs();
        }
    }
    
    /*
        Por si el usuario ya existai pero no su carpeta
    */
    public static void ValidarOCrearEstructuraUsuario(String username) {
        File baseusuario = new File(Constantes.RUTA_BASE, username);
        
        if (!baseusuario.exists() || baseusuario.listFiles() == null || baseusuario.listFiles().length == 0) {
            CrearEstructuraUsuario(username);
        }
    }
}
