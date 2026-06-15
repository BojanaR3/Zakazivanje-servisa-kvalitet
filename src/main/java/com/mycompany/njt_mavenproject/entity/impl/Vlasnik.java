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
@Table(
  name="vlasnik",
  uniqueConstraints = {
    @UniqueConstraint(name="uk_vlasnik_email", columnNames="email"),
    @UniqueConstraint(name="uk_vlasnik_username", columnNames="username")
  }
)

public class Vlasnik implements MyEntity{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=50)
    private String ime;

    @Column(nullable=false, length=50)
    private String prezime;

    @Column(nullable=false, length=120)
    private String email;

    @Column(nullable=false, length=50)
    private String username;

    @Column(nullable=false)
    private String lozinka;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private Uloga uloga = Uloga.VLASNIK;

    @Column(nullable=false)
    private boolean enabled = false; // ← VAŽNO za email verifikaciju


    @OneToMany(mappedBy="vlasnik", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<Vozilo> vozila = new ArrayList<>();

    @OneToMany(mappedBy="vlasnik")
    private List<Rezervacija> rezervacije = new ArrayList<>();

    public Vlasnik() {
    }

    public Vlasnik(Long id, String ime, String prezime, String email, String username, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.username = username;
        this.lozinka = lozinka;
    }

    public Vlasnik(Long id) {
        this.id = id;
        
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    

    public Uloga getUloga() {
        return uloga;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public List<Vozilo> getVozila() {
        return vozila;
    }

    public void setVozila(List<Vozilo> vozila) {
        this.vozila = vozila;
    }

    public List<Rezervacija> getRezervacije() {
        return rezervacije;
    }

    public void setRezervacije(List<Rezervacija> rezervacije) {
        this.rezervacije = rezervacije;
    }
    
    
    
}
