package com.client.ws.rasmooplus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "Atributo obrigatório")
    private String username;

    @NotBlank(message = "Atributo obrigatório")
    private String password;

}
