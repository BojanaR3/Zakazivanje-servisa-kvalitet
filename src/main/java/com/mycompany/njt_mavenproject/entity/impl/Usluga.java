/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

import java.util.ArrayList;
import java.util.List;
import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;
/**
 *
 * @author Korisnik
 */


import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

@Entity
@Table(name="usluga")
public class Usluga implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private Integer trajanje;
    private String jedinicaMere;

    // Možeš da zadržiš (nullable). Ne koristi se za obračun.
    @Column
    private Double cena;

    public Usluga() {}
    public Usluga(Long id) { this.id = id; }
    public Usluga(Long id, String naziv, Integer trajanje, String jedinicaMere) {
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
    public Double getCena() { return cena; }
    public void setCena(Double cena) { this.cena = cena; }
}
