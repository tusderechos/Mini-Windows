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
public class Insta implements Serializable, Comparable<Insta>{
    private String autorUsername;
    private Date fechaPublicacion;
    private String contenido;
    private String rutaImg;
    private String texto;
    
    public Insta(String autorUsername, String contenido, String rutaImg){
        this.autorUsername = autorUsername;
        this.fechaPublicacion = new Date();
        this.contenido = contenido;
        this.rutaImg = rutaImg;
        this.texto = texto;
    }
    
    public int compareTo(Insta otroInsta){
        return otroInsta.getFechaPublicacion().compareTo(this.fechaPublicacion);
    }
    
    public String getAutorUsername(){
        return autorUsername;
    }
    
    public Date getFechaPublicacion(){
        return fechaPublicacion;
    }
    
    public String getContenido(){
        return contenido;
    }
    
   public String getRutaImg(){
       return rutaImg;
   }
   
   public String getTexto(){
       return texto;
   }
}
