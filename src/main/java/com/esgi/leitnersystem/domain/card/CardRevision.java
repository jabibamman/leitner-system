package com.esgi.leitnersystem.domain.card;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class CardRevision {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  @Column(nullable = false) private UUID cardId;

  @Column(nullable = false) private String revisionDate;

  @Column(nullable = false) private boolean wasCorrect;

  public CardRevision() {}

  public CardRevision(UUID cardId, String revisionDate, boolean wasCorrect) {
    this.cardId = cardId;
    this.revisionDate = revisionDate;
    this.wasCorrect = wasCorrect;
  }
}