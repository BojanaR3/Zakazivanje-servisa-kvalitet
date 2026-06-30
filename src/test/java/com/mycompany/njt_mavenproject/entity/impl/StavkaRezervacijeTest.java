package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StavkaRezervacijeTest {

    StavkaRezervacije s;

    @BeforeEach
    void setUp() {
        s = new StavkaRezervacije();
    }

    @AfterEach
    void tearDown(){
        s = null;
    }

    @Test
    void testStavkaRezervacije() {
        assertNotNull(s);
    }

    @Test
    void testStavkaRezervacijeLongDoubleRezervacijaUsluga() {
        Rezervacija r = new Rezervacija(1L);
        Usluga u = new Usluga(1L);
        s = new StavkaRezervacije(1L, 500.0, r, u);
        assertNotNull(s);
        assertEquals(1L, s.getId());
        assertEquals(500.0, s.getUnitPrice());
        assertEquals(r, s.getRezervacija());
        assertEquals(u, s.getUsluga());
    }

    @Test
    void testGetUkupno() {
        s.setUnitPrice(500.0);
        s.setKolicina(2);
        assertEquals(1000.0, s.getUkupno());
    }

    @Test
    void testGetUkupnoUnitPriceNull() {
        s.setUnitPrice(null);
        s.setKolicina(2);
        assertEquals(0.0, s.getUkupno());
    }

    @Test
    void testGetUkupnoKolicinaNull() {
        s.setUnitPrice(500.0);
        s.setKolicina(null);
        assertEquals(0.0, s.getUkupno());
    }

    @Test
    void testGetUkupnoObaNulla() {
        s.setUnitPrice(null);
        s.setKolicina(null);
        assertEquals(0.0, s.getUkupno());
    }

    @Test
    void testSetId() {
        s.setId(1L);
        assertEquals(1L, s.getId());
    }

    @Test
    void testSetKolicina() {
        s.setKolicina(3);
        assertEquals(3, s.getKolicina());
    }

    @Test
    void testSetUnitPrice() {
        s.setUnitPrice(500.0);
        assertEquals(500.0, s.getUnitPrice());
    }

    @Test
    void testSetRezervacija() {
        Rezervacija r = new Rezervacija(1L);
        s.setRezervacija(r);
        assertEquals(r, s.getRezervacija());
    }

    @Test
    void testSetUsluga() {
        Usluga u = new Usluga(1L);
        s.setUsluga(u);
        assertEquals(u, s.getUsluga());
    }
}