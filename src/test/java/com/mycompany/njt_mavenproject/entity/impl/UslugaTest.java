package com.mycompany.njt_mavenproject.entity.impl;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UslugaTest {

    Usluga u;

    @BeforeEach
    void setUp() {
        u = new Usluga();
    }

    @AfterEach
    void tearDown() {
        u = null;
    }

    @Test
    void testUsluga() {
        assertNotNull(u);
    }

    @Test
    void testUslugaLong() {
        u = new Usluga(1L);
        assertNotNull(u);
        assertEquals(1L, u.getId());
    }

    @Test
    void testUslugaLongStringIntegerString() {
        u = new Usluga(1L, "Zamena ulja", 30, "min");
        assertNotNull(u);
        assertEquals(1L, u.getId());
        assertEquals("Zamena ulja", u.getNaziv());
        assertEquals(30, u.getTrajanje());
        assertEquals("min", u.getJedinicaMere());
    }

    @Test
    void testSetId() {
        u.setId(1L);
        assertEquals(1L, u.getId());
    }

    @Test
    void testSetNaziv() {
        u.setNaziv("Zamena ulja");
        assertEquals("Zamena ulja", u.getNaziv());
    }

    @Test
    void testSetTrajanje() {
        u.setTrajanje(30);
        assertEquals(30, u.getTrajanje());
    }

    @Test
    void testSetJedinicaMere() {
        u.setJedinicaMere("min");
        assertEquals("min", u.getJedinicaMere());
    }

    @Test
    void testSetCena() {
        u.setCena(1500.0);
        assertEquals(1500.0, u.getCena());
    }
}