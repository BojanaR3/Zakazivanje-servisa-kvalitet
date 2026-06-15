package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "servis")
public class Servis implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String naziv;
    private String adresa;
    private String telefon;

    public Servis() {}
    public Servis(Long id) { this.id = id; }
    public Servis(Long id, String naziv, String adresa, String telefon) {
        this.id = id; this.naziv = naziv; this.adresa = adresa; this.telefon = telefon;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNaziv() { return naziv; }
    public void setNaziv(String naziv) { this.naziv = naziv; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
}
