package com.api.model;  // Pacote onde a classe User está localizada.

import java.time.LocalDateTime;  // Importação para trabalhar com data e hora.

import org.hibernate.annotations.CreationTimestamp;  // Anotação do Hibernate para marcar o campo que recebe a data/hora de criação.
import org.hibernate.annotations.UpdateTimestamp;  // Anotação do Hibernate para marcar o campo que recebe a data/hora da última atualização.
import jakarta.persistence.*;  // Anotações JPA (Java Persistence API) para persistência no banco de dados.
import lombok.Data;  // Lombok: Gera automaticamente getters, setters, toString, hashCode e equals.
import jakarta.validation.constraints.Email;  // Anotação para validação de email.
import jakarta.validation.constraints.NotBlank;  // Anotação para garantir que o campo não esteja em branco.
import jakarta.validation.constraints.Size;  // Anotação para validar o tamanho dos campos.
import jakarta.validation.constraints.Pattern;  // Anotação para validar padrões com expressões regulares (por exemplo, para telefone).

@Entity  // Indica que esta classe é uma entidade JPA e será mapeada para uma tabela no banco de dados.
@Table(
    name = "tbl_users",  // Define o nome da tabela no banco de dados.
    uniqueConstraints = @UniqueConstraint(columnNames = {"email", "telefone"})  // Garante que os campos "email" e "telefone" são únicos na tabela.
)
@Data  // Lombok: Gera automaticamente os métodos getter, setter, toString, hashCode e equals para todos os campos.
public class User {

    @Id  // Indica que este campo é a chave primária da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Gera automaticamente o valor da chave primária com incremento no banco de dados.
    private Long id;  // ID do usuário (chave primária).

    @NotBlank(message = "O nome é obrigatório")  // Validação: Garante que o nome não pode ser vazio.
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")  // Validação: Limita o nome a 100 caracteres.
    @Column(nullable = false)  // Garante que o nome não pode ser nulo na tabela do banco de dados.
    private String name;  // Nome do usuário.

    @NotBlank(message = "O sobrenome é obrigatório")  // Validação: Garante que o sobrenome não pode ser vazio.
    @Size(max = 100, message = "O sobrenome deve ter no máximo 100 caracteres")  // Validação: Limita o sobrenome a 100 caracteres.
    @Column(nullable = false)  // Garante que o sobrenome não pode ser nulo na tabela do banco de dados.
    private String surname;  // Sobrenome do usuário.

    @NotBlank(message = "O email é obrigatório")  // Validação: Garante que o e-mail não pode ser vazio.
    @Size(max = 100, message = "O email deve ter no máximo 100 caracteres")  // Validação: Limita o e-mail a 100 caracteres.
    @Email(message = "O email deve ser válido")  // Validação: Garante que o e-mail tem formato válido.
    @Column(unique = true, nullable = false)  // Garante que o e-mail é único e não pode ser nulo na tabela do banco de dados.
    private String email;  // E-mail do usuário.

    @NotBlank(message = "A senha é obrigatória")  // Validação: Garante que a senha não pode ser vazia.
    @Size(min = 8, max = 100, message = "A senha deve ter entre 8 e 100 caracteres")  // Validação: Limita a senha a um tamanho entre 8 e 100 caracteres.
    @Column(nullable = false)  // Garante que a senha não pode ser nula na tabela do banco de dados.
    private String password;  // Senha do usuário.

    @Pattern(regexp = "\\d{10,11}", message = "O telefone deve conter apenas números e ter 10 ou 11 dígitos")  // Validação: Garante que o telefone tenha 10 ou 11 dígitos numéricos.
    @Column(unique = true)  // Garante que o telefone seja único na tabela do banco de dados.
    private String telephone;  // Telefone do usuário.

    @Pattern(regexp = "\\d{10,11}", message = "O whatsapp deve conter apenas números e ter 10 ou 11 dígitos")  // Validação: Garante que o WhatsApp tenha 10 ou 11 dígitos numéricos.
    @Column(unique = true)  // Garante que o WhatsApp seja único na tabela do banco de dados.
    private String whatsapp;  // Número de WhatsApp do usuário.

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")  // Validação: Limita o endereço a 255 caracteres.
    private String address;  // Endereço do usuário.

    @CreationTimestamp  // Insere automaticamente a data/hora de criação do registro no banco de dados.
    @Column(name = "date_creation", updatable = false, nullable = false)  // Define o nome da coluna como "date_creation", e impede que a data seja atualizada após a criação.
    private LocalDateTime dateCreation;  // Data e hora de criação do usuário.

    @UpdateTimestamp  // Insere automaticamente a data/hora da última atualização do registro no banco de dados.
    @Column(name = "date_update", nullable = false)  // Define o nome da coluna como "date_update" e garante que não pode ser nula.
    private LocalDateTime dateUpdate;  // Data e hora da última atualização do usuário.

    @Column(name = "role", nullable = false)  // Define o nome da coluna como "role" e garante que não pode ser nula.
    private String role;  // Função ou papel do usuário (exemplo: "USER", "ADMIN").
}
