package com.gabrielgcosta.imagecarousel.controllers;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gabrielgcosta.imagecarousel.domain.image.Image;
import com.gabrielgcosta.imagecarousel.domain.image.ImagePath;
import com.gabrielgcosta.imagecarousel.dtos.UpdateImageDto;
import com.gabrielgcosta.imagecarousel.services.ImageService;

@RestController
@RequestMapping("image")
public class ImageController {

    @Value("${api.upload.image.storage}")
    private String imageRootPath;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image) throws IllegalStateException, IOException{
        if (!image.getContentType().startsWith("image/")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is not an image");
        }

        try {
            ImagePath imagePath = new ImagePath(imageRootPath + image.getOriginalFilename());
            System.out.println(imagePath.getPath());
            image.transferTo(new File(imagePath.getPath()));
        
            return ResponseEntity.ok().body(imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error uploading image");
        }
        
    }

    @SuppressWarnings("rawtypes")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteImage(@PathVariable UUID id) throws Exception{
        this.imageService.deleteImageById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{id}")
    public ResponseEntity<Image> updateImage(@PathVariable UUID id, @RequestBody UpdateImageDto data) throws Exception{
        Image image = this.imageService.updateImage(id, data.description());
        return new ResponseEntity<>(image, HttpStatus.OK);
    }
    
}
