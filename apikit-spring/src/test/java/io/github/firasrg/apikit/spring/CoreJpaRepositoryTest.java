package io.github.firasrg.apikit.spring;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.firasrg.apikit.core.pagination.PageQuery;
import io.github.firasrg.apikit.core.pagination.SortOrder;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

/** Exercises filtering, mapped sorting, and pagination through the Spring Data JPA repository. */
@DataJpaTest
class CoreJpaRepositoryTest {

  private static final SortableProperties SORTABLE =
      SortableProperties.of(Map.of("productName", "name"));

  @Autowired private ProductRepository repository;

  // ... the two @Test methods unchanged
  @Test
  void findAll_shouldFilterAndSortByMappedPath_whenSpecificationAndWhitelistedSortAreUsed() {
    repository.saveAll(
        List.of(
            new ProductEntity("Keyboard", "Peripherals"),
            new ProductEntity("Monitor", "Displays"),
            new ProductEntity("Mouse", "Peripherals")));
    Specification<ProductEntity> peripherals =
        (root, query, cb) -> cb.equal(root.get("category"), "Peripherals");
    var pageable =
        SpringPaging.toPageable(
            new PageQuery(0, 10, List.of(new SortOrder("productName", SortOrder.Direction.ASC))),
            SortableProperties.of(Map.of("productName", "name")));

    var result = SpringPaging.toPageResult(repository.findAll(peripherals, pageable));

    assertThat(result.content())
        .extracting(ProductEntity::getName)
        .containsExactly("Keyboard", "Mouse");
    assertThat(result.totalElements()).isEqualTo(2);
  }

  @Test
  void findAll_shouldReturnSecondPage_whenPageSizeIsSmallerThanResultCount() {
    repository.saveAll(
        List.of(
            new ProductEntity("Keyboard", "Peripherals"),
            new ProductEntity("Monitor", "Displays"),
            new ProductEntity("Mouse", "Peripherals")));
    var pageable =
        SpringPaging.toPageable(
            new PageQuery(1, 2, List.of(new SortOrder("productName", SortOrder.Direction.ASC))),
            SORTABLE);

    var result = SpringPaging.toPageResult(repository.findAll(pageable));

    assertThat(result.content()).extracting(ProductEntity::getName).containsExactly("Mouse");
    assertThat(result.totalElements()).isEqualTo(3);
  }
}
