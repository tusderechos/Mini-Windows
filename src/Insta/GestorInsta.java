/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import Compartidas.Constantes;
import Compartidas.Usuario;
import static Compartidas.Constantes.RUTA_BASE;
import Compartidas.ManejoUsuarios;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author HP
 */
public class GestorInsta {
    public static void crearNuevaCuenta(Usuario nuevoUsuario) throws UsernameYaExiste, IOException{
        String username = nuevoUsuario.getNombreUsuario();
        ManejoUsuarios manager = new ManejoUsuarios();
        if(!manager.UsernameDisponible(username)){
            throw new UsernameYaExiste("El usernanme "+username+" no esta disponible");
        }
    
        boolean guardadoExitoso = manager.Agregar(nuevoUsuario);
        if(!guardadoExitoso){
            throw new IOException("Fallo la registrar usuario en users.ins");
        }
        
        /*if(ManejoArchivosBinarios.existeUsername(username)){
            throw new UsernameYaExiste("El usernanme "+username+" no esta disponible");
        } 
        
        try{
            ManejoArchivosBinarios.escribirUsuario(nuevoUsuario);
        }catch(IOException e){
            System.err.println("Error al guardar el usuario: "+e.getMessage());
            throw new IOException("Fallo la registrar usuario en users.ins");
        }*/
        
        boolean estructuraCreada = GestorSistemaArchivos.crearEstructuraUsuario(username);
        
        if(!estructuraCreada){
            System.err.println("Error al crear estructura de carpetas para "+username);
            throw new IOException("La creacion de carpetas (Z:\\"+username+") ha fallado.");
        }
    }
    
    /*public static void agregarFollow(String seguidor, String seguido) throws IOException {
        String rutaFollowing = Constantes.RUTA_BASE+seguidor+"\\following.ins";
        Follow followNuevo = new Follow(seguido);
        ManejoArchivosBinarios.escribirFollow(rutaFollowing, followNuevo);
        
        String rutaFollowers = Constantes.RUTA_BASE+seguido+"\\followers.ins";
        Follow followerNuevo = new Follow(seguidor);
        ManejoArchivosBinarios.escribirFollow(rutaFollowers, followerNuevo);
        
        System.out.println(seguidor+" ahora sigue a "+seguido);
    }*/
    
    public static ArrayList<Insta> generarTimeLine(String usuarioActual) throws IOException{
        ArrayList<Insta> timeLine = new ArrayList<>();
        
        String rutaFollowing = Constantes.RUTA_BASE+usuarioActual+"\\following.ins";
        ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
        
        for(Follow f : seguidos){
            if(f.isActivo()){
                /*ArrayList<Insta> instasSeguido = ManejoArchivosBinarios.leerInstasDeUsuario(f.getUsername());
                timeLine.addAll(instasSeguido);*/
                
                timeLine.addAll(ManejoArchivosBinarios.leerInstasDeUsuario(f.getUsername()));
            }
        }
        
        ArrayList<Insta> instasPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual);
        timeLine.addAll(instasPropios);
        
