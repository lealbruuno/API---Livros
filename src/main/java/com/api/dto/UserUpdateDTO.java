package com.api.dto;  // Pacote onde o DTO (Data Transfer Object) do usuário está localizado.

// Usando record para representar o DTO (Data Transfer Object)
// Um record é uma forma especial de classe imutável, disponível a partir do Java 14.
// Ele é usado para representar dados de forma compacta e simples, sem a necessidade de escrever muito código boilerplate.
public record UserUpdateDTO(String name,String surname,String email,String telephone,String whatsapp,String address,String password) {

    // O record já gera automaticamente o construtor, os métodos getters, toString, equals e hashCode para os campos.
    // Não precisamos escrever explicitamente esses métodos, pois o record faz isso automaticamente.
    //
    // A utilização do record torna o código mais enxuto, pois os campos são definidos diretamente
    // no cabeçalho e as operações básicas são automaticamente implementadas.
    //
    // No caso do record UserDTO, ele tem:
    // - Um construtor para inicializar os valores.
    // - Métodos "getter" para acessar os valores dos campos (por exemplo, name(), surname(), email(), password()).
    // - Um método "toString" para gerar uma string representando o objeto.
    // - Métodos "equals" e "hashCode" para comparar objetos e gerar o código de hash adequado.
    //   
    // Exemplificando:
    // - Para criar uma instância desse record, você usa: 
    //    UserDTO user = new UserDTO("John", "Doe", "john.doe@example.com", "password123");
    // - O Java automaticamente fornece os métodos:
    //    - user.name() para acessar o nome.
    //    - user.surname() para acessar o sobrenome.
    //    - user.email() para acessar o e-mail.
    //    - user.password() para acessar a senha.
    //
    // O uso de records é recomendado quando você precisa de objetos imutáveis e compactos para carregar ou transferir dados.

}
