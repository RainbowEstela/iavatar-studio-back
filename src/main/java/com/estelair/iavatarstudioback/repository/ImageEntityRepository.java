package com.estelair.iavatarstudioback.repository;

import com.estelair.iavatarstudioback.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageEntityRepository extends JpaRepository<ImageEntity,Long> {

}
