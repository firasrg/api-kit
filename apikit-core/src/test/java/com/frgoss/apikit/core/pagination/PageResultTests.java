package com.frgoss.apikit.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class PageResultTests {

  @Test
  void totalPages_shouldRoundUp_whenLastPageIsPartial() {
    PageResult<String> result = new PageResult<>(List.of("a"), 0, 10, 21);

    assertThat(result.totalPages()).isEqualTo(3);
  }

  @Test
  void map_shouldKeepMetadata_whenItemsAreConverted() {
    PageResult<Integer> result = new PageResult<>(List.of("a", "bb"), 1, 2, 7).map(String::length);

    assertThat(result.content()).containsExactly(1, 2);
    assertThat(result.page()).isEqualTo(1);
    assertThat(result.totalElements()).isEqualTo(7);
  }
}
