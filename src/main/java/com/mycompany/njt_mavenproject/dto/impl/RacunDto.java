package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

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

    public RacunDto() {}

    public RacunDto(Long id, String broj, LocalDateTime datumIzdavanja,
                    Double ukupanIznos, String statusPlacanja, Long rezervacijaId) {
        this.id = id;
        this.broj = broj;
        this.datumIzdavanja = datumIzdavanja;
        this.ukupanIznos = ukupanIznos;
        this.statusPlacanja = statusPlacanja;
        this.rezervacijaId = rezervacijaId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBroj() { return broj; }
    public void setBroj(String broj) { this.broj = broj; }
    public LocalDateTime getDatumIzdavanja() { return datumIzdavanja; }
    public void setDatumIzdavanja(LocalDateTime d) { this.datumIzdavanja = d; }
    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }
    public String getStatusPlacanja() { return statusPlacanja; }
    public void setStatusPlacanja(String statusPlacanja) { this.statusPlacanja = statusPlacanja; }
    public Long getRezervacijaId() { return rezervacijaId; }
    public void setRezervacijaId(Long rezervacijaId) { this.rezervacijaId = rezervacijaId; }
}