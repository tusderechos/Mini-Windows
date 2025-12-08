/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import Compartidas.Constantes;
import Compartidas.Usuario;
import Compartidas.ManejoUsuarios;
import OS.Core.GestorCarpetasUsuario;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author HP
 */
public class GestorInsta {

    public static void crearNuevaCuenta(Usuario nuevoUsuario) throws UsernameYaExiste, IOException {
        String username = nuevoUsuario.getUsuario();

        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                throw new UsernameYaExiste("El username " + username + " no está disponible.");
            }
        }

        nuevoUsuario.setActivo(true);
        listaUsuarios.add(nuevoUsuario);

        try {
            ManejoArchivosBinarios.reescribirListaCompletaUsuarios(listaUsuarios);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar usuario: Falló la escritura en users.ins.");
            System.err.println("Error al guardar el usuario en users.ins: " + e.getMessage());
            throw new IOException("Fallo al registrar usuario en users.ins");
        }

        try {
            GestorCarpetasUsuario.CrearEstructuraUsuario(username);
            System.out.println("Estructura de archivos creada para: " + username);
        } catch (Throwable ignorar) {
            System.err.println("Advertencia: Falló la creación de la estructura de carpetas personales.");
        }

        System.out.println("Cuenta creada, debe abrir la ventana principal de INSTA.");
    }

    /*public static ArrayList<Insta> generarTimeLine(String usuarioActual) throws IOException {
        ArrayList<Insta> timeLine = new ArrayList<>();

        String rutaFollowing = Constantes.RUTA_BASE + usuarioActual + "\\following.ins";
        ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

        for (Follow f : seguidos) {
            if (f.isActivo()) {
                timeLine.addAll(ManejoArchivosBinarios.leerInstasDeUsuario(f.getUsername()));
            }
        }

        ArrayList<Insta> instasPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioActual);
        timeLine.addAll(instasPropios);

        java.util.Collections.sort(timeLine, java.util.Collections.reverseOrder());
        return timeLine;
    }*/
    
    public static ArrayList<Insta> generarTimeLine(String usuarioActual) throws IOException {
    ArrayList<Insta> timeLine = new ArrayList<>();
    
    Usuario usuarioLogueado = buscarUsuarioPorUsername(usuarioActual); 
    if (usuarioLogueado == null) {
        return timeLine; 
    }
    
    String rutaFollowing = Constantes.RUTA_BASE + usuarioActual + "\\following.ins";
    ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

    for (Follow f : seguidos) {
        if (f.isActivo()) {
            
            Usuario usuarioSeguido = buscarUsuarioPorUsername(f.getUsername());
            
            if (usuarioSeguido != null) {
                String clavePosts = usuarioSeguido.getUsuario(); 
                timeLine.addAll(ManejoArchivosBinarios.leerInstasDeUsuario(clavePosts));
            }
        }
    }

    ArrayList<Insta> instasPropios = ManejoArchivosBinarios.leerInstasDeUsuario(usuarioLogueado.getUsuario());
    timeLine.addAll(instasPropios);

    java.util.Collections.sort(timeLine, java.util.Collections.reverseOrder());
    return timeLine;
}

    public static ArrayList<Insta> buscarPorMencion(String usuarioActual) throws IOException {
        ArrayList<Insta> instasMencionados = new ArrayList<>();
        String mencionBuscada = "@" + usuarioActual.toLowerCase();

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            for (Usuario u : todosLosUsuarios) {
                if (!u.isActivo()) {
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getUsuario());
                for (Insta i : instasDelUsuario) {
                    if (i.getTexto().toLowerCase().contains(mencionBuscada)) {
                        instasMencionados.add(i);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error al acceder a los archivos para buscar menciones: " + e.getMessage());
        }
        java.util.Collections.sort(instasMencionados, java.util.Collections.reverseOrder());
        return instasMencionados;
    }

    public static ArrayList<Insta> buscarPorHashtag(String hashtag) throws IOException {
        ArrayList<Insta> instasEncontrados = new ArrayList<>();
        String hashtagBuscado = hashtag.toLowerCase();
        if (!hashtagBuscado.startsWith("#")) {
            hashtagBuscado = "#" + hashtagBuscado;
        }

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            for (Usuario u : todosLosUsuarios) {
                if (!u.isActivo()) {
                    continue;
                }
                ArrayList<Insta> instasDelUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(u.getUsuario());
                for (Insta i : instasDelUsuario) {
                    if (i.getContenido().toLowerCase().contains(hashtagBuscado)) {
                        instasEncontrados.add(i);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al acceder a los archivos para buscar hashtags: " + e.getMessage());
        }
        java.util.Collections.sort(instasEncontrados, java.util.Collections.reverseOrder());
        return instasEncontrados;
    }

    public static boolean actualizarEstadoCuenta(String username, boolean nuevoEstado) throws IOException {
        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

        boolean estadoCambiado = false;
        //boolean nuevoEstado = false;

        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                if (u.isActivo()) {
                    u.setActivo(false);
                    //nuevoEstado = false;
                    System.out.println("Cuenta de " + username + " Desactivada");
                } else {
                    u.setActivo(true);
                    //nuevoEstado = true;
                    System.out.println("Cuenta de " + username + " Activada automaticamente");
                }
                estadoCambiado = true;
                break;
            }
        }
        if (!estadoCambiado) {
            throw new IOException("Usuario no encontrado para la actualizacion");
        }
        ManejoArchivosBinarios.reescribirTodosLosUsuarios(listaUsuarios);
        return nuevoEstado;
    }

    public static int contarFollows(String username, boolean esFollowing) throws IOException {
        String nombreArchivo = esFollowing ? "following.ins" : "followers.ins";
        String rutaArchivo = Constantes.RUTA_BASE + username + File.separator + nombreArchivo;
        ArrayList<Follow> lista = ManejoArchivosBinarios.leerListaFollows(rutaArchivo);

        int contador = 0;

        for (Follow f : lista) {
            if (f.isActivo()) {
                contador++;
            }
        }
        return contador;
    }

    public static void actualizarEstadoFollow(String seguidor, String seguido, boolean estado) throws IOException {
        String rutaFollowing = Constantes.RUTA_BASE + seguidor + File.separator + "\\following.ins";
        ArrayList<Follow> followsExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

        boolean encontrado = false;
        for (Follow f : followsExistentes) {
            if (f.getUsername().equals(seguido)) {
                f.setActivo(estado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado && estado) {
            followsExistentes.add(new Follow(seguido));
        }

        File carpetaSeguidor = new File(Constantes.RUTA_BASE + seguidor);
        if (!carpetaSeguidor.exists()) {
            carpetaSeguidor.mkdirs();
        }
        ManejoArchivosBinarios.reescribirFollows(rutaFollowing, followsExistentes);

        String rutaFollowers = Constantes.RUTA_BASE + seguido + File.separator + "\\followers.ins";
        ArrayList<Follow> followersExistentes = ManejoArchivosBinarios.leerListaFollows(rutaFollowers);

        encontrado = false;
        for (Follow f : followersExistentes) {
            if (f.getUsername().equals(seguidor)) {
                f.setActivo(estado);
                encontrado = true;
                break;
            }
        }
        if (!encontrado && estado) {
            followsExistentes.add(new Follow(seguidor));
        }

        File carpetaSeguido = new File(Constantes.RUTA_BASE + seguido);
        if (!carpetaSeguido.exists()) {
            carpetaSeguido.mkdirs();
        }
        ManejoArchivosBinarios.reescribirFollows(rutaFollowers, followersExistentes);
    }

    public static ArrayList<Usuario> buscarPersonas(String textoBusqueda, String usuarioLogueado) {
        ArrayList<Usuario> resultados = new ArrayList<>();
        String busqueda = textoBusqueda.toLowerCase();

        try {
            ArrayList<Usuario> todosLosUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

            String rutaFollowing = Constantes.RUTA_BASE + usuarioLogueado + "\\following.ins";
            ArrayList<Follow> seguidos = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

            for (Usuario u : todosLosUsuarios) {
                boolean contieneTexto = u.getUsuario().toLowerCase().contains(busqueda);

                boolean estaActivo = u.isActivo();
                boolean diferenteUser = !u.getUsuario().equalsIgnoreCase(usuarioLogueado);

                if (contieneTexto && estaActivo && diferenteUser) {
                    resultados.add(u);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer los usuarios para la busqueda: " + e.getMessage());
        }
        return resultados;
    }

    public static boolean estaSiguiendo(String seguidor, String seguido) throws IOException {
        String rutaFollowing = Constantes.RUTA_BASE + seguidor + File.separator + "\\following.ins";
        if (!new File(rutaFollowing).exists()) {
            return false;
        }

        ArrayList<Follow> listaFollowing = ManejoArchivosBinarios.leerListaFollows(rutaFollowing);

        for (Follow f : listaFollowing) {
            if (f.getUsername().equalsIgnoreCase(seguido) && f.isActivo()) {
                return true;
            }
        }
        return false;
    }

    /*public static DatosPerfil obtenerPefilCompleto(String perfilUsername, String usuarioLogueado) throws PerfilNoEncontrado, IOException {
        Usuario perfil = null;
        ArrayList<Usuario> todos = ManejoArchivosBinarios.leerTodosLosUsuarios();

        for (Usuario u : todos) {
            if (u.getNombreUsuario().equalsIgnoreCase(perfilUsername) && u.isActivo()) {
                perfil = u;
                break;
            }
        }
        if (perfil == null) {
            throw new PerfilNoEncontrado("El perfil de " + perfilUsername + " no existe o esta desactivado");
        }

        int seguidores = contarFollows(perfilUsername, false);
        int seguidos = contarFollows(perfilUsername, true);

        boolean loSigue = false;
        if (!perfilUsername.equalsIgnoreCase(usuarioLogueado)) {
            loSigue = estaSiguiendo(usuarioLogueado, perfilUsername);
        }

        ArrayList<Insta> instas = ManejoArchivosBinarios.leerInstasDeUsuario(perfilUsername);
        java.util.Collections.sort(instas);

        return new DatosPerfil(perfil, seguidores, seguidos, loSigue, instas);
    }*/
    
    public static DatosPerfil obtenerPefilCompleto(String perfilUsername, String usuarioLogueado) throws PerfilNoEncontrado, IOException {
    Usuario perfil = null;
    ArrayList<Usuario> todos = ManejoArchivosBinarios.leerTodosLosUsuarios();

    // 🚨 CORRECCIÓN CLAVE: Buscar por el Nombre de Usuario (Alias)
    for (Usuario u : todos) {
        // Comparamos el perfilUsername (Alias recibido) con u.getNombreUsuario() (Alias guardado)
        if (u.getNombreUsuario().equalsIgnoreCase(perfilUsername) && u.isActivo()) { 
            perfil = u;
            break;
        }
    }
    
    if (perfil == null) {
        throw new PerfilNoEncontrado("El perfil de @" + perfilUsername + " no existe o esta desactivado");
    }

    // ---------------------------------------------------------------------------------
    // ⚠️ USO DE CLAVES:
    // Follows/Seguimiento usa el Username (Alias): perfilUsername
    // Lectura de Posts usa el Nombre Real (Clave de archivo): perfil.getUsuario()
    // ---------------------------------------------------------------------------------

    // Contadores de Follows (usan el Username)
    int seguidores = contarFollows(perfilUsername, false);
    int seguidos = contarFollows(perfilUsername, true);

    // Seguimiento (usa el Username)
    boolean loSigue = estaSiguiendo(usuarioLogueado, perfilUsername);
    if (!perfilUsername.equalsIgnoreCase(usuarioLogueado)) {
        loSigue = estaSiguiendo(usuarioLogueado, perfilUsername);
    }
    
    // 🚨 CORRECCIÓN CLAVE: Lectura de Posts (debe usar el Nombre Real/Clave de archivo)
    String claveArchivoPosts = perfil.getUsuario();
    ArrayList<Insta> instas = ManejoArchivosBinarios.leerInstasDeUsuario(claveArchivoPosts);
    java.util.Collections.sort(instas);

    return new DatosPerfil(perfil, seguidores, seguidos, loSigue, instas);
}

    public static final int MAX_CARACTERES_INSTA = 140;

    public static void publicarInsta(String username, String contenido) throws LongitudInstaInvalida, IOException {
        if (contenido.length() > MAX_CARACTERES_INSTA) {
            throw new LongitudInstaInvalida("El contenido excede el maximo permitido. Limite: " + MAX_CARACTERES_INSTA, MAX_CARACTERES_INSTA);
        }

        Insta nuevoInsta = new Insta(username, contenido, "");
        String rutaCompletaArchivo = Constantes.RUTA_BASE + username + "\\instas.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoInsta, rutaCompletaArchivo);
    }

    public static Usuario logIn(String username, String password) throws CredencialesInvalidas, IOException {
        ManejoUsuarios manager = new ManejoUsuarios();
        ArrayList<Compartidas.Usuario> todosLosUsuarios = manager.getUsuarios();
        if (todosLosUsuarios.isEmpty()) {
            System.err.println("La lista de usuarios está vacía.");
        }

        for (Compartidas.Usuario u : todosLosUsuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(username)
                    && u.getContrasena().equals(password)
                    && u.isActivo()) {

                return u;
            }
        }
        throw new CredencialesInvalidas("Credenciales inválidas: Usuario no encontrado.");

    }

    public static String copiarFotoPerfil(String username, String rutaOriginal, String extension) throws IOException {
        if (rutaOriginal == null || rutaOriginal.isEmpty()) {
            return null;
        }

        String rutaDestinoCarpeta = "Z" + File.separator + username;

        File carpeta = new File(rutaDestinoCarpeta);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }

        String nombreBase = "perfil";
        String nombreFinal = nombreBase + extension;
        Path origen = new File(rutaOriginal).toPath();
        Path destino = new File(rutaDestinoCarpeta + File.separator + nombreFinal).toPath();

        Files.copy(origen, destino, StandardCopyOption.REPLACE_EXISTING);

        return rutaDestinoCarpeta + File.separator + nombreFinal;
    }

    public static void crearInsta(Insta nuevoPost) throws LongitudInstaInvalida, IOException {
        final int LONGITUD_MAXIMA = 140;
        if (nuevoPost.getTexto().length() > LONGITUD_MAXIMA) {
            throw new LongitudInstaInvalida("El texto es demasiado largo." + nuevoPost.getTexto().length(), nuevoPost.getTexto().length());
        }

        String autor = nuevoPost.getAutorUsername();
        String rutaArchivoInstas = Constantes.RUTA_BASE + autor + "\\instas.ins";
        ManejoArchivosBinarios.escribirInsta(nuevoPost, rutaArchivoInstas);
    }

    public static void eliminarInsta(Insta postAEliminar) throws IOException {
        String username = postAEliminar.getAutorUsername();

        ArrayList<Insta> instasUsuario = ManejoArchivosBinarios.leerInstasDeUsuario(username);

        boolean eliminado = instasUsuario.removeIf(i
                -> i.getFechaPublicacion().equals(postAEliminar.getFechaPublicacion())
                && i.getRutaImg().equals(postAEliminar.getRutaImg()));

        if (!eliminado) {
            throw new IOException("No se encontró el post para eliminar.");
        }

        String rutaArchivoInstas = Constantes.RUTA_BASE + username + "\\instas.ins";
        ManejoArchivosBinarios.reescribirInstas(instasUsuario, rutaArchivoInstas);
    }

    public static void guardarComentario(Insta postComentado, Comentario nuevoComentario) throws IOException {
        String rutaArchivoComentarios = Constantes.RUTA_BASE
                + postComentado.getAutorUsername()
                + "\\comentarios_"
                + postComentado.getIdPost()
                + ".ins";

        ManejoArchivosBinarios.escribirComentario(nuevoComentario, rutaArchivoComentarios);
    }

    public static ArrayList<Comentario> leerComentarios(Insta postComentado) {
        String rutaArchivoComentarios = Constantes.RUTA_BASE
                + postComentado.getAutorUsername()
                + "\\comentarios_"
                + postComentado.getIdPost()
                + ".ins";

        return ManejoArchivosBinarios.leerComentariosDePost(rutaArchivoComentarios);
    }

    public static Usuario buscarUsuarioPorUsername(String username) {
        ArrayList<Usuario> listaUsuarios = ManejoArchivosBinarios.leerTodosLosUsuarios();

        for (Usuario u : listaUsuarios) {
            if (u.getNombreUsuario().equalsIgnoreCase(username)) {
                return u; 
            }
        }
        return null;
    }
}
