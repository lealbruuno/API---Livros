package com.api.security;  // Pacote onde o filtro de autenticação JWT está localizado.

import jakarta.servlet.FilterChain;  // Importação necessária para manipular a cadeia de filtros HTTP.
import jakarta.servlet.ServletException;  // Exceção relacionada a problemas com filtros Servlet.
import jakarta.servlet.http.HttpServletRequest;  // Importação da classe para lidar com as requisições HTTP.
import jakarta.servlet.http.HttpServletResponse;  // Importação da classe para manipular a resposta HTTP.

import org.springframework.lang.NonNull;  // Anotação que indica que os parâmetros não podem ser nulos.
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;  // Classe usada para representar a autenticação do usuário no Spring Security.
import org.springframework.security.core.context.SecurityContextHolder;  // Classe que mantém o contexto de segurança global da aplicação.
import org.springframework.security.core.userdetails.User;  // Classe usada para representar o usuário autenticado no Spring Security.
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;  // Classe para obter detalhes da autenticação, como IP e endereço do cliente.
import org.springframework.stereotype.Component;  // Anotação para indicar que a classe é um componente Spring (injetável).
import org.springframework.web.filter.OncePerRequestFilter;  // Classe base para filtros que devem ser executados uma vez por requisição.

import java.io.IOException;  // Exceção relacionada a problemas de entrada e saída de dados.
import java.util.Collections;  // Utilitário para criar coleções imutáveis.

@Component  // Indica que essa classe será gerenciada pelo contexto do Spring e poderá ser injetada em outras classes.
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;  // Variável que armazenará a instância de JwtUtil, que lida com a validação do token JWT.

    // Construtor que recebe a instância de JwtUtil. O Spring cuidará da injeção dessa dependência.
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;  // Inicializa a variável jwtUtil com a instância recebida.
    }

    // O método doFilterInternal é chamado a cada requisição para que possamos processar o JWT.
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,  // Requisição HTTP, onde o filtro pode ler informações, como cabeçalhos.
            @NonNull HttpServletResponse response,  // Resposta HTTP, onde o filtro pode alterar o status ou outros parâmetros.
            @NonNull FilterChain filterChain)  // A cadeia de filtros para continuar o processamento da requisição.
            throws ServletException, IOException {  // Exceções que podem ser lançadas se houver falha ao processar a requisição.

        // Recupera o cabeçalho "Authorization" da requisição.
        // Esse cabeçalho deve conter o token JWT no formato "Bearer <token>".
        String authHeader = request.getHeader("Authorization");

        // Verifica se o cabeçalho "Authorization" existe e se começa com "Bearer " (indicação de token JWT).
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " para obter apenas o token.
            String token = authHeader.substring(7);

            try {
                // Verifica a validade do token e extrai o e-mail do usuário que está associado a esse token.
                String email = jwtUtil.verifyToken(token).getSubject();  // Verifica o token e recupera o "subject", que no caso é o e-mail do usuário.

                // Cria um objeto de autenticação para o usuário com base no e-mail extraído do token.
                // A lista de roles (permissões) está vazia aqui, mas poderia ser preenchida se necessário.
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        new User(email, "", Collections.emptyList()),  // Usuário autenticado com o e-mail extraído do token.
                        null,  // O token é utilizado como credencial, portanto não é necessário fornecer uma senha.
                        Collections.emptyList()  // A lista de roles está vazia, mas poderia ser preenchida com permissões.
                );

                // Adiciona detalhes à autenticação, como o IP e informações da requisição (ajuda no rastreamento da requisição).
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define o contexto de segurança do Spring com a autenticação do usuário.
                // Isso indica ao Spring Security que o usuário foi autenticado com sucesso.
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Log de sucesso: Exibe no console que o token foi validado e o usuário autenticado.
                System.out.println("Token JWT válido, usuário autenticado: " + email);

            } catch (Exception e) {
                // Se ocorrer qualquer erro durante a verificação do token, como o token ser inválido,
                // o status HTTP 403 (Forbidden) será retornado e a requisição não será processada.
                System.out.println("Erro ao processar token JWT: " + e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // Define o status de resposta como 403 (proibido).
                return;  // Interrompe a execução da requisição, impedindo que ela prossiga.
            }
        }

        // Independentemente de o token ser válido ou não, a requisição é passada para o próximo filtro na cadeia.
        // Isso permite que a requisição continue a ser processada após a verificação do JWT.
        filterChain.doFilter(request, response);
    }
}
