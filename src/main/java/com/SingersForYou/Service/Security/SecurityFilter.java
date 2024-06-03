package com.SingersForYou.Service.Security;


import com.SingersForYou.Entities.UsuarioEntity;
import com.SingersForYou.Repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;


@Component  //ele executa uma vez pra casa request que chegar na api
public class SecurityFilter extends OncePerRequestFilter {


    @Autowired
    TokenService tokenService;

    @Autowired
    UsuarioRepository usuarioRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token); //ele retorna o email

        if(login != null){
            UsuarioEntity usuario = usuarioRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));//busca o email no banco de dados
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
            //objeto de authenticacao e as roles dele
            var authentication = new UsernamePasswordAuthenticationToken(usuario,null, authorities);
            //contexto de seguranca do spring security ( para saber o que ja falidou ou não)
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    /* recebe o request do usuario, pega o request authorization
    * O objetivo deste método é recuperar e retornar o token de autorização da solicitação HTTP.*/
    private String recoverToken(HttpServletRequest request){
        var authHeader = request.getHeader("Authorization");
        if(authHeader == null) return  null;
        /*uthorization" após remover a parte "Bearer " dele. A string "Bearer " é removida usando o método replace(),
         que substitui todas as ocorrências de "Bearer " por uma string vazia, deixando apenas o token. */
        return authHeader.replace("Bearer ", "");
        /*"Bearer ", com espaço após a palavra, você garante que somente o prefixo exato "Bearer " seja removido, deixando apenas o token como esperado. */
    }
}

/* Authorization : Bearer EJNDUIKSJID21S52D44
    entao a funcao vai pegar o token, deixar vazio onde vem escrito bearer e pega só o token
 */