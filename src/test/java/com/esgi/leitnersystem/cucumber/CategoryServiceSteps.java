package com.esgi.leitnersystem.cucumber;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

import com.esgi.leitnersystem.domain.card.Card;
import com.esgi.leitnersystem.domain.card.CardRepositoryPort;
import com.esgi.leitnersystem.domain.category.Category;
import com.esgi.leitnersystem.domain.category.CategoryService;
import com.esgi.leitnersystem.infrastructure.repository.CardRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CategoryServiceSteps {

  private Card card;
  private CategoryService categoryService;
  private CardRepositoryPort cardRepository;

  @Given("a card in category {string}")
  public void a_card_in_category_first(String category) {
    card = new Card();
    card.setCategory(Category.valueOf(category));
    cardRepository = mock(CardRepositoryPort.class);
    when(cardRepository.save(any(Card.class))).then(returnsFirstArg());
    categoryService = new CategoryService(cardRepository);
  }

  @When("the card is answered correctly")
  public void the_card_is_answered_correctly() {
    card = categoryService.promoteCard(card);
  }

  @Then("the card should be promoted to category {string}")
  public void the_card_should_be_promoted_to_category(String expectedCategory) {
    assertEquals(Category.valueOf(expectedCategory), card.getCategory());
  }

  @When("the card is answered incorrectly")
  public void the_card_is_answered_incorrectly() {
    card = categoryService.demoteCardToFirst(card);
  }

  @Then("the card should be demoted to category {string}")
  public void the_card_should_be_demoted_to_category(String expectedCategory) {
    assertEquals(Category.valueOf(expectedCategory), card.getCategory());
  }

  @Then("the card should be marked as done")
  public void the_card_should_be_marked_as_done() {
    assertEquals(Category.DONE, card.getCategory());
  }
}
