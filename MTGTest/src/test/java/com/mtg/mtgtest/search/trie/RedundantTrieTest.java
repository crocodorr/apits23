package com.mtg.mtgtest.search.trie;
import java.util.function.Function;
import org.junit.jupiter.api.Test;

class RedundantTrieTest {


  @Test
  void get_requiresExactMatch() {
    final Trie<String> trie = Trie.withKeyMapping(Function.identity());
    trie.add("abcd");
    trie.search("bbcd", 2);
  }

  @Test
  void get_multipleBranches_requiresExactMatch() {
    final Trie<String> trie = Trie.withKeyMapping(Function.identity());
    trie.add("abc");
    trie.add("ax");
  }
}
