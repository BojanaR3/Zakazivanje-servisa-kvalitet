package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class VlasnikTest {

    Vlasnik v;

    @BeforeEach
    void setUp() {
        v = new Vlasnik();
    }

    @AfterEach
    void tearDown() {
        v = null;
    }

    @Test
    void testVlasnik() {
        assertNotNull(v);
    }

    @Test
    void testVlasnikLong() {
        v = new Vlasnik(1L);
        assertNotNull(v);
        assertEquals(1L, v.getId());
    }

    @Test
    void testVlasnikLongStringStringStringStringString() {
        v = new Vlasnik(1L, "Marko", "Markovic", "marko@gmail.com", "marko123", "lozinka");
        assertNotNull(v);
        assertEquals(1L, v.getId());
        assertEquals("Marko", v.getIme());
        assertEquals("Markovic", v.getPrezime());
        assertEquals("marko@gmail.com", v.getEmail());
        assertEquals("marko123", v.getUsername());
        assertEquals("lozinka", v.getLozinka());
    }

    @Test
    void testSetId() {
        v.setId(1L);
        assertEquals(1L, v.getId());
    }

    @Test
    void testSetIme() {
        v.setIme("Marko");
        assertEquals("Marko", v.getIme());
    }

    @Test
    void testSetPrezime() {
        v.setPrezime("Markovic");
        assertEquals("Markovic", v.getPrezime());
    }

    @Test
    void testSetEmail() {
        v.setEmail("marko@gmail.com");
        assertEquals("marko@gmail.com", v.getEmail());
    }

    @Test
    void testSetUsername() {
        v.setUsername("marko123");
        assertEquals("marko123", v.getUsername());
    }

    @Test
    void testSetLozinka() {
        v.setLozinka("lozinka123");
        assertEquals("lozinka123", v.getLozinka());
    }

    @Test
    void testSetUloga() {
        v.setUloga(Uloga.ADMIN);
        assertEquals(Uloga.ADMIN, v.getUloga());
    }

    @Test
    void testIsEnabledPodrazumevano() {
        assertFalse(v.isEnabled());
    }

    @Test
    void testSetEnabled() {
        v.setEnabled(true);
        assertTrue(v.isEnabled());
    }

    @Test
    void testSetEnabledFalse() {
        v.setEnabled(true);
        v.setEnabled(false);
        assertFalse(v.isEnabled());
    }

    @Test
    void testSetVozila() {
        List<Vozilo> vozila = new ArrayList<>();
        vozila.add(new Vozilo(1L));
        v.setVozila(vozila);
        assertEquals(vozila, v.getVozila());
    }

    @Test
    void testSetRezervacije() {
        List<Rezervacija> rezervacije = new ArrayList<>();
        rezervacije.add(new Rezervacija(1L));
        v.setRezervacije(rezervacije);
        assertEquals(rezervacije, v.getRezervacije());
    }
}