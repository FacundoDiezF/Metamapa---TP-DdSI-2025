package interfaz.grafica.ddsi.config;

import interfaz.grafica.ddsi.providers.CustomAuthProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomAuthProvider customAuthProvider;

  public SecurityConfig(CustomAuthProvider customAuthProvider) {
    this.customAuthProvider = customAuthProvider;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth
            // 1. Recursos estáticos (CSS, JS, Imágenes) -> PÚBLICOS
            .requestMatchers("/css/**", "/js/**", "/shared/**", "/images/**").permitAll()

            // 2. Páginas de Acceso (Login, Register, Home pública) -> PÚBLICAS
            .requestMatchers("/login", "/register", "/home", "/").permitAll()

            // 3. Lo demás -> REQUIERE LOGIN
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/login")        // TODO: La URL de tu controlador que muestra el HTML
            .loginProcessingUrl("/login") // TODO: La URL donde el HTML hace POST (Spring lo intercepta)
            .defaultSuccessUrl("/", true) // A dónde ir si sale bien (ej: Home o Mapa)
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
          // Usuario no autenticado → redirigir a login
          .authenticationEntryPoint((request, response, authException) ->
              response.sendRedirect("/login?unauthorized")
          )
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