package com.esgi.leitnersystem.domain.card;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description =
            "Nature de la carte : ATOMIC pour une revision silencieuse "
            + "classique, ORAL pour une carte a repondre a voix haute. "
            + "Les deux suivent le systeme de Leitner independamment : "
            + "melanger les deux dans une meme session fait disparaitre "
            + "l'oral au profit du silencieux.")
public enum CardType {
  ATOMIC,
  ORAL
}
