/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Korisnik
 */
public class VoziloDto implements Dto{
    
    private Long id;

    @NotEmpty(message = "Marka je obavezna.")
    @Size(min = 2, max = 50, message = "Marka mora imati između 2 i 50 karaktera.")
    private String marka;

    @NotEmpty(message = "Model je obavezan.")
    @Size(min = 1, max = 50, message = "Model mora imati između 1 i 50 karaktera.")
    private String model;

    @NotEmpty(message = "Registracija je obavezna.")
    @Size(min = 2, max = 20, message = "Registracija mora imati između 2 i 20 karaktera.")
    @Column(nullable = false, unique = true)
    private String registracija;

    @Min(value = 0, message = "Kilometraža ne može biti negativna.")
    private Double kilometraza;

    @NotEmpty(message = "Jedinica kilometraže je obavezna.")
    @Pattern(regexp = "km|mi", message = "Jedinica kilometraže mora biti 'km' ili 'mi'.")
    private String jedinicaKilometraze;

    @Min(value = 1900, message = "Godina proizvodnje ne može biti pre 1900.")
    @Max(value = 2100, message = "Godina proizvodnje ne može biti posle 2100.")
    private Integer godinaProizvodnje;

    @NotEmpty(message = "Tip goriva je obavezan.")
    private String tipGoriva;

    private VlasnikDto vlasnik;

    private List<ServisDto> servisi = new ArrayList<>();

    public VoziloDto() {
    }

    public VoziloDto(Long id, String marka, String model, String registracija, Double kilometraza,
                     String jedinicaKilometraze, Integer godinaProizvodnje, String tipGoriva) {
        this.id = id;
        this.marka = marka;
        this.model = model;
        this.registracija = registracija;
        this.kilometraza = kilometraza;
        this.jedinicaKilometraze = jedinicaKilometraze;
        this.godinaProizvodnje = godinaProizvodnje;
        this.tipGoriva = tipGoriva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarka() {
        return marka;
    }

    public void setMarka(String marka) {
        this.marka = marka;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRegistracija() {
        return registracija;
    }

    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    public Double getKilometraza() {
        return kilometraza;
    }

    public void setKilometraza(Double kilometraza) {
        this.kilometraza = kilometraza;
    }

    public String getJedinicaKilometraze() {
        return jedinicaKilometraze;
    }

    public void setJedinicaKilometraze(String jedinicaKilometraze) {
        this.jedinicaKilometraze = jedinicaKilometraze;
    }

    public Integer getGodinaProizvodnje() {
        return godinaProizvodnje;
    }

    public void setGodinaProizvodnje(Integer godinaProizvodnje) {
        this.godinaProizvodnje = godinaProizvodnje;
    }

    public String getTipGoriva() {
        return tipGoriva;
    }

    public void setTipGoriva(String tipGoriva) {
        this.tipGoriva = tipGoriva;
    }

    public VlasnikDto getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(VlasnikDto vlasnik) {
        this.vlasnik = vlasnik;
    }

    public List<ServisDto> getServisi() {
        return servisi;
    }

    public void setServisi(List<ServisDto> servisi) {
        this.servisi = servisi;
    }
    
}
