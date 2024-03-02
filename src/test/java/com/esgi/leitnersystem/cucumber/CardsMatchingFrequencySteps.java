package com.esgi.leitnersystem.cucumber;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRevision;
import com.esgi.leitnersystem.domain.card.CardService;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.Quiz.QuizService;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.domain.revision.RevisionService;
import com.esgi.leitnersystem.infrastructure.dto.CardUserData;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CardsMatchingFrequencySteps {

    @Autowired
    private QuizService quizService;

    @Autowired
    private CardService cardService;

    @Autowired
    private RevisionService revisionService;

    @Autowired
    private CategoryService categoryService;
    private List<Card> cardsForQuiz;
    private LocalDate quizDate;

    @Given("a connected user is ready to take a quiz on the current date")
    public void a_connected_user_is_ready_to_take_a_quiz() {
        quizDate = LocalDate.now();
    }

    @And("the system has cards in various categories with last revision dates")
    public void the_system_has_cards_in_various_categories_with_last_revision_dates() {
        cardService.createCard(new CardUserData("What is the capital of France?", "Paris", "geography"));
        cardService.createCard(new CardUserData("What is the capital of Germany?", "Berlin", "geography"));
        cardService.createCard(new CardUserData("What is the capital of Italy?", "Rome", "geography"));
        cardService.createCard(new CardUserData("What is the capital of Spain?", "Madrid", "geography"));
        cardService.createCard(new CardUserData("What is the capital of Portugal?", "Lisbon", "geography"));
        cardService.createCard(new CardUserData("What is the capital of England?", "London", "geography"));
        cardService.createCard(new CardUserData("What is the capital of the USA?", "Washington", "geography"));
        cardService.createCard(new CardUserData("What is the capital of Canada?", "Ottawa", "geography"));

        List<Card> cards = cardService.fetchAllCards(Optional.empty());
        Category[] categories = Category.values();

        if (!cards.isEmpty()) {
            Card firstCard = cards.getFirst();
            cardService.processCardAnswer(firstCard.getId(), false);
        }

        for (int i = 1; i < cards.size(); i++) {
            Card card = cards.get(i);
            Category nextCategory = categories[Math.min(i + 1, categories.length - 1)];
            while (Objects.requireNonNull(card).getCategory() != nextCategory) {
                cardService.processCardAnswer(card.getId(), true);
                Card finalCard = card;
                card = cardService.fetchAllCards(Optional.empty()).stream().filter(c -> c.getId().equals(finalCard.getId())).findFirst().orElse(null);
            }
        }

        assertFalse(cards.isEmpty(), "At least one card should be created");
    }

    @When("the user triggers the quiz {int} days later")
    public void the_user_triggers_the_quiz_days_later(int daysLater) {
        quizDate = quizDate.plusDays(daysLater);
        cardsForQuiz = quizService.getCardsDueForQuiz(quizDate);
    }
    
    @Then("only cards eligible for review based on their category frequency are shown")
    public void only_cards_eligible_for_review_based_on_their_category_frequency_are_shown() {
        for (Card card : cardsForQuiz) {
            CardRevision lastRevision = revisionService.findLatestRevisionByCardId(card.getId());
            LocalDate lastRevisionDate;
            if (lastRevision != null && lastRevision.getRevisionDate() != null) {
                lastRevisionDate = LocalDate.parse(lastRevision.getRevisionDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            } else {
                lastRevisionDate = LocalDate.now();
            }

            long daysSinceLastRevision = ChronoUnit.DAYS.between(lastRevisionDate, quizDate);
            int expectedDaysUntilNextRevision = categoryService.getDaysUntilNextRevision(card.getCategory());
            boolean isEligibleForReview = daysSinceLastRevision >= expectedDaysUntilNextRevision;

            assertTrue(isEligibleForReview, String.format("Card with ID %s should be eligible for review based on its category frequency.", card.getId()));
        }

        if (quizDate.isEqual(LocalDate.now().plusDays(1))) {
            boolean parisIsEligible = cardsForQuiz.stream().anyMatch(card -> "Paris".equals(card.getAnswer()) && card.getCategory() == Category.FIRST);
            assertTrue(parisIsEligible, "Paris should be eligible for review the day after creation.");
        }
    }
}
