package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji predstavlja rezervaciju termina u servisu za vozila.
 * Kombinacija servis_id i datum mora biti jedinstvena.
 *
 * @author Bojana
 */
@Entity
@Table(
    name = "rezervacija",
    uniqueConstraints = @UniqueConstraint(columnNames = {"servis_id", "datum"})
)
public class Rezervacija implements MyEntity {

    /** Jedinstveni identifikator rezervacije. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Datum i vreme rezervisanog termina. */
    @Column(nullable = false)
    private LocalDateTime datum;

    /** Status rezervacije (CREATED, CONFIRMED, CANCELED, COMPLETED). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusRezervacije status = StatusRezervacije.CREATED;

    /** Ukupan iznos rezervacije izračunat iz stavki. */
    @Column(nullable = false)
    private Double ukupanIznos = 0.0;

    /** Ukupno trajanje rezervacije u minutima, koristi se za proveru preklapanja termina. */
    @Column(name = "trajanje_min", nullable = false)
    private Integer trajanjeMin = 0;

    /** Vlasnik koji je kreirao rezervaciju. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vlasnik_id", nullable = false)
    private Vlasnik vlasnik;

    /** Vozilo koje se servisira u okviru rezervacije. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vozilo_id", nullable = false)
    private Vozilo vozilo;

    /** Servis u kome je rezervisan termin. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "servis_id", nullable = false)
    private Servis servis;

    /** Lista stavki rezervacije. */
    @OneToMany(mappedBy = "rezervacija", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StavkaRezervacije> stavke = new ArrayList<>();

    /**
     * Konstruktor koji kreira referencu na rezervaciju sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator rezervacije
     */
    public Rezervacija(Long id) {
        this.id = id;
    }

    /**
     * Podrazumevani konstruktor.
     */
    public Rezervacija() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id          jedinstveni identifikator rezervacije
     * @param datum       datum i vreme rezervacije
     * @param ukupanIznos ukupan iznos rezervacije
     * @param status      status rezervacije
     * @param vlasnik     vlasnik koji je kreirao rezervaciju
     * @param vozilo      vozilo koje se servisira
     * @param servis      servis u kome je rezervisan termin
     */
    public Rezervacija(Long id, LocalDateTime datum, Double ukupanIznos, StatusRezervacije status,
                       Vlasnik vlasnik, Vozilo vozilo, Servis servis) {
        this.id = id;
        this.datum = datum;
        this.ukupanIznos = ukupanIznos;
        this.status = status;
        this.vlasnik = vlasnik;
        this.vozilo = vozilo;
        this.servis = servis;
    }

    /**
     * Metoda koja se poziva pre čuvanja entiteta u bazu.
     * Postavlja datum na trenutno vreme ako nije zadat i izračunava ukupan iznos.
     */
    @PrePersist
    public void prePersist() {
        if (datum == null) datum = LocalDateTime.now(Clock.systemDefaultZone());
        recalcTotal();
        if (trajanjeMin == null) trajanjeMin = 0;
    }

    /**
     * Dodaje stavku u rezervaciju i ažurira ukupan iznos.
     *
     * @param item stavka rezervacije koja se dodaje
     */
    public void addItem(StavkaRezervacije item) {
        item.setRezervacija(this);
        this.stavke.add(item);
        recalcTotal();
    }

    /**
     * Uklanja stavku iz rezervacije i ažurira ukupan iznos.
     *
     * @param item stavka rezervacije koja se uklanja
     */
    public void removeItem(StavkaRezervacije item) {
        item.setRezervacija(null);
        this.stavke.remove(item);
        recalcTotal();
    }

    /**
     * Izračunava i ažurira ukupan iznos rezervacije na osnovu svih stavki.
     * Treba pozivati nakon svake izmene stavki, količine ili popusta.
     */
    public void recalcTotal() {
        double ukupno = 0.0;
        for (StavkaRezervacije stavka : stavke) {
            ukupno = ukupno + stavka.getUkupno();
        }
        this.ukupanIznos = ukupno;
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
     * Vraća vlasnika koji je kreirao rezervaciju.
     *
     * @return vlasnik
     */
    public Vlasnik getVlasnik() { return vlasnik; }

    /**
     * Postavlja vlasnika rezervacije.
     *
     * @param vlasnik vlasnik rezervacije
     */
    public void setVlasnik(Vlasnik vlasnik) { this.vlasnik = vlasnik; }

    /**
     * Vraća vozilo koje se servisira.
     *
     * @return vozilo
     */
    public Vozilo getVozilo() { return vozilo; }

    /**
     * Postavlja vozilo koje se servisira.
     *
     * @param vozilo vozilo rezervacije
     */
    public void setVozilo(Vozilo vozilo) { this.vozilo = vozilo; }

    /**
     * Vraća servis u kome je rezervisan termin.
     *
     * @return servis
     */
    public Servis getServis() { return servis; }

    /**
     * Postavlja servis rezervacije.
     *
     * @param servis servis u kome je termin rezervisan
     */
    public void setServis(Servis servis) { this.servis = servis; }

    /**
     * Vraća listu stavki rezervacije.
     *
     * @return lista stavki
     */
    public List<StavkaRezervacije> getStavke() { return stavke; }

    /**
     * Postavlja listu stavki rezervacije.
     *
     * @param stavke lista stavki rezervacije
     */
    public void setStavke(List<StavkaRezervacije> stavke) { this.stavke = stavke; }

    /**
     * Vraća ukupno trajanje rezervacije u minutima.
     *
     * @return trajanje u minutima
     */
    public Integer getTrajanjeMin() { return trajanjeMin; }

    /**
     * Postavlja ukupno trajanje rezervacije u minutima.
     *
     * @param trajanjeMin trajanje u minutima
     */
    public void setTrajanjeMin(Integer trajanjeMin) { this.trajanjeMin = trajanjeMin; }
}