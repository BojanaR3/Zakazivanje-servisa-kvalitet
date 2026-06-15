/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.entity.impl;

/**
 *
 * @author Korisnik
 */



import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "servis_usluga",
       uniqueConstraints = @UniqueConstraint(columnNames = {"servis_id", "usluga_id"}))
public class ServisUsluga implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servis_id", nullable = false)
    private Servis servis;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usluga_id", nullable = false)
    private Usluga usluga;

    @Column(nullable = false)
    private Double cena;

    public ServisUsluga() {}
    public ServisUsluga(Servis servis, Usluga usluga, Double cena) {
        this.servis = servis; this.usluga = usluga; this.cena = cena;
    }

    public Long getId() { return id; }
    public Servis getServis() { return servis; }
    public void setServis(Servis servis) { this.servis = servis; }
    public Usluga getUsluga() { return usluga; }
    public void setUsluga(Usluga usluga) { this.usluga = usluga; }
    public Double getCena() { return cena; }
    public void setCena(Double cena) { this.cena = cena; }
}


