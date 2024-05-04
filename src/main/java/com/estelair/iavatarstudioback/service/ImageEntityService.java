package com.estelair.iavatarstudioback.service;

import com.estelair.iavatarstudioback.entity.ImageEntity;
import com.estelair.iavatarstudioback.repository.ImageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageEntityService {

    @Autowired
    public ImageEntityRepository imageEntityRepository;


    // Operaciones CRUD
    public List<ImageEntity> findAll() {
        return imageEntityRepository.findAll();
    }

    public Optional<ImageEntity> findById(Long id) {
        return imageEntityRepository.findById(id);
    }

    public ImageEntity save(ImageEntity image) {
        imageEntityRepository.save(image);

        // guardar la url como imagen local


        // cambiar la url por el nombre de la imagen

        return imageEntityRepository.save(image);
    }

    public void deteleById(Long id) {
        imageEntityRepository.deleteById(id);
    }
}
