package com.soulcode.Servicos.Security;

import com.soulcode.Servicos.Util.JWTUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    private JWTUtils jwtUtils;
    public JWTAuthorizationFilter(AuthenticationManager manager, JWTUtils jwtUtils){
        super(manager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = request.getHeader("Authorization"); //bearer dsadsad234234234(token)
        if(token != null && token.startsWith("Bearer")){ //token "válido"
            UsernamePasswordAuthenticationToken authToken = getAuthetication(token.substring(7));
            if(authToken != null){
                SecurityContextHolder.getContext().setAuthentication(authToken);
                //SecutiryContextHolder.getContext().getAuthetication();
                // Guarda informações do usuário autenticado no contexto do Spring
                // Essa informação pode ser utilizada dentro dos controllers da aplicação
            }
            // concluir autorização
        }
        chain.doFilter(request, response);
    }

    public UsernamePasswordAuthenticationToken getAuthetication(String token){
        String login = jwtUtils.getLogin(token); //extrai o login do subject
        if(login == null){
            return null;
        }

        return new UsernamePasswordAuthenticationToken(login, null, new ArrayList<>());
    }

}
