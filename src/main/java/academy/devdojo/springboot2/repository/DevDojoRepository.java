package academy.devdojo.springboot2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.devdojo.springboot2.domain.DevDojoUser;

public interface DevDojoRepository extends JpaRepository<DevDojoUser, Long> {
  
  DevDojoUser findByUsername(String username);
  
}
