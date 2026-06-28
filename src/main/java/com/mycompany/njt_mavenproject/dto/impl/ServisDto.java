package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO klasa koja predstavlja servis za vozila.
 * Koristi se za prenos podataka o servisu između slojeva aplikacije.
 *
 * @author Bojana
 */
public class ServisDto implements Dto {

    private Long id;

    @NotEmpty(message = "Polje za naziv ne sme biti prazno.")
    private String naziv;

    @NotBlank(message = "Polje za adresu ne sme biti prazno.")
    @Size(max = 200, message = "Adresa ne moze biti duza od 200 karaktera.")
    private String adresa;

    @Pattern(regexp = "^\\+?[0-9\\s-]{6,20}$", message = "Telefon nije u ispravnom formatu.")
    private String telefon;

    private List<UslugaDto> usluge = new ArrayList<>();

    /**
     * Konstruktor sa osnovnim parametrima.
     *
     * @param id      jedinstveni identifikator servisa
     * @param naziv   naziv servisa
     * @param adresa  adresa servisa
     * @param telefon kontakt telefon servisa
     */
    public ServisDto(Long id, String naziv, String adresa, String telefon) {
        this.id = id;
        this.naziv = naziv;
        this.adresa = adresa;
        this.telefon = telefon;
    }

    /**
     * Vraća ID servisa.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID servisa.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća naziv servisa.
     *
     * @return naziv servisa
     */
    public String getNaziv() { return naziv; }

    /**
     * Postavlja naziv servisa.
     *
     * @param naziv naziv servisa
     */
    public void setNaziv(String naziv) { this.naziv = naziv; }

    /**
     * Vraća adresu servisa.
     *
     * @return adresa servisa
     */
    public String getAdresa() { return adresa; }

    /**
     * Postavlja adresu servisa.
     *
     * @param adresa adresa servisa (max 200 karaktera)
     */
    public void setAdresa(String adresa) { this.adresa = adresa; }

    /**
     * Vraća telefon servisa.
     *
     * @return kontakt telefon
     */
    public String getTelefon() { return telefon; }

    /**
     * Postavlja telefon servisa.
     *
     * @param telefon kontakt telefon servisa
     */
    public void setTelefon(String telefon) { this.telefon = telefon; }

    /**
     * Vraća listu usluga koje servis nudi.
     *
     * @return lista usluga
     */
    public List<UslugaDto> getUsluge() { return usluge; }

    /**
     * Postavlja listu usluga servisa.
     *
     * @param usluge lista usluga
     */
    public void setUsluge(List<UslugaDto> usluge) { this.usluge = usluge; }
}