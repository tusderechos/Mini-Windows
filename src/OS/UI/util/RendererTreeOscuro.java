/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI.util;

/**
 *
 * @author Hp
 */

import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

public class RendererTreeOscuro extends DefaultTreeCellRenderer {
    
    private final Icon IconoFolder = UIManager.getIcon("FileView.IconoDirectorio");
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        
        setBackgroundNonSelectionColor(TemaOscuro.BG);
        setTextNonSelectionColor(TemaOscuro.TEXTO);
        setBackgroundSelectionColor(new Color(70, 70, 75));
        setTextSelectionColor(TemaOscuro.TEXTO);
        setBorderSelectionColor(TemaOscuro.LINEA);
        
        setIcon(IconoFolder);
        
        return this;
    }
}
