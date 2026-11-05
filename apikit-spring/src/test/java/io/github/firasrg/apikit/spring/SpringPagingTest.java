package io.github.firasrg.apikit.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.firasrg.apikit.core.exception.InvalidSortException;
import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.SortOrder;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

/** Verifies conversion between core paging models and Spring Data paging types. */
class SpringPagingTest {

  private static final SortableProperties SORTABLE =
      SortableProperties.of(java.util.Map.of("name", "name", "city", "address.city"));

  @Test
  void toPageable_shouldMapPageSizeAndSort_whenQueryIsValid() {
    var query =
        new PageQuery(
            2,
            20,
            List.of(
                new SortOrder("city", SortOrder.Direction.DESC),
                new SortOrder("name", SortOrder.Direction.ASC)));

    var pageable = SpringPaging.toPageable(query, SORTABLE);

    assertThat(pageable.getPageNumber()).isEqualTo(2);
    assertThat(pageable.getPageSize()).isEqualTo(20);
    assertThat(pageable.getSort())
        .containsExactly(Sort.Order.desc("address.city"), Sort.Order.asc("name"));
  }

  @Test
  void toPageable_shouldBeUnsorted_whenQueryHasNoSort() {
    var pageable = SpringPaging.toPageable(new PageQuery(0, 10, List.of()), SORTABLE);

    assertThat(pageable.getSort().isUnsorted()).isTrue();
  }

  @Test
  void toPageable_shouldThrowInvalidSort_whenPropertyIsNotWhitelisted() {
    var query = new PageQuery(0, 10, List.of(new SortOrder("password", SortOrder.Direction.ASC)));

    assertThatThrownBy(() -> SpringPaging.toPageable(query, SORTABLE))
        .isInstanceOf(InvalidSortException.class);
  }

  @Test
  void toPageResult_shouldCopyContentAndMetadata_whenPageIsPaged() {
    var page = new PageImpl<>(List.of("a", "b"), PageRequest.of(1, 2), 5);

    var result = SpringPaging.toPageResult(page);

    assertThat(result.content()).containsExactly("a", "b");
    assertThat(result.page()).isEqualTo(1);
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.totalElements()).isEqualTo(5);
  }
}
