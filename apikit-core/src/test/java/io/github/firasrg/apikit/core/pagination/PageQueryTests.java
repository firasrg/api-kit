package io.github.firasrg.apikit.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class PageQueryTests {

  @Test
  void firstPage_shouldUseDefaultSize_whenCalled() {
    PageQuery query = PageQuery.firstPage();

    assertThat(query.page()).isZero();
    assertThat(query.size()).isEqualTo(PageQuery.DEFAULT_SIZE);
    assertThat(query.sort()).isEmpty();
  }

  @Test
  void constructor_shouldThrow_whenPageIsNegative() {
    assertThatThrownBy(() -> PageQuery.of(-1, 10)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void constructor_shouldThrow_whenSizeIsZero() {
    assertThatThrownBy(() -> PageQuery.of(0, 0)).isInstanceOf(IllegalArgumentException.class);
  }
}
