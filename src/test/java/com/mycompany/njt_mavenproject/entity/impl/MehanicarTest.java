package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MehanicarTest {

    Mehanicar m;

    @BeforeEach
    void setUp() {
        m = new Mehanicar();
    }

    @AfterEach
    void tearDown() {
        m = null;
    }

    @Test
    void testMehanicar() {
        assertNotNull(m);
    }

    @Test
    void testMehanicarLong() {
        m = new Mehanicar(1L);
        assertNotNull(m);
        assertEquals(1L, m.getId());
    }

    @Test
    void testSetId() {
        m.setId(1L);
        assertEquals(1L, m.getId());
    }

    @Test
    void testSetIme() {
        m.setIme("Marko");
        assertEquals("Marko", m.getIme());
    }

    @Test
    void testSetPrezime() {
        m.setPrezime("Markovic");
        assertEquals("Markovic", m.getPrezime());
    }

    @Test
    void testSetSpecijalnost() {
        m.setSpecijalnost("Elektrika");
        assertEquals("Elektrika", m.getSpecijalnost());
    }

    @Test
    void testSetTelefon() {
        m.setTelefon("0601234567");
        assertEquals("0601234567", m.getTelefon());
    }

    @Test
    void testSetServis() {
        Servis s = new Servis(1L, "Auto servis", "Bulevar 1", "0111234567");
        m.setServis(s);
        assertEquals(s, m.getServis());
    }
}