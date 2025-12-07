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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class DesktopIcon extends JPanel implements MouseListener, MouseMotionListener {
    
    public final AppShortcut Shortcut;
    private final PosicionGuardar Guardar;
    private final DesktopIconsPanel Padre;
    private Point DragOffset;
    
    public DesktopIcon(AppShortcut Shortcut, PosicionGuardar Guardar, DesktopIconsPanel Padre) {
        this.Shortcut = Shortcut;
        this.Guardar = Guardar;
        this.Padre = Padre;
        
        setLayout(new BorderLayout(0, 4));
        setOpaque(false);
        setBorder(new EmptyBorder(6, 6, 6, 6));
        
        JLabel icono = new JLabel(Shortcut.getIcono(), SwingConstants.CENTER);
        
        JLabel texto = new JLabel(Shortcut.getNombre(), SwingConstants.CENTER);
        texto.setForeground(new Color(235, 235, 235));
        texto.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        add(icono, BorderLayout.CENTER);
        add(texto, BorderLayout.SOUTH);
        
        //Posicion inicial si existe
        Point punto = Guardar.get(Shortcut.getID());
        
        if (punto != null) {
            setLocation(punto);
        }
    }
    
    /*
        Guardar cada vez que se suelta
    */
    private void SavePosition() {
        Guardar.put(Shortcut.getID(), getLocation());
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
            Shortcut.getAccion().run();
        }
        
        if (SwingUtilities.isRightMouseButton(e)) {
            MostrarMenu(e);
        }
    }
    
    private void MostrarMenu(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem miabrir = new JMenuItem("Abrir");
        JMenuItem mieliminar = new JMenuItem("Eliminar acceso");
        
        miabrir.addActionListener(a -> Shortcut.getAccion().run());
        mieliminar.addActionListener(a -> Padre.RemoveShortcut(this));
        
        menu.add(miabrir);
        menu.addSeparator();
        menu.add(mieliminar);
        
        menu.show(this, e.getX(), e.getY());
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        DragOffset = e.getPoint();
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        if (DragOffset == null) {
            return;
        }
        
        Point punto = SwingUtilities.convertPoint(this, e.getPoint(), Padre);
        int nx = punto.x - DragOffset.x;
        int ny = punto.y - DragOffset.y;
        
        setLocation(Padre.snap(nx, ny));
        Padre.repaint();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        SavePosition();
    }

    /*
        Las siguientes 3 funciones me las pide el implements, pero no las uso al barro
    */
    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
