package com.mycompany.njt_mavenproject.entity.impl;

import com.mycompany.njt_mavenproject.entity.MyEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entitet koji predstavlja vlasnika vozila i korisnika sistema.
 * Email i username moraju biti jedinstveni.
 *
 * @author Bojana
 */
@Entity
@Table(
    name = "vlasnik",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_vlasnik_email", columnNames = "email"),
        @UniqueConstraint(name = "uk_vlasnik_username", columnNames = "username")
    }
)
public class Vlasnik implements MyEntity {

    /** Jedinstveni identifikator vlasnika. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ime vlasnika. */
    @Column(nullable = false, length = 50)
    private String ime;

    /** Prezime vlasnika. */
    @Column(nullable = false, length = 50)
    private String prezime;

    /** Email adresa vlasnika, mora biti jedinstvena. */
    @Column(nullable = false, length = 120)
    private String email;

    /** Korisničko ime vlasnika, mora biti jedinstveno. */
    @Column(nullable = false, length = 50)
    private String username;

    /** Hešovana lozinka vlasnika. */
    @Column(nullable = false)
    private String lozinka;

    /** Uloga korisnika u sistemu, podrazumevano VLASNIK. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Uloga uloga = Uloga.VLASNIK;

    /** Oznaka da li je nalog aktiviran putem email verifikacije. */
    @Column(nullable = false)
    private boolean enabled = false;

    /** Lista vozila u vlasništvu korisnika. */
    @OneToMany(mappedBy = "vlasnik", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vozilo> vozila = new ArrayList<>();

    /** Lista rezervacija koje je vlasnik kreirao. */
    @OneToMany(mappedBy = "vlasnik")
    private List<Rezervacija> rezervacije = new ArrayList<>();

    /**
     * Podrazumevani konstruktor.
     */
    public Vlasnik() {}

    /**
     * Konstruktor sa svim parametrima.
     *
     * @param id       jedinstveni identifikator vlasnika
     * @param ime      ime vlasnika
     * @param prezime  prezime vlasnika
     * @param email    email adresa vlasnika
     * @param username korisničko ime vlasnika
     * @param lozinka  lozinka vlasnika
     */
    public Vlasnik(Long id, String ime, String prezime, String email, String username, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.email = email;
        this.username = username;
        this.lozinka = lozinka;
    }

    /**
     * Konstruktor koji kreira referencu na vlasnika sa zadatim ID-em.
     *
     * @param id jedinstveni identifikator vlasnika
     */
    public Vlasnik(Long id) { this.id = id; }

    /**
     * Vraća ID vlasnika.
     *
     * @return jedinstveni identifikator
     */
    public Long getId() { return id; }

    /**
     * Postavlja ID vlasnika.
     *
     * @param id jedinstveni identifikator
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Vraća ime vlasnika.
     *
     * @return ime
     */
    public String getIme() { return ime; }

    /**
     * Postavlja ime vlasnika.
     *
     * @param ime ime vlasnika
     */
    public void setIme(String ime) { this.ime = ime; }

    /**
     * Vraća prezime vlasnika.
     *
     * @return prezime
     */
    public String getPrezime() { return prezime; }

    /**
     * Postavlja prezime vlasnika.
     *
     * @param prezime prezime vlasnika
     */
    public void setPrezime(String prezime) { this.prezime = prezime; }

    /**
     * Vraća email adresu vlasnika.
     *
     * @return email adresa
     */
    public String getEmail() { return email; }

    /**
     * Postavlja email adresu vlasnika.
     *
     * @param email email adresa vlasnika
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Vraća korisničko ime vlasnika.
     *
     * @return korisničko ime
     */
    public String getUsername() { return username; }

    /**
     * Postavlja korisničko ime vlasnika.
     *
     * @param username korisničko ime vlasnika
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Vraća hešovanu lozinku vlasnika.
     *
     * @return lozinka
     */
    public String getLozinka() { return lozinka; }

    /**
     * Postavlja hešovanu lozinku vlasnika.
     *
     * @param lozinka hešovana lozinka
     */
    public void setLozinka(String lozinka) { this.lozinka = lozinka; }

    /**
     * Vraća ulogu korisnika u sistemu.
     *
     * @return uloga (VLASNIK ili ADMIN)
     */
    public Uloga getUloga() { return uloga; }

    /**
     * Postavlja ulogu korisnika u sistemu.
     *
     * @param uloga uloga korisnika
     */
    public void setUloga(Uloga uloga) { this.uloga = uloga; }

    /**
     * Proverava da li je nalog aktiviran.
     *
     * @return true ako je nalog aktiviran, false ako nije
     */
    public boolean isEnabled() { return enabled; }

    /**
     * Postavlja status aktivacije naloga.
     *
     * @param enabled true ako je nalog aktiviran
     */
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    /**
     * Vraća listu vozila vlasnika.
     *
     * @return lista vozila
     */
    public List<Vozilo> getVozila() { return vozila; }

    /**
     * Postavlja listu vozila vlasnika.
     *
     * @param vozila lista vozila
     */
    public void setVozila(List<Vozilo> vozila) { this.vozila = vozila; }

    /**
     * Vraća listu rezervacija vlasnika.
     *
     * @return lista rezervacija
     */
    public List<Rezervacija> getRezervacije() { return rezervacije; }

    /**
     * Postavlja listu rezervacija vlasnika.
     *
     * @param rezervacije lista rezervacija
     */
    public void setRezervacije(List<Rezervacija> rezervacije) { this.rezervacije = rezervacije; }
}