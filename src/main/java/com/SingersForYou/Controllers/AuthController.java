package com.SingersForYou.Controllers;


import com.SingersForYou.Dtos.LoginRequestDTO;
import com.SingersForYou.Dtos.ResponseDTO;
import com.SingersForYou.Entities.UsuarioEntity;
import com.SingersForYou.Repositories.UsuarioRepository;
import com.SingersForYou.Service.Security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    //lombok não esta funcionando - construtor
    public AuthController(UsuarioRepository repository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }


    //endpoint de login
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDTO body) {
        UsuarioEntity usuario = this.repository.findByEmail(body.email()).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        if (passwordEncoder.matches(body.senha(),usuario.getSenha())) {//primeiro a descriptografada e depois a criptografada pra comparar
            String token = this.tokenService.GenerateToken(usuario);
            return ResponseEntity.ok(new ResponseDTO(usuario.getName(), token));
        }
        return ResponseEntity.badRequest().build(); //caso não exista vai retornar um bad request
    }


    //endpoint de registro do novo usuario
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody LoginRequestDTO body) {
        //vai pesquiser se esse usuario existe via email
        Optional<UsuarioEntity> usuario = this.repository.findByEmail(body.email());

        if (usuario.isEmpty()) {; //se usuario não existir, ou retornar vazio
        //entao realizar a criacao de um novo
        UsuarioEntity novousuario = new UsuarioEntity();
        novousuario.setSenha(passwordEncoder.encode(body.senha()));
        novousuario.setEmail(body.email());
        novousuario.setName(body.name());
        this.repository.save(novousuario);//Salvo o usuario no banco

        //criacao do token
        String token = this.tokenService.GenerateToken(novousuario);
        return ResponseEntity.ok(new ResponseDTO(novousuario.getName(), token));
    }
    return ResponseEntity.badRequest().build();// caso não der certo a criação de um novo usuario

    }
}