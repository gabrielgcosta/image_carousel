package com.gabrielgcosta.imagecarousel.domain.carousel;

import java.util.List;
import java.util.UUID;

import com.gabrielgcosta.imagecarousel.domain.image.Image;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "carousels")
public class Carousel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;
    
    @OneToMany
    @JoinColumn(name = "image_id")
    private List<Image> images;
    
}
