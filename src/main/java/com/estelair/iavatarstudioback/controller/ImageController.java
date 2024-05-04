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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;

@RestController
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
            return response;
        }

        return ResponseEntity.ok(response.getBody()) ;
    }

    @PostMapping("/imagen/guardar")
    public ResponseEntity<ImageEntity> guardar(@RequestBody ImageEntity image) {
        // buscar el nick del usuario
        String userString = SecurityContextHolder.getContext().getAuthentication().getName();

        // pasar el nick a objeto
        UserEntity userObject = this.userEntityService.findByUsername(userString).orElse(null);

        // guardar el usuario objeto en imagen
        image.setCreador(userObject);

        // guardar la imagen objeto
        this.imageEntityService.save(image);
        return ResponseEntity.ok(image);
    }

}
