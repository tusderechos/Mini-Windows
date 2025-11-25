/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.apps.editor;

/**
 *
 * @author Hp
 */

import java.io.*;

public class EditorTextoCore {
    
    /*
        Guarda el objeto ArchivoTexto en binario
    */
    public static boolean GuardarComoBinario(ArchivoTexto doc, String rutabin) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutabin));
            oos.writeObject(doc);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /*
        Carga un ArchivoTexto desde binario
    */
    public static ArchivoTexto AbrirDesdeBinario(String rutabin) {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutabin));
            Object obj = ois.readObject();
            return (obj instanceof ArchivoTexto) ? (ArchivoTexto) obj : null;
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
    
    /*
        Guarda solo el contenido como .txt plano y sin ningun formato
    */
    public static boolean GuardarComoTxt(ArchivoTexto doc) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(doc.getRutaAbsoluta()));
            bw.write(doc.getContenido());
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    /*
        Abre un .txt plano a un ArchivoTexto nuevo (formato por defecto)
    */
    public static ArchivoTexto AbrirTxt(String rutatxt, String nombre) {
        StringBuilder sb = new StringBuilder();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(rutatxt));
            String linea;
            
            while((linea = br.readLine()) != null) {
                sb.append(linea).append(System.lineSeparator());
            }
        } catch (IOException e) {
            return null;
        }
        
        ArchivoTexto at = new ArchivoTexto(nombre, rutatxt);
        at.setContenido(sb.toString());
        
        return at;
    }
}
