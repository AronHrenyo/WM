package com.wm.dto;

public record PartnerResponseDto(
        Long partnerId,
        String partnerName,
        String partnerEmail,
        String partnerTelephone,
        String partnerAddress
) {}