/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.njt_mavenproject.dto.impl;

// package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;

public class StavkaRezervacijeDto implements Dto{

    private Long id;

    @NotNull(message = "uslugaId je obavezan")
    private Long uslugaId;

    @Min(value = 1, message = "kolicina mora biti ≥ 1")
    private Integer kolicina = 1;

    // PROCIJENJENI popust po stavci — procentualno (0..100)
    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    private Double popustProcenat = 0.0;

    // read-only ka frontu: jedinicna cena izračunata u trenutku kreiranja
    private Double unitPrice;

    public StavkaRezervacijeDto() {}

    public StavkaRezervacijeDto(Long id, Long uslugaId, Integer kolicina, Double unitPrice) {
        this.id = id;
        this.uslugaId = uslugaId;
        this.kolicina = kolicina;
        this.unitPrice = unitPrice;
    }

    // get/set ...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUslugaId() { return uslugaId; }
    public void setUslugaId(Long uslugaId) { this.uslugaId = uslugaId; }

    public Integer getKolicina() { return kolicina; }
    public void setKolicina(Integer kolicina) { this.kolicina = kolicina; }

    public Double getPopustProcenat() { return popustProcenat; }
    public void setPopustProcenat(Double popustProcenat) { this.popustProcenat = popustProcenat; }

    public Double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}
