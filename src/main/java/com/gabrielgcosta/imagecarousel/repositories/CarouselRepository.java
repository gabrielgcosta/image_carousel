package com.gabrielgcosta.imagecarousel.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gabrielgcosta.imagecarousel.domain.carousel.Carousel;
import java.util.Optional;


public interface CarouselRepository extends JpaRepository<Carousel, UUID>{

    Optional<Carousel> findById(UUID id);
    
}
