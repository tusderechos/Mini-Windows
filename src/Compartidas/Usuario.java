/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Compartidas;

/**
 *
 * @author Hp -d
 */

import java.io.Serializable;
import java.time.LocalDate;

public class Usuario implements Serializable {
    
    private String NombreUsuario;
    private char Genero;
    private String Usuario;
    private String Contrasena;
    private LocalDate FechaEntrada;
    private int Edad;
    private boolean Activo;
    private String RutaFotoPerfil;
    
    private boolean Administrador;

    public Usuario(String NombreUsuario, char Genero, String Usuario, String Contrasena, int Edad, String RutaFotoPerfil, boolean Administrador) {
        this.NombreUsuario = NombreUsuario;
        this.Genero = Genero;
        this.Usuario = Usuario;
        this.Contrasena = Contrasena;
        this.Edad = Edad;
        this.RutaFotoPerfil = RutaFotoPerfil;
        this.Administrador = Administrador;
        
        Activo = true;
        FechaEntrada = LocalDate.now();
    }

    public String getNombreUsuario() {
        return NombreUsuario;
    }

    public char getGenero() {
        return Genero;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public LocalDate getFechaEntrada() {
        return FechaEntrada;
    }

    public int getEdad() {
        return Edad;
    }

    public boolean isActivo() {
        return Activo;
    }

    public String getRutaFotoPerfil() {
        return RutaFotoPerfil;
    }

    public void setNombreUsuario(String NombreUsuario) {
        this.NombreUsuario = NombreUsuario;
    }

    public void setGenero(char Genero) {
        this.Genero = Genero;
    }

    public void setContrasena(String Contrasena) {
        this.Contrasena = Contrasena;
    }

    public void setEdad(int Edad) {
        this.Edad = Edad;
    }

    public void setActivo(boolean Activo) {
        this.Activo = Activo;
    }

    public void setRutaFotoPerfil(String RutaFotoPerfil) {
        this.RutaFotoPerfil = RutaFotoPerfil;
    }

    public boolean isAdministrador() {
        return Administrador;
    }

    public void setAdministrador(boolean Administrador) {
        this.Administrador = Administrador;
    }
    
    @Override
    public String toString() {
        return Usuario + " - " + NombreUsuario + " (" + Edad + " años)"; 
    }
}
