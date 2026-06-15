/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;


/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

public class UslugaDto implements Dto {

    private Long id;

    @NotEmpty(message = "Polje za naziv ne sme biti prazno.")
    @Size(min = 2, max = 100, message = "Naziv mora biti izmedju 2 i 100 karaktera.")
    private String naziv;

    @NotNull(message = "Polje za trajanje ne sme biti prazno.")
    @Min(value = 1, message = "Trajanje mora biti pozitivno.")
    private Integer trajanje;

    @NotBlank(message = "Polje za jedinicu mere ne sme biti prazno.")
    private String jedinicaMere;
    
    private Double cena;

    /** FRONT -> BACK: ID-jevi servisa iz multiselect-a */
    private List<Long> servisIds = new ArrayList<>();

    /** BACK -> FRONT: lagani pregled povezanih servisa (id+naziv) */
    private List<ServisLiteDto> servisi = new ArrayList<>();

    public UslugaDto() {}
    public UslugaDto(Long id, String naziv, Integer trajanje, String jedinicaMere) {
        this.id = id; this.naziv = naziv; this.trajanje = trajanje; this.jedinicaMere = jedinicaMere;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public Integer getTrajanje() { return trajanje; }
    public void setTrajanje(Integer trajanje) { this.trajanje = trajanje; }
    public String getJedinicaMere() { return jedinicaMere; }
    public void setJedinicaMere(String jedinicaMere) { this.jedinicaMere = jedinicaMere; }

    public List<Long> getServisIds() { return servisIds; }
    public void setServisIds(List<Long> servisIds) { this.servisIds = (servisIds == null) ? new ArrayList<>() : servisIds; }

    public List<ServisLiteDto> getServisi() { return servisi; }
    public void setServisi(List<ServisLiteDto> servisi) { this.servisi = (servisi == null) ? new ArrayList<>() : servisi; }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }

    
    
    // mali DTO u okviru UslugaDto
    public static class ServisLiteDto {
        private Long id;
        private String naziv;
        public ServisLiteDto() {}
        public ServisLiteDto(Long id, String naziv) { this.id = id; this.naziv = naziv; }
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getNaziv() { return naziv; }
        public void setNaziv(String naziv) { this.naziv = naziv; }
    }
}

