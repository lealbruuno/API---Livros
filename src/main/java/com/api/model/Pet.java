package com.api.model;  // Pacote onde a classe Pet está localizada.

import com.fasterxml.jackson.annotation.JsonFormat;  // Anotação para formatar a data no JSON.
import jakarta.persistence.*;  // Anotações JPA (Java Persistence API) para persistência no banco de dados.
import jakarta.validation.constraints.*;  // Anotações para validação de campos.
import lombok.Data;  // Lombok: Gera automaticamente getters, setters, toString, hashCode e equals.
import org.hibernate.annotations.CreationTimestamp;  // Anotação do Hibernate para marcar o campo que recebe a data/hora de criação.
import org.hibernate.annotations.UpdateTimestamp;  // Anotação do Hibernate para marcar o campo que recebe a data/hora da última atualização.

import java.math.BigDecimal;  // Importação para trabalhar com valores monetários ou de precisão arbitrária.
import java.time.LocalDate;  // Importação para trabalhar com datas.
import java.time.LocalDateTime;  // Importação para trabalhar com data e hora.

@Entity  // Indica que esta classe é uma entidade JPA e será mapeada para uma tabela no banco de dados.
@Table(name = "tbl_pets")  // Define o nome da tabela no banco de dados.
@Data  // Lombok: Gera automaticamente os métodos getter, setter, toString, hashCode e equals para todos os campos.
public class Pet {

    @Id  // Indica que este campo é a chave primária da tabela.
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Gera automaticamente o valor da chave primária com incremento no banco de dados.
    private Long id;  // ID do pet (chave primária).

    @NotBlank(message = "O nome é obrigatório")  // Validação: Garante que o nome não pode ser vazio.
    @Size(max = 50, message = "O nome deve ter no máximo 50 caracteres")  // Validação: Limita o nome a 50 caracteres.
    @Column(nullable = false)  // Garante que o nome não pode ser nulo na tabela do banco de dados.
    private String nome;  // Nome do pet.

    @NotBlank(message = "A espécie é obrigatória")  // Validação: Garante que a espécie não pode ser vazia.
    @Size(max = 50, message = "A espécie deve ter no máximo 50 caracteres")  // Validação: Limita a espécie a 50 caracteres.
    @Column(nullable = false)  // Garante que a espécie não pode ser nula na tabela do banco de dados.
    private String especie;  // Espécie do pet.

    @NotBlank(message = "A data de nascimento é obrigatória")  // Validação: Garante que a data de nascimento não pode ser vazia.
    @JsonFormat(pattern = "dd/MM/yyyy")  // Formata a data no padrão "dd/MM/yyyy" ao serializar para JSON.
    @Column(nullable = false)  // Garante que a data de nascimento não pode ser nula na tabela do banco de dados.
    private LocalDate dataNascimento;  // Data de nascimento do pet.

    @NotBlank(message = "O gênero é obrigatório")  // Validação: Garante que o gênero não pode ser vazio.
    @Size(max = 50, message = "O gênero deve ter no máximo 50 caracteres")  // Validação: Limita o gênero a 50 caracteres.
    @Column(nullable = false)  // Garante que o gênero não pode ser nulo na tabela do banco de dados.
    private String genero;  // Gênero do pet.

    @NotNull(message = "O peso é obrigatório")  // Validação: Garante que o peso não pode ser nulo.
    @DecimalMax(value = "999.99", message = "O peso deve ser menor que 1000 kg")  // Validação: Limita o peso a um valor máximo de 999.99.
    @Column(nullable = false)  // Garante que o peso não pode ser nulo na tabela do banco de dados.
    private BigDecimal peso;  // Peso do pet.

    @NotBlank(message = "A cor é obrigatória")  // Validação: Garante que a cor não pode ser vazia.
    @Size(max = 50, message = "A cor deve ter no máximo 50 caracteres")  // Validação: Limita a cor a 50 caracteres.
    @Column(nullable = false)  // Garante que a cor não pode ser nula na tabela do banco de dados.
    private String cor;  // Cor do pet.

    @NotBlank(message = "A castração é obrigatória")  // Validação: Garante que a castração não pode ser vazia.
    @Size(max = 3, message = "A castração deve ter no máximo 3 caracteres")  // Validação: Limita a castração a 3 caracteres.
    @Column(nullable = false)  // Garante que a castração não pode ser nula na tabela do banco de dados.
    private String castracao;  // Status de castração do pet.

    @NotBlank(message = "A raça é obrigatória")  // Validação: Garante que a raça não pode ser vazia.
    @Size(max = 50, message = "A raça deve ter no máximo 50 caracteres")  // Validação: Limita a raça a 50 caracteres.
    @Column(nullable = false)  // Garante que a raça não pode ser nula na tabela do banco de dados.
    private String raca;  // Raça do pet.

    @Size(max = 255, message = "A URL da foto deve ter no máximo 255 caracteres")  // Validação: Limita a URL da foto a 255 caracteres.
    @Column(nullable = true)  // Permite que a URL da foto seja nula na tabela do banco de dados.
    private String foto;  // URL da foto do pet.

    @Lob  // Indica que o campo é um Large Object (LOB), podendo armazenar grandes quantidades de dados.
    @Column(columnDefinition = "TEXT", unique = true)  // Define o tipo da coluna como TEXT e garante que o QR Code seja único.
    private String qrCode;  // Código QR associado ao pet.

    @CreationTimestamp  // Insere automaticamente a data/hora de criação do registro no banco de dados.
    @Column(name = "date_creation", updatable = false, nullable = false)  // Define o nome da coluna como "date_creation", e impede que a data seja atualizada após a criação.
    private LocalDateTime dateCreation;  // Data e hora de criação do registro do pet.

    @UpdateTimestamp  // Insere automaticamente a data/hora da última atualização do registro no banco de dados.
    @Column(name = "date_update", nullable = false)  // Define o nome da coluna como "date_update" e garante que não pode ser nula.
    private LocalDateTime dateUpdate;  // Data e hora da última atualização do registro do pet.

    @ManyToOne  // Define um relacionamento muitos-para-um com a entidade User.
    @JoinColumn(name = "usuario_id", nullable = false)  // Define a coluna de chave estrangeira e garante que não pode ser nula.
    private User usuario;  // Usuário associado ao pet.
}