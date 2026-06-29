package com.mycompany.njt_mavenproject.servis;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mycompany.njt_mavenproject.dto.impl.AuthResponse;
import com.mycompany.njt_mavenproject.dto.impl.LoginRequest;
import com.mycompany.njt_mavenproject.dto.impl.RegisterRequest;
import com.mycompany.njt_mavenproject.dto.impl.VlasnikDto;
import com.mycompany.njt_mavenproject.entity.impl.PasswordResetToken;
import com.mycompany.njt_mavenproject.entity.impl.Uloga;
import com.mycompany.njt_mavenproject.entity.impl.Vlasnik;
import com.mycompany.njt_mavenproject.mapper.impl.VlasnikMapper;
import com.mycompany.njt_mavenproject.repository.impl.PasswordResetTokenRepository;
import com.mycompany.njt_mavenproject.repository.impl.VerificationTokenRepository;
import com.mycompany.njt_mavenproject.repository.impl.VlasnikRepository;
import com.mycompany.njt_mavenproject.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    AuthenticationManager authManager;

    @Mock
    JwtService jwt;

    @Mock
    VlasnikRepository users;

    @Mock
    VerificationTokenRepository tokens;

    @Mock
    PasswordEncoder encoder;

    @Mock
    VlasnikMapper userMapper;

    @Mock
    PasswordResetTokenRepository resetTokens;

    @Mock
    MailService mail;

    @InjectMocks
    AuthService authService;

    Vlasnik vlasnik;
    VlasnikDto vlasnikDto;
    RegisterRequest registerRequest;
    LoginRequest loginRequest;

    @BeforeEach
    void setUp() throws Exception {
        vlasnik = new Vlasnik(1L, "Marko", "Markovic", "marko@gmail.com", "marko123", "hash123");
        vlasnik.setUloga(Uloga.VLASNIK);
        vlasnik.setEnabled(true);

        vlasnikDto = new VlasnikDto();
        vlasnikDto.setId(1L);
        vlasnikDto.setIme("Marko");
        vlasnikDto.setPrezime("Markovic");
        vlasnikDto.setUsername("marko123");
        vlasnikDto.setEmail("marko@gmail.com");

        registerRequest = new RegisterRequest();
        registerRequest.setIme("Marko");
        registerRequest.setPrezime("Markovic");
        registerRequest.setUsername("marko123");
        registerRequest.setEmail("marko@gmail.com");
        registerRequest.setLozinka("lozinka123");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("marko123");
        loginRequest.setLozinka("lozinka123");
    }

    @AfterEach
    void tearDown() throws Exception {
        vlasnik = null;
        vlasnikDto = null;
        registerRequest = null;
        loginRequest = null;
    }

    @Test
    void testAuthService() {
        assertNotNull(authService);
    }

    @Test
    void testRegister() throws Exception {
        when(users.existsByUsername("marko123")).thenReturn(false);
        when(users.existsByEmail("marko@gmail.com")).thenReturn(false);
        when(encoder.encode("lozinka123")).thenReturn("hash123");
        when(userMapper.toDto(any(Vlasnik.class))).thenReturn(vlasnikDto);

        VlasnikDto rezultat = authService.register(registerRequest);

        assertNotNull(rezultat);
        assertEquals(vlasnikDto, rezultat);
        verify(users, times(1)).save(any(Vlasnik.class));
        verify(mail, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void testRegisterUsernameZauzet() {
        when(users.existsByUsername("marko123")).thenReturn(true);

        Exception ex = assertThrows(Exception.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("Username already taken", ex.getMessage());
        verify(users, never()).save(any());
    }

    @Test
    void testRegisterEmailZauzet() {
        when(users.existsByUsername("marko123")).thenReturn(false);
        when(users.existsByEmail("marko@gmail.com")).thenReturn(true);

        Exception ex = assertThrows(Exception.class, () -> {
            authService.register(registerRequest);
        });
        assertEquals("Email already taken", ex.getMessage());
        verify(users, never()).save(any());
    }

    @Test
    void testLogin() {
        Authentication auth = mock(Authentication.class);
        org.springframework.security.core.userdetails.User userDetails =
            mock(org.springframework.security.core.userdetails.User.class);

        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(users.findByUsername("marko123")).thenReturn(vlasnik);
        when(jwt.generate(any(), any())).thenReturn("jwt-token");
        when(userMapper.toDto(vlasnik)).thenReturn(vlasnikDto);

        AuthResponse rezultat = authService.login(loginRequest);

        assertNotNull(rezultat);
        assertEquals("jwt-token", rezultat.getToken());
        verify(authManager, times(1)).authenticate(any());
    }

    @Test
    void testRequestPasswordResetEmailPostoji() {
        when(users.findByEmail("marko@gmail.com")).thenReturn(vlasnik);

        authService.requestPasswordReset("marko@gmail.com");

        verify(resetTokens, times(1)).save(any(PasswordResetToken.class));
        verify(mail, times(1)).sendHtml(anyString(), anyString(), anyString());
    }

    @Test
    void testRequestPasswordResetEmailNePostoji() {
        when(users.findByEmail("nepostoji@gmail.com")).thenReturn(null);

        authService.requestPasswordReset("nepostoji@gmail.com");

        verify(resetTokens, never()).save(any());
        verify(mail, never()).sendHtml(anyString(), anyString(), anyString());
    }

    @Test
    void testResetPassword() {
        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(resetTokens.find("validtoken")).thenReturn(prt);
        when(prt.isUsed()).thenReturn(false);
        when(prt.isExpired()).thenReturn(false);
        when(prt.getVlasnik()).thenReturn(vlasnik);
        when(encoder.encode("novaLozinka")).thenReturn("noviHash");

        authService.resetPassword("validtoken", "novaLozinka");

        verify(users, times(1)).save(vlasnik);
        verify(prt, times(1)).setUsed(true);
        verify(resetTokens, times(1)).save(prt);
    }

    @Test
    void testResetPasswordTokenNull() {
        when(resetTokens.find("lostoken")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.resetPassword("lostoken", "novaLozinka");
        });
        assertEquals("Neispravan ili istekao token.", ex.getMessage());
    }

    @Test
    void testResetPasswordTokenIstekao() {
        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(resetTokens.find("istekaotoken")).thenReturn(prt);
        when(prt.isUsed()).thenReturn(false);
        when(prt.isExpired()).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.resetPassword("istekaotoken", "novaLozinka");
        });
        assertEquals("Neispravan ili istekao token.", ex.getMessage());
    }

    @Test
    void testResetPasswordTokenIskoriscen() {
        PasswordResetToken prt = mock(PasswordResetToken.class);
        when(resetTokens.find("iskoriscentoken")).thenReturn(prt);
        when(prt.isUsed()).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            authService.resetPassword("iskoriscentoken", "novaLozinka");
        });
        assertEquals("Neispravan ili istekao token.", ex.getMessage());
    }
}