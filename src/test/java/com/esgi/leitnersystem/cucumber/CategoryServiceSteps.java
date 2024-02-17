package com.esgi.leitnersystem.cucumber;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CategoryServiceSteps {

    private Card card;
    private CategoryService categoryService;
    private CardRepository cardRepository;

    @Given("a card in category {string}")
    public void a_card_in_category_first(String category) {
        card = new Card();
        card.setCategory(Category.valueOf(category));
        cardRepository = mock(CardRepository.class);
        when(cardRepository.save(any(Card.class))).then(returnsFirstArg());
        categoryService = new CategoryService(cardRepository);
    }

    @When("the card is answered correctly")
    public void the_card_is_answered_correctly() {
        card = categoryService.promoteCard(card);
    }

    @Then("the card should be promoted to category SECOND")
    public void the_card_should_be_promoted_to_category_second() {
        assertEquals(Category.SECOND, card.getCategory());
    }

}
