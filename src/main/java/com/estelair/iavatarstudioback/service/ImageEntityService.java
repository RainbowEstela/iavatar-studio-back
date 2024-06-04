package com.estelair.iavatarstudioback.service;

import com.estelair.iavatarstudioback.entity.ImageEntity;
import com.estelair.iavatarstudioback.entity.UserEntity;
import com.estelair.iavatarstudioback.repository.ImageEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageEntityService {

    @Autowired
    public ImageEntityRepository imageEntityRepository;

    @Value("${spring.external.directory}")
    private String directorio;


    // Operaciones CRUD
    public List<ImageEntity> findAll() {
        return imageEntityRepository.findAll();
    }

    public Optional<ImageEntity> findById(Long id) {
        return imageEntityRepository.findById(id);
    }

    public void deteleById(Long id) {
        imageEntityRepository.deleteById(id);
    }

    public ImageEntity save(ImageEntity image, String imagenGuardar, UserEntity creador, String prompt) {
        imageEntityRepository.save(image);

        image.setCreador(creador);
        image.setFavorito(false);
        image.setFechaCreacion(LocalDate.now());
        image.setPrompt(prompt);

        // guardar la url como imagen local
        try {
            URI uri = URI.create(imagenGuardar);
            URL url = uri.toURL();

            InputStream inputStream = url.openStream();
            Files.copy(inputStream, Paths.get(directorio + image.getId() +".png"));

        } catch ( Exception e) {

        }


        // cambiar la url por el nombre de la imagen
        image.setImagenNombre(image.getId() + ".png");

        return imageEntityRepository.save(image);
    }

    // Busca las imagenes de un usuario
    public List<ImageEntity> findByCreador(UserEntity creador) {
        return imageEntityRepository.findByCreador(creador);
    }

    // Busca las imagenes favoritas de un usuario
    public List<ImageEntity> findByIdAndFavoritoSi(UserEntity creador) {
        return imageEntityRepository.findByCreadorAndFavorito(creador, true);
    }

    // hace una imagen favorita
    public ImageEntity hacerFavorito(ImageEntity imagen) {
        imagen.setFavorito(true);
        imageEntityRepository.save(imagen);

        return imagen;
    }

    public ImageEntity deshacerFavorito(ImageEntity imagen) {
        imagen.setFavorito(false);
        imageEntityRepository.save(imagen);

        return imagen;
    }

    // calcula las peticiones en el dia de hoy y las devuelve
    public Integer peticionesDeHoy(UserEntity creador) {
        List<ImageEntity> imagenesHoy = this.imageEntityRepository.findByCreadorAndFechaCreacion(creador, LocalDate.now());

        return imagenesHoy.size();
    }
}
