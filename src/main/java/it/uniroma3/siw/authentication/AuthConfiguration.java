
package it.uniroma3.siw.authentication;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AuthConfiguration {

  public static final String DEFAULT_ROLE = "DEFAULT";
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String CUOCO_ROLE = "CUOCO";
@Autowired
  private DataSource dataSource;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
        .dataSource(dataSource)
        .authoritiesByUsernameQuery("SELECT username, role FROM credentials WHERE username=?")
        .usersByUsernameQuery("SELECT username, password, 1 as enabled FROM credentials WHERE username=?");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected SecurityFilterChain configure(final HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf().disable()
        .cors().disable()
        .authorizeHttpRequests()
            // chiunque (autenticato o no) può accedere alle pagine index, login, register, ai css e alle immagini
            .requestMatchers(HttpMethod.GET, "/", "/generico/**", "/generico/", "/css/**", "/images/**", "favicon.ico").permitAll()
            // chiunque (autenticato o no) può mandare richieste POST al punto di accesso per login e register 
            .requestMatchers(HttpMethod.POST, "/generico/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/cuoco/aggiungiRicetta/","/cuoco/aggiungiIngrediente.html/","/admin/aggiornaRicetta/**").hasAnyAuthority(CUOCO_ROLE,ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, "/cuoco/nuovaRicetta/**","/cuoco/nuovoIngrediente/","/cuoco/aggiornaRicetta/**","cuoco/nuovoIngrediente","/cuoco/nuovaRicetta").hasAnyAuthority(CUOCO_ROLE,ADMIN_ROLE)

            .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
            .requestMatchers(HttpMethod.POST, "/admin/**").hasAnyAuthority(ADMIN_ROLE)
            // solo gli amministratori possono accedere alle pagine admin
            .requestMatchers(HttpMethod.GET, "/cuoco/**").hasAnyAuthority(CUOCO_ROLE)
            .requestMatchers(HttpMethod.POST, "/cuoco/**").hasAnyAuthority(CUOCO_ROLE)
            
            // tutti gli utenti autenticati possono accedere alle pagine rimanenti 
            .anyRequest().authenticated()
        // LOGIN: qui definiamo il login
        .and()
        .formLogin()
            .loginPage("/login")
            .permitAll()
            .defaultSuccessUrl("/success", true)
            .failureUrl("/login?error=true")
        // LOGOUT: qui definiamo il logout
        .and()
        .logout()
            .logoutUrl("/logout")
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .clearAuthentication(true)
            .permitAll();
    return httpSecurity.build();
  }
}

