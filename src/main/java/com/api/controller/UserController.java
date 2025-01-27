package com.api.controller;

// Importações necessárias para o funcionamento da aplicação
import org.springframework.http.ResponseEntity;  // Utilizado para criar respostas HTTP
import org.springframework.web.bind.annotation.*;  // Anotações que definem os endpoints da API
import com.api.dto.UserRegisterDTO;  // Importa a classe UserRegisterDTO que é um objeto de transferência de dados do usuário
import com.api.dto.UserUpdateDTO;  // Importa a classe UserUpdateDTO que é um objeto de transferência de dados do usuário
import com.api.security.JwtUtil;  // Importa a classe que gerencia a criação e validação de tokens JWT
import com.api.service.UserService;  // Importa o serviço de usuários que contém a lógica de negócios
import com.api.model.User;  // Importa o modelo de dados que representa um usuário no banco de dados
import com.api.repository.UserRepository;  // Importa o repositório que faz a comunicação com o banco de dados

import jakarta.servlet.http.HttpServletRequest;  // Necessário para acessar os dados da requisição HTTP

// Define esta classe como um controlador REST. Essa anotação indica que os métodos dentro dessa classe irão
// lidar com as requisições HTTP (GET, POST, PUT, etc.)
@RestController
public class UserController {

    // Declaração das dependências necessárias para o funcionamento do controlador.
    private final JwtUtil jwtUtil;  // Responsável pela geração e validação do token JWT
    private final UserService userService;  // Contém a lógica de negócios para registrar e gerenciar usuários
    private final UserRepository userRepository;  // Faz a interação com o banco de dados para acessar dados do usuário

    // Construtor da classe UserController. Ele recebe as dependências que o Spring irá injetar automaticamente.
    // Esse é o padrão de injeção de dependência do Spring (IoC - Inversão de Controle).
    public UserController(JwtUtil jwtUtil, UserService userService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    /**
     * Endpoint para registrar um novo usuário.
     * Esse método recebe os dados do usuário, tenta registrá-lo e retorna uma resposta apropriada.
     * 
     * @param userRegisterDTO Dados do usuário para registrar (enviados no corpo da requisição).
     * @return Retorna um ResponseEntity com o status e a mensagem correspondente.
     */
    @PostMapping("/register")  // Mapeia a URL "/register" para este método com o método HTTP POST.
    public ResponseEntity<?> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        try {
            // Tenta registrar o usuário chamando o método do serviço que processa o registro.
            // O userRegisterDTO contém os dados enviados no corpo da requisição.
            userService.registerUser(userRegisterDTO);
            // Se o registro for bem-sucedido, retorna uma resposta com status 200 OK e uma mensagem de sucesso.
            return ResponseEntity.ok("Usuário registrado com sucesso!");
        } catch (IllegalArgumentException e) {
            // Se ocorrer algum erro (ex: dados inválidos), retorna uma resposta com status 400 (Bad Request) 
            // e a mensagem de erro do Exception.
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Endpoint para realizar o login do usuário.
     * Este método valida as credenciais e, se válidas, gera um token JWT.
     * 
     * @param userRegisterDTO Contém o e-mail e senha do usuário para validação.
     * @return Retorna o token JWT se as credenciais forem válidas, ou uma mensagem de erro caso contrário.
     */
    @PostMapping("/login")  // Mapeia a URL "/login" para este método com o método HTTP POST.
    public ResponseEntity<?> login(@RequestBody UserRegisterDTO userRegisterDTO) {
        // Chama o serviço de usuários para validar as credenciais (e-mail e senha).
        // O método userService.validateUser verifica se as credenciais do usuário são corretas.
        if (userService.validateUser(userRegisterDTO.email(), userRegisterDTO.password())) {
            // Se as credenciais forem válidas, gera um token JWT com base no e-mail do usuário.
            String token = jwtUtil.generateToken(userRegisterDTO.email());
            // Retorna o token gerado com status 200 OK.
            return ResponseEntity.ok(token);
        }
        // Se as credenciais forem inválidas, retorna uma resposta 401 (Unauthorized) com uma mensagem de erro.
        return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    /**
     * Endpoint para atualizar as informações de um usuário específico.
     * Verifica se o usuário está autenticado usando o token JWT, e se for válido, atualiza as informações.
     * 
     * @param id O ID do usuário a ser atualizado (parte da URL).
     * @param userUpdateDTO Os dados atualizados do usuário (enviados no corpo da requisição).
     * @param request A requisição HTTP, usada para acessar o cabeçalho e obter o token JWT.
     * @return Retorna o usuário atualizado ou uma mensagem de erro dependendo do resultado.
     */
    @PutMapping("/user/{id}")  // Mapeia a URL "/user/{id}" para este método com o método HTTP PUT.
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO,
                                        HttpServletRequest request) {
        try {
            // Chama o método auxiliar 'extractToken' para obter o token JWT do cabeçalho da requisição.
            String token = extractToken(request);
            if (token == null) {
                // Se o token não for encontrado ou for inválido, retorna uma resposta 403 (Forbidden).
                return ResponseEntity.status(403).body("Token de autenticação ausente ou inválido");
            }

            // Verifica a validade do token e extrai o e-mail do usuário a partir do token JWT.
            String emailFromToken = jwtUtil.verifyToken(token).getSubject();

            // Tenta buscar o usuário no banco de dados pelo ID.
            User user = userRepository.findById(id).orElse(null);
            if (user == null) {
                // Se o usuário não for encontrado, retorna uma resposta 404 (Not Found).
                return ResponseEntity.status(404).body("Usuário não encontrado");
            }

            // Verifica se o e-mail extraído do token corresponde ao e-mail do usuário encontrado no banco de dados.
            if (!user.getEmail().equalsIgnoreCase(emailFromToken)) {
                // Se os e-mails não corresponderem, retorna uma resposta 403 (Forbidden).
                return ResponseEntity.status(403).body("Acesso negado: e-mail não corresponde");
            }

            // Se as validações forem bem-sucedidas, chama o método do serviço para atualizar os dados do usuário.
            UserUpdateDTO updatedUser = userService.updateUser(id, userUpdateDTO);
            // Retorna os dados do usuário atualizado com status 200 OK.
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            // Se qualquer erro acontecer durante o processo, retorna uma resposta 500 (Internal Server Error)
            // com a mensagem do erro.
            return ResponseEntity.status(500).body("Erro interno: " + e.getMessage());
        }
    }

    /**
     * Método auxiliar que extrai o token JWT do cabeçalho da requisição.
     * Verifica se o cabeçalho "Authorization" está presente e no formato correto.
     * 
     * @param request A requisição HTTP, usada para acessar o cabeçalho.
     * @return Retorna o token JWT se presente e no formato correto, ou null se não encontrado ou inválido.
     */
    private String extractToken(HttpServletRequest request) {
        // Obtém o cabeçalho "Authorization" da requisição.
        String authHeader = request.getHeader("Authorization");
        // Verifica se o cabeçalho começa com o prefixo "Bearer ", que é o formato padrão para tokens JWT.
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " e retorna o restante do cabeçalho, que é o token JWT.
            return authHeader.substring(7);  // Remove "Bearer " do início do cabeçalho.
        }
        // Se o cabeçalho não for encontrado ou não começar com "Bearer ", retorna null.
        return null;
    }
}
