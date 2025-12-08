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
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import javax.swing.filechooser.FileNameExtensionFilter;

import OS.UI.util.TemaOscuro;
import OS.UI.util.BotonesBarra;
import OS.UI.util.GradientWallpaper;
import OS.UI.util.Mp3Info;
import OS.UI.util.Mp3PlayerThread;
import OS.UI.util.Mp3Utils;
import OS.UI.util.NeonSliderUI;
import OS.UI.util.VolumeControl;
import OS.utils.MusicaImporter;
import OS.utils.RutasUsuario;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.event.ChangeListener;

public class ReproductorMusica extends JFrame {
    
    private boolean EndLatch = false;
    private Timer UITimer;
    private long IgnorarFinHasta = 0L;
    
    private volatile boolean BuscandoConSlider = false;
    
    private final DefaultListModel<File> Modelo = new DefaultListModel<>();
    private final JList<File> Lista = new JList<>(Modelo);
    
    private final JLabel LblNow = new JLabel("-");
    private final JLabel LblTime = new JLabel("00:00 / 00:00");
    private final JSlider Seek = new JSlider(0, 1000, 0);
    private final JSlider Volumen = new JSlider(0, 100, 80);
    
    private final JLabel LblVolPct = new JLabel("80%");
    
    private JLabel cover = new JLabel("Sin caratula", SwingConstants.CENTER);
    private BufferedImage CoverBaseIcon;
    
    private final BotonesBarra BtnPrev = new BotonesBarra("Anterior", "icons/20/prev.png", this::Anterior);
    private final BotonesBarra BtnPlay = new BotonesBarra("Play/Pausa", "icons/20/play.png", this::PlayPause);
    private final BotonesBarra BtnNext = new BotonesBarra("Siguiente", "icons/20/next.png", this::Siguiente);
        
    private final JToggleButton BtnShuffle = CrearToggle();
    private final JToggleButton BtnRepetir = CrearToggle();
    
    private Mp3PlayerThread Hilo;
    private Mp3Info InfoActual;
    
    private final Icon prev26, prev30, next26, next30, shuffleon26, shuffleoff26, shuffleon30, shuffleoff30;
    private final Icon play26, play30, pause26, pause30, repetiron26, repetiroff26, repetiron30, repetiroff30;
    
    private Icon PlayNormal, PlayHover;
    private boolean isPlayingVisual = false;
    
    private int Indice = -1;
    private final Random random = new Random();
    
    private JLabel LblHint = new JLabel("Selecciona una cancion para empezar", SwingConstants.CENTER);
    
    private boolean UINeonInstalado = false;
    
    public ReproductorMusica() {
        super("Mini-Windows - Reproductor de Musica");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1060, 680);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(TemaOscuro.BG);
        
