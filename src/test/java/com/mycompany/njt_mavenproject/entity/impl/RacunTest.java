package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.time.Month;

class RacunTest {

    Racun r;

    @BeforeEach
    void setUp() {
        r = new Racun();
    }

    @AfterEach
    void tearDown() {
        r = null;
    }

    @Test
    void testRacun() {
        assertNotNull(r);
    }

    @Test
    void testRacunLong() {
        r = new Racun(1L);
        assertNotNull(r);
        assertEquals(1L, r.getId());
    }

    @Test
    void testSetId() {
        r.setId(1L);
        assertEquals(1L, r.getId());
    }

    @Test
    void testSetBroj() {
        r.setBroj("R-001");
        assertEquals("R-001", r.getBroj());
    }

    @Test
    void testSetDatumIzdavanja() {
        LocalDateTime datum = LocalDateTime.of(2025, Month.JUNE, 1, 10, 0);
        r.setDatumIzdavanja(datum);
        assertEquals(datum, r.getDatumIzdavanja());
    }

    @Test
    void testSetUkupanIznos() {
        r.setUkupanIznos(1500.0);
        assertEquals(1500.0, r.getUkupanIznos());
    }

    @Test
    void testSetStatusPlacanja() {
        r.setStatusPlacanja("PLACENO");
        assertEquals("PLACENO", r.getStatusPlacanja());
    }

    @Test
    void testSetRezervacija() {
        Rezervacija rez = new Rezervacija(1L);
        r.setRezervacija(rez);
        assertEquals(rez, r.getRezervacija());
    }
}