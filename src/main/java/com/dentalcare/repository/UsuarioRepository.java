package com.dentalcare.repository;

import com.dentalcare.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {
    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol WHERE u.email = :email")
    Optional<Usuario> findByEmailWithRol(@Param("email") String email);

    Optional<Usuario> findByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Usuario> findByActivoTrue();
}
