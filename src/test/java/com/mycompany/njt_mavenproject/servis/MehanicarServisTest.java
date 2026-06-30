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

import com.mycompany.njt_mavenproject.dto.impl.MehanicarDto;
import com.mycompany.njt_mavenproject.entity.impl.Mehanicar;
import com.mycompany.njt_mavenproject.exception.EntityNotFoundException;
import com.mycompany.njt_mavenproject.mapper.impl.MehanicarMapper;
import com.mycompany.njt_mavenproject.repository.impl.MehanicarRepository;

@ExtendWith(MockitoExtension.class)
class MehanicarServisTest {

    @Mock
    MehanicarRepository repo;

    @Mock
    MehanicarMapper mapper;

    @InjectMocks
    MehanicarServis mehanicarServis;

    Mehanicar mehanicar;
    MehanicarDto mehanicarDto;

    @BeforeEach
    void setUp() {
        mehanicar = new Mehanicar(1L);
        mehanicar.setIme("Marko");
        mehanicar.setPrezime("Markovic");
        mehanicar.setSpecijalnost("Elektrika");
        mehanicar.setTelefon("0601234567");

        mehanicarDto = new MehanicarDto();
        mehanicarDto.setId(1L);
        mehanicarDto.setIme("Marko");
        mehanicarDto.setPrezime("Markovic");
        mehanicarDto.setSpecijalnost("Elektrika");
        mehanicarDto.setTelefon("0601234567");
    }

    @AfterEach
    void tearDown(){
        mehanicar = null;
        mehanicarDto = null;
    }

    @Test
    void testMehanicarServis() {
        assertNotNull(mehanicarServis);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(mehanicar));
        when(mapper.toDto(mehanicar)).thenReturn(mehanicarDto);

        List<MehanicarDto> rezultat = mehanicarServis.findAll();

        assertEquals(1, rezultat.size());
        assertEquals(mehanicarDto, rezultat.get(0));
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAllPrazno() {
        when(repo.findAll()).thenReturn(Arrays.asList());

        List<MehanicarDto> rezultat = mehanicarServis.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindByServis() {
        when(repo.findByServisId(1L)).thenReturn(Arrays.asList(mehanicar));
        when(mapper.toDto(mehanicar)).thenReturn(mehanicarDto);

        List<MehanicarDto> rezultat = mehanicarServis.findByServis(1L);

        assertEquals(1, rezultat.size());
        assertEquals(mehanicarDto, rezultat.get(0));
        verify(repo, times(1)).findByServisId(1L);
    }

    @Test
    void testFindByServisPrazno() {
        when(repo.findByServisId(99L)).thenReturn(Arrays.asList());

        List<MehanicarDto> rezultat = mehanicarServis.findByServis(99L);

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindById() throws Exception {
        when(repo.findById(1L)).thenReturn(mehanicar);
        when(mapper.toDto(mehanicar)).thenReturn(mehanicarDto);

        MehanicarDto rezultat = mehanicarServis.findById(1L);

        assertEquals(mehanicarDto, rezultat);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new EntityNotFoundException("Mehanicar #99 ne postoji."));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            mehanicarServis.findById(99L);
        });
        assertEquals("Mehanicar #99 ne postoji.", exception.getMessage());
    }

    @Test
    void testCreate() {
        when(mapper.toEntity(mehanicarDto)).thenReturn(mehanicar);
        when(mapper.toDto(mehanicar)).thenReturn(mehanicarDto);

        MehanicarDto rezultat = mehanicarServis.create(mehanicarDto);

        assertEquals(mehanicarDto, rezultat);
        verify(repo, times(1)).save(mehanicar);
    }

    @Test
    void testUpdate() throws Exception {
        when(repo.findById(1L)).thenReturn(mehanicar);
        when(mapper.toDto(mehanicar)).thenReturn(mehanicarDto);

        MehanicarDto rezultat = mehanicarServis.update(mehanicarDto);

        assertEquals(mehanicarDto, rezultat);
        verify(repo, times(1)).save(mehanicar);
    }

    @Test
    void testUpdateNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new EntityNotFoundException("Mehanicar #99 ne postoji."));
        mehanicarDto.setId(99L);

        assertThrows(EntityNotFoundException.class, () -> {
            mehanicarServis.update(mehanicarDto);
        });
    }

    @Test
    void testDeleteById() {
        mehanicarServis.deleteById(1L);
        verify(repo, times(1)).deleteById(1L);
    }
}