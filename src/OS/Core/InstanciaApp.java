/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package OS.Core;

/**
 *
 * @author Hp
 */

import java.time.LocalDateTime;

public class InstanciaApp {
    
    private AppId appId;
    private String InstanciaId;
    private LocalDateTime AbiertaEn;

    public InstanciaApp(AppId appId, String InstanciaId) {
        this.appId = appId;
        this.InstanciaId = InstanciaId;
        AbiertaEn = LocalDateTime.now();
    }

    public AppId getAppId() {
        return appId;
    }

    public String getInstanciaId() {
        return InstanciaId;
    }

    public LocalDateTime getAbiertaEn() {
        return AbiertaEn;
    }
    
    
}
