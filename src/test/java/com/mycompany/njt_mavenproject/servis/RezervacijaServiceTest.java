package com.mycompany.njt_mavenproject.servis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.njt_mavenproject.dto.impl.RezervacijaDto;
import com.mycompany.njt_mavenproject.entity.impl.Rezervacija;
import com.mycompany.njt_mavenproject.entity.impl.Servis;
import com.mycompany.njt_mavenproject.entity.impl.StatusRezervacije;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.mapper.impl.RezervacijaMapper;
import com.mycompany.njt_mavenproject.repository.impl.RezervacijaRepository;
import com.mycompany.njt_mavenproject.repository.impl.ServisUslugaRepository;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;

@ExtendWith(MockitoExtension.class)
class RezervacijaServiceTest {

    @Mock
    RezervacijaRepository repo;

    @Mock
    VlasnikRepository vlasnici;

    @Mock
    RezervacijaMapper mapper;

    @Mock
    ServisUslugaRepository cenovnik;

    @InjectMocks
    RezervacijaService rezervacijaService;

    Vlasnik vlasnik;
    Rezervacija rezervacija;
    RezervacijaDto rezervacijaDto;

    @BeforeEach
    void setUp() throws Exception {
        vlasnik = new Vlasnik(1L);
        vlasnik.setUsername("marko123");

        Servis servis = new Servis(1L);
        rezervacija = new Rezervacija(1L);
        rezervacija.setVlasnik(vlasnik);
        rezervacija.setServis(servis);
        rezervacija.setDatum(LocalDateTime.of(2025, 6, 2, 10, 0)); // ponedeljak
        rezervacija.setStatus(StatusRezervacije.CREATED);
        rezervacija.setTrajanjeMin(60);

        rezervacijaDto = new RezervacijaDto();
        rezervacijaDto.setId(1L);
        rezervacijaDto.setDatum(LocalDateTime.of(2025, 6, 2, 10, 0));
        rezervacijaDto.setStatus(StatusRezervacije.CREATED);
    }

    @AfterEach
    void tearDown() throws Exception {
        vlasnik = null;
        rezervacija = null;
        rezervacijaDto = null;
    }

    @Test
    void testRezervacijaService() {
        assertNotNull(rezervacijaService);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(Arrays.asList(rezervacija));
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        List<RezervacijaDto> rezultat = rezervacijaService.findAll();

        assertEquals(1, rezultat.size());
        assertEquals(rezervacijaDto, rezultat.get(0));
        verify(repo, times(1)).findAll();
    }

    @Test
    void testFindAllPrazno() {
        when(repo.findAll()).thenReturn(Arrays.asList());

        List<RezervacijaDto> rezultat = rezervacijaService.findAll();

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindMine() {
        when(repo.findByVlasnikId(1L)).thenReturn(Arrays.asList(rezervacija));
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        List<RezervacijaDto> rezultat = rezervacijaService.findMine(1L);

        assertEquals(1, rezultat.size());
        assertEquals(rezervacijaDto, rezultat.get(0));
        verify(repo, times(1)).findByVlasnikId(1L);
    }

    @Test
    void testFindMineByUsername() {
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findByVlasnikId(1L)).thenReturn(Arrays.asList(rezervacija));
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        List<RezervacijaDto> rezultat = rezervacijaService.findMineByUsername("marko123");

        assertEquals(1, rezultat.size());
        verify(vlasnici, times(1)).findByUsername("marko123");
    }

    @Test
    void testFindMineByUsernameNePostoji() {
        when(vlasnici.findByUsername("nepostoji")).thenReturn(null);

        List<RezervacijaDto> rezultat = rezervacijaService.findMineByUsername("nepostoji");

        assertTrue(rezultat.isEmpty());
    }

    @Test
    void testFindById() throws Exception {
        when(repo.findById(1L)).thenReturn(rezervacija);
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        RezervacijaDto rezultat = rezervacijaService.findById(1L);

        assertEquals(rezervacijaDto, rezultat);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Rezervacija nije pronađena: 99"));

        Exception ex = assertThrows(Exception.class, () -> {
            rezervacijaService.findById(99L);
        });
        assertEquals("Rezervacija nije pronađena: 99", ex.getMessage());
    }

