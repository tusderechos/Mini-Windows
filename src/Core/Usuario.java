/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Core;

/**
 *
 * @author Hp
 */

import java.time.LocalDate;

public class Usuario {
    
    private String NombreUsuario;
    private char Genero;
    private String Usuario;
    private String Contrasena;
    private LocalDate FechaEntrada;
    private int Edad;
    private boolean Activo;
    private String RutaFotoPerfil;

    public Usuario(String NombreUsuario, char Genero, String Usuario, String Contrasena, int Edad, String RutaFotoPerfil) {
        this.NombreUsuario = NombreUsuario;
        this.Genero = Genero;
        this.Usuario = Usuario;
        this.Contrasena = Contrasena;
        this.Edad = Edad;
        this.RutaFotoPerfil = RutaFotoPerfil;
        
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
    
    @Override
    public String toString() {
        return Usuario + " - " + NombreUsuario + " (" + Edad + " años)"; 
    }
}
