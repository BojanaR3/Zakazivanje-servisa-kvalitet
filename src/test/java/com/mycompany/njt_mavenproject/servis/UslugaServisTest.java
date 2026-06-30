package com.mycompany.njt_mavenproject.servis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.njt_mavenproject.dto.impl.UslugaDto;
import com.mycompany.njt_mavenproject.entity.impl.Usluga;
import com.mycompany.njt_mavenproject.mapper.impl.UslugaMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import com.mycompany.njt_mavenproject.repository.impl.UslugaRepository;

@ExtendWith(MockitoExtension.class)
class UslugaServisTest {

    @Mock
    UslugaRepository usluge;

    @Mock
    ServisUslugaRepository cenovnik;

    @Mock
    UslugaMapper mapper;

    @InjectMocks
    UslugaServis uslugaServis;

    Usluga usluga;
    UslugaDto uslugaDto;

    @BeforeEach
    void setUp() {
        usluga = new Usluga(1L, "Zamena ulja", 30, "min");
        uslugaDto = new UslugaDto(1L, "Zamena ulja", 30, "min");
    }

    @AfterEach
    void tearDown() { 
        usluga = null;
        uslugaDto = null;
    }

    @Test
    void testUslugaServis() {
        assertNotNull(uslugaServis);
    }

    @Test
    void testFindAll() {
        when(usluge.findAll()).thenReturn(Arrays.asList(usluga));
        when(mapper.toDto(usluga)).thenReturn(uslugaDto);

        List<UslugaDto> rezultat = uslugaServis.findAll();

        assertEquals(1, rezultat.size());
        assertEquals(uslugaDto, rezultat.get(0));
        verify(usluge, times(1)).findAll();
    }

    @Test
    void testFindAllPrazno() {
        when(usluge.findAll()).thenReturn(Arrays.asList());

        List<UslugaDto> rezultat = uslugaServis.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindById() throws Exception {
        when(usluge.findById(1L)).thenReturn(usluga);
        when(mapper.toDto(usluga)).thenReturn(uslugaDto);

        UslugaDto rezultat = uslugaServis.findById(1L);

        assertEquals(uslugaDto, rezultat);
        verify(usluge, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNePostoji() throws Exception {
        when(usluge.findById(99L)).thenThrow(new Exception("Usluga nije pronađena!"));

        Exception ex = assertThrows(Exception.class, () -> {
            uslugaServis.findById(99L);
        });
        assertEquals("Usluga nije pronađena!", ex.getMessage());
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(uslugaDto)).thenReturn(usluga);
        when(mapper.toDto(usluga)).thenReturn(uslugaDto);

        UslugaDto rezultat = uslugaServis.create(uslugaDto);

        assertEquals(uslugaDto, rezultat);
        verify(usluge, times(1)).save(usluga);
    }

    @Test
    void testUpdate() throws Exception {
        when(usluge.findById(1L)).thenReturn(usluga);
        when(mapper.toDto(usluga)).thenReturn(uslugaDto);

        UslugaDto rezultat = uslugaServis.update(uslugaDto);

        assertEquals(uslugaDto, rezultat);
        verify(usluge, times(1)).save(usluga);
    }

    @Test
    void testUpdateNePostoji() throws Exception {
        when(usluge.findById(99L)).thenThrow(new Exception("Usluga nije pronađena!"));
        uslugaDto = new UslugaDto(99L, "Zamena ulja", 30, "min");

        assertThrows(RuntimeException.class, () -> {
            uslugaServis.update(uslugaDto);
        });
    }

    @Test
    void testDeleteById() {
        uslugaServis.deleteById(1L);

        verify(cenovnik, times(1)).deleteByUsluga(1L);
        verify(usluge, times(1)).deleteById(1L);
    }
}