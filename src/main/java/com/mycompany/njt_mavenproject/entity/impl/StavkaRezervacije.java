/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(name="stavkarezervacije")

public class StavkaRezervacije implements MyEntity{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer kolicina = 1;

    // zaključana cena po jedinici u trenutku pravljenja rezervacije
    @Column(nullable = false)
    private Double unitPrice;  

    // procentualni popust 0–100 (ako želiš fiksni iznos, vidi ispod)
    //@Column(nullable = false)
    //private Double popustProcenat = 0.0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="rezervacija_id", nullable = false)
    private Rezervacija rezervacija;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="usluga_id", nullable = false)
    private Usluga usluga;

    /** Konačna suma za ovu stavku: kolicina * unitPrice * (1 - popust%) 
    public Double getUkupno(){
        double osnova = (unitPrice != null ? unitPrice : 0.0) * (kolicina != null ? kolicina : 0);
        double faktorPopusta = 1.0 - (popustProcenat != null ? popustProcenat : 0.0) / 100.0;
        return osnova * faktorPopusta;
    }
    **/
    
    public Double getUkupno(){
    	if (unitPrice == null || kolicina == null) return 0.0;
    	return unitPrice * kolicina;
    }
    
    public StavkaRezervacije() {
    }

    public StavkaRezervacije(Long id, Double unitPrice, Rezervacija rezervacija, Usluga usluga) {
        this.id = id;
        this.unitPrice = unitPrice;
        this.rezervacija = rezervacija;
        this.usluga = usluga;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        this.unitPrice = unitPrice;
    }

    //public Double getPopustProcenat() {
       // return popustProcenat;
    //}

    //public void setPopustProcenat(Double popustProcenat) {
       // this.popustProcenat = popustProcenat;
    //}

    

    public Rezervacija getRezervacija() {
        return rezervacija;
    }

    public void setRezervacija(Rezervacija rezervacija) {
        this.rezervacija = rezervacija;
    }

    public Usluga getUsluga() {
        return usluga;
    }

    public void setUsluga(Usluga usluga) {
        this.usluga = usluga;
    }
    
    
    
}
