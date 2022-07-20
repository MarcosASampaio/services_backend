package com.soulcode.Servicos.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soulcode.Servicos.Models.User;
import com.soulcode.Servicos.Util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

// Entra em ação em toda endpoits que está protegida
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JWTUtils jwtUtils;

    public JWTAuthenticationFilter(AuthenticationManager manager, JWTUtils jwtUtils){
        this.authenticationManager = manager;
        this.jwtUtils = jwtUtils;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Tenta autenticar o usuário
        try{
            //{"login": "", "password":""}
            //extrair informações de user da request "bruta"
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
            return authenticationManager.authenticate(//chama a autenticação do spring
                    new UsernamePasswordAuthenticationToken(
                    user.getLogin(),
                    user.getPassword(),
                    new ArrayList<>()
            )
            );
        }catch(IOException io){
            //caso o Json da requisição não bater com o User.class
            throw new RuntimeException(io.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        AuthUserDetail user = (AuthUserDetail) authResult.getPrincipal();
        String token = jwtUtils.generateToken(user.getUsername()); //(token) asdasds9909897675646

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        //{Authorization": "<token>"}
        response.getWriter().write("{\"Authorization\": \"" + token + "\"}");
        response.getWriter().flush(); //termina a escrita
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // Customizar a resposta de erro do login que falhou
        response.setStatus(401);//Unauthorized
        response.setContentType("application/Json");
        response.getWriter().write(json());//mensagem de erro no body
        response.getWriter().flush();//termina a escrita
    }
    String json(){ //formatar a mensagem de erro
        long date = new Date().getTime();
        return "{"
                + "\"timestamp\": " + date + ", "
                + "\"status\": 401, "
                + "\"error\" : \"Não autorizado\", "
                + "\"message\": \"Email/senha inválidos\","
                + "\"path\": \"/login\""
                + "}";
    }
}

//Front manda {"login": "jr@gmail.com", "password": "12345"}
//A partir do JSONS -> USER
//Tenta realizar autenticação
//      caso dê:
//          -Gera o token JWT
//          -retorna o token para o FRONT