        //Top
        GradientWallpaper top = new GradientWallpaper();
        top.setPreferredSize(new Dimension(0, 45));
        top.setGradient(new Color(35, 35, 40), Color.MAGENTA.darker().darker());
        top.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(70, 70, 70)));
        
        JButton BtnAbrir = CrearBoton("Reproducir Cancion", true, 15);
        JButton BtnCarpeta = CrearBoton("Reproducir Carpeta", false, 15);
        JButton BtnBiblioteca = CrearBoton("Biblioteca", true, 15);
        JButton BtnImportar = CrearBoton("Importar", false, 15);
        JButton BtnExportar = CrearBoton("Exportar", false, 15);
        
        JLabel lblvolumen = new JLabel("Volumen ");
        lblvolumen.setFont(lblvolumen.getFont().deriveFont(Font.BOLD, 14));
        lblvolumen.setForeground(TemaOscuro.TEXTO);
        
        
        top.add(BtnAbrir);
        top.add(BtnCarpeta);
        top.add(Box.createHorizontalStrut(4));
        top.add(BtnBiblioteca);
        top.add(BtnImportar);
        top.add(BtnExportar);
        top.add(Box.createHorizontalStrut(16));
        top.add(lblvolumen);
        
        Volumen.setPreferredSize(new Dimension(180, 22));
        top.add(Volumen);
        
        top.add(Box.createHorizontalStrut(6));
        LblVolPct.setForeground(TemaOscuro.TEXTO);
        LblVolPct.setFont(LblVolPct.getFont().deriveFont(Font.BOLD, 14));
        top.add(LblVolPct);
        
        Volumen.addChangeListener(e -> {
            int vol = Volumen.getValue();
            VolumeControl.setVolumen(vol / 100.0);
            
            if (!Volumen.getValueIsAdjusting()) {
                LblVolPct.setText(vol + "%");
            }
        });
        
        add(top, BorderLayout.NORTH);
        
        //Centro
        JPanel centro = new JPanel();
        centro.setLayout(new BorderLayout());
        centro.setOpaque(true);
        centro.setBackground(TemaOscuro.BG);
        
        //Cover
        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBackground(TemaOscuro.CARD);
        izquierda.setBorder(new EmptyBorder(12, 12, 12, 8));
        
        cover.setForeground(TemaOscuro.SUTIL);
        cover.setHorizontalAlignment(SwingConstants.CENTER);
        cover.setVerticalAlignment(SwingConstants.CENTER);
        cover.setBorder(new EmptyBorder(20, 20, 20, 20));
        cover.setOpaque(false);
        
        LblHint.setForeground(TemaOscuro.SUTIL);
        LblHint.setBorder(new EmptyBorder(10, 0, 8, 0));
        
        izquierda.add(cover, BorderLayout.CENTER);
        izquierda.add(LblHint, BorderLayout.SOUTH);
        
        izquierda.setMinimumSize(new Dimension(220, 140));
        izquierda.setPreferredSize(new Dimension(320, 240));
        
        //Lista (derecha)
        Lista.setBackground(TemaOscuro.CARD);
        Lista.setForeground(TemaOscuro.TEXTO);
        Lista.setSelectionBackground(new Color(0x4C8DFF));
        Lista.setSelectionForeground(Color.WHITE);
        Lista.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        Lista.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof File) {
                    setText(((File) value).getName()); //Para no tener toda la ruta sino que solo el nombre
                }
                
                setBorder(new EmptyBorder(6, 10, 6, 10));
                
                return c;
            }
        });
        
        JScrollPane scrollderecha = new JScrollPane(Lista);
        scrollderecha.setBorder(BorderFactory.createEmptyBorder());
        scrollderecha.getViewport().setBackground(TemaOscuro.CARD);
        
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, scrollderecha);
        split.setDividerSize(4);
        split.setContinuousLayout(true);
        split.setBorder(null);
        split.setBackground(TemaOscuro.BG);
        split.setResizeWeight(0.35);
        split.setEnabled(false);
                        
        centro.add(split, BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(() -> split.setDividerLocation(0.35));
        
        add(centro, BorderLayout.CENTER);
        
        //Toda la parte de abajo
        GradientWallpaper bottom = new GradientWallpaper();
        bottom.setPreferredSize(new Dimension(0, 160));
        bottom.setGradient(Color.MAGENTA.darker().darker().darker(), new Color(10, 10, 15).brighter().brighter());
        bottom.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 70, 70)));
        
        //Info
        JPanel info = new JPanel();
        info.setLayout(new BorderLayout());
        info.setOpaque(false);
        
        LblNow.setForeground(TemaOscuro.TEXTO);
        LblNow.setFont(LblNow.getFont().deriveFont(Font.BOLD, 14));
        LblNow.setBorder(new EmptyBorder(6, 10, 4, 10));
        
        info.add(LblNow, BorderLayout.WEST);
        
        LblTime.setForeground(TemaOscuro.SUTIL);
        LblTime.setFont(LblTime.getFont().deriveFont(Font.BOLD, 14));
        LblTime.setBorder(new EmptyBorder(6, 10, 4, 10));
        
        info.add(LblTime, BorderLayout.EAST);
        
        bottom.add(info, BorderLayout.NORTH);
        
        Seek.setBackground(TemaOscuro.BAR);
        Seek.setOpaque(false);
        Seek.setBorder(null);
        Seek.setPreferredSize(new Dimension(360, 24));
        Seek.setPaintTicks(false);
        Seek.setPaintLabels(false);
        
        Volumen.setBackground(TemaOscuro.BAR);
        Volumen.setOpaque(false);
        Volumen.setBorder(null);
        Volumen.setPreferredSize(new Dimension(160, 18));
        Volumen.setPaintTicks(false);
        Volumen.setPaintLabels(false);
        
        if (!UINeonInstalado) {
            try {
                Seek.updateUI();
                Volumen.updateUI();
                Seek.setUI(new NeonSliderUI(Seek));
                Volumen.setUI(new NeonSliderUI(Volumen));
                UINeonInstalado = true;
            } catch (Throwable ignorar) {
            }
        }
        
        //Barra de seek        
        JPanel panelseek = new JPanel();
        panelseek.setLayout(new BorderLayout());
        panelseek.setOpaque(false);
        panelseek.setBorder(new EmptyBorder(0, 10, 6, 10));
        
        panelseek.add(Seek, BorderLayout.CENTER);
        
        bottom.add(panelseek, BorderLayout.CENTER);
        
        //Controles
        JPanel controles = new JPanel();
        controles.setLayout(new FlowLayout(FlowLayout.CENTER, 22, 12));
        controles.setOpaque(false);
        
        BtnPrev.setPreferredSize(new Dimension(100, 100));
        BtnPlay.setPreferredSize(new Dimension(100, 100));
        BtnNext.setPreferredSize(new Dimension(100, 100));
        
        BtnPrev.setText(null);
        BtnPlay.setText(null);
        BtnNext.setText(null);
        
        pause26 = LoadIcon("/icons/20/stop.png", 26);
        pause30 = LoadIcon("/icons/20/stop.png", 30);
                
        prev26 = LoadIcon("/icons/20/prev.png", 26);
        prev30 = LoadIcon("/icons/20/prev.png", 30);
        BtnPrev.setIcon(prev26);
        AplicarZoomHover(BtnPrev, prev26, prev30);
        
        play26 = LoadIcon("/icons/20/play.png", 26);
        play30 = LoadIcon("/icons/20/play.png", 30);
        
        next26 = LoadIcon("/icons/20/next.png", 26);
        next30 = LoadIcon("/icons/20/next.png", 30);
        BtnNext.setIcon(next26);
        AplicarZoomHover(BtnNext, next26, next30);
        
        shuffleon26 = LoadIconTint("/icons/20/shuffle.png", 26, Color.MAGENTA);
        shuffleon30 = LoadIconTint("/icons/20/shuffle.png", 30, Color.MAGENTA);
        shuffleoff26 = LoadIconTint("/icons/20/shuffle.png", 26, null);
        shuffleoff30 = LoadIconTint("/icons/20/shuffle.png", 30, null);
        BtnShuffle.setSelected(false);
        AplicarZoomHoverToggle(BtnShuffle, shuffleoff26, shuffleoff30, shuffleon26, shuffleon30);
        
        repetiron26 = LoadIconTint("/icons/20/repetir.png", 26, Color.MAGENTA);
        repetiron30 = LoadIconTint("/icons/20/repetir.png", 30, Color.MAGENTA);
        repetiroff26 = LoadIconTint("/icons/20/repetir.png", 26, null);
        repetiroff30 = LoadIconTint("/icons/20/repetir.png", 30, null);
        BtnRepetir.setSelected(false);
        AplicarZoomHoverToggle(BtnRepetir, repetiroff26, repetiroff30, repetiron26, repetiron30);
        
        setPlayVisual(false);

        AplicarZoomHoverPlay(BtnPlay);
        
        controles.removeAll();
                
        controles.add(BtnShuffle);
        controles.add(BtnPrev);
        controles.add(BtnPlay);
        controles.add(BtnNext);
        controles.add(BtnRepetir);
        
        bottom.add(controles, BorderLayout.SOUTH);
        
        add(bottom, BorderLayout.SOUTH);
        
        //Acciones
        BtnAbrir.addActionListener(e -> AbrirArchivos());
        BtnCarpeta.addActionListener(e -> AbrirCarpeta());
        BtnBiblioteca.addActionListener(e -> AbrirBiblioteca());
        BtnImportar.addActionListener(e -> Importar());
        BtnExportar.addActionListener(e -> Exportar());
        
        InicializarEventos();
        
        setDefaultCover("/icons/caratula.png");
        
        izquierda.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                ReescalarCover();
            }
        });
        
        JButton[] botones = {BtnPrev, BtnPlay, BtnNext};
        for(JButton boton : botones) {
            boton.setFocusPainted(false);
            boton.setBorderPainted(false);
            boton.setContentAreaFilled(false);
            boton.setOpaque(false);
            boton.setBorder(new EmptyBorder(6, 10, 6, 10));
        }
    }
    
    private void InicializarEventos() {
        Lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Indice = Lista.getSelectedIndex();
                    Reproducir(Indice, 0);
                }
            }
        });
        
        
        Seek.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (InfoActual == null || Hilo == null) {
                    return;
                }
                
                BuscandoConSlider = true;
                Seek.setValueIsAdjusting(true);
                
                //Calcular fraccion segun el click en la pista
                int x = e.getX();
                int w = Seek.getWidth();
                
                if (w > 0) {
                    double frac = (w <= 0) ? 0 : (x / (double) w);
                    Seek.setValue((int) Math.round(frac * 1000.0)); //Mover visualmente al click
                    Seek.repaint();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                //Si el usuario de verdad arrastro, se cierra igual con un salto preciso
                if (InfoActual == null || Hilo == null) {
                    BuscandoConSlider = false;
                    Seek.setValueIsAdjusting(false);
                    return;
                }
                
                double frac = Math.max(0, Math.min(1, Seek.getValue() / 1000.0));
                int nuevoframe = (int) Math.round(frac * InfoActual.TotalFrames);
                nuevoframe = Math.max(0, Math.min(InfoActual.TotalFrames, nuevoframe));
                
                Hilo.SaltarA(nuevoframe);
                
                IgnorarFinHasta = System.currentTimeMillis() + 1000;
                
                Seek.setValueIsAdjusting(false);
                BuscandoConSlider = false;
                Seek.repaint();
                
                new Timer(120, ev -> {
                    BuscandoConSlider = false;
                    ((Timer) ev.getSource()).stop();
                }).start();
            }
        });
        
        //Mientras se arrastra, muestro un preview sin empujar el hilo
        Seek.addChangeListener(e -> {
            if (InfoActual == null) {
                return;
            }
            
            if (!Seek.getValueIsAdjusting()) {
                return;
            }
            
            int frameprev = (int) Math.round((Seek.getValue() / 1000.0) * InfoActual.TotalFrames);
            int msprev = (int) Math.round(frameprev * InfoActual.msPerFrame);
            int mstotal =(InfoActual.DurationMs > 0) ? InfoActual.DurationMs : (int) Math.round(InfoActual.TotalFrames * InfoActual.msPerFrame);
            
            if (msprev > mstotal) {
                msprev = mstotal;
            }
            
            LblTime.setText(Mp3Utils.fmtt(msprev) + " / " + Mp3Utils.fmtt(mstotal));
        });
        
        UITimer = new Timer(200, e -> {
            SyncUI();
            CheckTrackEnd();
        });
        UITimer.start();
        
        ChangeListener RepaintOnChange = e -> {
            JSlider slider = (JSlider) e.getSource();
            
            if (slider.getValueIsAdjusting()) {
                slider.repaint();
            }
        };
        
        Seek.addChangeListener(RepaintOnChange);
        Volumen.addChangeListener(RepaintOnChange);
        
        //Atajos
        getRootPane().registerKeyboardAction(e -> PlayPause(), KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Siguiente(), KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Anterior(), KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        getRootPane().registerKeyboardAction(e -> Stop(), KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK), JComponent.WHEN_IN_FOCUSED_WINDOW);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Stop();
            }
        });
    }
    
    private void AbrirArchivos() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(RutasUsuario.dirMusica());
        fc.setMultiSelectionEnabled(true);
        fc.setFileFilter(new FileNameExtensionFilter("MP3", "mp3"));
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File[] arr = fc.getSelectedFiles();
            
            for (File file : arr) {
                AgregarSiNoExiste(file);
            }
            
            IniciarSiVacio();
        }
    }
    
    private void AbrirCarpeta() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(RutasUsuario.dirMusica());
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            CargarCarpeta(fc.getSelectedFile());
            IniciarSiVacio();
        }
    }
    
    private void AbrirBiblioteca() {
        File dir = RutasUsuario.dirMusica();
        
        //Leer mp3 de la carpeta
        File[] arr = dir.listFiles();
        
        if (arr == null) {
            return;
        }
        
        ArrayList<File> mp3s = new ArrayList<>();
        
        for (File file : arr) {
            String nombre = file.getName().toLowerCase();
            
            if (file.isFile() && nombre.endsWith(".mp3")) {
                mp3s.add(file);
            }
        }
        
        mp3s.sort(Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
        
        //Que recuerde la cancion actual si es que hay una
        File actual = (Indice >= 0 && Indice < Modelo.size()) ? Modelo.get(Indice) : null;
        
        //Reconstruir el modelo sin tocar el icono del boton play, porque antes siempre que tocaba el boton de Biblioteca me borraba el icono del boton de play
        Modelo.clear();
        
        for (File file : mp3s) {
            Modelo.addElement(file);
        }
        
        //Si la cancion actual sigue existiendo, mantener selecicon/indice
        if (actual != null) {
            int nuevoindice = -1;
            
            for (int i = 0; i < Modelo.size(); i++) {
                if (Modelo.get(i).equals(actual)) {
                    nuevoindice = i;
                    break;
                }
            }
            
            if (nuevoindice >= 0) {
                Indice = nuevoindice;
                Lista.setSelectedIndex(Indice);
                Lista.ensureIndexIsVisible(Indice);
                return; //Dejamos el estado tal cual
            }
        }
        
        //Si no habia nada reproduciendo, o la anterior ya no esta, auto-lpay solo si estaba vacio
        if (Hilo == null && Modelo.size() > 0) {
            Indice = 0;
            Reproducir(0, 0);
        } else if (Hilo != null && (actual == null || Modelo.size() == 0)) {
            //Si hay hilo pero la pista actual ya no existe, detener para evitar que suene asi tipo "fantasma"
            Stop();
            Indice = (Modelo.size() > 0) ? 0 : -1;
        }
    }
    
    private void Importar() {
        ArrayList<File> nuevos = MusicaImporter.Importar(this);
        
        for (File file : nuevos) {
            AgregarSiNoExiste(file);
        }
        
        IniciarSiVacio();
    }
    
    private void Exportar() {
        File actual = (Indice >= 0 && Indice < Modelo.size()) ? Modelo.get(Indice) : null;
        MusicaImporter.Exportar(this, actual);
    }
    
    private void Reproducir(int i, int desdeframe) {
        if (i < 0 || i >= Modelo.size()) {
            return;
        }
        
        BuscandoConSlider = false;
        Seek.setValueIsAdjusting(false);
        EndLatch = false;
        
        File file = Modelo.get(i);
        Lista.setSelectedIndex(i);
        LblNow.setText(file.getName());
        
        if (Hilo != null) {
            Hilo.Detener();
        }
        
        try {
            InfoActual = Mp3Info.LeerInfo(file);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el MP3");
            return;
        }
        
        Hilo = new Mp3PlayerThread(file, desdeframe, InfoActual);
        Hilo.start();
        
        IgnorarFinHasta = System.currentTimeMillis() + 700;
        
        setPlayVisual(true);
    }
    
    private void Anterior() {
        if (Modelo.isEmpty()) {
            return;
        }
        
        if (BtnShuffle.isSelected()) {
            Indice = ElegirDiferenteRandom(Indice, Modelo.size());
        } else {
            Indice = (Indice - 1 + Modelo.size()) % Modelo.size();
        }
        
        Reproducir(Indice, 0);
    }
    
    private void Siguiente() {
        if (Modelo.isEmpty()) {
            return;
        }
        
        if (BtnRepetir.isSelected() && Indice >= 0) {
            Reproducir(Indice, 0);
            return;
        }
        
        if (BtnShuffle.isSelected()) {
            Indice = ElegirDiferenteRandom(Indice, Modelo.size());
        } else {
            Indice = (Indice + 1) % Modelo.size();
        }
        
        Reproducir(Indice, 0);
    }
    
    private void PlayPause() {
        if (Hilo == null) {
            return;
        }
        
        if (Hilo.estaPausado()) {
            Hilo.Reanudar();
            setPlayVisual(true);
        } else {
            Hilo.Pausar();
            setPlayVisual(false);
        }
    }
    
    private void Stop() {
        if (Hilo != null) {
            Hilo.Detener();
        }
        
        EndLatch = true;
        BuscandoConSlider = false;
        Seek.setValueIsAdjusting(false);
        Seek.setValue(0);
        LblTime.setText("00:00 / 00:00");
        setPlayVisual(false);
    }
    
    private void SyncUI() {
        if (Hilo == null || InfoActual == null) {
            return;
        }
        
        int frame = Hilo.getFrameEstimadoVivo();
        frame = Math.max(0, Math.min(InfoActual.TotalFrames, frame));
        
        int total = Math.max(1, InfoActual.TotalFrames);
        int msactual = (int)Math.round(frame * InfoActual.msPerFrame);
        int mstotal = (InfoActual.DurationMs > 0) ? InfoActual.DurationMs : (int)Math.round(total * InfoActual.msPerFrame);
        
        if (msactual > mstotal) {
            msactual = mstotal;
        }
        
        LblTime.setText(Mp3Utils.fmtt(msactual) + " / " + Mp3Utils.fmtt(mstotal));

        if (!Seek.getValueIsAdjusting() && !BuscandoConSlider) {
            int pos = (int) Math.round(1000.0 * frame / total);
            pos = Math.max(0, Math.min(1000, pos));
            Seek.setValue(pos);
        }
    }
    
    private boolean esMp3(File file) {
        if (file == null || !file.isFile()) {
            return false;
        }
        
        String nombre = file.getName().toLowerCase();
        return nombre.endsWith(".mp3");
    }
    
    private boolean yaEnLista(File file) {
        for (int i = 0; i < Modelo.size(); i++) {
            if (Modelo.get(i).equals(file)) {
                return true;
            }
        }
        
        return false;
    }
    
    private void AgregarSiNoExiste(File file) {
        if (file != null && esMp3(file) && !yaEnLista(file)) {
            Modelo.addElement(file);
        }
    }
    
    private void CargarCarpeta(File carpeta) {
        if (carpeta == null || !carpeta.isDirectory()) {
            return;
        }
        
        File[] arr = carpeta.listFiles();
        
        if (arr == null) {
            return;
        }
        
        ArrayList<File> mp3s = new ArrayList<>();
        
        for (File file : arr) {            
            if (esMp3(file)) {
                mp3s.add(file);
            }
        }
        
        //Ordenar por nombre
        mp3s.sort(Comparator.comparing(File::getName, String.CASE_INSENSITIVE_ORDER));
        
        for (File file : mp3s) {
            AgregarSiNoExiste(file);
        }
    }
    
    private void IniciarSiVacio() {
        if (Indice == -1 && Modelo.size() > 0) {
            Indice = 0;
            Reproducir(0, 0);
        }
    }
    
    private void OnTrackEnd() {
        if (Modelo.size() <= 1 && !BtnRepetir.isSelected()) {
            setPlayVisual(false);
            return;
        }
        
        if (BtnRepetir.isSelected() && Indice >= 0) {
            Reproducir(Indice, 0);
            return;
        }
        
        //Avanzar segun el shuffle o secuencial
        if (BtnShuffle.isSelected()) {
            Indice = ElegirDiferenteRandom(Indice, Modelo.size());            
        } else {
            Indice = (Indice + 1) % Modelo.size();
        }
        
        Reproducir(Indice, 0);
    }
    
    private void CheckTrackEnd() {
        if (Hilo == null || InfoActual == null || EndLatch) {
            return;
        }
        
        //Si esta dentro del 'cooldown', que no se evalue el fin de la cancion (asi evito que salte de canciones con cada movimiento)
        if (System.currentTimeMillis() < IgnorarFinHasta) {
            return;
        }
        
        if (Hilo.ConsumirFinNatural()) {
            EndLatch = true;
            SwingUtilities.invokeLater(this::OnTrackEnd);
        }
    }
    
    private int ElegirDiferenteRandom(int excluir, int tamano) {
        if (tamano <= 1) {
            return excluir;
        }
        
        int n;
        
        do {            
            n = random.nextInt(tamano);
        } while (n == excluir);
        
        return n;
    }
    
    private Icon LoadIcon(String path, int tamanopixel) {        
        try {
            InputStream entrada = getClass().getResourceAsStream(path);
            
            if (entrada == null) {
                return null;
            }
            
            BufferedImage bi = ImageIO.read(entrada);            
            Image img = bi.getScaledInstance(tamanopixel, tamanopixel, Image.SCALE_SMOOTH);
            
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }    
    private Icon LoadIconTint(String path, int tamanopixel, Color tinte) {        
        try {
            InputStream entrada = getClass().getResourceAsStream(path);
            
            if (entrada == null) {
                return null;
            }
            
            BufferedImage bi = ImageIO.read(entrada);
            BufferedImage work = (tinte != null) ? tint(bi, tinte) : bi;
            Image img = work.getScaledInstance(tamanopixel, tamanopixel, Image.SCALE_SMOOTH);
            
            return new ImageIcon(img);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }    
    
    private BufferedImage tint(BufferedImage src, Color color) {
        BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(src, 0, 0, null);
        g2d.setComposite(AlphaComposite.SrcAtop);
        g2d.setColor(color);
        g2d.fillRect(0, 0, src.getWidth(), src.getHeight());
        g2d.dispose();
        
        return img;
    }
    
    private JToggleButton CrearToggle() {
        JToggleButton boton = new JToggleButton();
        boton.setOpaque(false);
        boton.setForeground(TemaOscuro.TEXTO);
        boton.setBackground(TemaOscuro.CARD);
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(6, 10, 6, 10));
        
        return boton;
    }
    
    private JButton CrearBoton(String texto, boolean primario, int radio) {
        //Colores base segun el tipo
        Color BG = primario ? new Color(84, 36, 122) : new Color(44, 44, 50);
        Color hover = primario ? new Color(110, 50, 150) : new Color(60, 60, 68);
        Color presionado = primario ? new Color(60, 20, 95) : new Color(24, 24, 28);
        Color textoC = primario ? Color.WHITE : new Color(230, 230, 230);
        
        JButton boton = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                int w = getWidth();
                int h = getHeight();
                
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                //Estado actual
                ButtonModel modelo = getModel();
                Color fill = !isEnabled() ? BG.darker().darker() : modelo.isPressed() ? presionado : modelo.isRollover() ? hover : BG;
                
                //Fondo redondeado
                Shape rr = new RoundRectangle2D.Float(0, 0, w - 1, h - 1, radio, radio);
                
                //Sombra sutil
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.fillRoundRect(2, 3, w - 4, h - 5, radio + 2, radio + 2);
                
                //Relleno
                g2d.setColor(fill);
                g2d.fill(rr);
                
                //Borde
                g2d.setColor(new Color(0, 0, 0, 40));
                g2d.draw(new RoundRectangle2D.Float(2, 3, w - 1, h - 1, radio, radio));
                
                g2d.setClip(rr);
                super.paintComponent(g);
                g2d.dispose();
            }
            
            @Override
            public boolean contains(int x, int y) {
                Shape rr = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radio, radio);
                return rr.contains(x, y);
            }
        };
        
        //Baseline de estilo
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setForeground(textoC);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setRolloverEnabled(true);
        boton.setFont(boton.getFont().deriveFont(Font.BOLD, 14f));
        boton.setBorder(new EmptyBorder(6, 16, 6, 16));
        
        return boton;
    }
    
    private void AplicarZoomHover(JButton boton, Icon normal, Icon zoom) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setIcon(zoom);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setIcon(normal);
            }
        });
    }
    
    private void AplicarZoomHoverPlay(JButton boton) {
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setIcon(PlayHover);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setIcon(PlayNormal);
            }
        });
    }
    
    private void AplicarZoomHoverToggle(JToggleButton boton, Icon off26, Icon on26, Icon off30, Icon on30) {
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setOpaque(false);
        boton.setBorder(new EmptyBorder(6, 10, 6, 10));
        
        //Estado visual actual
        final boolean[] hover = {false};
        
        //Actualiza segun si el usuario lo selecciono o esta haciendo un hover
        Runnable UpdateIconState = () -> {
            boolean seleccion = boton.isSelected();
            
            boton.setIcon(hover[0] ? (seleccion ? on30 : on26) : (seleccion ? off30 : off26));
        };
        
        //Estado inicial
        UpdateIconState.run();
        
        boton.addItemListener(ev -> UpdateIconState.run());
        
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover[0] = true;
                UpdateIconState.run();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hover[0] = false;
                UpdateIconState.run();
            }
        });
    }
    
    private void setPlayVisual(boolean playing) {
        isPlayingVisual = playing;
        
        if (playing) {
            PlayNormal = pause26;
            PlayHover = pause30;
        } else {
            PlayNormal = play26;
            PlayHover = play30;
        }
        
        BtnPlay.setIcon(PlayNormal);
    }
    
    private void setDefaultCover(String resourcepath) {
        CoverBaseIcon = loadImageResource(resourcepath);
        
        if (CoverBaseIcon != null) {
            ReescalarCover();
            LblHint.setText(" ");
        } else {
            cover.setIcon(null);
            cover.setText("Sin caratula");
            LblHint.setText(" ");
        }
    }
    
    private BufferedImage loadImageResource(String path) {
        try {
            InputStream entrada = getClass().getResourceAsStream(path);
            
            if (entrada == null) {
                return null;
            }
            
            return ImageIO.read(entrada);
        } catch (Exception e) {
            return null;
        }
    }
    
    private BufferedImage scaledHQ(BufferedImage src, int targetW, int targetH) {
        BufferedImage destino = new BufferedImage(targetW, targetH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = destino.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(src, 0, 0, targetW, targetH, null);
        g2d.dispose();
        
        return destino;
    }
    
    private void ReescalarCover() {
        if (CoverBaseIcon == null) {
            return;
        }
        
        int w = cover.getWidth();
        int h = cover.getHeight();
        
        if (w <= 0 || h <= 0) {
            return;
        }
        
        ImageIcon icono = AjustarCover(CoverBaseIcon, w - 40, h - 40, 1.75);
        cover.setIcon(icono);
        cover.setText(null);
    }
    
    private ImageIcon AjustarCover(BufferedImage src, int boxw, int boxh, double maxupscale) {
        if (src == null || boxw <= 0 || boxh <= 0) {
            return null;
        }
        
        int sw = src.getWidth();
        int sh = src.getHeight();
        
        //tamano objetivo real
        double rw = (double) boxw / sw;
        double rh = (double) boxh / sh;
        double escala = Math.min(rw, rh);
        
        //Limitar el upscale porque sino se escala la imagen y termina siendo toda pixelada
        if (escala > maxupscale) {
            escala = maxupscale;
        }
        
        int tw = Math.max(1, (int) Math.round(sw * escala));
        int th = Math.max(1, (int) Math.round(sh * escala));
        
        BufferedImage escalada = scaledHQ(src, tw, th);
        
        return new ImageIcon(escalada);
    }
}
