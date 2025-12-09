package interfaz.grafica.ddsi.config;

import interfaz.grafica.ddsi.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomAuthProvider customAuthProvider;

  public SecurityConfig(CustomAuthProvider customAuthProvider) {
    this.customAuthProvider = customAuthProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // Recursos estáticos (CSS, JS, Imágenes) -> PÚBLICOS
            .requestMatchers("/css/**", "/js/**", "/shared/**", "/images/**").permitAll()

            // Páginas de Acceso (Landing, Explorar, Colecciones, Mapa) -> PÚBLICAS
            .requestMatchers("/", "/landing", "/explore", "/collections", "/map").permitAll() //! no se por qué a veces aparece http://localhost:8085/static, asumo que es un error y no debería pasar, por eso no la voy a poner como pública

            // Páginas de Autenticación (Login, Registro) -> PÚBLICAS
            .requestMatchers("/login", "/register").permitAll()

            // Páginas de MODALES -> PÚBLICAS
            .requestMatchers("/modals/**").permitAll() //! Hay que permitir solo las modales que sean públicas (y securizar las otras)

            // Endpoints para fragments usados en AJAX -> PÚBLICOS
            .requestMatchers("/category-input-fragment", "/location-input-fragment").permitAll() //!

            // POSTS
            .requestMatchers("/share/**").permitAll() //! Hay que permitir solo los POSTS que sean públicos (y securizar los otros)

            // Páginas de ERRORES -> PÚBLICAS
            .requestMatchers("/403", "/404", "/error").permitAll()

            // Lo demás -> REQUIERE LOGIN
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")        // TODO: La URL de tu controlador que muestra el HTML
            .loginProcessingUrl("/login") // TODO: La URL donde el HTML hace POST (Spring lo intercepta)
            .defaultSuccessUrl("/explore", true) // A dónde ir si sale bien (ej: Home o Mapa)
            .failureUrl("/login?error=true") // TODO: A dónde ir si falla
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/login?logout")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .exceptionHandling(ex -> ex
          // Usuario autenticado pero sin permisos → redirigir a página de error
          .accessDeniedHandler((request, response, accessDeniedException) ->
              response.sendRedirect("/403")
          )
        );

    return http.build();
  }

  @Bean
  public AuthenticationManager authManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
        http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder.authenticationProvider(customAuthProvider);
    return authenticationManagerBuilder.build();
  }
}