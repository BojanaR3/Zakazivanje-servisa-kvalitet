package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Predstavlja vozilo u sistemu.
 * Klasa sadrži podatke o vozilu, njegovom vlasniku i rezervacijama
 * koje su vezane za to vozilo.
 *
 * @author Bojana
 */
@Entity
@Table(name="vozilo")
public class Vozilo implements MyEntity{

    /**
     * Jedinstveni identifikator vozila.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Marka vozila.
     */
    private String marka;

    /**
     * Model vozila.
     */
    private String model;

    /**
     * Registarska oznaka vozila.
     */
    private String registracija;

    /**
     * Kilometraža vozila.
     */
    private Double kilometraza;

    /**
     * Jedinica kilometraže (km ili mi).
     */
    private String jedinicaKilometraze;

    /**
     * Godina proizvodnje vozila.
     */
    private Integer godProizvodnje;

    /**
     * Tip goriva koje vozilo koristi.
     */
    private String tipGoriva;

    /**
     * Vlasnik vozila.
     */
    @ManyToOne(optional = true)
    @JoinColumn(name="vlasnik_id", nullable = true)
    private Vlasnik vlasnik;

    /**
     * Lista rezervacija povezanih sa vozilom.
     */
    @OneToMany(mappedBy="vozilo")
    private List<Rezervacija> rezervacije = new ArrayList<>();

    /**
     * Podrazumevani konstruktor.
     */
    public Vozilo() {
    }

    /**
     * Kreira objekat vozila sa svim atributima.
     *
     * @param id identifikator vozila
     * @param marka marka vozila
     * @param model model vozila
     * @param registracija registarska oznaka vozila
     * @param kilometraza kilometraža vozila
     * @param jedinicaKilometraze jedinica kilometraže
     * @param godProizvodnje godina proizvodnje vozila
     * @param tipGoriva tip goriva
     * @param vlasnik vlasnik vozila
     */
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

    /**
     * Kreira objekat vozila sa zadatim identifikatorom.
     *
     * @param id identifikator vozila
     */
    public Vozilo(Long id) {
        this.id = id;
    }

    /**
     * Vraća identifikator vozila.
     *
     * @return identifikator vozila
     */
    public Long getId() {
        return id;
    }

    /**
     * Postavlja identifikator vozila.
     *
     * @param id identifikator vozila
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Vraća marku vozila.
     *
     * @return marka vozila
     */
    public String getMarka() {
        return marka;
    }

    /**
     * Postavlja marku vozila.
     *
     * @param marka marka vozila
     */
    public void setMarka(String marka) {
        this.marka = marka;
    }

    /**
     * Vraća model vozila.
     *
     * @return model vozila
     */
    public String getModel() {
        return model;
    }

    /**
     * Postavlja model vozila.
     *
     * @param model model vozila
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Vraća registarsku oznaku vozila.
     *
     * @return registarska oznaka vozila
     */
    public String getRegistracija() {
        return registracija;
    }

    /**
     * Postavlja registarsku oznaku vozila.
     *
     * @param registracija registarska oznaka vozila
     */
    public void setRegistracija(String registracija) {
        this.registracija = registracija;
    }

    /**
     * Vraća kilometražu vozila.
     *
     * @return kilometraža vozila
     */
    public Double getKilometraza() {
        return kilometraza;
    }

    /**
     * Postavlja kilometražu vozila.
     *
     * @param kilometraza kilometraža vozila
     */
    public void setKilometraza(Double kilometraza) {
        this.kilometraza = kilometraza;
    }

    /**
     * Vraća jedinicu kilometraže.
     *
     * @return jedinica kilometraže
     */
    public String getJedinicaKilometraze() {
        return jedinicaKilometraze;
    }

    /**
     * Postavlja jedinicu kilometraže.
     *
     * @param jedinicaKilometraze jedinica kilometraže
     */
    public void setJedinicaKilometraze(String jedinicaKilometraze) {
        this.jedinicaKilometraze = jedinicaKilometraze;
    }

    /**
     * Vraća godinu proizvodnje vozila.
     *
     * @return godina proizvodnje vozila
     */
    public Integer getGodProizvodnje() {
        return godProizvodnje;
    }

    /**
     * Postavlja godinu proizvodnje vozila.
     *
     * @param godProizvodnje godina proizvodnje vozila
     */
    public void setGodProizvodnje(Integer godProizvodnje) {
        this.godProizvodnje = godProizvodnje;
    }

    /**
     * Vraća tip goriva.
     *
     * @return tip goriva
     */
    public String getTipGoriva() {
        return tipGoriva;
    }

    /**
     * Postavlja tip goriva.
     *
     * @param tipGoriva tip goriva
     */
    public void setTipGoriva(String tipGoriva) {
        this.tipGoriva = tipGoriva;
    }

    /**
     * Vraća vlasnika vozila.
     *
     * @return vlasnik vozila
     */
    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    /**
     * Postavlja vlasnika vozila.
     *
     * @param vlasnik vlasnik vozila
     */
    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    /**
     * Vraća listu rezervacija vozila.
     *
     * @return lista rezervacija
     */
    public List<Rezervacija> getRezervacije() {
        return rezervacije;
    }

    /**
     * Postavlja listu rezervacija vozila.
     *
     * @param rezervacije lista rezervacija
     */
    public void setRezervacije(List<Rezervacija> rezervacije) {
        this.rezervacije = rezervacije;
    }
}

