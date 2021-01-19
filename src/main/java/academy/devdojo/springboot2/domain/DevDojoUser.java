package academy.devdojo.springboot2.domain;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class DevDojoUser implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty(message = "The user name cannot be empty")
  private String name;

  private String username;
  private String password;
  private String authorities; // ROLE_ADMIN, ROLE_USER

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(authorities.split(","))
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList());
  }

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean isAccountNonExpired() {
    // to simplify, it is returning true
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    // to simplify, it is returning true
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    // to simplify, it is returning true
    return true;
  }

  @Override
  public boolean isEnabled() {
    // to simplify, it is returning true
    return true;
  }

}
