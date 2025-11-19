/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author HP
 */
public class Usuario implements Serializable{
    private String username;
    private String nombre;
    private String password;
    private char genero;
    private int edad;
    private Date fechaIngreso;
    private boolean activo;
    private String rutaFotoPerfil;
    
    public Usuario(String username ,String nombre, String password, char genero, int edad){
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.genero = genero;
        this.edad = edad;
        this.fechaIngreso = new Date();
        this.activo = true;
        this.rutaFotoPerfil = "";
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(){
        this.username = username;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(){
        this.nombre = nombre;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPssword(){
        this.password = password;
    }
    
    public char getGenero(){
        return genero;
    }
    
    public void setGenero(){
        this.genero = genero;
    }
    
    public int getEdad(){
        return edad;
    }
    
    public void setEdad(){
        this.edad = edad;
    }
    
    public Date getFechaIngreso(){
        return fechaIngreso;
    }
    
    public boolean getActivo(){
        return activo;
    }
    
    public void setActivo(boolean activo){
        this.activo = activo;
    }
    
    public String getRutaFotoPerfil(){
        return rutaFotoPerfil;
    }
    
    public void setRutaFotoPerfil(){
        this.rutaFotoPerfil = rutaFotoPerfil;
    }
    
    @Override
    public String toString(){
        return "Usuario: "+username+"\n"
                +"Nombre: "+nombre+"\n"
                +"Edad: "+edad;
    }
}
