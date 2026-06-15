/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Korisnik
 */
@Entity
@Table(
    name = "rezervacija",
    uniqueConstraints = @UniqueConstraint(columnNames = {"servis_id", "datum"})
)

public class Rezervacija implements MyEntity{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime datum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusRezervacije status = StatusRezervacije.CREATED;

    @Column(nullable = false)
    private Double ukupanIznos = 0.0;

    // NOVO: ukupno trajanje rezervacije u minutima (za overlap check)
    @Column(name = "trajanje_min", nullable = false)
    private Integer trajanjeMin = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="vlasnik_id", nullable = false)
    private Vlasnik vlasnik;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="vozilo_id", nullable = false)
    private Vozilo vozilo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) //vise rezervacija moze biti za jedan servis
    @JoinColumn(name="servis_id", nullable = false)
    private Servis servis;

    @OneToMany(mappedBy="rezervacija", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaRezervacije> stavke = new ArrayList<>();
    
    public Rezervacija(Long id) {
		// TODO Auto-generated constructor stub
    	this.id = id;
	}

    @PrePersist
    public void prePersist() {
        if (datum == null) datum = LocalDateTime.now();
        recalcTotal();
        if (trajanjeMin == null) trajanjeMin = 0;
    }

    public void addItem(StavkaRezervacije item){
        item.setRezervacija(this);
        this.stavke.add(item);
        recalcTotal();
    }
    public void removeItem(StavkaRezervacije item){
        item.setRezervacija(null);
        this.stavke.remove(item);
        recalcTotal();
    }

    //Uvek pozovi nakon izmene stavki/popusta/kol. 
    public void recalcTotal() {
        double ukupno = 0.0;
        for (StavkaRezervacije stavka : stavke) {
            ukupno = ukupno + stavka.getUkupno();
        }
        this.ukupanIznos = ukupno;
    }

    public Rezervacija() {
    }

    public Rezervacija(Long id, LocalDateTime datum, Double ukupanIznos, StatusRezervacije status, Vlasnik vlasnik, Vozilo vozilo, Servis servis) {
        this.id = id;
        this.datum = datum;
        this.ukupanIznos = ukupanIznos;
        this.status = status;
        this.vlasnik = vlasnik;
        this.vozilo = vozilo;
        this.servis = servis;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDatum() {
        return datum;
    }

    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }

    public Double getUkupanIznos() {
        return ukupanIznos;
    }

    public void setUkupanIznos(Double ukupanIznos) {
        this.ukupanIznos = ukupanIznos;
    }

    public StatusRezervacije getStatus() {
        return status;
    }

    public void setStatus(StatusRezervacije status) {
        this.status = status;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public Vozilo getVozilo() {
        return vozilo;
    }

    public void setVozilo(Vozilo vozilo) {
        this.vozilo = vozilo;
    }

    public Servis getServis() {
        return servis;
    }

    public void setServis(Servis servis) {
        this.servis = servis;
    }

    public List<StavkaRezervacije> getStavke() {
        return stavke;
    }

    public void setStavke(List<StavkaRezervacije> stavke) {
        this.stavke = stavke;
    }

    public Integer getTrajanjeMin() {
        return trajanjeMin;
    }

    public void setTrajanjeMin(Integer trajanjeMin) {
        this.trajanjeMin = trajanjeMin;
    }
   
    
}
