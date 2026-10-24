package io.github.firasrg.apikit.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class PageQueryTests {

  @Test
  void of_shouldCreateUnsortedQuery_whenArgumentsAreValid() {
    PageQuery query = PageQuery.of(2, 50);

    assertThat(query.page()).isEqualTo(2);
    assertThat(query.size()).isEqualTo(50);
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

  @Test
  void constructor_shouldThrow_whenSizeIsNegative() {
    assertThatThrownBy(() -> PageQuery.of(0, -5)).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void constructor_shouldAcceptLargeSize_whenSizeIsPositive() {
    assertThat(PageQuery.of(0, Integer.MAX_VALUE).size()).isEqualTo(Integer.MAX_VALUE);
  }

  @Test
  void constructor_shouldThrow_whenSortIsNull() {
    assertThatThrownBy(() -> new PageQuery(0, 10, null)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void constructor_shouldThrow_whenSortContainsNull() {
    List<SortOrder> sort = Collections.singletonList(null);

    assertThatThrownBy(() -> new PageQuery(0, 10, sort)).isInstanceOf(NullPointerException.class);
  }

  @Test
  void constructor_shouldNotReflectLaterChanges_whenSourceListIsMutated() {
    List<SortOrder> source = new ArrayList<>();
    PageQuery query = new PageQuery(0, 10, source);

    source.add(new SortOrder("name", SortOrder.Direction.ASC));

    assertThat(query.sort()).isEmpty();
  }

  @Test
  void sort_shouldBeUnmodifiable_whenReturned() {
    PageQuery query = PageQuery.of(0, 10);

    assertThatThrownBy(() -> query.sort().add(new SortOrder("name", SortOrder.Direction.ASC)))
        .isInstanceOf(UnsupportedOperationException.class);
  }
}
