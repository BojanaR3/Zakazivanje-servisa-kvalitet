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

import com.mycompany.njt_mavenproject.dto.impl.ServisDto;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.mapper.impl.ServisMapper;
import com.mycompany.njt_mavenproject.repository.impl.ServisRepository;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;

@ExtendWith(MockitoExtension.class)
class ServisServisTest {

    @Mock
    ServisRepository repo;

    @Mock
    ServisMapper mapper;

    @Mock
    ServisUslugaRepository cenovnik;

    @InjectMocks
    ServisServis servisServis;

    Servis servis;
    ServisDto servisDto;

    @BeforeEach
    void setUp() throws Exception {
        servis = new Servis(1L, "Auto servis", "Bulevar 1", "0111234567");
        servisDto = new ServisDto(1L, "Auto servis", "Bulevar 1", "0111234567");
    }

    @AfterEach
    void tearDown() throws Exception {
        servis = null;
        servisDto = null;
    }

    @Test
    void testServisServis() {
        assertNotNull(servisServis);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(servis));
        when(mapper.toDto(servis)).thenReturn(servisDto);

        List<ServisDto> rezultat = servisServis.findAll();

        assertEquals(1, rezultat.size());
        assertEquals(servisDto, rezultat.get(0));
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAllPrazno() {
        when(repo.findAll()).thenReturn(Arrays.asList());

        List<ServisDto> rezultat = servisServis.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindById() throws Exception {
        when(repo.findById(1L)).thenReturn(servis);
        when(mapper.toDto(servis)).thenReturn(servisDto);

        ServisDto rezultat = servisServis.findById(1L);

        assertEquals(servisDto, rezultat);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Servis nije pronađen!"));

        Exception ex = assertThrows(Exception.class, () -> {
            servisServis.findById(99L);
        });
        assertEquals("Servis nije pronađen!", ex.getMessage());
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(servisDto)).thenReturn(servis);
        when(mapper.toDto(servis)).thenReturn(servisDto);

        ServisDto rezultat = servisServis.create(servisDto);

        assertEquals(servisDto, rezultat);
        verify(repo, times(1)).save(servis);
    }

    @Test
    void testUpdate() throws Exception {
        when(repo.findById(1L)).thenReturn(servis);
        when(mapper.toDto(servis)).thenReturn(servisDto);

        ServisDto rezultat = servisServis.update(servisDto);

        assertEquals(servisDto, rezultat);
        verify(repo, times(1)).save(servis);
    }

    @Test
    void testUpdateNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Servis nije pronađen!"));
        servisDto = new ServisDto(99L, "Auto servis", "Bulevar 1", "0111234567");

        assertThrows(RuntimeException.class, () -> {
            servisServis.update(servisDto);
        });
    }

    @Test
    void testDeleteById() {
        servisServis.deleteById(1L);

        verify(cenovnik, times(1)).deleteByServis(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}