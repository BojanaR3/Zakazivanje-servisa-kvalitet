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
public class LoginRequest {
    
    @NotBlank private String username;
    @NotBlank private String lozinka;

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
    
    
}
