/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Archivos;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import OS.Core.SesionActual;
import Compartidas.Constantes;
import Compartidas.Usuario;

public class SistemaArchivo {
    
    private static Usuario SesionRequerida() throws IllegalStateException {
        Usuario usuario = SesionActual.getUsuario();
        
        if (usuario == null) {
            throw new IllegalStateException("No hay sesion activa");
        }
        
        return usuario;
    }
    
    /*
        Se crea el File y los separadores
    */
    private static File file(String ruta) {
        String separador = File.separator;
        String normalizar = ruta.replace("\\", separador).replace("/", separador);
        
        return new File(normalizar);
    }
    
    /*
        Contruye la raiz del usuario actual
        osea la de Z:/(usuario xyz)
    */
    public static String getRutaUsuario() throws IllegalStateException {
        Usuario usuario = SesionRequerida();
        String separador = File.separator;
        String base = Constantes.RUTA_BASE.replace("\\", separador).replace("/", separador);
        
        if (!base.endsWith(separador)) {
            base += separador;
        }
        
        return base + usuario.getUsuario();
    }
    
    
    /*
        Crea una carpeta dentro de la ruta Padre (solamente si existe y si es un directorio)
    */
    public static boolean CrearCarpeta(String rutapadre, String nombre) throws IllegalStateException {
        SesionRequerida();
        File padre = file(rutapadre);
        
        if (!padre.exists() || !padre.isDirectory()) {
            return false;
        }
        
        File nueva = new File(padre, nombre);
        
        return nueva.exists() ? false : nueva.mkdirs();
    }
    
    /*
        Crea un archivo vacio en la ruta Padre con su nombre mas una extension
    */
    public static boolean CrearArchivo(String rutapadre, String nombre, String extension) throws IllegalStateException, IOException {
        SesionRequerida();
        File padre = file(rutapadre);
        
        if (!padre.exists() || !padre.isDirectory()) {
            return false;
        }
        
        String ext = (extension == null || extension.isBlank()) ? "" : "." + extension.toLowerCase();
        File nuevo = new File(padre, nombre + ext);
        
        if (nuevo.exists()) {
            return false;
        }
        
        return nuevo.createNewFile();
    }
    
    
    /*
        Borra archivos o carpetas
    */
    public static boolean Eliminar(String ruta) throws IllegalStateException {
        SesionRequerida();
        
        return BorrarRec(file(ruta));
    }
    
    public static boolean BorrarRec(File objetivo) {
        if (!objetivo.exists()) {
            return false;
        }
        
        if (objetivo.isDirectory()) {
            File[] hijos = objetivo.listFiles();
            
            if (hijos != null) {
                for(File hijo : hijos) {
                    if (!BorrarRec(hijo)) {
                        return false;
                    }
                }
            }
        }
        
        return objetivo.delete();
    }
    
    
    /*
        Renombra cualquier archivo o carpeta
        si es archivo, siempre guarda la extension
        si es carpeta, solamente cambia el nombre y todo masiso
    */
    public static boolean Renombrar(String ruta, String nuevonombre) throws IllegalStateException {
        SesionRequerida();
        File actual = file(ruta);
        
        if (!actual.exists()) {
            return false;
        }
        
        File padre = actual.getParentFile();
        
        if (padre == null) {
            return false;
        }
        
        if (actual.isFile()) {
            String nombre = actual.getName();
            int indice = nombre.lastIndexOf('.');
            String ext = (indice == -1) ? "" : nombre.substring(indice); //pa incluir el punto justo antes de la extension
            File destino = new File(padre, nuevonombre + ext);
            
            return actual.renameTo(destino);
        } else {
            File destino = new File(padre, nuevonombre);
            
            return actual.renameTo(destino);
        }
    }
    
    
    /*
        Listado todo tumbado masiso masisongo
    */
    public static ResultadoListado ListarContenido(String ruta) throws IllegalStateException {
        SesionRequerida();
        
        File dir = file(ruta);
        ArrayList<Carpeta> Carpetas = new ArrayList<>();
        ArrayList<Archivo> Archivos = new ArrayList<>();
        
        if (!dir.exists() || !dir.isDirectory()) {
            return new ResultadoListado(Carpetas, Archivos);
        }
        
        File[] hijos = dir.listFiles();
        
        if (hijos == null) {
            return new ResultadoListado(Carpetas, Archivos);
        }
        
        for (File hijo : hijos) {
            if (hijo.isDirectory()) {
                Carpetas.add(new Carpeta(hijo.getAbsolutePath()));
            } else {
                Archivos.add(new Archivo(hijo.getAbsolutePath()));
            }
        }
        
        return new ResultadoListado(Carpetas, Archivos);
    }
    
    
    /*
        Organizacion por tipo
    */
    public static void OrganizarPorTipo(String ruta) throws IllegalStateException {
        SesionRequerida();
        
        File dir = file(ruta);
        
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        
        File[] hijos = dir.listFiles();
        if (hijos == null) {
            return;
        }
        
        File docs = new File(dir, "Documentos");
        File imgs = new File(dir, "Imagenes");
        File musica = new File(dir, "Musica");
        File videos = new File(dir, "Videos");
        File otros = new File(dir, "Otros");
        
        docs.mkdirs();
        imgs.mkdirs();
        musica.mkdirs();
        videos.mkdirs();
        otros.mkdirs();
        
        for (File hijo : hijos) {
            if (!hijo.isFile()) {
                return;
            }
            
            Archivo archivo = new Archivo(hijo.getAbsolutePath());
            File destino;
            
            switch (archivo.getTipo()) {
                case DOCUMENTO:
                    destino = new File(docs, hijo.getName());
                    break;
                case IMAGEN:
                    destino = new File(imgs, hijo.getName());
                    break;
                case MUSICA:
                    destino = new File(musica, hijo.getName());
                    break;
                case VIDEO:
                    destino = new File(videos, hijo.getName());
                    break;
                default:
                    destino = new File(otros, hijo.getName());
            }
            
            destino = EvitarColision(destino);
            hijo.renameTo(destino);
        }
    }
    
    /*
        Aqui se evita que se sobreescriban archivos cuando se mueva un archivo x a una carpeta que ya tenga un archivo x
        osea como cuando tengo un archivo 'patito.txt', y la muevo a una carpeta donde ya existe 'patito.txt', entonces el primero sera renombrado a 'patito (1).txt'
    */
    private static File EvitarColision(File destino) {
        if (!destino.exists()) {
            return destino;
        }
        
        String nombre = destino.getName();
        int indice = nombre.lastIndexOf('.');
        
        String base = (indice == -1) ? nombre : nombre.substring(0, indice);
        String ext = (indice == -1) ? "" : nombre.substring(indice);
        
        File carpeta = destino.getParentFile();
        int i = 1;
        File candidato;
        
        do {            
            candidato = new File(carpeta, base + " (" + i + ")" + ext);
            i++;
        } while (candidato.exists());
        
        return candidato;
    }
}
