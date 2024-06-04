package com.estelair.iavatarstudioback.repository;

import com.estelair.iavatarstudioback.entity.ImageEntity;
import com.estelair.iavatarstudioback.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ImageEntityRepository extends JpaRepository<ImageEntity,Long> {
    List<ImageEntity> findByCreador(UserEntity creador);
    List<ImageEntity> findByCreadorAndFavorito (UserEntity creador, Boolean favorito);
    List<ImageEntity> findByCreadorAndFechaCreacion (UserEntity creador, LocalDate fecha);
}
