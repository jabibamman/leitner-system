package com.esgi.leitnersystem.domain.repository;

import com.esgi.leitnersystem.domain.model.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {}
