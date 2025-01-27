package com.api.dto;

import java.util.List;

// DTO para retornar os dados do usuário com os pets vinculados
public record UserWithPetsDTO(
    Long id,
    String name,
    String surname,
    String email,
    String telephone,
    String whatsapp,
    String address,
    List<PetDTO> pets  // Lista de pets vinculados ao usuário
) {}