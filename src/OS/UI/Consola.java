/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.UI;

/**
 *
 * @author Hp
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalTime;

import OS.UI.util.TemaOscuro;
import OS.Consola.ProcesadorComandos;

public class Consola extends JFrame {
    
    private final ProcesadorComandos shell;
    private final JTextArea AreaSalida = new JTextArea();
    private final JTextField FieldDentro = new JTextField();
    private final JLabel LblPrompt = new JLabel();
    
    private final HistorialComandos Historial = new HistorialComandos();
    
    public Consola() {
        shell = new ProcesadorComandos();
        
        setTitle("Mini-Windows - Consola");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(900, 550);
        getContentPane().setBackground(TemaOscuro.BG);
        
        AreaSalida.setBackground(TemaOscuro.BG);
        AreaSalida.setForeground(TemaOscuro.TEXTO);
        
        FieldDentro.setBackground(TemaOscuro.CARD);
        FieldDentro.setForeground(TemaOscuro.TEXTO);
        
        LblPrompt.setForeground(TemaOscuro.SUTIL);
        
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(720, 420));
        
        AplicarLook();
        
        //Output
        AreaSalida.setEditable(false);
        AreaSalida.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        AreaSalida.setMargin(new Insets(8, 10, 8, 10));
        
        JScrollPane scroll = new JScrollPane(AreaSalida);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        
        //Panel de prompts (el comando a ejecutar)
        JPanel abajo = new JPanel(new BorderLayout(8, 8));
        abajo.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));
        abajo.setBackground(Color.BLACK);
        
        LblPrompt.setFont(new Font("Consolas", Font.PLAIN, 13));
        LblPrompt.setForeground(Color.WHITE);
        
        abajo.add(LblPrompt, BorderLayout.WEST);
        
        FieldDentro.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        
        abajo.add(FieldDentro, BorderLayout.CENTER);
        
        
        setLayout(new BorderLayout());
        add(scroll, BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);
        
        //Bienvenida y prompt inicial
        ImprimirBienvenida();
        RefrescarPrompt();
        
        //Listeners
        FieldDentro.addActionListener(e -> EjecutarLinea());
        FieldDentro.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    FieldDentro.setText(Historial.prev());
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    FieldDentro.setText(Historial.siguiente());
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_L && e.isControlDown()) {
                    Limpiar();
                    e.consume();
                }
            }
        });
    }
    
    private void AplicarLook() {
        UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 13));
        UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));
        
        getContentPane().setBackground(Color.WHITE);
        
        AreaSalida.setBackground(Color.BLACK);
        AreaSalida.setForeground(Color.WHITE);
        
        FieldDentro.setBackground(Color.BLACK);
        FieldDentro.setForeground(Color.WHITE);
    }
    
    private void ImprimirBienvenida() {
        AnadirLinea("(c) Patito Corporation. Todos los derechos reservados.\n\n");
        AnadirLinea("(Escribe 'help' para ideas; 'cls' para limpiar)");
        AnadirLinea("");
    }
    
    private void RefrescarPrompt() {
        LblPrompt.setText(SafePrompt(shell.getCurrentDir()) + ">");
    }
    
    private String SafePrompt(String ruta) {
        //Aqui lo tengo como que solo muestre la ultima parte del prompt si llega a ser muy larga
        if (ruta == null) {
            return "";
        }
        
        String s = ruta.replace('\\', '/');
        
        if (s.length() <= 60) {
            return s;
        }
        
        int ultimo = s.lastIndexOf('/');
        
        return (ultimo > 0 ? ".../" + s.substring(ultimo + 1) : s);
    }
    
    private void EjecutarLinea() {
        String input = FieldDentro.getText().trim();
        
        if (input.isBlank()) {
            return;
        }
        
        Historial.add(input);
        
        //Imprimir el comando con hora
        String ts = LocalTime.now().withNano(0).toString();
        AnadirLinea("[" + ts + "] " + LblPrompt.getText() + " " + input);
        
        //Comandos locales
        if (input.equalsIgnoreCase("cls")) {
            Limpiar();
            FieldDentro.setText("");
            RefrescarPrompt();
            return;
        }
        
        if (input.equalsIgnoreCase("help")) {
            MostrarAyuda();
            FieldDentro.setText("");
            RefrescarPrompt();
            return;
        }
        
        String salida;
        
        try {
            salida = shell.Ejecutar(input);
        } catch (Exception e) {
            salida = "Error: " + e.getMessage();
        }
        
        if (salida != null && !salida.isBlank()) {
            AnadirLinea(salida);
        }
        
        AnadirLinea(""); //Separador visual
        
        //Por si cambio con cd
        RefrescarPrompt();
        FieldDentro.setText("");
        FieldDentro.requestFocusInWindow();
    }
    
    private void MostrarAyuda() {
        AnadirLinea("Comandos disponibles:");
        AnadirLinea(" mkdir <nombre>            Crea Carpeta");
        AnadirLinea(" rm    <nombre>            Elimina Archivo/Carpeta");
        AnadirLinea(" cd    [ruta]              Cambia de Directorio (sin argumentos: raiz del usuario)");
        AnadirLinea(" cd..                      Sube un Nivel (sin salir de la raiz del usuario)");
        AnadirLinea(" dir                       Lista Contenido");
        AnadirLinea(" copy <origen> <destino>   Copiar Archivo/Carpeta a Carpeta Destino");
        AnadirLinea(" move <origen> <destino>   Mueve Archivo/Carpeta a Carpeta Destino");
        AnadirLinea(" search <patron> [-r]      Busca Coincidencias (con -r recursivo)");
        AnadirLinea(" date | time               Fecha/Hora");
        AnadirLinea(" cls                       Limpia Pantalla");
    }
    
    private void Limpiar() {
        AreaSalida.setText("");
    }
    
    private void AnadirLinea(String s) {
        AreaSalida.append(s == null ? "" : s);
        AreaSalida.append("\n");
        AreaSalida.setCaretPosition(AreaSalida.getDocument().getLength());
    }
}
