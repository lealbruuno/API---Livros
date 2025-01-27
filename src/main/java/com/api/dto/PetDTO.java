package com.api.dto;  // Pacote onde o DTO (Data Transfer Object) do pet está localizado.

import java.math.BigDecimal;  // Importação para trabalhar com valores monetários ou de precisão arbitrária.
import java.time.LocalDate;  // Importação para trabalhar com datas.

// Usando record para representar o DTO (Data Transfer Object)
// Um record é uma forma especial de classe imutável, disponível a partir do Java 14.
// Ele é usado para representar dados de forma compacta e simples, sem a necessidade de escrever muito código boilerplate.
public record PetDTO(
    Long id,
    String nome,
    String especie,
    LocalDate dataNascimento,
    String genero,
    BigDecimal peso,
    String cor,
    String castracao,
    String raca,
    String foto,
    String qrCode,
    UserRegisterDTO usuario  // Usuário associado ao pet, representado por um UserRegisterDTO.
) {
    // O record já gera automaticamente o construtor, os métodos getters, toString, equals e hashCode para os campos.
    // Não precisamos escrever explicitamente esses métodos, pois o record faz isso automaticamente.
    //
    // A utilização do record torna o código mais enxuto, pois os campos são definidos diretamente
    // no cabeçalho e as operações básicas são automaticamente implementadas.
    //
    // No caso do record PetDTO, ele tem:
    // - Um construtor para inicializar os valores.
    // - Métodos "getter" para acessar os valores dos campos (por exemplo, id(), nome(), especie(), etc.).
    // - Um método "toString" para gerar uma string representando o objeto.
    // - Métodos "equals" e "hashCode" para comparar objetos e gerar o código de hash adequado.
    //
    // Exemplificando:
    // - Para criar uma instância desse record, você usa:
    //    PetDTO pet = new PetDTO(1L, "Rex", "Cachorro", LocalDate.of(2020, 1, 1), "Macho", new BigDecimal("10.5"), "Preto", "Sim", "Vira-lata", "url_da_foto", "qr_code", userDTO);
    // - O Java automaticamente fornece os métodos:
    //    - pet.id() para acessar o ID.
    //    - pet.nome() para acessar o nome.
    //    - pet.especie() para acessar a espécie.
    //    - pet.dataNascimento() para acessar a data de nascimento.
    //    - pet.genero() para acessar o gênero.
    //    - pet.peso() para acessar o peso.
    //    - pet.cor() para acessar a cor.
    //    - pet.castracao() para acessar o status de castração.
    //    - pet.raca() para acessar a raça.
    //    - pet.foto() para acessar a URL da foto.
    //    - pet.qrCode() para acessar o código QR.
    //    - pet.usuario() para acessar o usuário associado.
    //
    // O uso de records é recomendado quando você precisa de objetos imutáveis e compactos para carregar ou transferir dados.
}