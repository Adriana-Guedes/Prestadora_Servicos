package com.SingersForYou.Controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


//classe para testar se essas informações irão ser acessadas somente com o token



@RestController
@RequestMapping("/user")
public class UsuarioController {

    @GetMapping
    public ResponseEntity<String> getUsuario() {
        return ResponseEntity.ok("Sucesso de conexão");
    }
}
