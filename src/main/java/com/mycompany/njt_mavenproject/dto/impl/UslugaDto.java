package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO klasa koja predstavlja uslugu.
 * Koristi se za prenos podataka o usluzi između slojeva aplikacije.
 *
 * @author Bojana
 */
public class UslugaDto implements Dto {

    private Long id;

    @NotEmpty(message = "Polje za naziv ne sme biti prazno.")
    @Size(min = 2, max = 100, message = "Naziv mora biti izmedju 2 i 100 karaktera.")
    private String naziv;

    @NotNull(message = "Polje za trajanje ne sme biti prazno.")
    @Min(value = 1, message = "Trajanje mora biti pozitivno.")
    private Integer trajanje;

    @NotBlank(message = "Polje za jedinicu mere ne sme biti prazno.")
    private String jedinicaMere;

    private Double cena;

    private List<Long> servisIds = new ArrayList<>();
    private List<ServisLiteDto> servisi = new ArrayList<>();

    /**
     * Podrazumevani konstruktor.
     */
    public UslugaDto() {}

    /**
     * Konstruktor sa osnovnim parametrima.
     *
     * @param id           jedinstveni identifikator usluge
     * @param naziv        naziv usluge
     * @param trajanje     trajanje usluge
     * @param jedinicaMere jedinica mere trajanja (npr. "min", "h")
     */
    public UslugaDto(Long id, String naziv, Integer trajanje, String jedinicaMere) {
        this.id = id;
        this.naziv = naziv;
        this.trajanje = trajanje;
        this.jedinicaMere = jedinicaMere;
    }

    /**
     * Vraća ID usluge.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID usluge.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća naziv usluge.
     *
     * @return naziv usluge
     */
    public String getNaziv() { return naziv; }

    /**
     * Postavlja naziv usluge.
     *
     * @param naziv naziv usluge (2-100 karaktera)
     */
    public void setNaziv(String naziv) { this.naziv = naziv; }

    /**
     * Vraća trajanje usluge.
     *
     * @return trajanje usluge
     */
    public Integer getTrajanje() { return trajanje; }

    /**
     * Postavlja trajanje usluge.
     *
     * @param trajanje trajanje usluge (minimum 1)
     */
    public void setTrajanje(Integer trajanje) { this.trajanje = trajanje; }

    /**
     * Vraća jedinicu mere trajanja usluge.
     *
     * @return jedinica mere (npr. "min", "h")
     */
    public String getJedinicaMere() { return jedinicaMere; }

    /**
     * Postavlja jedinicu mere trajanja usluge.
     *
     * @param jedinicaMere jedinica mere trajanja
     */
    public void setJedinicaMere(String jedinicaMere) { this.jedinicaMere = jedinicaMere; }

    /**
     * Vraća listu ID-eva servisa koji nude ovu uslugu.
     * Koristi se pri slanju podataka sa fronta na back.
     *
     * @return lista ID-eva servisa
     */
    public List<Long> getServisIds() { return servisIds; }

    /**
     * Postavlja listu ID-eva servisa koji nude ovu uslugu.
     *
     * @param servisIds lista ID-eva servisa
     */
    public void setServisIds(List<Long> servisIds) {
        this.servisIds = (servisIds == null) ? new ArrayList<>() : servisIds;
    }

    /**
     * Vraća listu osnovnih podataka o servisima koji nude ovu uslugu.
     * Koristi se pri slanju podataka sa back-a na front.
     *
     * @return lista DTO objekata servisa
     */
    public List<ServisLiteDto> getServisi() { return servisi; }

    /**
     * Postavlja listu osnovnih podataka o servisima.
     *
     * @param servisi lista DTO objekata servisa
     */
    public void setServisi(List<ServisLiteDto> servisi) {
        this.servisi = (servisi == null) ? new ArrayList<>() : servisi;
    }

    /**
     * Vraća cenu usluge.
     *
     * @return cena usluge
     */
    public Double getCena() { return cena; }

    /**
     * Postavlja cenu usluge.
     *
     * @param cena cena usluge
     */
    public void setCena(Double cena) { this.cena = cena; }

    /**
     * Ugniježđena DTO klasa koja predstavlja osnovne podatke o servisu.
     * Koristi se za prikaz servisa unutar usluge bez cirkularnih referenci.
     *
     * @author Bojana
     */
    public static class ServisLiteDto {

        private Long id;
        private String naziv;

        /**
         * Podrazumevani konstruktor.
         */
        public ServisLiteDto() {}

        /**
         * Konstruktor sa parametrima.
         *
         * @param id    jedinstveni identifikator servisa
         * @param naziv naziv servisa
         */
        public ServisLiteDto(Long id, String naziv) {
            this.id = id;
            this.naziv = naziv;
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
    }
}