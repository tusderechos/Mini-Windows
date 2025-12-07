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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

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
        return new File(Constantes.RUTA_BASE, SesionActual.getUsuario().getUsuario()).getAbsolutePath();
    }
    
    /*
        Devuelve Z\
    */
    public static String getRutaBaseUsuarios(){
        return Constantes.RUTA_BASE;
    }
    
    public static File getCarpetaUsuario(String username) {
        return new File(Constantes.RUTA_BASE, username);
    }
    
    /*
        Elimina la carpeta fisica del usuario
    */
    public static boolean EliminarCarpetaUsuario(String username) {
        try {
            Path base = Paths.get(Constantes.RUTA_BASE).toAbsolutePath().normalize();
            Path objetivo = base.resolve(username).toAbsolutePath().normalize();
            
            if (!objetivo.startsWith(base)) {
                return false; //Para no salir de la raiz
            }
            if (objetivo.equals(base)) {
                return false; //Para no salir de la raiz
            }
            if (!Files.exists(objetivo)) {
                return true; //Por si ya no existe
            }
            
            Files.walkFileTree(objetivo, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes atributos) throws IOException {
                    try {
                        Files.deleteIfExists(file);
                    } catch (IOException e) {
                    }
                    
                    return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException excepcion) throws IOException {
                    try {
                        Files.deleteIfExists(dir);
                    } catch (IOException e) {
                    }
                
                   return FileVisitResult.CONTINUE;
                }
                
                @Override
                public FileVisitResult visitFileFailed(Path file, IOException excepcion) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            
            return true;
        } catch (Exception e) {
            return false;
        }
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
        
        String limpio = (nombre == null) ? "" : nombre.trim();
        
        if (limpio.isEmpty()) {
            return false;
        }
        
        File nueva = new File(padre, limpio);
        
        if (nueva.exists()) {
            return false; //Porque la carpeta ya existe
        }
        
        return nueva.mkdirs();
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
        if (hijos == null || hijos.length == 0) {
            return;
        }
        
        File docs = new File(dir, "Documentos");
        File imgs = new File(dir, "Imagenes");
        File musica = new File(dir, "Musica");
        File videos = new File(dir, "Videos");
        File otros = new File(dir, "Otros");
        
        int movidos = 0;
        int saltados = 0;
        
        for (File hijo : hijos) {
            if (!hijo.isFile()) {
                continue;
            }
            
            File padre = hijo.getParentFile();
            
            if (padre.equals(docs) ||padre.equals(imgs) || padre.equals(musica) || padre.equals(videos) || padre.equals(otros)) {
                continue;
            }
            
            Archivo archivo = new Archivo(hijo.getAbsolutePath());
            File destinobase;
            
            switch (archivo.getTipo()) {
                case DOCUMENTO:
                    destinobase = docs;
                    break;
                case IMAGEN:
                    destinobase = imgs;
                    break;
                case MUSICA:
                    destinobase = musica;
                    break;
                case VIDEO:
                    destinobase = videos;
                    break;
                default:
                    destinobase = otros;
            }
            
            if (!destinobase.exists()) {
                destinobase.mkdirs();
            }
            
            File destino = new File(destinobase, hijo.getName());
            destino = EvitarColision(destino);
            
            try {
                Files.move(hijo.toPath(), destino.toPath());
                movidos++;
            } catch (Exception e) {
                saltados++;
            }
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
    
    /*
        Copiar un archivo o carpeta dentro de DestinoCarpeta
    */
    public static boolean Copiar(String origen, String destinocarpeta) throws IllegalStateException, IOException {
        SesionRequerida();
        
        File src = file(origen);
        File dirdestino = file(destinocarpeta);
        
        if (!src.exists() || !dirdestino.exists() || !dirdestino.isDirectory()) {
            return false;
        }
        
        File destino = new File(dirdestino, src.getName());
        destino = src.isDirectory() ? EvitarColisionDirectorio(destino) : EvitarColision(destino);
        
        return CopiarRec(src, destino);
    }
    
    private static boolean CopiarRec(File src, File destino) throws IOException {
        if (src.isDirectory()) {
            
            if (!destino.exists() && !destino.mkdirs()) {
                return false;
            }
            
            File[] hijos = src.listFiles();
            
            if (hijos != null) {
                for (File hijo : hijos) {
                    File nd = new File(destino, hijo.getName());
                    
                    if (hijo.isFile() && nd.exists()) {
                        nd = EvitarColision(nd);
                    }
                    
                    if (!CopiarRec(hijo, nd)) {
                        return false;
                    }
                }
            }
            
            return true;
        } else {
            //Archivo
            if (destino.exists()) {
                destino = EvitarColision(destino);
            }
            
            Files.copy(src.toPath(), destino.toPath(), StandardCopyOption.COPY_ATTRIBUTES);
            return true;
        }
    }
    
    /*
        Evita colision para directorios
        como el otro, crea "Nombre (1)", "Nombre (2) y etc etc
    */
    private static File EvitarColisionDirectorio(File destino) {
        if (!destino.exists()) {
            return destino;
        }
        
        String base = destino.getName();
        File carpeta = destino.getParentFile();
        int i = 1;
        File candidato;
        
        do {            
            candidato = new File(carpeta, base + " (" + i + ")");
            i++;
        } while (candidato.exists());
        
        return candidato;
    }
    
    /*
        Mueve un archivo o carpeta, usando renameTo y un fallback a copiar + eliminar
    */
    public static boolean Mover(String origen, String destinocarpeta) throws IllegalStateException, IOException {
        SesionRequerida();
        
        File src = file(origen);
        File dirdestino = file(destinocarpeta);
        
        if (!src.exists() || !dirdestino.exists() || !dirdestino.isDirectory()) {
            return false;
        }
        
        File destino = new File(dirdestino, src.getName());
        destino = src.isDirectory() ? EvitarColisionDirectorio(destino) : EvitarColision(destino);
        
        //Intento rapido (con el mismo volumen)
        if (src.renameTo(destino)) {
            return true;
        }
        
        //Fallback cross-volume (copiar y luego borrar)
        if (!CopiarRec(src, destino)) {
            return false;
        }
        
        return BorrarRec(src);
    }
}
