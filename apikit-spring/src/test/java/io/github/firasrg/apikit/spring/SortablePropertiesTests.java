package io.github.firasrg.apikit.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.firasrg.apikit.core.exception.InvalidSortException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SortablePropertiesTests {

  @Test
  void resolve_shouldReturnMappedPath_whenNameIsMapped() {
    var sortable = SortableProperties.of(Map.of("city", "address.city"));

    assertThat(sortable.resolve("city")).isEqualTo("address.city");
  }

  @Test
  void resolve_shouldReturnSameName_whenCreatedFromNames() {
    var sortable = SortableProperties.of("name", "createdAt");

    assertThat(sortable.resolve("name")).isEqualTo("name");
    assertThat(sortable.resolve("createdAt")).isEqualTo("createdAt");
  }

  @Test
  void resolve_shouldThrowInvalidSort_whenNameIsNotWhitelisted() {
    var sortable = SortableProperties.of("name");

    assertThatThrownBy(() -> sortable.resolve("password")).isInstanceOf(InvalidSortException.class);
  }

  @Test
  void resolve_shouldNotExposePersistencePath_whenOnlyPathIsRequested() {
    var sortable = SortableProperties.of(Map.of("city", "address.city"));

    assertThatThrownBy(() -> sortable.resolve("address.city"))
        .isInstanceOf(InvalidSortException.class);
  }

  @Test
  void of_shouldNotReflectLaterChanges_whenSourceMapIsMutated() {
    var source = new java.util.HashMap<>(Map.of("name", "name"));
    var sortable = SortableProperties.of(source);
    source.put("secret", "secret");

    assertThatThrownBy(() -> sortable.resolve("secret")).isInstanceOf(InvalidSortException.class);
  }
}
