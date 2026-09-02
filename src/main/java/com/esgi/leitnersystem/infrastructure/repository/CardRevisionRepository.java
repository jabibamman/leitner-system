package com.esgi.leitnersystem.infrastructure.repository;
import com.esgi.leitnersystem.domain.card.CardRevision;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CardRevisionRepository
    extends JpaRepository<CardRevision, Long> {
  List<CardRevision> findByRevisionDate(String date);

  Optional<CardRevision> findTopByCardIdOrderByRevisionDateDesc(UUID cardId);

  CardRevision findByCardId(UUID revisionId);

  // Une seule requete pour la derniere revision de chaque carte, au lieu
  // d'une requete par carte (voir QuizService.getCardsDueForQuiz).
  @Query("SELECT cr FROM CardRevision cr WHERE cr.revisionDate = "
       + "(SELECT MAX(cr2.revisionDate) FROM CardRevision cr2 WHERE cr2.cardId = cr.cardId)")
  List<CardRevision> findLatestRevisionPerCard();
}
