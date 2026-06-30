package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class VoziloTest {

    Vozilo v;

    @BeforeEach
    void setUp() {
        v = new Vozilo();
    }

    @AfterEach
    void tearDown() {
        v = null;
    }

    @Test
    void testVozilo() {
        assertNotNull(v);
    }

    @Test
    void testVoziloLong() {
        v = new Vozilo(1L);
        assertNotNull(v);
        assertEquals(1L, v.getId());
    }

    @Test
    void testVoziloLongStringStringStringDoubleStringIntegerStringVlasnik() {
        Vlasnik vlasnik = new Vlasnik(1L);
        v = new Vozilo(1L, "Toyota", "Corolla", "BG123AB", 50000.0, "km", 2020, "Benzin", vlasnik);
        assertNotNull(v);
        assertEquals(1L, v.getId());
        assertEquals("Toyota", v.getMarka());
        assertEquals("Corolla", v.getModel());
        assertEquals("BG123AB", v.getRegistracija());
        assertEquals(50000.0, v.getKilometraza());
        assertEquals("km", v.getJedinicaKilometraze());
        assertEquals(2020, v.getGodProizvodnje());
        assertEquals("Benzin", v.getTipGoriva());
        assertEquals(vlasnik, v.getVlasnik());
    }

    @Test
    void testSetId() {
        v.setId(1L);
        assertEquals(1L, v.getId());
    }

    @Test
    void testSetMarka() {
        v.setMarka("Toyota");
        assertEquals("Toyota", v.getMarka());
    }

    @Test
    void testSetModel() {
        v.setModel("Corolla");
        assertEquals("Corolla", v.getModel());
    }

    @Test
    void testSetRegistracija() {
        v.setRegistracija("BG123AB");
        assertEquals("BG123AB", v.getRegistracija());
    }

    @Test
    void testSetKilometraza() {
        v.setKilometraza(50000.0);
        assertEquals(50000.0, v.getKilometraza());
    }

    @Test
    void testSetJedinicaKilometraze() {
        v.setJedinicaKilometraze("km");
        assertEquals("km", v.getJedinicaKilometraze());
    }

    @Test
    void testSetGodProizvodnje() {
        v.setGodProizvodnje(2020);
        assertEquals(2020, v.getGodProizvodnje());
    }

    @Test
    void testSetTipGoriva() {
        v.setTipGoriva("Benzin");
        assertEquals("Benzin", v.getTipGoriva());
    }

    @Test
    void testSetVlasnik() {
        Vlasnik vlasnik = new Vlasnik(1L);
        v.setVlasnik(vlasnik);
        assertEquals(vlasnik, v.getVlasnik());
    }

    @Test
    void testSetRezervacije() {
        List<Rezervacija> rezervacije = new ArrayList<>();
        rezervacije.add(new Rezervacija(1L));
        v.setRezervacije(rezervacije);
        assertEquals(rezervacije, v.getRezervacije());
    }
}