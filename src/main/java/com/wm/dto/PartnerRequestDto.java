package com.wm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PartnerRequestDto(
        @NotBlank(message = "Name is required")
        String partnerName,

        @Email(message = "Email should be valid")
        String partnerEmail,
        String partnerTelephone,
        String partnerAddress
) {}