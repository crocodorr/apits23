package com.mtg.mtgtest.search.trie;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import com.mtg.mtgtest.TestResourceConfigurationException;
import com.mtg.mtgtest.card.catalog.model.Card;
import com.mtg.mtgtest.card.catalog.model.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import com.mtg.mtgtest.card.catalog.model.ImmutableCard;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.core.ResolvableType;

@ExtendWith(MockitoExtension.class)
final class CardSearchTrieUnitTest {

  private static final JacksonTester<List<String>> DESERIALIZER = new JacksonTester<>(
      CardSearchTrieUnitTest.class, ResolvableType.forClassWithGenerics(List.class, String.class), new ObjectMapper()
  );

  private static final List<Card> CARDS = createCardsFromResources();
  private final Trie<Card> trie = Trie.withKeyMapping(Card::name, CARDS);

  @Test
  void exactMatch_onlyOneResult() {
    final Card birds = getCardByName("Birds of Paradise");
    assertThat(trie.search(birds.name(), 0)).isEqualTo(List.of(birds));
  }

  @Test
  void distanceOfOne_twoPrefixMatches() {
    System.out.println(trie.search("Angel ", 3));

  }

  private static List<Card> createCardsFromResources() {
    try {
      return DESERIALIZER.parse(
          Files.readString(Paths.get(ClassLoader.getSystemResource("card-names.json").toURI()))
      ).getObject().stream().map(
          name -> ImmutableCard.builder()
              .name(name)
              .convertedManaCost(1)
              .addColorIdentity(Color.GREEN)
              .build()
      ).collect(toList());
    } catch (Throwable thrown) {
      throw new TestResourceConfigurationException("card-names.json", thrown);
    }
  }

  private static Card getCardByName(String name) {
    return CARDS.stream().filter(card -> name.equals(card.name())).findFirst().orElseThrow(RuntimeException::new);
  }
}
