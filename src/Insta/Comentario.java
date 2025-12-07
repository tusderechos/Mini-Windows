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
public class Comentario implements Serializable {

    private String autorUsername;
    private String texto;
    private Date fecha;

    public Comentario(String autorUsername, String texto) {
        this.autorUsername = autorUsername;
        this.texto = texto;
        this.fecha = new Date();
    }

    public String getAutorUsername() {
        return autorUsername;
    }

    public String getTexto() {
        return texto;
    }

    public Date getFecha() {
        return fecha;
    }
}
