package com.gabrielgcosta.imagecarousel.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabrielgcosta.imagecarousel.domain.image.Image;
import com.gabrielgcosta.imagecarousel.dtos.ImageDto;
import com.gabrielgcosta.imagecarousel.repositories.ImageRepository;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image saveImage(ImageDto image){
        Image newImage = new Image(image.path(), image.description());
        this.imageRepository.save(newImage);
        return newImage;
    }

    public Image findById(UUID id) throws Exception{
        return this.imageRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
    }

    public void deleteImage(Image image){
        this.imageRepository.delete(image);
    }

    public void deleteImageById(UUID id) throws Exception{
        Image image = this.findById(id);
        this.imageRepository.delete(image);
    }

    public Image updateImage(UUID id, String description) throws Exception{
        Image image = this.findById(id);
        image.setDescription(description);
        this.imageRepository.save(image);
        return image;
    }

    public List<Image> findAll(){
        return this.imageRepository.findAll();
    }
    
}
