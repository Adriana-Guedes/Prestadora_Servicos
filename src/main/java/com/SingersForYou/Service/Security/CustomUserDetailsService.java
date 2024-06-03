package com.SingersForYou.Service.Security;

import com.SingersForYou.Entities.UsuarioEntity;
import com.SingersForYou.Repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
//implementa esse metodo do security e sobreescreve ele
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Override //metodo obrigatŕio
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //procurar o usuario
        UsuarioEntity user = this.repository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        //obrigatorio retornar um userDetails
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getSenha(), new ArrayList<>());
    }
}
