package com.mycompany.njt_mavenproject.config;

import com.mycompany.njt_mavenproject.security.AppUserDetailsService;
import com.mycompany.njt_mavenproject.security.JwtAuthFilter;
import jakarta.servlet.DispatcherType;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Konfiguraciona klasa za Spring Security.
 * Definiše pravila pristupa endpointima, JWT autentifikaciju i CORS podešavanja.
 *
 * @author Bojana
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;
    private final AppUserDetailsService uds;

    /**
     * Konstruktor koji injektuje JWT filter i servis za učitavanje korisnika.
     *
     * @param jwtFilter JWT filter za autentifikaciju
     * @param uds       servis za učitavanje korisničkih podataka
     */
    public SecurityConfig(JwtAuthFilter jwtFilter, AppUserDetailsService uds) {
        this.jwtFilter = jwtFilter;
        this.uds = uds;
    }

    /**
     * Konfiguracija sigurnosnog lanca filtera.
     * Definiše koja ruta je javna, koja zahteva autentifikaciju i koja je dostupna samo ADMINu.
     *
     * @param http HttpSecurity objekat za konfiguraciju
     * @return konfigurisani SecurityFilterChain
     * @throws Exception u slučaju greške pri konfiguraciji
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error").permitAll()
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                .requestMatchers("/api/auth/**").permitAll()

                .requestMatchers(HttpMethod.GET,    "/api/servis/**").permitAll()
                .requestMatchers(HttpMethod.POST,   "/api/servis/**").permitAll()
                .requestMatchers(HttpMethod.PUT,    "/api/servis/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/servis/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,    "/api/usluga/**").permitAll()
                .requestMatchers(HttpMethod.POST,   "/api/usluga/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/usluga/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/usluga/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,    "/api/mehanicari/**").permitAll()
                .requestMatchers(HttpMethod.POST,   "/api/mehanicari/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,    "/api/mehanicari/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/mehanicari/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET,    "/api/racuni/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST,   "/api/racuni/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH,  "/api/racuni/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/racuni/**").hasRole("ADMIN")

                .requestMatchers("/api/rezervacija/**").authenticated()

                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Kreira DaoAuthenticationProvider sa podešenim servisom za korisnike i enkoderom lozinke.
     *
     * @return konfigurisani DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = new DaoAuthenticationProvider();
        p.setUserDetailsService(uds);
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    /**
     * Kreira BCrypt enkoder za hešovanje lozinki.
     *
     * @return PasswordEncoder implementacija
     */
    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    /**
     * Kreira AuthenticationManager iz Spring Security konfiguracije.
     *
     * @param config AuthenticationConfiguration objekat
     * @return AuthenticationManager
     * @throws Exception u slučaju greške pri kreiranju
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Kreira CORS konfiguraciju koja dozvoljava zahteve sa frontend aplikacije.
     *
     * @return CorsConfigurationSource sa podešenim pravilima
     */
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of("http://localhost:3000"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/**", cfg);
        return src;
    }
}