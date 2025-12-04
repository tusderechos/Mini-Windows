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
import OS.Archivos.Buscar.*;
import java.util.ArrayList;

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
        Relativiza ruta a la raiz del usuario (pa que se imprima todo tumbado todo tuani)
    */
    private String RelativizarARutaUsuario(String abs) {
        String ruta = norm(SistemaArchivo.getRutaUsuario());
        String nabs = norm(abs);
        
        if (nabs.startsWith(ruta)) {
            String relativizar = nabs.substring(ruta.length());
            
            if (relativizar.startsWith(File.separator)) {
                relativizar = relativizar.substring(1);
            }
            
            return relativizar.isEmpty() ? "." : relativizar;
        }
        
        return nabs;
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
                case "search":
                    return cmdSearch(argumento);
                case "copy":
                    return cmdCopy(argumento);
                case "move":
                    return cmdMove(argumento);
                default:
                    return "Comando desconocido: " + cmd;
            }
        } catch (IllegalStateException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) { 
            return "Error inesperado: " + e.getMessage();
        }
    }
    
    
    private String cmdMkdir(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "Uso: mkdir <nombre>";
        }
        
        boolean ok = SistemaArchivo.CrearCarpeta(CurrentDir, nombre.trim());
        
        return ok ? "Carpeta creada: " + nombre.trim() : "No se pudo crear la carpeta";
    }
    
    private String cmdRm(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return "Uso: rm <nombre>";
        }
        
        String objetivo = Resolver(nombre.trim());
        boolean ok = SistemaArchivo.Eliminar(objetivo);
        
        return ok ? "Eliminado: " + nombre.trim() : "No se pudo eliminar: " + objetivo;
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
    
    private String cmdSearch(String argumento) {
        if (argumento == null || argumento.isBlank()) {
            return "Uso: search <patron> [/r | -r]";
        }
        
        boolean recursivo = false;
        String patron = argumento.trim();
        
        //Ahora el odioso y increiblemente innecesario paso de que se pueda soportar /r o -r en cualquier posicion
        if (patron.contains(" /r")) {
            patron = patron.replace(" /r", "");
            recursivo = true;
        }
        if (patron.contains("/r ")) {
            patron = patron.replace("/r ", "");
            recursivo = true;
        }
        if (patron.endsWith("/r")) {
            patron = patron.substring(0, patron.length() - 2).trim();
            recursivo = true;
        }
        
        if (patron.contains(" -r")) {
            patron = patron.replace(" -r", "");
            recursivo = true;
        }
        if (patron.contains("-r ")) {
            patron = patron.replace("-r ", "");
            recursivo = true;
        }
        if (patron.endsWith("-r")) {
            patron = patron.substring(0, patron.length() - 2).trim();
            recursivo = true;
        }
        
        //Quitar las comillas si vienen
        patron = unquote(patron);
        
        ResultadoBusqueda res = BuscadorArchivos.Buscar(CurrentDir, patron, recursivo);
        
        ArrayList<String> lista = res.getCoincidencias();
        
        if (lista.isEmpty()) {
            return "No se encontraron coincidencias";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Resultados (").append(lista.size()).append(")").append(recursivo ? " [recursivo]" : "").append(":\n");
        
        for (String abs : lista) {
            sb.append(" - ").append(RelativizarARutaUsuario(abs)).append("\n");
        }
        
        return sb.toString();
    }
    
    private String cmdCopy(String argumentos) {
        if (argumentos == null || argumentos.isBlank()) {
            return "Uso: copy <origen> <destino>";
        }
        
        String[] partes = argumentos.split("\\s+");
        
        if (partes.length < 2) {
            return "Uso: copy <origen> <destino>";
        }
        
        String origen = Resolver(partes[0]);
        String destino = Resolver(partes[1]);
        
        try {
            boolean ok = SistemaArchivo.Copiar(origen, destino);
            
            return ok ? "Copiado" : "No se pudo copiar.";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private String cmdMove(String argumento) {
        if (argumento == null || argumento.isBlank()) {
            return "Uso: move <origen> <destino>";
        }
        
        String[] partes = argumento.split("\\s+");
        
        if (partes.length < 2) {
            return "Uso: move <origen> <destino>";
        }
        
        String origen = Resolver(partes[0]);
        String destino = Resolver(partes[1]);
        
        try {
            boolean ok = SistemaArchivo.Mover(origen, destino);
            
            return ok ? "Movido" : "No se pudo mover";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    private String unquote(String s) {
        if (s == null) {
            return null;
        }
        
        s = s.trim();
        
        if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1);
        }
        
        return s;
    }
}