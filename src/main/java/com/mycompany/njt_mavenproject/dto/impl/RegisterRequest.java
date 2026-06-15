/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

import jakarta.validation.constraints.*;

/**
 *
 * @author Korisnik
 */
public class RegisterRequest {
    
    @NotBlank @Size(min=3,max=50)
    private String username;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min=6,max=100)
    private String lozinka;

    @NotBlank @Size(min=2,max=50)
    private String ime;

    @NotBlank @Size(min=2,max=50)
    private String prezime;

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public String getLozinka(){ return lozinka; }
    public void setLozinka(String lozinka){ this.lozinka = lozinka; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    
    
}
