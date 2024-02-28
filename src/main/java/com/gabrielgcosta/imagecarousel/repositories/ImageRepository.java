package com.gabrielgcosta.imagecarousel.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielgcosta.imagecarousel.domain.image.Image;

public interface ImageRepository extends JpaRepository<Image, UUID> {

    Optional<Image> findById(UUID id);
    
}
