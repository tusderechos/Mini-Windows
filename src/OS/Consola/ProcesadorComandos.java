/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Consola;

/**
 *
 * @author Hp
 */

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import OS.Archivos.*;

public class ProcesadorComandos {
    
    //Directorio actual del usuario
    private String CurrentDir;
    
    public ProcesadorComandos() {
        CurrentDir = SistemaArchivo.getRutaUsuario();
    }
    
    public String getCurrentDir() {
        return CurrentDir;
    }
    
    /*
        Normaliza separadores
    */
    private String norm(String ruta) {
        String separador = File.separator;
        
        return ruta.replace("\\", separador).replace("/", separador);
    }
    
    /*
        Resuelve rutas relativas segun el dir actual
    */
    private String Resolver(String entrada) {
        if (entrada == null || entrada.isBlank()) {
            return CurrentDir;
        }
        
        String separador = File.separator;
        
        //Absoluta si empieza igual que la raiz del usuario
        String ruta = norm(SistemaArchivo.getRutaUsuario());
        String in = norm(entrada);
        
        if (in.startsWith(ruta)) {
            return in;
        }
        if (in.startsWith(separador)) {
            return ruta + in;
        }
        
        return CurrentDir + separador + in;
    }
    
    /*
        Ejecuta un comando estilo cmd y devuelve el texto a imprimir
    */
    public String Ejecutar(String linea) {
        if (linea == null || linea.isBlank()) {
            return "";
        }
        
        String[] partes = linea.trim().split("\\s+", 2);
        String cmd = partes[0].toLowerCase();
        String argumento = (partes.length > 1) ? partes[1] : null;
        
        try {
            switch (cmd) {
                case "mkdir":
                    return cmdMkdir(argumento);
                case "rm":
                    return cmdRm(argumento);
                case "cd":
                    return cmdCd(argumento);
                case "cd..":
                    return cmdCdUp();
                case "dir":
                    return cmdDir();
                case "date":
                    return LocalDate.now().toString();
                case "time":
                    return LocalTime.now().withNano(0).toString();
                default:
                    return "Comando desconocido: " + cmd;
            }
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) { //AQUI DEBERIA DE HABER UNA IOEXCEPTION
            return "IO Error: " + e.getMessage();
        }
    }
    
    
    private String cmdMkdir(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "Uso: mkdir <nombre>";
        }
        
        boolean ok = SistemaArchivo.CrearCarpeta(CurrentDir, nombre.trim());
        
        return ok ? "" : "No se pudo crear la carpeta";
    }
    
    private String cmdRm(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "Uso: rm <nombre>";
        }
        
        String objetivo = Resolver(nombre.trim());
        boolean ok = SistemaArchivo.Eliminar(objetivo);
        
        return ok ? "" : "No se pudo eliminar: " + objetivo;
    }
    
    private String cmdCd(String carpeta) {
        if (carpeta == null || carpeta.isBlank()) {
            //Si el cd no tiene ningun tipo de argumentos, va directamente a la raiz del usuario
            CurrentDir = norm(SistemaArchivo.getRutaUsuario());
            return CurrentDir;
        }
        
        String destino = Resolver(carpeta.trim());
        File dest = new File(destino);
        
        if (dest.exists() && dest.isDirectory()) {
            CurrentDir = dest.getAbsolutePath();
            return CurrentDir;
        }
        
        return "El sistema no puede encontrar la ruta especificada";
    }
    
    private String cmdCdUp() {
        File actual = new File(CurrentDir);
        File padre = actual.getParentFile();
        
        if (padre == null) {
            return CurrentDir; //Porque ya esta en la raiz fisica del volumen
        }
        
        //No permitir salir de la raiz del usuario
        String ruta = norm(SistemaArchivo.getRutaUsuario());
        String siguiente = padre.getAbsolutePath();
        
        if (!siguiente.startsWith(norm(ruta))) {
            CurrentDir = ruta;
        } else {
            CurrentDir = siguiente;
        }
        
        return CurrentDir;
    }
    
    private String cmdDir() {
        ResultadoListado rl = SistemaArchivo.ListarContenido(CurrentDir);
        StringBuilder sb = new StringBuilder();
        
        sb.append(" Directorio de ").append(CurrentDir).append("\n\n");
        
        //Las carpetas van primero
        for (Carpeta carpeta : rl.getCarpetas()) {
            sb.append("<DIR>    ").append(new File(carpeta.getRutaAbsoluta()).getName());
        }
        
        //Archivo
        for (Archivo archivo : rl.getArchivos()) {
            File file = new File(archivo.getRutaAbsoluta());
            sb.append(String.format("%-10d %s%n", file.length(), file.getName()));
        }
        
        return sb.toString();
    }
}