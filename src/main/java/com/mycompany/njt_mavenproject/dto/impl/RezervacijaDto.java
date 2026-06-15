/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

// package com.mycompany.njt_mavenproject.dto.impl;


import com.mycompany.njt_mavenproject.dto.Dto;
import com.mycompany.njt_mavenproject.entity.impl.StatusRezervacije;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class RezervacijaDto implements Dto {

    private Long id;

    private StatusRezervacije status;     // read/write po potrebi
    private LocalDateTime datum;          // read-only: @PrePersist u entitetu
    private Double ukupanIznos;           // read-only: računa se iz stavki

    @NotNull(message = "servisId je obavezan")
    private Long servisId;

    // Ako korisnik želi ručno da odabere postojeće vozilo:
    private Long voziloId;

    // Ako NEMA vozilo u bazi (prva rezervacija) — šalje ceo objekat vozila:
    private VoziloDto vozilo;

    private Long vlasnikId;               // read-only u responsu

    @Valid
    @NotEmpty(message = "Rezervacija mora imati bar jednu stavku")
    private List<StavkaRezervacijeDto> stavke;

    public RezervacijaDto() {}

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public StatusRezervacije getStatus() { return status; }
    public void setStatus(StatusRezervacije status) { this.status = status; }

    public LocalDateTime getDatum() { return datum; }
    public void setDatum(LocalDateTime datum) { this.datum = datum; }

    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }

    public Long getServisId() { return servisId; }
    public void setServisId(Long servisId) { this.servisId = servisId; }

    public Long getVoziloId() { return voziloId; }
    public void setVoziloId(Long voziloId) { this.voziloId = voziloId; }

    public VoziloDto getVozilo() { return vozilo; }
    public void setVozilo(VoziloDto vozilo) { this.vozilo = vozilo; }

    public Long getVlasnikId() { return vlasnikId; }
    public void setVlasnikId(Long vlasnikId) { this.vlasnikId = vlasnikId; }

    public List<StavkaRezervacijeDto> getStavke() { return stavke; }
    public void setStavke(List<StavkaRezervacijeDto> stavke) { this.stavke = stavke; }
}


