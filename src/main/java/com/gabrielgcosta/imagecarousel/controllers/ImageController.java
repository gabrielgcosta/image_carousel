package com.gabrielgcosta.imagecarousel.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("image")
public class ImageController {

    @Value("${api.upload.image.storage}")
    private String imageRootPath;

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload-image")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image, HttpServletRequest request) throws IllegalStateException, IOException{
        if (!image.getContentType().startsWith("image/")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The file is not an image");
        }

        try {
            Path projectPath = Paths.get("").toAbsolutePath();
            ImagePath imagePath = new ImagePath(imageRootPath + image.getOriginalFilename());
            image.transferTo(new File(projectPath + imagePath.getPath()));
        
            return ResponseEntity.ok().body(imagePath);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(e.getMessage());
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

    @GetMapping()
    public ResponseEntity<List<Image>> getImages(){
        List<Image> images = this.imageService.findAll();
        return ResponseEntity.ok().body(images);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Image> getImages(@PathVariable UUID id) throws Exception{
        Image image = this.imageService.findById(id);
        return ResponseEntity.ok().body(image);
    }
    
}
