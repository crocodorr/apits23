package com.mtg.mtgtest.search.trie;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
final class RedundantTrieUnitTest {
  @Nested
  final class SimpleNames {
    final Trie<String> trie =
        Trie.create("Adam", "Ddom", "Kyle", "Adom", "Adelle", "Adamology");

    @Test
    void distanceOfZero_oneExactMatch_onePrefixMatch() {
      assertThat(trie.search("Adam", 0)).containsExactlyInAnyOrder("Adam", "Adamology");
    }

    @Test
    void distanceOfOne_oneApproximateMatch_oneApproximatePrefixMatch() {
      assertThat(trie.search("Aaam", 1)).containsExactlyInAnyOrder("Adam", "Adamology");
    }

    @Test
    void distanceOfOne_threeApproximatePrefixMatches() {
      assertThat(trie.search("Ad", 1)).containsExactlyInAnyOrder("Adam", "Ddom", "Adom", "Adelle", "Adamology");
    }

    @Test
    void distanceOfTwo_twoApproximateMatches_oneApproximatePrefixMatch() {
      assertThat(trie.search("Aaam", 2)).containsExactlyInAnyOrder("Adam", "Adom", "Adamology");
    }

    @Test
    @Disabled("At one point I had more rigorous distance requirements for prefix matches, but am testing a more lenient policy.")
    void prefixMatchIsExcludedWhenDistanceIsGreaterThanOne() {
      assertThat(trie.search("Ddom", 2)).containsExactlyInAnyOrder("Adam", "Adom", "Ddom");
    }
  }
}
