package com.api.repository;  // Pacote onde a interface UserRepository está localizada.

import com.api.model.User;  // Importação da classe User, que representa a entidade de usuário.
import org.springframework.data.jpa.repository.JpaRepository;  // Interface base do Spring Data JPA para realizar operações CRUD.
import java.util.Optional;  // Classe Optional, usada para evitar problemas com valores nulos.


// Interface que estende JpaRepository para fornecer operações CRUD de forma automática.
// O JpaRepository recebe dois parâmetros genéricos:
// - O primeiro parâmetro (User) é o tipo da entidade/model que estamos manipulando (neste caso, a classe/model User).
// - O segundo parâmetro (Long) é o tipo do identificador (ID) da entidade, ou seja, o tipo da chave primária.
// Portanto, estamos dizendo que o repositório vai trabalhar com a entidade User e que o tipo de dado da chave primária é Long.
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar um usuário pelo seu email. Ele retorna um Optional<User>, o que significa que
    // pode retornar um valor presente (caso o email seja encontrado) ou um valor vazio (caso não exista nenhum usuário com o email fornecido).
    // Isso ajuda a evitar o uso de valores nulos diretamente, tornando o código mais seguro e evitando NullPointerExceptions.
    Optional<User> findByEmail(String email); // Busca um usuário com base no email.

    // O JpaRepository já fornece diversos métodos padrão como:
    // - save(): para salvar ou atualizar um usuário.
    // - findById(): para buscar um usuário pelo seu ID.
    // - findAll(): para buscar todos os usuários.
    // - deleteById(): para excluir um usuário pelo seu ID.
    // - count(): para contar o número total de usuários no banco de dados, entre outros.
    // Esses métodos são gerados automaticamente pelo Spring Data JPA e não precisam ser implementados.
}
