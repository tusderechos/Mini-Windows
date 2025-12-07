/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.utils;

/**
 *
 * @author Hp
 */

import java.io.File;

import Compartidas.Constantes;
import Compartidas.Usuario;
import OS.Core.SesionActual;

public class RutasUsuario {
    
    private static String UsuarioActual() {
        Usuario usuario = SesionActual.getUsuario();
        
        if (usuario != null && usuario.getUsuario() != null && !usuario.getUsuario().isBlank()) {
            return usuario.getUsuario();
        }
        
        return "admin";
    }
    
    private static File BaseUsuario() {
        File base = new File(Constantes.RUTA_BASE, UsuarioActual());
        
        if (!base.exists()) {
            base.mkdirs();
        }
        
        return base;
    }
    
    public static File dirImagenes() {
        File destino = new File(BaseUsuario(), "Mis Imagenes");
        
        if (!destino.exists()) {
            destino.mkdirs();
        }
        
        return destino;
    }
    
    public static File dirMusica() {
        File destino = new File(BaseUsuario(), "Musica");
        
        if (!destino.exists()) {
            destino.mkdirs();
        }
        
        return destino;
    }
}