        java.util.Collections.sort(timeLine);
        return timeLine;
    }
    
    public static ArrayList<Insta> obtenerInstasConMenciones(String usuarioActual){
        ArrayList<Insta> instasMencionados = new ArrayList<>();
        String mencionBuscada = "@"+usuarioActual.toLowerCase();
        
        try{
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
            
            for(Usuario u : todosLosUsuarios){
                if(!u.isActivo()){
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getNombreUsuario());
                for(Insta i : instasDelUsuario){
                    if(i.getContenido().toLowerCase().contains(mencionBuscada)){
                        instasMencionados.add(i);
                    }
                }
            }
            
        } catch(IOException e){
            System.err.println("Error al acceder a los archivos para buscar menciones: "+e.getMessage());
        }
        java.util.Collections.sort(instasMencionados);
        return instasMencionados;
    }
    
    public static ArrayList<Insta> buscarInstasPorHashtag(String hashtag){
        ArrayList<Insta> instasEncontrados = new ArrayList<>();
        String hashtagBuscado = "#"+hashtag.toLowerCase();
        
        try{
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
            
            for(Usuario u : todosLosUsuarios){
                if(!u.isActivo()){
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getNombreUsuario());
                for(Insta i : instasDelUsuario){
                    if(i.getContenido().toLowerCase().contains(hashtagBuscado)){
                        instasEncontrados.add(i);
                    }
                }
            }
        } catch(IOException e){
            System.err.println("Error al acceder a lso archivos para buscar hashtags: "+e.getMessage());
        }
        return instasEncontrados;
    }
    
    public static boolean actualizarEstadoCuenta(String username) throws IOException{
        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
        
        boolean estadoCambiado = false;
        boolean nuevoEstado = false;
        
        for(Usuario u : listaUsuarios){
            if(u.getNombreUsuario().equalsIgnoreCase(username)){
                if(u.isActivo()){
                    u.setActivo(false);
                    nuevoEstado =  false;
                    System.out.println("Cuenta de "+username+" Desactivada");
                } else{
                    u.setActivo(true);
                    nuevoEstado = true;
                    System.out.println("Cuenta de "+username+" Activada automaticamente");
                }
                estadoCambiado = true;
                break;
            }
        }
        if(!estadoCambiado){
            throw new IOException("Usuario no encontrado para la actualizacion");
        }
        ManejoArchivosBinarios.reescribirTodosLosUsuarios(listaUsuarios);
        return nuevoEstado;
    }
    
    public static int contarFollows(String username, boolean esFollowing) throws IOException{
        String nombreArchivo = esFollowing ? "following.ins" : "followers.ins";
        String rutaArchivo = Constantes.RUTA_BASE+username+"\\"+nombreArchivo;
        ArrayList<Follow> lista = ManejoArchivosBinarios.leerListaFollows(rutaArchivo);
        
        int contador = 0;
        
        for(Follow f : lista){
            if(f.isActivo()){
                contador++;
            }
        }
        return contador;
    }
    
    public static void actualizarEstadoFollow(String seguidor, String seguido, boolean estado) throws IOException{
        String rutaFollowing = Constantes.RUTA_BASE+seguidor+"\\following.ins";
        ArrayList<Follow> followsExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
        
        boolean encontrado = false;
        for(Follow f : followsExistentes){
            if(f.getUsername().equals(seguido)){
                f.setActivo(estado);
                encontrado = true;
                break;
            }
        }
        if(!encontrado && estado){
            followsExistentes.add(new Follow(seguido));
        }
        
        ManejoArchivosBinarios.reescribirFollows(rutaFollowing, followsExistentes);
        
        String rutaFollowers = Constantes.RUTA_BASE+seguido+"\\followers.ins";
        ArrayList<Follow> followersExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowers);
        
        encontrado = false;
        for(Follow f : followersExistentes){
            if(f.getUsername().equals(seguidor)){
                f.setActivo(estado);
                encontrado = true;
                break;
            }
        }
        if(!encontrado && estado){
            followsExistentes.add(new Follow(seguidor));
        }
        
        ManejoArchivosBinarios.reescribirFollows(rutaFollowers, followersExistentes);
    }
    
    public static ArrayList<Usuario> buscarPersonas(String textoBusqueda, String usuarioLogueado){
        ArrayList<Usuario> resultados = new ArrayList<>();
        String busqueda = textoBusqueda.toLowerCase();
        
        try{
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
            
            String rutaFollowing = Constantes.RUTA_BASE+usuarioLogueado+"\\following.ins";
            ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
            
            for(Usuario u : todosLosUsuarios){
                boolean contieneTexto = u.getNombreUsuario().toLowerCase().contains(busqueda);
                
                boolean estaActivo = u.isActivo();
                boolean diferenteUser = !u.getNombreUsuario().equalsIgnoreCase(usuarioLogueado);
                
                if(contieneTexto && estaActivo && diferenteUser){
                    resultados.add(u);
                }
            }
            
        } catch(IOException e){
            System.err.println("Error al leer los usuarios para la busqueda: "+e.getMessage());
        }
        return resultados;
    }
    
    public static boolean estaSiguiendo(String seguidor, String seguido)throws IOException{
        String rutaFollowing = Constantes.RUTA_BASE+seguidor+"\\following.ins";
        ArrayList<Follow> listaFollowing = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);
        
        for(Follow f : listaFollowing){
            if(f.getUsername().equalsIgnoreCase(seguido) && f.isActivo()){
                return true;
            }
        }
        return false;
    }
    
    public static DatosPerfil obtenerPefilCompleto(String perfilUsername, String usuarioLogueado)throws PerfilNoEncontrado, IOException{
        Usuario perfil = null;
        ArrayList<Usuario> todos = ManejoArchivosBinarios.leerTodosLosUsuarios();
        
        for(Usuario u : todos){
            if(u.getNombreUsuario().equalsIgnoreCase(perfilUsername) && u.isActivo()){
                perfil = u;
                break;
            }
        }
        if(perfil == null){
            throw new PerfilNoEncontrado("El perfil de "+perfilUsername+" no existe o esta desactivado");
        }
        
        int seguidores = contarFollows(perfilUsername, false);
        int seguidos = contarFollows(perfilUsername, true);
        
        boolean loSigue = false;
        if(!perfilUsername.equalsIgnoreCase(usuarioLogueado)){
            loSigue = estaSiguiendo(usuarioLogueado, perfilUsername);
        }
        
        ArrayList<Insta> instas = ManejoArchivosBinarios.leerInstasDeUsuario(perfilUsername);
        java.util.Collections.sort(instas);
        
        return new DatosPerfil(perfil, seguidores, seguidos, loSigue, instas);
    }
    
    public static final int MAX_CARACTERES_INSTA = 140;
    
    public static void publicarInsta(String username, String contenido) throws LongitudInstaInvalida, IOException{
        if(contenido.length() > MAX_CARACTERES_INSTA){
            throw new LongitudInstaInvalida("El contenido excede el maximo permitido. Limite: "+MAX_CARACTERES_INSTA, MAX_CARACTERES_INSTA);
        }
        
        Insta nuevoInsta = new Insta(username, contenido, "");
        String rutaCompletaArchivo = Constantes.RUTA_BASE+username+"\\instas.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoInsta, rutaCompletaArchivo);
    }
    
    public static Usuario logIn(String username, String password) throws CredencialesInvalidas, IOException{
        ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();
        
        for(Usuario u : todosLosUsuarios){
            if(u.getNombreUsuario().equalsIgnoreCase(username)){
                if(u.getContrasena().equalsIgnoreCase(password)){
                    if(u.isActivo()){
                        return u;
                    }else{
                        throw new CredencialesInvalidas("La cuenta de "+username+" esta desactivada.");
                    }
                }else{
                    throw new CredencialesInvalidas("Credenciales invalidas: Uusario o contraseña incorrectos.");
                }
            }
        }
        throw new CredencialesInvalidas("Credenciales invalidas: Uusario o contraseña incorrectos.");
    }
    
    public static void crearInsta(Insta nuevoPost) throws LongitudInstaInvalida, IOException{
        final int LONGITUD_MAXIMA = 140;
        if(nuevoPost.getTexto().length() > LONGITUD_MAXIMA){
            throw new LongitudInstaInvalida("El texto es demasiado largo."+nuevoPost.getTexto().length(), nuevoPost.getTexto().length());
        }
        
        String autor = nuevoPost.getAutorUsername();
        String rutaArchivoInstas = Constantes.RUTA_BASE+autor+"\\instas.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoPost, rutaArchivoInstas);
    }
}
