package com.api.security;  // Pacote onde a classe SecurityConfig está localizada.

import org.springframework.context.annotation.Bean;  // Para criar beans de configuração no contexto do Spring.
import org.springframework.context.annotation.Configuration;  // Para marcar a classe como uma classe de configuração do Spring.
import org.springframework.security.authentication.AuthenticationManager;  // Para gerenciar a autenticação de usuários.
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;  // Para configurar a autenticação do Spring Security.
import org.springframework.security.config.annotation.web.builders.HttpSecurity;  // Para configurar a segurança de rotas HTTP.
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  // Para criptografar senhas com o algoritmo BCrypt.
import org.springframework.security.web.SecurityFilterChain;  // Para definir a cadeia de filtros de segurança HTTP.
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;  // Para lidar com autenticação via nome de usuário e senha.

@Configuration  // Indica que essa classe é uma classe de configuração para o Spring.
public class SecurityConfig {

    private final JwtFilter jwtFilter;  // Instância do filtro JWT que será adicionado à cadeia de filtros HTTP.

    // Construtor que recebe uma instância de JwtFilter, que é injetada pelo Spring.
    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;  // Atribui o filtro JWT à variável de instância.
    }

    // Bean para criar o codificador de senhas BCrypt, que será usado para criptografar senhas.
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // Retorna uma instância do BCryptPasswordEncoder para codificar as senhas.
    }

    // Bean para configurar a cadeia de filtros de segurança HTTP, que define como as requisições HTTP serão tratadas.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // Desabilita a proteção CSRF (Cross-Site Request Forgery). A proteção CSRF é geralmente desabilitada para APIs REST, pois não usamos cookies para autenticação.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register", "/login").permitAll()  // Permite acesso sem autenticação para as rotas "/register" e "/login".
                        .requestMatchers("/h2-console/**").permitAll()  // Permite acesso sem autenticação ao console do banco H2, útil para ambiente de desenvolvimento.
                        .anyRequest().authenticated()  // Exige autenticação para todas as outras rotas da API.
                )
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))  // Necessário para permitir o acesso ao console H2. Impede que o H2 seja carregado em iframes de origens diferentes.
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  // Adiciona o filtro JWT antes do filtro de autenticação padrão (UsernamePasswordAuthenticationFilter).

        return http.build();  // Retorna a configuração final da cadeia de filtros HTTP.
    }

    // Bean para criar o AuthenticationManager, que é responsável por gerenciar a autenticação de usuários.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();  // Obtém o AuthenticationManager configurado a partir da configuração do Spring Security.
    }
}
