package com.esgi.leitnersystem.domain.card;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardResponse {
    private Card cards;
    private String errorMessage;
}
