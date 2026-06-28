package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO klasa koja predstavlja vozilo.
 * Koristi se za prenos podataka o vozilu između slojeva aplikacije.
 *
 * @author Bojana
 */
public class VoziloDto implements Dto {

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

    /**
     * Podrazumevani konstruktor.
     */
    public VoziloDto() {}

    /**
     * Konstruktor sa osnovnim parametrima.
     *
     * @param id                  jedinstveni identifikator vozila
     * @param marka               marka vozila
     * @param model               model vozila
     * @param registracija        registarska oznaka vozila
     * @param kilometraza         pređena kilometraža
     * @param jedinicaKilometraze jedinica mere kilometraže (km ili mi)
     * @param godinaProizvodnje   godina proizvodnje vozila
     * @param tipGoriva           tip goriva vozila
     */
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

    /**
     * Vraća ID vozila.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID vozila.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća marku vozila.
     *
     * @return marka vozila
     */
    public String getMarka() { return marka; }

    /**
     * Postavlja marku vozila.
     *
     * @param marka marka vozila (2-50 karaktera)
     */
    public void setMarka(String marka) { this.marka = marka; }

    /**
     * Vraća model vozila.
     *
     * @return model vozila
     */
    public String getModel() { return model; }

    /**
     * Postavlja model vozila.
     *
     * @param model model vozila (1-50 karaktera)
     */
    public void setModel(String model) { this.model = model; }

    /**
     * Vraća registarsku oznaku vozila.
     *
     * @return registracija
     */
    public String getRegistracija() { return registracija; }

    /**
     * Postavlja registarsku oznaku vozila.
     *
     * @param registracija registarska oznaka (2-20 karaktera, mora biti jedinstvena)
     */
    public void setRegistracija(String registracija) { this.registracija = registracija; }

    /**
     * Vraća pređenu kilometražu vozila.
     *
     * @return kilometraža
     */
    public Double getKilometraza() { return kilometraza; }

    /**
     * Postavlja pređenu kilometražu vozila.
     *
     * @param kilometraza kilometraža (ne može biti negativna)
     */
    public void setKilometraza(Double kilometraza) { this.kilometraza = kilometraza; }

    /**
     * Vraća jedinicu mere kilometraže.
     *
     * @return jedinica mere (km ili mi)
     */
    public String getJedinicaKilometraze() { return jedinicaKilometraze; }

    /**
     * Postavlja jedinicu mere kilometraže.
     *
     * @param jedinicaKilometraze jedinica mere (km ili mi)
     */
    public void setJedinicaKilometraze(String jedinicaKilometraze) { this.jedinicaKilometraze = jedinicaKilometraze; }

    /**
     * Vraća godinu proizvodnje vozila.
     *
     * @return godina proizvodnje
     */
    public Integer getGodinaProizvodnje() { return godinaProizvodnje; }

    /**
     * Postavlja godinu proizvodnje vozila.
     *
     * @param godinaProizvodnje godina proizvodnje (1900-2100)
     */
    public void setGodinaProizvodnje(Integer godinaProizvodnje) { this.godinaProizvodnje = godinaProizvodnje; }

    /**
     * Vraća tip goriva vozila.
     *
     * @return tip goriva
     */
    public String getTipGoriva() { return tipGoriva; }

    /**
     * Postavlja tip goriva vozila.
     *
     * @param tipGoriva tip goriva (npr. Benzin, Dizel, Elektro)
     */
    public void setTipGoriva(String tipGoriva) { this.tipGoriva = tipGoriva; }

    /**
     * Vraća vlasnika vozila.
     *
     * @return DTO objekat vlasnika
     */
    public VlasnikDto getVlasnik() { return vlasnik; }

    /**
     * Postavlja vlasnika vozila.
     *
     * @param vlasnik DTO objekat vlasnika
     */
    public void setVlasnik(VlasnikDto vlasnik) { this.vlasnik = vlasnik; }

    /**
     * Vraća listu servisa vezanih za ovo vozilo.
     *
     * @return lista servisa
     */
    public List<ServisDto> getServisi() { return servisi; }

    /**
     * Postavlja listu servisa vezanih za ovo vozilo.
     *
     * @param servisi lista servisa
     */
    public void setServisi(List<ServisDto> servisi) { this.servisi = servisi; }
}