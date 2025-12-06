/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import Compartidas.Constantes;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import Compartidas.Usuario;

/**
 *
 * @author HP
 */
public class ManejoArchivosBinarios {

    public static final String ARCHIVO_USUARIOS = "users.ins";

    public static void escribirUsuario(Usuario nuevoUsuario) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_USUARIOS, true); AppendingObjectOutputStream oos = new AppendingObjectOutputStream(fos)) {

            oos.writeObject(nuevoUsuario);
            System.out.println("usuario " + nuevoUsuario.getUsuario() + " escrito con exito");
        } catch (FileNotFoundException e) {
            System.err.println("archivo no encontrado: " + e.getMessage());
            throw e;
        }
    }

    public static void asegurarArchivoUsuario() {
        try {
            File archivo = new File(ARCHIVO_USUARIOS);
            if (!archivo.exists()) {
                System.out.println("Creando archivo users.ins por primera vez.");
                archivo.createNewFile();

                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                    oos.writeObject(new ArrayList<Compartidas.Usuario>());
                }
            }
        } catch (IOException e) {
            System.err.println("Advertencia: Fallo al asegurar la existencia de users.ins: " + e.getMessage());
        }
    }

    public static ArrayList<Usuario> leerTodosLosUsuarios() /*throws IOException*/ {
        ArrayList<Compartidas.Usuario> usuarios = new ArrayList<>();

        File archivo = new File(ARCHIVO_USUARIOS);
        if (!archivo.exists() || archivo.length() == 0) {
            return usuarios;
        }
        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            Object obj = ois.readObject();

            if (obj instanceof ArrayList) {
                usuarios = (ArrayList<Compartidas.Usuario>) obj;
            }

        } catch (FileNotFoundException e) {
            System.err.println("Advertencia: Archivo users.ins no encontrado.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error al leer users.ins: La clase de Usuario no fue encontrada.");
        } catch (IOException e) {
            System.err.println("Error de I/O al leer users.ins (posiblemente archivo corrupto): " + e.getMessage());
        }

        return usuarios;
    }

    public static boolean existeUsername(String username) {
        ArrayList<Usuario> listaUsuarios = leerTodosLosUsuarios();
        for (Usuario u : listaUsuarios) {
            if (u.getUsuario().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    public static void escribirInsta(Insta nuevoInsta, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        File directorioPadre = archivo.getParentFile();
        if (directorioPadre != null && !directorioPadre.exists()) {
            if (!directorioPadre.mkdirs()) {
                throw new IOException("Fallo al crear la estructura de directorios para el post.");
            }
        }

        if (!archivo.exists() || archivo.length() == 0) {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
                oos.writeObject(nuevoInsta);
            }
        } else {
            try (AppendingObjectOutputStream aoos = new AppendingObjectOutputStream(new FileOutputStream(archivo, true))) {
                aoos.writeObject(nuevoInsta);
            }
        }
    }

    public static ArrayList<Insta> leerInstasDeUsuario(String username) throws IOException {
        ArrayList<Insta> instas = new ArrayList<>();
        String rutaArchivo = Constantes.RUTA_BASE + username + "/instas.ins";
        File archivo = new File(rutaArchivo);

        if (!archivo.exists() || archivo.length() == 0) {
            return instas;
        }

        try (FileInputStream fis = new FileInputStream(rutaArchivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Insta insta = (Insta) ois.readObject();
                    instas.add(insta);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new IOException("Error de formato al leer Insta: " + e.getMessage(), e);
                }
            }
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw e;
        }
        return instas;
    }

    public static ArrayList<Follow> leerListaFollows(String rutaArchivo) throws IOException {
        ArrayList<Follow> follows = new ArrayList();

        File archivo = new File(rutaArchivo);
        if (!archivo.exists() || archivo.length() == 0) {
            return follows;
        }

        try (FileInputStream fis = new FileInputStream(rutaArchivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Follow follow = (Follow) ois.readObject();
                    follows.add(follow);
                } catch (EOFException e) {
                    break;
                } catch (ClassNotFoundException e) {
                    throw new IOException("Error de formato en archivo binario: " + e.getMessage(), e);
                }
            }

        }
        return follows;
    }

    public static void reescribirFollows(String rutaArchivo, ArrayList<Follow> follows) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo, false); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Follow f : follows) {
                oos.writeObject(f);
            }
        }
    }

    public static void reescribirTodosLosUsuarios(ArrayList<Usuario> listaUsuarios) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_USUARIOS, false); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Usuario u : listaUsuarios) {
                oos.writeObject(u);
            }

        } catch (FileNotFoundException e) {
            System.err.println("Archivo users.ins no encontrado para reescritura");
            throw e;
        }
    }

    public static void reescribirListaCompletaUsuarios(ArrayList<Usuario> listaUsuarios) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_USUARIOS, false); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(listaUsuarios);

        } catch (FileNotFoundException e) {
            System.err.println("Archivo users.ins no encontrado para reescritura: " + e.getMessage());
            throw e;
        }
    }

    public static void reescribirInstas(ArrayList<Insta> instas, String rutaArchivo) throws IOException {
        File archivo = new File(rutaArchivo);

        if (instas.isEmpty()) {
            if (archivo.exists()) {
                archivo.delete();
                System.out.println("Archivo de instas eliminado: " + rutaArchivo);
            }
            return;
        }

        try (FileOutputStream fos = new FileOutputStream(archivo); ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            for (Insta post : instas) {
                oos.writeObject(post);
            }
            System.out.println("Archivo de instas reescrito exitosamente: " + rutaArchivo);

        } catch (IOException e) {
            System.err.println("Error al reescribir la lista de Instas en " + rutaArchivo + ": " + e.getMessage());
            throw e;
        }
    }

    public static void escribirComentario(Comentario nuevoComentario, String rutaArchivoComentarios) throws IOException {
        File archivo = new File(rutaArchivoComentarios);

        boolean append = archivo.exists() && archivo.length() > 0;

        try (FileOutputStream fos = new FileOutputStream(archivo, true); 
                 ObjectOutputStream oos = append ? new AppendingObjectOutputStream(fos) : new ObjectOutputStream(fos)) {

            oos.writeObject(nuevoComentario);
            System.out.println("Comentario escrito en: " + rutaArchivoComentarios);

        } catch (IOException e) {
            System.err.println("Error al escribir el comentario en " + rutaArchivoComentarios + ": " + e.getMessage());
            throw e;
        }
    }
    
    public static ArrayList<Comentario> leerComentariosDePost(String rutaArchivoComentarios) {
        ArrayList<Comentario> comentarios = new ArrayList<>();
        File archivo = new File(rutaArchivoComentarios);

        if (!archivo.exists() || archivo.length() == 0) {
            return comentarios;
        }

        try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

            while (true) {
                try {
                    Comentario comentario = (Comentario) ois.readObject();
                    comentarios.add(comentario);
                } catch (EOFException e) {
                    break;
                }
            }
            System.out.println("Comentarios leídos exitosamente de: " + rutaArchivoComentarios);

        } catch (IOException e) {
            System.err.println("Error de E/S al leer comentarios: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Clase Comentario no encontrada: " + e.getMessage());
        }

        return comentarios;
    }
}
