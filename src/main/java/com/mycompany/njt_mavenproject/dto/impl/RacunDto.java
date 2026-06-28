package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

/**
 * DTO klasa koja predstavlja račun.
 * Koristi se za prenos podataka o računu između slojeva aplikacije.
 *
 * @author Bojana
 */
public class RacunDto implements Dto {

    private Long id;

    @NotBlank(message = "Broj racuna ne sme biti prazan.")
    private String broj;

    private LocalDateTime datumIzdavanja;

    @NotNull(message = "Ukupan iznos je obavezan.")
    private Double ukupanIznos;

    @NotBlank(message = "Status placanja ne sme biti prazan.")
    private String statusPlacanja;

    @NotNull(message = "rezervacijaId je obavezan.")
    private Long rezervacijaId;

    /**
     * Podrazumevani konstruktor.
     */
    public RacunDto() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id             jedinstveni identifikator računa
     * @param broj           broj računa
     * @param datumIzdavanja datum i vreme izdavanja računa
     * @param ukupanIznos    ukupan iznos na računu
     * @param statusPlacanja status plaćanja računa
     * @param rezervacijaId  ID rezervacije na osnovu koje je račun izdat
     */
    public RacunDto(Long id, String broj, LocalDateTime datumIzdavanja,
                    Double ukupanIznos, String statusPlacanja, Long rezervacijaId) {
        this.id = id;
        this.broj = broj;
        this.datumIzdavanja = datumIzdavanja;
        this.ukupanIznos = ukupanIznos;
        this.statusPlacanja = statusPlacanja;
        this.rezervacijaId = rezervacijaId;
    }

    /**
     * Vraća ID računa.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID računa.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća broj računa.
     *
     * @return broj računa
     */
    public String getBroj() { return broj; }

    /**
     * Postavlja broj računa.
     *
     * @param broj broj računa
     */
    public void setBroj(String broj) { this.broj = broj; }

    /**
     * Vraća datum i vreme izdavanja računa.
     *
     * @return datum izdavanja
     */
    public LocalDateTime getDatumIzdavanja() { return datumIzdavanja; }

    /**
     * Postavlja datum i vreme izdavanja računa.
     *
     * @param d datum izdavanja
     */
    public void setDatumIzdavanja(LocalDateTime d) { this.datumIzdavanja = d; }

    /**
     * Vraća ukupan iznos računa.
     *
     * @return ukupan iznos
     */
    public Double getUkupanIznos() { return ukupanIznos; }

    /**
     * Postavlja ukupan iznos računa.
     *
     * @param ukupanIznos ukupan iznos
     */
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }

    /**
     * Vraća status plaćanja računa.
     *
     * @return status plaćanja
     */
    public String getStatusPlacanja() { return statusPlacanja; }

    /**
     * Postavlja status plaćanja računa.
     *
     * @param statusPlacanja status plaćanja
     */
    public void setStatusPlacanja(String statusPlacanja) { this.statusPlacanja = statusPlacanja; }

    /**
     * Vraća ID rezervacije vezane za ovaj račun.
     *
     * @return ID rezervacije
     */
    public Long getRezervacijaId() { return rezervacijaId; }

    /**
     * Postavlja ID rezervacije vezane za ovaj račun.
     *
     * @param rezervacijaId ID rezervacije
     */
    public void setRezervacijaId(Long rezervacijaId) { this.rezervacijaId = rezervacijaId; }
}