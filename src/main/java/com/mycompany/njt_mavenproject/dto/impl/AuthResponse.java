/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

/**
 *
 * @author Korisnik
 */
public class AuthResponse {
   
    private String token;
    private VlasnikDto user;

    public AuthResponse() {}

    public AuthResponse(String token, VlasnikDto user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public VlasnikDto getUser() { return user; }
    public void setUser(VlasnikDto user) { this.user = user; }
    
}
