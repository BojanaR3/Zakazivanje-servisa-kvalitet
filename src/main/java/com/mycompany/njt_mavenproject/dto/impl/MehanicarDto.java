package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;

public class MehanicarDto implements Dto {

    private Long id;

    @NotBlank(message = "Ime ne sme biti prazno.")
    private String ime;

    @NotBlank(message = "Prezime ne sme biti prazno.")
    private String prezime;

    private String specijalnost;
    private String telefon;

    @NotNull(message = "servisId je obavezan.")
    private Long servisId;

    public MehanicarDto() {}

    public MehanicarDto(Long id, String ime, String prezime, String specijalnost, String telefon, Long servisId) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.specijalnost = specijalnost;
        this.telefon = telefon;
        this.servisId = servisId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }
    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }
    public String getSpecijalnost() { return specijalnost; }
    public void setSpecijalnost(String specijalnost) { this.specijalnost = specijalnost; }
    public String getTelefon() { return telefon; }
    public void setTelefon(String telefon) { this.telefon = telefon; }
    public Long getServisId() { return servisId; }
    public void setServisId(Long servisId) { this.servisId = servisId; }
}