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
//    http
//        // 1. Autorización: Permitir tod0
//        .authorizeHttpRequests(auth -> auth
//            .anyRequest().permitAll()
//        )
//        // 2. Desactivar protecciones que molestan en desarrollo (CSRF)
//        .csrf(csrf -> csrf.disable())
//        // 3. Desactivar el Formulario de Login automático
//        .formLogin(form -> form.disable())
//        // 4. Desactivar Logout automático
//        .logout(logout -> logout.disable());
    http
        .authorizeHttpRequests(auth -> auth
            // 1. Recursos estáticos (CSS, JS, Imágenes) -> PÚBLICOS
            .requestMatchers("/css/**", "/js/**", "/shared/**", "/images/**").permitAll()

            // 2. Páginas de Acceso (Landing, Explorar, Colecciones, Mapa) -> PÚBLICAS
            .requestMatchers("/", "/landing", "/explore", "/collections", "/map").permitAll() //! no se por qué a veces aparece http://localhost:8085/static, asumo que es un error y no debería pasar, por eso no la voy a poner como pública

            // 3. Páginas de Autenticación (Login, Registro) -> PÚBLICAS
            .requestMatchers("/login", "/register").permitAll()

            .requestMatchers("/modals/**").permitAll() //! Hay que permitir solo las modales que sean públicas (y securizar las otras)

            .requestMatchers("/category-input-fragment", "/location-input-fragment").permitAll() //!


            // 4. Páginas de ERRORES -> PÚBLICAS
            .requestMatchers("/403", "/404", "/error").permitAll()

            // 5. Lo demás -> REQUIERE LOGIN
            .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf //! Esto es temporal, hay que ver como hacerlo bien porque si se usa irresponsablemente deja una SUPER vulnerabilidad en el sistema (Cross Site Request Forgery).
            .ignoringRequestMatchers("/category-input-fragment", "/location-input-fragment")
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