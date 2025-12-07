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
import java.awt.*;
import java.util.ArrayList;

public class DesktopIconsPanel extends JLayeredPane {
    
    private final int CellW = 96;
    private final int CellH = 108;
    private final int Margen = 16;
    private final PosicionGuardar Guardar;
    private final ArrayList<DesktopIcon> Items = new ArrayList<>();
    
    public DesktopIconsPanel(PosicionGuardar Guardar) {
        this.Guardar = Guardar;
        setOpaque(false);
        setLayout(null);
    }
    
    public void AddShorcut(AppShortcut shortcut) {
        DesktopIcon icono = new DesktopIcon(shortcut, Guardar, this);
        
        //Si no hay posicion previa, se buca la primera celda libre
        if (Guardar.get(shortcut.getID()) == null) {
            Point libre = PrimeraLibre();
            icono.setBounds(libre.x, libre.y, CellW, CellH);
            Guardar.put(shortcut.getID(), libre);
        } else {
            icono.setBounds(Guardar.get(shortcut.getID()).x, Guardar.get(shortcut.getID()).y, CellW, CellH);
        }
        
        Items.add(icono);
        add(icono, JLayeredPane.DEFAULT_LAYER);
        repaint();
    }
    
    public void RemoveShortcut(DesktopIcon icono) {
        Guardar.remove(icono.Shortcut.getID());
        Items.remove(icono);
        remove(icono);
        repaint();
    }
    
    public Point snap(int x, int y) {
        int gx = Math.max(Margen, Math.round((x - Margen) / (float) CellW) * CellW + Margen);
        int gy = Math.max(Margen, Math.round((y - Margen) / (float) CellH) * CellH + Margen);
        
        //Limites
        gx = Math.min(gx, Math.max(Margen, getWidth() - CellW - Margen));
        gy = Math.min(gy, Math.max(Margen, getHeight() - CellH - Margen));
        
        return new Point(gx, gy);
    }
    
    /*
        No el partido libre
    */
    private Point PrimeraLibre() {
        //Barrido columna/columna
        for (int y = Margen; y < getHeight() - CellH; y += CellH) {
            for (int x = Margen; x < getWidth()- CellW; y += CellW) {
                Rectangle celda = new Rectangle(x, y, CellW, CellH);
                boolean ocupado = Items.stream().anyMatch(i -> i.getBounds().intersects(celda));
                
                if (!ocupado) {
                    return new Point(x, y);
                }
            }
        }
        
        return new Point(Margen, Margen);
    }
    
    public void AlinearTodosAGrid() {
        for (DesktopIcon icon : Items) {
            Point s = snap(icon.getX(), icon.getY());
            icon.setLocation(s);
            Guardar.put(icon.Shortcut.getID(), s);
        }
        
        repaint();
    }
}
