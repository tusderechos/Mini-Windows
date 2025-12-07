/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;

/**
 *
 * @author HP
 */
public class Follow implements Serializable{
    private String username;
    private boolean activo;
    
    public Follow(String username){
        this.username = username;
        this.activo = true;
    }
    
    public String getUsername(){
        return username;
    }
    
    public boolean isActivo(){
        return activo;
    }
    
    public void setActivo(boolean activo){
        this.activo = activo;
    }

}