    @Test
    void testUpdateStatus() throws Exception {
        when(repo.findById(1L)).thenReturn(rezervacija);
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        RezervacijaDto rezultat = rezervacijaService.updateStatus(1L, StatusRezervacije.CONFIRMED);

        assertEquals(StatusRezervacije.CONFIRMED, rezervacija.getStatus());
        verify(repo, times(1)).save(rezervacija);
    }

    @Test
    void testUpdateStatusNePostoji() throws Exception {
        when(repo.findById(99L)).thenThrow(new Exception("Rezervacija nije pronađena: 99"));

        assertThrows(Exception.class, () -> {
            rezervacijaService.updateStatus(99L, StatusRezervacije.CONFIRMED);
        });
    }

    @Test
    void testDeleteById() {
        rezervacijaService.deleteById(1L);
        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testCancelMy() throws Exception {
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findById(1L)).thenReturn(rezervacija);

        rezervacijaService.cancelMy(1L, "marko123");

        verify(repo, times(1)).deleteById(1L);
    }

    @Test
    void testCancelMyNepoznatKorisnik() {
        when(vlasnici.findByUsername("nepostoji")).thenReturn(null);

        assertThrows(Exception.class, () -> {
            rezervacijaService.cancelMy(1L, "nepostoji");
        });
    }

    @Test
    void testCancelMyNijeTvoja() throws Exception {
        Vlasnik drugi = new Vlasnik(2L);
        drugi.setUsername("drugi");
        when(vlasnici.findByUsername("drugi")).thenReturn(drugi);
        when(repo.findById(1L)).thenReturn(rezervacija);

        assertThrows(IllegalStateException.class, () -> {
            rezervacijaService.cancelMy(1L, "drugi");
        });
    }

    @Test
    void testCancelMyNijeCreated() throws Exception {
        rezervacija.setStatus(StatusRezervacije.CONFIRMED);
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findById(1L)).thenReturn(rezervacija);

        assertThrows(IllegalStateException.class, () -> {
            rezervacijaService.cancelMy(1L, "marko123");
        });
    }

    @Test
    void testRescheduleMy() throws Exception {
        LocalDateTime noviDatum = LocalDateTime.of(2025, 6, 3, 10, 0);
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findById(1L)).thenReturn(rezervacija);
        when(repo.existsOverlap(anyLong(), any(), any())).thenReturn(false);
        when(mapper.toDto(rezervacija)).thenReturn(rezervacijaDto);

        RezervacijaDto rezultat = rezervacijaService.rescheduleMy(1L, noviDatum, "marko123");

        assertNotNull(rezultat);
        assertEquals(noviDatum, rezervacija.getDatum());
        verify(repo, times(1)).save(rezervacija);
    }

    @Test
    void testRescheduleMyDatumNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            rezervacijaService.rescheduleMy(1L, null, "marko123");
        });
    }

    @Test
    void testRescheduleMyTerminZauzet() throws Exception {
        LocalDateTime noviDatum = LocalDateTime.of(2025, 6, 3, 10, 0);
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findById(1L)).thenReturn(rezervacija);
        when(repo.existsOverlap(anyLong(), any(), any())).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> {
            rezervacijaService.rescheduleMy(1L, noviDatum, "marko123");
        });
    }

    @Test
    void testRescheduleMyNijeCreated() throws Exception {
        rezervacija.setStatus(StatusRezervacije.CONFIRMED);
        LocalDateTime noviDatum = LocalDateTime.of(2025, 6, 3, 10, 0);
        when(vlasnici.findByUsername("marko123")).thenReturn(vlasnik);
        when(repo.findById(1L)).thenReturn(rezervacija);

        assertThrows(IllegalStateException.class, () -> {
            rezervacijaService.rescheduleMy(1L, noviDatum, "marko123");
        });
    }
}