/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Insta;

import java.io.Serializable;
import java.util.ArrayList;
import Compartidas.Usuario;

/**
 *
 * @author HP
 */
public class DatosPerfil implements Serializable{
    private Usuario datosGenerales;
    private int totalSeguidores;
    private int totalSeguidos;
    private boolean loSigueElUsuarioActual;
    private ArrayList<Insta> instasPropios;
    
    public DatosPerfil(Usuario datosGenerales, int totalSeguidores, int totalSeguidos, boolean loSigueElUsuarioActual, ArrayList<Insta> instasPropios){
        this.datosGenerales = datosGenerales;
        this.totalSeguidores = totalSeguidores;
        this.totalSeguidos = totalSeguidos;
        this.loSigueElUsuarioActual = loSigueElUsuarioActual;
        this.instasPropios = instasPropios;
    }
    
    public Usuario getDatosGenerales(){
        return datosGenerales;
    }
    
    public int getTotalSeguidores(){
        return totalSeguidores;
    }
    
    public int getTotalSeguidos(){
        return totalSeguidos;
    }
    
    public boolean getloSigueElUsuarioActual(){
        return loSigueElUsuarioActual;
    }
    
    public ArrayList<Insta> getInstasPropios(){
        return instasPropios;
    }
}
