package com.api.security;  // Pacote onde a classe JwtUtil está localizada.

import com.auth0.jwt.JWT;  // Classe da biblioteca Auth0 que lida com a criação e manipulação de tokens JWT.
import com.auth0.jwt.algorithms.Algorithm;  // Classe da biblioteca Auth0 para definir o algoritmo de assinatura do token.
import com.auth0.jwt.interfaces.DecodedJWT;  // Interface que representa o token JWT decodificado após a verificação.
import com.auth0.jwt.interfaces.JWTVerifier;  // Interface da biblioteca Auth0 usada para verificar a validade do token JWT.
import org.springframework.beans.factory.annotation.Value;  // Anotação para injetar valores de configuração de um arquivo de propriedades.
import org.springframework.stereotype.Component;  // Anotação que indica que esta classe é um componente gerido pelo Spring.

import java.util.Date;  // Classe para manipular e representar datas.

@Component  // Indica que essa classe é um componente Spring e será gerenciada pelo contexto do Spring.
public class JwtUtil {

    @Value("${jwt.secret}")  // Injeção do valor da chave secreta JWT a partir do arquivo de configuração (application.properties ou application.yml).
    private String secret;  // Chave secreta usada para assinar o token JWT.

    @Value("${jwt.expiration}")  // Injeção do valor do tempo de expiração do token JWT.
    private long expiration;  // Tempo de expiração do token JWT (em milissegundos).

    // Método para gerar um token JWT para um usuário.
    public String generateToken(String user) {
        // Define o algoritmo de assinatura, que será HMAC com a chave secreta fornecida.
        Algorithm algorithm = Algorithm.HMAC256(secret);
        
        // Cria um token JWT com as seguintes propriedades:
        // - O "subject" (assunto) é o nome do usuário.
        // - O token contém a data de emissão e a data de expiração (calculada a partir do tempo atual + tempo de expiração).
        // - O token é assinado usando o algoritmo HMAC256 e a chave secreta.
        return JWT.create()
                .withSubject(user)  // O "subject" do token é o nome do usuário (quem o token representa).
                .withIssuedAt(new Date())  // Data de emissão do token (momento atual).
                .withExpiresAt(new Date(System.currentTimeMillis() + expiration))  // Data de expiração do token.
                .sign(algorithm);  // Assina o token com o algoritmo especificado.
    }

    // Método para verificar a validade de um token JWT.
    public DecodedJWT verifyToken(String token) {
        try {
            // Define o algoritmo de assinatura com a chave secreta.
            Algorithm algorithm = Algorithm.HMAC256(secret);
            
            // Cria um verificador JWT com o algoritmo especificado.
            JWTVerifier verifier = JWT.require(algorithm).build();
            
            // Verifica o token e retorna o JWT decodificado. Se o token for inválido ou expirado, uma exceção será lançada.
            return verifier.verify(token);
        } catch (Exception e) {
            // Se o token for inválido ou expirado, lança uma exceção com uma mensagem detalhada.
            throw new RuntimeException("Token inválido ou expirado: " + e.getMessage());
        }
    }
}
