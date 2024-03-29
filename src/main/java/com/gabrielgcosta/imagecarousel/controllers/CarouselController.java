package com.gabrielgcosta.imagecarousel.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielgcosta.imagecarousel.domain.carousel.Carousel;
import com.gabrielgcosta.imagecarousel.domain.image.Image;
import com.gabrielgcosta.imagecarousel.dtos.AlterOrderDto;
import com.gabrielgcosta.imagecarousel.dtos.CarouselDto;
import com.gabrielgcosta.imagecarousel.dtos.ImageDto;
import com.gabrielgcosta.imagecarousel.services.CarouselService;
import com.gabrielgcosta.imagecarousel.services.ImageService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private ImageService imageService;

    @PostMapping()
    public ResponseEntity<Carousel> createCarousel(@RequestBody CarouselDto data) {
        Carousel newCarousel = this.carouselService.createCarousel(data);
        return new ResponseEntity<>(newCarousel, HttpStatus.CREATED);
    }

    @GetMapping()
    public ResponseEntity<List<Carousel>> getCarousels(){
        List<Carousel> carousels = this.carouselService.getCarousels();
        return ResponseEntity.ok().body(carousels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carousel> getCarousel(@PathVariable UUID id) throws Exception{
        Carousel carousel = this.carouselService.findById(id);
        return ResponseEntity.ok().body(carousel);
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteCarousel(@PathVariable UUID id) throws Exception{
        this.carouselService.deleteCarouselById(id);
        return new ResponseEntity<>("Carousel deleted sucefully",HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Carousel> addImage(@PathVariable UUID id, @RequestBody ImageDto data) throws Exception{
        Image newImage = this.imageService.saveImage(data);
        Carousel carousel = this.carouselService.findById(id);

        carousel.getImages().add(newImage);
        this.carouselService.saveCarousel(carousel);
        return new ResponseEntity<>(carousel, HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping("/alter-order")
    public ResponseEntity<?> alterOrder(@RequestBody AlterOrderDto data) throws Exception{

        Carousel carousel = this.carouselService.findById(data.carouselId());
        Integer indexImage1 = null;
        Integer indexImage2 = null;
        Image auxImage = new Image();
        for(int i = 0; i < carousel.getImages().size(); i++){

            if(carousel.getImages().get(i).getId().equals(data.idImage1())){
                indexImage1 = i;
            }else if(carousel.getImages().get(i).getId().equals(data.idImage2())){
                indexImage2 = i;
            }
        }
        if(indexImage1 != null && indexImage2 != null){
            auxImage = carousel.getImages().get(indexImage1);
            carousel.getImages().set(indexImage1, carousel.getImages().get(indexImage2));
            carousel.getImages().set(indexImage2, auxImage);
            this.carouselService.saveCarousel(carousel);
            return ResponseEntity.ok().body(carousel);
        }
        return ResponseEntity.badRequest().body("Image was not found");

    }
    
    
}
