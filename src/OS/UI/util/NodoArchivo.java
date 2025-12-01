/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

public class NodoArchivo extends DefaultMutableTreeNode {
    
    public final File file;
    public boolean cargado = false;
    
    public NodoArchivo(File file) {
        super(file);
        this.file = file;
    }
    
    @Override
    public String toString() {
        String nombre = file.getName();
        
        return (nombre == null || nombre.isBlank()) ? file.getAbsolutePath() : nombre;
    }
}
