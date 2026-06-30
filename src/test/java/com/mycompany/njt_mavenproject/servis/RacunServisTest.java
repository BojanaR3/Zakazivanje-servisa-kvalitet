package com.mycompany.njt_mavenproject.servis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.njt_mavenproject.dto.impl.RacunDto;
import com.mycompany.njt_mavenproject.entity.impl.Racun;
import com.mycompany.njt_mavenproject.mapper.impl.RacunMapper;
import com.mycompany.njt_mavenproject.repository.impl.RacunRepository;

@ExtendWith(MockitoExtension.class)
class RacunServisTest {

    @Mock
    RacunRepository repo;

    @Mock
    RacunMapper mapper;

    @InjectMocks
    RacunServis racunServis;

    Racun racun;
    RacunDto racunDto;

    @BeforeEach
    void setUp() {
        racun = new Racun(1L);
        racun.setBroj("R-001");
        racun.setDatumIzdavanja(LocalDateTime.of(2025, Month.JUNE, 1, 10, 0));
        racun.setUkupanIznos(1500.0);
        racun.setStatusPlacanja("NEPLACENO");

        racunDto = new RacunDto();
        racunDto.setId(1L);
        racunDto.setBroj("R-001");
        racunDto.setUkupanIznos(1500.0);
        racunDto.setStatusPlacanja("NEPLACENO");
    }

    @AfterEach
    void tearDown(){
        racun = null;
        racunDto = null;
    }

    @Test
    void testRacunServis() {
        assertNotNull(racunServis);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(racun));
        when(mapper.toDto(racun)).thenReturn(racunDto);

        List<RacunDto> rezultat = racunServis.findAll();

        assertEquals(1, rezultat.size());
        assertEquals(racunDto, rezultat.get(0));
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAllPrazno() {
        when(repo.findAll()).thenReturn(Arrays.asList());

        List<RacunDto> rezultat = racunServis.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindById() throws Exception {
        when(repo.findById(1L)).thenReturn(racun);
        when(mapper.toDto(racun)).thenReturn(racunDto);

        RacunDto rezultat = racunServis.findById(1L);

        assertEquals(racunDto, rezultat);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Racun #99 ne postoji."));

        Exception ex = assertThrows(Exception.class, () -> {
            racunServis.findById(99L);
        });
        assertEquals("Racun #99 ne postoji.", ex.getMessage());
    }

    @Test
    void testFindByRezervacijaPostoji() {
        when(repo.findByRezervacijaId(1L)).thenReturn(racun);
        when(mapper.toDto(racun)).thenReturn(racunDto);

        RacunDto rezultat = racunServis.findByRezervacija(1L);

        assertNotNull(rezultat);
        assertEquals(racunDto, rezultat);
        verify(repo, times(1)).findByRezervacijaId(1L);
    }

    @Test
    void testFindByRezervacijaNePostoji() {
        when(repo.findByRezervacijaId(99L)).thenReturn(null);

        RacunDto rezultat = racunServis.findByRezervacija(99L);

        assertNull(rezultat);
    }

    @Test
    void testUpdateStatus() throws Exception {
        when(repo.findById(1L)).thenReturn(racun);
        when(mapper.toDto(racun)).thenReturn(racunDto);

        RacunDto rezultat = racunServis.updateStatus(1L, "PLACENO");

        assertEquals("PLACENO", racun.getStatusPlacanja());
        verify(repo, times(1)).save(racun);
    }

    @Test
    void testUpdateStatusNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Racun #99 ne postoji."));

        assertThrows(Exception.class, () -> {
            racunServis.updateStatus(99L, "PLACENO");
        });
    }

    @Test
    void testDeleteById() {
        racunServis.deleteById(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}