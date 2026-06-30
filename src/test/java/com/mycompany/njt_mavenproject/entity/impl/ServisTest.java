package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServisTest {

    Servis s;

    @BeforeEach
    void setUp() {
        s = new Servis();
    }

    @AfterEach
    void tearDown()  {
        s = null;
    }

    @Test
    void testServis() {
        assertNotNull(s);
    }

    @Test
    void testServisLong() {
        s = new Servis(1L);
        assertNotNull(s);
        assertEquals(1L, s.getId());
    }

    @Test
    void testServisLongStringStringString() {
        s = new Servis(1L, "Auto servis", "Bulevar 1", "0111234567");
        assertNotNull(s);
        assertEquals(1L, s.getId());
        assertEquals("Auto servis", s.getNaziv());
        assertEquals("Bulevar 1", s.getAdresa());
        assertEquals("0111234567", s.getTelefon());
    }

    @Test
    void testSetId() {
        s.setId(1L);
        assertEquals(1L, s.getId());
    }

    @Test
    void testSetNaziv() {
        s.setNaziv("Auto servis");
        assertEquals("Auto servis", s.getNaziv());
    }

    @Test
    void testSetAdresa() {
        s.setAdresa("Bulevar 1");
        assertEquals("Bulevar 1", s.getAdresa());
    }

    @Test
    void testSetTelefon() {
        s.setTelefon("0111234567");
        assertEquals("0111234567", s.getTelefon());
    }
}