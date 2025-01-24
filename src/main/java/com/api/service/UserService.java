package com.api.service;  // Pacote onde a classe UserService está localizada.

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;  // Para criptografar e comparar senhas com BCrypt.
import org.springframework.stereotype.Service;  // Marca a classe como um serviço do Spring, gerenciado pelo contexto do Spring.

import com.api.dto.UserDTO;  // Classe DTO (Data Transfer Object) usada para transferir dados do usuário.
import com.api.model.User;  // Modelo da entidade de usuário, mapeada para a tabela no banco de dados.
import com.api.repository.UserRepository;  // Repositório que permite interagir com o banco de dados para a entidade User.

import java.util.Optional;  // Classe para encapsular valores que podem ser nulos e evitar NullPointerException.

@Service  // Anotação que marca a classe como um serviço Spring, permitindo que seja injetada onde necessário.
public class UserService {

    private final UserRepository userRepository;  // Repositório usado para acessar dados de usuários no banco de dados.
    private final BCryptPasswordEncoder passwordEncoder;  // Codificador BCrypt para criptografar e verificar senhas.

    // Construtor que injeta as dependências necessárias.
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;  // O Spring irá injetar uma instância do UserRepository.
        this.passwordEncoder = new BCryptPasswordEncoder();  // Instancia o codificador de senha BCrypt.
    }

    // Método para registrar um novo usuário.
    public User registerUser(UserDTO userDTO) {
        // Verifica se já existe um usuário com o mesmo email no banco de dados.
        if (userRepository.findByEmail(userDTO.email()).isPresent()) {
            throw new IllegalArgumentException("O email já existe!");  // Lança uma exceção se o email já estiver cadastrado.
        }

        // Cria um novo objeto de usuário.
        User user = new User();
        user.setName(userDTO.name());  // Define o nome do usuário.
        user.setSurname(userDTO.surname());  // Define o sobrenome do usuário.
        user.setEmail(userDTO.email());  // Define o email do usuário.
        user.setPassword(passwordEncoder.encode(userDTO.password()));  // Criptografa a senha antes de salvar.
        user.setRole("USER");  // Define o papel do usuário como "USER".

        // Salva o usuário no banco de dados e retorna o objeto User recém-criado.
        return userRepository.save(user);
    }

    // Método para validar um usuário durante o login.
    public boolean validateUser(String email, String password) {
        // Busca o usuário pelo email no banco de dados.
        Optional<User> user = userRepository.findByEmail(email);
        // Verifica se o usuário foi encontrado e se a senha fornecida corresponde à senha criptografada no banco de dados.
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    // Método para atualizar os dados de um usuário existente.
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        // Busca o usuário no banco de dados pelo ID.
        Optional<User> existingUserOpt = userRepository.findById(id);
        // Se o usuário não for encontrado, lança uma exceção.
        if (existingUserOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Obtém o usuário existente.
        User existingUser = existingUserOpt.get();
        // Atualiza os dados do usuário com os dados do DTO.
        existingUser.setName(userDTO.name());
        existingUser.setSurname(userDTO.surname());
        existingUser.setEmail(userDTO.email());

        // Se uma nova senha for fornecida, ela será criptografada e atualizada.
        if (!userDTO.password().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDTO.password()));
        }

        // Salva o usuário atualizado no banco de dados.
        User updatedUser = userRepository.save(existingUser);
        // Retorna os dados atualizados do usuário no formato de um DTO.
        return new UserDTO(updatedUser.getName(), updatedUser.getSurname(), updatedUser.getEmail(), userDTO.password());
    }
}
