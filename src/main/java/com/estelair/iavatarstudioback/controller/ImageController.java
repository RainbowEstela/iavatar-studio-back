package com.estelair.iavatarstudioback.controller;

import com.estelair.iavatarstudioback.entity.ImageEntity;
import com.estelair.iavatarstudioback.entity.UserEntity;
import com.estelair.iavatarstudioback.service.ImageEntityService;
import com.estelair.iavatarstudioback.service.UserEntityService;
import org.apache.coyote.Response;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class ImageController {
    @Value("${spring.external.token}")
    private String token;

    @Autowired
    private ImageEntityService imageEntityService;

    @Autowired
    private UserEntityService userEntityService;

    @PostMapping("/imagen/nueva")
    public ResponseEntity<?> callOpenAI(@RequestBody String peticion) {

        peticion = peticion.split("\"")[3];

        // sacar al usuario del token
        // buscar el nick del usuario
        String userString = SecurityContextHolder.getContext().getAuthentication().getName();

        // pasar el nick a objeto
        UserEntity userObject = this.userEntityService.findByUsername(userString).orElse(null);


        // LLAMAR A OPEN AI
        ResponseEntity<?> response =  ResponseEntity.internalServerError().body("Ha habido un error en la petición");

        // url
        String url = "https://api.openai.com/v1/images/generations";

        // headers
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<String,String>();
        Map<String,String> map = new HashMap<String,String>();

        map.put("Content-Type","application/json");
        map.put("Authorization","Bearer " + token);

        headers.setAll(map);

        // contenido
        Map body = new HashMap();
        body.put("model","dall-e-3");
        body.put("prompt", peticion);
        body.put("n",Integer.valueOf(1));
        body.put("size","1024x1024");

        // request
        HttpEntity<?> request = new HttpEntity<>(body,headers);

        try {
            // peticion
            response = new RestTemplate().postForEntity(url, request, String.class);
        } catch (Exception e) {
            return response; // si hay error devolvemos un error
        }

        // si tiene exito sacamos la url de la respuesta
        String responseString = response.toString();
        String imagenIA = responseString.split("\"")[11];

        // guardarmos la imagen objeto en base de datos y la imagen local
        ImageEntity imagen = new ImageEntity();
        imageEntityService.save(imagen,imagenIA,userObject,peticion);



        return ResponseEntity.ok(imagen);

    }


    @GetMapping("/imagen/todas/{userName}")
    public ResponseEntity<List<ImageEntity>> findAllByUsuario(@PathVariable String userName) {

        // buscar usuario
        UserEntity creador = this.userEntityService.findByUsername(userName).orElse(null);

        // retornar error si no existe
        if (creador == null) {
            return ResponseEntity.notFound().build();
        }

        // buscar imagenes de usuario
        List<ImageEntity> imagenes = this.imageEntityService.findByCreador(creador);

        // mandar erro si no hay
        if(imagenes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // devolver imagenes
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/imagen/todas/{userName}/favoritos")
    public ResponseEntity<List<ImageEntity>> findByUsuarioFavoritas(@PathVariable String userName) {
        // buscar usuario
        UserEntity creador = this.userEntityService.findByUsername(userName).orElse(null);

        // error si no existe
        if (creador == null) {
            return ResponseEntity.notFound().build();
        }

        // buscar imagenes
        List<ImageEntity> imagenes = this.imageEntityService.findByIdAndFavoritoSi(creador);

        // error si no existe
        if(imagenes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // devolver imagenes
        return ResponseEntity.ok(imagenes);
    }

    @GetMapping("/imagen/favorito/{idImagen}")
    public ResponseEntity<ImageEntity> changeFavoritoTrue(@PathVariable Long idImagen) {

        // buscar el nick del usuario
        String userString = SecurityContextHolder.getContext().getAuthentication().getName();

        // pasar el nick a objeto
        UserEntity userObject = this.userEntityService.findByUsername(userString).orElse(null);

        // buscar imagen
        ImageEntity imagen = this.imageEntityService.findById(idImagen).orElse(null);

        // devolver error
        if(imagen == null) {
            return ResponseEntity.notFound().build();
        }

        // comprobar que el autor es la misma persona que pide el cambio
        if(!imagen.getCreador().getId().equals(userObject.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // modificar el estado
        this.imageEntityService.hacerFavorito(imagen);

        // devolver la imagen
        return ResponseEntity.ok(imagen);
    }

    @GetMapping("/imagen/desfavorito/{idImagen}")
    public ResponseEntity<ImageEntity> changeFavoritoFalse(@PathVariable Long idImagen) {

        // buscar el nick del usuario
        String userString = SecurityContextHolder.getContext().getAuthentication().getName();

        // pasar el nick a objeto
        UserEntity userObject = this.userEntityService.findByUsername(userString).orElse(null);

        // buscar imagen
        ImageEntity imagen = this.imageEntityService.findById(idImagen).orElse(null);

        // devolver error
        if(imagen == null) {
            return ResponseEntity.notFound().build();
        }

        // comprobar que el autor es la misma persona que pide el cambio
        if(!imagen.getCreador().getId().equals(userObject.getId())) {
            return ResponseEntity.badRequest().build();
        }

        // modificar el estado
        this.imageEntityService.deshacerFavorito(imagen);

        // devolver la imagen
        return ResponseEntity.ok(imagen);
    }
}
