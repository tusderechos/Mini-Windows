/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Core;

/**
 *
 * @author Hp
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorApps {
    
    private final ArrayList<InstanciaApp> Abiertas = new ArrayList<>();
    
    public InstanciaApp Abrir(AppId appid, String instanciaid) {
        InstanciaApp instancia = new InstanciaApp(appid, instanciaid);
        
        Abiertas.add(instancia);
        
        return instancia;
    }
    
    public boolean Cerrar(AppId appid, String instanciaid) {
        return Abiertas.removeIf(a -> a.getAppId() == appid && (instanciaid == null || instanciaid.equals(a.getInstanciaId())));
    }
    
    public ArrayList<InstanciaApp> Listar() {
        return new ArrayList<>(Abiertas);
    }
    
    public List<InstanciaApp> Listar(AppId appid) {
        return Abiertas.stream().filter(a -> a.getAppId() == appid).collect(Collectors.toList());
    }
    
    public boolean hayAierta(AppId appid) {
        return Abiertas.stream().anyMatch(a -> a.getAppId() == appid);
    }
}
