package com.mycompany.njt_mavenproject.dto.impl;

import com.mycompany.njt_mavenproject.dto.Dto;
import jakarta.validation.constraints.*;

/**
 * DTO klasa koja predstavlja stavku rezervacije.
 * Sadrži podatke o usluzi, količini, popustu i jediničnoj ceni u okviru jedne rezervacije.
 *
 * @author Bojana
 */
public class StavkaRezervacijeDto implements Dto {

    private Long id;

    @NotNull(message = "uslugaId je obavezan")
    private Long uslugaId;

    @Min(value = 1, message = "kolicina mora biti ≥ 1")
    private Integer kolicina = 1;

    @DecimalMin(value = "0.0") @DecimalMax(value = "100.0")
    private Double popustProcenat = 0.0;

    private Double unitPrice;

    /**
     * Podrazumevani konstruktor.
     */
    public StavkaRezervacijeDto() {}

    /**
     * Konstruktor sa osnovnim parametrima.
     *
     * @param id        jedinstveni identifikator stavke
     * @param uslugaId  ID usluge koja se dodaje u stavku
     * @param kolicina  količina usluge
     * @param unitPrice jedinična cena usluge u trenutku kreiranja rezervacije
     */
    public StavkaRezervacijeDto(Long id, Long uslugaId, Integer kolicina, Double unitPrice) {
        this.id = id;
        this.uslugaId = uslugaId;
        this.kolicina = kolicina;
        this.unitPrice = unitPrice;
    }

    /**
     * Vraća ID stavke rezervacije.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID stavke rezervacije.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća ID usluge u stavci.
     *
     * @return ID usluge
     */
    public Long getUslugaId() { return uslugaId; }

    /**
     * Postavlja ID usluge u stavci.
     *
     * @param uslugaId ID usluge
     */
    public void setUslugaId(Long uslugaId) { this.uslugaId = uslugaId; }

    /**
     * Vraća količinu usluge u stavci.
     *
     * @return količina (minimum 1)
     */
    public Integer getKolicina() { return kolicina; }

    /**
     * Postavlja količinu usluge u stavci.
     *
     * @param kolicina količina (minimum 1)
     */
    public void setKolicina(Integer kolicina) { this.kolicina = kolicina; }

    /**
     * Vraća procentualni popust na stavku.
     *
     * @return popust u procentima (0.0 - 100.0)
     */
    public Double getPopustProcenat() { return popustProcenat; }

    /**
     * Postavlja procentualni popust na stavku.
     *
     * @param popustProcenat popust u procentima (0.0 - 100.0)
     */
    public void setPopustProcenat(Double popustProcenat) { this.popustProcenat = popustProcenat; }

    /**
     * Vraća jediničnu cenu usluge zaključanu u trenutku kreiranja rezervacije.
     *
     * @return jedinična cena
     */
    public Double getUnitPrice() { return unitPrice; }

    /**
     * Postavlja jediničnu cenu usluge.
     *
     * @param unitPrice jedinična cena
     */
    public void setUnitPrice(Double unitPrice) { this.unitPrice = unitPrice; }
}