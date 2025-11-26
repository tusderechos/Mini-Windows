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
    
    /*
        Aqui se crea Z:/username y las demas carpetas basicas
    */
    public static void CrearEstructuraUsuario(String username) {
        //La base Z:/
        File Base = new File(Constantes.RUTA_BASE);
        
        if (!Base.exists()) {
            Base.mkdirs();
        }
        
        //Carpetas del usuario: Z:/username
        File CarpetaUsuario = new File(Base, username);
        
        if (!CarpetaUsuario.exists()) {
            CarpetaUsuario.mkdirs();
        }
        
        //Carpetas - Mis Documentos, Musica, Mis Imagenes
        for (String NombreCarpeta : Constantes.CARPETAS_PREDETERMINADAS) {
            File sub = new File(CarpetaUsuario, NombreCarpeta);
            
            if (!sub.exists()) {
                sub.mkdirs();
            }
        }
    }
}
