package com.client.ws.rasmooplus.dto;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionTypeDto {
    private Long id;

    @NotBlank(message = "Campo name não pode ser nulo ou vazio")
    @Size(min = 5, max = 40, message = "Campo productKey deve ter entre 5 e 40 caracteres")
    private String name;

    @Max(value = 12, message = "Campo accessMonth não pode ser maior que 12")
    private Long accessMonths;

    @NotNull(message = "Campo price não pode ser nulo")
    private BigDecimal price;

    @NotBlank(message = "Campo productKey não pode ser nulo ou vazio")
    @Size(min = 5, max = 15, message = "Campo productKey deve ter entre 5 e 15 caracteres")
    private String productKey;
}
