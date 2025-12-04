/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import java.util.ArrayList;

public class HistorialComandos {
    
    private final ArrayList<String> Items = new ArrayList<>();
    private int indice = -1; //Es para apuntar despues del ultimo
    
    public void add(String cmd) {
        if (cmd == null || cmd.isBlank()) {
            return;
        }
        
        //Evita duplicados consecutivos
        if (Items.isEmpty() || !Items.get(Items.size() - 1).equals(cmd)) {
            Items.add(cmd);
        }
        
        indice = Items.size();
    }
    
    /*
        Flecha arriba
    */
    public String prev() {
        if (Items.isEmpty()) {
            return "";
        }
        
        indice = Math.max(0, indice - 1);
        
        return Items.get(indice);
    }
    
    /*
        Fecha abajo
    */
    public String siguiente() {
        if (Items.isEmpty()) {
            return "";
        }
        
        indice = Math.min(Items.size(), indice + 1);
        
        if (indice == Items.size()) {
            return ""; //Linea vacia al final
        }
        
        return Items.get(indice);
    }
    
    public void clear() {
        Items.clear();
        indice = -1;
    }
}
