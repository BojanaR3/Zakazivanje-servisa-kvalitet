/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(name="vozilo")

public class Vozilo implements MyEntity{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marka;
    private String model;
    private String registracija;
    private Double kilometraza;
    private String jedinicaKilometraze; // km(kilometri) ili mi(milje)
    private Integer godProizvodnje;
    private String tipGoriva;

    @ManyToOne(optional = true) // dozvoljava null
    @JoinColumn(name="vlasnik_id", nullable = true)
    private Vlasnik vlasnik;

    @OneToMany(mappedBy="vozilo")
    private List<Rezervacija> rezervacije = new ArrayList<>();

    public Vozilo() {
    }

    public Vozilo(Long id, String marka, String model, String registracija, Double kilometraza, String jedinicaKilometraze, Integer godProizvodnje, String tipGoriva, Vlasnik vlasnik) {
        this.id = id;
        this.marka = marka;
        this.model = model;
        this.registracija = registracija;
        this.kilometraza = kilometraza;
        this.jedinicaKilometraze = jedinicaKilometraze;
        this.godProizvodnje = godProizvodnje;
        this.tipGoriva = tipGoriva;
        this.vlasnik = vlasnik;
    }

    public Vozilo(Long id) {
        this.id = id;
        
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

    public Integer getGodProizvodnje() {
        return godProizvodnje;
    }

    public void setGodProizvodnje(Integer godProizvodnje) {
        this.godProizvodnje = godProizvodnje;
    }

    public String getTipGoriva() {
        return tipGoriva;
    }

    public void setTipGoriva(String tipGoriva) {
        this.tipGoriva = tipGoriva;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public List<Rezervacija> getRezervacije() {
        return rezervacije;
    }

    public void setRezervacije(List<Rezervacija> rezervacije) {
        this.rezervacije = rezervacije;
    }
    
    
    
}
