package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import com.mycompany.njt_mavenproject.entity.impl.StatusRezervacije;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO klasa koja predstavlja rezervaciju.
 * Koristi se za prenos podataka o rezervaciji između slojeva aplikacije.
 * Ukupan iznos i ID vlasnika su read-only polja koja se računaju ili postavljaju na serverskoj strani.
 *
 * @author Bojana
 */
public class RezervacijaDto implements Dto {

    private Long id;
    private StatusRezervacije status;
    private LocalDateTime datum;
    private Double ukupanIznos;

    @NotNull(message = "servisId je obavezan")
    private Long servisId;

    private Long voziloId;
    private VoziloDto vozilo;
    private Long vlasnikId;

    @Valid
    @NotEmpty(message = "Rezervacija mora imati bar jednu stavku")
    private List<StavkaRezervacijeDto> stavke;

    /**
     * Podrazumevani konstruktor.
     */
    public RezervacijaDto() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id           jedinstveni identifikator rezervacije
     * @param status       status rezervacije
     * @param datum        datum i vreme rezervacije
     * @param ukupanIznos  ukupan iznos rezervacije
     * @param servisId     ID servisa za koji se pravi rezervacija
     * @param voziloId     ID vozila koje se servisira
     * @param vlasnikId    ID vlasnika koji je kreirao rezervaciju
     * @param stavke       lista stavki rezervacije
     */
    public RezervacijaDto(Long id, StatusRezervacije status, LocalDateTime datum, Double ukupanIznos,
                          Long servisId, Long voziloId, Long vlasnikId,
                          List<StavkaRezervacijeDto> stavke) {
        this.id = id;
        this.status = status;
        this.datum = datum;
        this.ukupanIznos = ukupanIznos;
        this.servisId = servisId;
        this.voziloId = voziloId;
        this.vlasnikId = vlasnikId;
        this.stavke = stavke;
    }

    /**
     * Vraća ID rezervacije.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID rezervacije.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća status rezervacije.
     *
     * @return status rezervacije
     */
    public StatusRezervacije getStatus() { return status; }

    /**
     * Postavlja status rezervacije.
     *
     * @param status status rezervacije
     */
    public void setStatus(StatusRezervacije status) { this.status = status; }

    /**
     * Vraća datum i vreme rezervacije.
     *
     * @return datum rezervacije
     */
    public LocalDateTime getDatum() { return datum; }

    /**
     * Postavlja datum i vreme rezervacije.
     *
     * @param datum datum i vreme rezervacije
     */
    public void setDatum(LocalDateTime datum) { this.datum = datum; }

    /**
     * Vraća ukupan iznos rezervacije.
     *
     * @return ukupan iznos
     */
    public Double getUkupanIznos() { return ukupanIznos; }

    /**
     * Postavlja ukupan iznos rezervacije.
     *
     * @param ukupanIznos ukupan iznos
     */
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }

    /**
     * Vraća ID servisa za koji je napravljena rezervacija.
     *
     * @return ID servisa
     */
    public Long getServisId() { return servisId; }

    /**
     * Postavlja ID servisa rezervacije.
     *
     * @param servisId ID servisa
     */
    public void setServisId(Long servisId) { this.servisId = servisId; }

    /**
     * Vraća ID vozila koje se servisira.
     *
     * @return ID vozila
     */
    public Long getVoziloId() { return voziloId; }

    /**
     * Postavlja ID vozila koje se servisira.
     *
     * @param voziloId ID vozila
     */
    public void setVoziloId(Long voziloId) { this.voziloId = voziloId; }

    /**
     * Vraća DTO objekat vozila (koristi se kada vozilo još nije u bazi).
     *
     * @return DTO vozila
     */
    public VoziloDto getVozilo() { return vozilo; }

    /**
     * Postavlja DTO objekat vozila.
     *
     * @param vozilo DTO vozila
     */
    public void setVozilo(VoziloDto vozilo) { this.vozilo = vozilo; }

    /**
     * Vraća ID vlasnika koji je kreirao rezervaciju.
     *
     * @return ID vlasnika
     */
    public Long getVlasnikId() { return vlasnikId; }

    /**
     * Postavlja ID vlasnika rezervacije.
     *
     * @param vlasnikId ID vlasnika
     */
    public void setVlasnikId(Long vlasnikId) { this.vlasnikId = vlasnikId; }

    /**
     * Vraća listu stavki rezervacije.
     *
     * @return lista stavki
     */
    public List<StavkaRezervacijeDto> getStavke() { return stavke; }

    /**
     * Postavlja listu stavki rezervacije.
     *
     * @param stavke lista stavki rezervacije
     */
    public void setStavke(List<StavkaRezervacijeDto> stavke) { this.stavke = stavke; }
}