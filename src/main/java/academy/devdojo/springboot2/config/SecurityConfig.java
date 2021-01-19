package academy.devdojo.springboot2.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import academy.devdojo.springboot2.service.DevDojoUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
@SuppressWarnings("java:S5344")
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  private final DevDojoUserDetailsService devDojoUserDetailsService;
  
  // there exists other filters, such as UsernamePasswordAuthenticationFilter

  // class that generation default login page => DefaultLoginPageGeneratingFilter
  // class that generation default logout page => DefaultLogoutPageGeneratingFilter
  // FilterSecurityInterceptor: verifies if you are authorized

  /**
   * BasicAuthenticationFilter
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
          .antMatchers("/animes/admin/**").hasRole("ADMIN")
          .antMatchers("/animes/**").hasRole("USER")
          .anyRequest()
          .authenticated()
          .and()
          .formLogin()
          .and()
          .httpBasic();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    PasswordEncoder passwordEncoder= PasswordEncoderFactories.createDelegatingPasswordEncoder();

    log.info("Password encoded {}", passwordEncoder.encode("devdojo-pass"));

    auth.inMemoryAuthentication()
            .withUser("admin2")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER", "ADMIN")
            .and()
            .withUser("devdojo2")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER");

    auth.userDetailsService(devDojoUserDetailsService)
          .passwordEncoder(passwordEncoder);
  }
}
