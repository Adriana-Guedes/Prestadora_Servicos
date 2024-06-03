package com.SingersForYou.Service.Security;


import com.SingersForYou.Entities.UsuarioEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // testa o tempo da funcao em milessegundos

    @Value("{api.security.token.secret}") // esse valor estará no app.properties
    private  String secret;

    public String GenerateToken(UsuarioEntity usuario){
        try {

            /*algoritimo para gerar o token esse abaixo o hash +
            (chave privada para criptografar e descriptografar - salvo no properties)
             */
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer("SingerForYou") //aplicacao que esta gerando o token
                    .withSubject(usuario.getEmail()) //retorna o email
                    .withExpiresAt(this.generateExpirationDate())//funcao de tempo de expiracao
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erros while Authenticating");

        }

    }


    //funcao validacao token
    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return  JWT.require(algorithm)
                    .withIssuer("SingerForYou")
                    .build()//monta o objeto para verificacao
                    .verify(token)
                    .getSubject();
            //verifica erro de validacão do token
        } catch (JWTVerificationException exception){
            return null;
        }

    }

    //funcao para controlar tempo para expirar o token
    private Instant generateExpirationDate(){
        //tempo de agora + 2 horas de plus + conversão em instante
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));


    }



}
