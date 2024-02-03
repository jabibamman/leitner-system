package com.esgi.leitnersystem.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;

import java.io.Serializable;

@Data
@Embeddable
public class CardId implements Serializable {
    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private String id;
}
