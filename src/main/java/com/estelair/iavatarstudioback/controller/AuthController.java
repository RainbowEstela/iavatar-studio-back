package com.estelair.iavatarstudioback.controller;


import com.estelair.iavatarstudioback.dto.LoginRequest;
import com.estelair.iavatarstudioback.dto.LoginResponse;
import com.estelair.iavatarstudioback.dto.UserRegisterDTO;
import com.estelair.iavatarstudioback.entity.UserEntity;
import com.estelair.iavatarstudioback.security.JwtTokenProvider;
import com.estelair.iavatarstudioback.service.UserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserEntityService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authManager;


    @PostMapping("/auth/register")
    public UserEntity save(@RequestBody UserRegisterDTO userDTO){
        return this.userService.save(userDTO);
    }

    @PostMapping("/auth/login")
    public LoginResponse login(@RequestBody LoginRequest loginDTO){

        Authentication authDTO = new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password());

        //Este método es el que llama al AuthenticationManager correspondiente para ver si la autenticación es correcta
        Authentication authentication = this.authManager.authenticate(authDTO);

        //El método nos devuelve un UserEntity (con UserDetailService) para con esos datos generar el token
        UserEntity user = (UserEntity) authentication.getPrincipal();

        String token = this.jwtTokenProvider.generateToken(authentication);

        return new LoginResponse(user.getUsername(),
                user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(),
                token);
    }
}