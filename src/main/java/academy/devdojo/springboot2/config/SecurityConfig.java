package academy.devdojo.springboot2.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import lombok.extern.log4j.Log4j2;

@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  
  // there exists other filters, such as UsernamePasswordAuthenticationFilter

  // class that generation default login page => DefaultLoginPageGeneratingFilter
  // class that generation default logout page => DefaultLogoutPageGeneratingFilter

  /**
   * BasicAuthenticationFilter
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        // .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
        .authorizeRequests()
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

    log.info("Password encoded {}", passwordEncoder.encode("test"));

    auth.inMemoryAuthentication()
            .withUser("AdminUser")
            .password(passwordEncoder.encode("admin-pass"))
            .roles("USER", "ADMIN")
            .and()
            .withUser("devdojo")
            .password(passwordEncoder.encode("devdojo-pass"))
            .roles("USER");
  }
}
