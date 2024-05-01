package com.estelair.iavatarstudioback.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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


}
