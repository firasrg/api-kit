package io.github.firasrg.apikit.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.firasrg.apikit.core.pagination.SortOrder.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SortOrderTests {

  @Test
  void constructor_shouldKeepValues_whenInputIsValid() {
    SortOrder order = new SortOrder("createdAt", Direction.DESC);

    assertThat(order.property()).isEqualTo("createdAt");
    assertThat(order.direction()).isEqualTo(Direction.DESC);
  }

  @Test
  void constructor_shouldThrowNullPointer_whenDirectionIsNull() {
    assertThatThrownBy(() -> new SortOrder("name", null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("direction");
  }

  @Test
  void constructor_shouldThrowNullPointer_whenPropertyIsNull() {
    assertThatThrownBy(() -> new SortOrder(null, Direction.ASC))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
  void constructor_shouldThrowIllegalArgument_whenPropertyIsBlank(String blank) {
    assertThatThrownBy(() -> new SortOrder(blank, Direction.ASC))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("must not be null or blank");
  }

  @Test
  void equals_shouldBeTrue_whenPropertyAndDirectionMatch() {
    assertThat(new SortOrder("name", Direction.ASC))
        .isEqualTo(new SortOrder("name", Direction.ASC));
  }

  @Test
  void equals_shouldBeFalse_whenDirectionDiffers() {
    assertThat(new SortOrder("name", Direction.ASC))
        .isNotEqualTo(new SortOrder("name", Direction.DESC));
  }
}
