package com.esgi.leitnersystem.infrastructure.repository;
import com.esgi.leitnersystem.domain.card.CardRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface CardRevisionRepository extends JpaRepository<CardRevision, Long> {
    List<CardRevision> findByRevisionDate(String date);

    CardRevision findTopByCardIdOrderByRevisionDateDesc(UUID cardId);
}
