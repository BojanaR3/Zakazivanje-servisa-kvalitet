/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;

/**
 *
 * @author Korisnik
 */
public class VlasnikDto implements Dto{
   
    private Long id;
    private String ime;
    private String prezime;
    private String username;
    private String email;
    private Uloga uloga;     // npr. USER / ADMIN

    public VlasnikDto() {
    }

    public VlasnikDto(Long id, String ime, String prezime, String username, String email, Uloga uloga) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.username = username;
        this.email = email;
        this.uloga = uloga;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Uloga getUloga() {
        return uloga;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }


    
    
}
