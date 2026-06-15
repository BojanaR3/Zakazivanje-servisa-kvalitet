/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Korisnik
 */
public class ServisDto implements Dto{
    
    private Long id;
    @NotEmpty(message = "Polje za naziv ne sme biti prazno.")
    private String naziv;
    @NotBlank(message = "Polje za adresu ne sme biti prazno.")
    @Size(max = 200, message = "Adresa ne moze biti duza od 200 karaktera.")
    private String adresa;
    @Pattern(regexp = "^\\+?[0-9\\s-]{6,20}$", message = "Telefon nije u ispravnom formatu.")
    private String telefon;
    //private List<Usluga> usluge = new ArrayList<>();
    private List<UslugaDto> usluge = new ArrayList<>();

    public ServisDto(Long id, String naziv, String adresa, String telefon) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public List<UslugaDto> getUsluge() {
        return usluge;
    }

    public void setUsluge(List<UslugaDto> usluge) {
        this.usluge = usluge;
    }

    
    
    
    
}
