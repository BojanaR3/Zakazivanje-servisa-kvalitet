package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "racun")
public class Racun implements MyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String broj;

    @Column(nullable = false)
    private LocalDateTime datumIzdavanja;

    @Column(nullable = false)
    private Double ukupanIznos;

    @Column(nullable = false)
    private String statusPlacanja;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rezervacija_id", nullable = false, unique = true)
    private Rezervacija rezervacija;

    public Racun() {}
    public Racun(Long id) { this.id = id; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBroj() { return broj; }
    public void setBroj(String broj) { this.broj = broj; }
    public LocalDateTime getDatumIzdavanja() { return datumIzdavanja; }
    public void setDatumIzdavanja(LocalDateTime datumIzdavanja) { this.datumIzdavanja = datumIzdavanja; }
    public Double getUkupanIznos() { return ukupanIznos; }
    public void setUkupanIznos(Double ukupanIznos) { this.ukupanIznos = ukupanIznos; }
    public String getStatusPlacanja() { return statusPlacanja; }
    public void setStatusPlacanja(String statusPlacanja) { this.statusPlacanja = statusPlacanja; }
    public Rezervacija getRezervacija() { return rezervacija; }
    public void setRezervacija(Rezervacija rezervacija) { this.rezervacija = rezervacija; }
}