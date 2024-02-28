package com.gabrielgcosta.imagecarousel.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielgcosta.imagecarousel.domain.carousel.Carousel;
import com.gabrielgcosta.imagecarousel.domain.image.Image;
import com.gabrielgcosta.imagecarousel.dtos.CarouselDto;
import com.gabrielgcosta.imagecarousel.repositories.CarouselRepository;

@Service
public class CarouselService {

    @Autowired
    CarouselRepository carouselRepository;

    @Autowired
    ImageService imageService;

    public Carousel createCarousel(CarouselDto data){
        List<Image> imagesList = new ArrayList<>();

        for(int i = 0; i < data.images().size(); i++){

            Image newImage = this.imageService.saveImage(data.images().get(i));
            imagesList.add(newImage);

        }

        Carousel newCarousel = new Carousel(data.title(), imagesList);
        carouselRepository.save(newCarousel);

        return newCarousel;
    }

    public List<Carousel> getCarousels(){
        return this.carouselRepository.findAll();
    }

    public Carousel findById(UUID id) throws Exception{
        return this.carouselRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void deleteCarouselById(UUID id) throws Exception{
        Carousel carousel = this.findById(id);
        for(int i=0; i<carousel.getImages().size(); i++){
            Image image = carousel.getImages().get(i);
            this.imageService.deleteImage(image);
        }
        this.carouselRepository.delete(carousel);
    }

    public void saveCarousel(Carousel carousel){
        this.carouselRepository.save(carousel);
    }
    
}
