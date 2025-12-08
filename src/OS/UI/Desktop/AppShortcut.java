/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.Desktop;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.util.Objects;

public class AppShortcut {
    
    private final String ID;
    private final String Nombre;
    private final Icon Icono;
    private final Runnable Accion;
    
    public AppShortcut(String ID, String Nombre, Icon Icono, Runnable Accion) {
        this.ID = Objects.requireNonNull(ID);
        this.Nombre = Objects.requireNonNull(Nombre);
        this.Icono = Icono;
        this.Accion = Objects.requireNonNull(Accion);
    }

    public String getID() {
        return ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public Icon getIcono() {
        return Icono;
    }

    public Runnable getAccion() {
        return Accion;
    }
    
    
}
