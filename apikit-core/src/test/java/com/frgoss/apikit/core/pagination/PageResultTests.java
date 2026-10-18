package com.frgoss.apikit.core.pagination;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;

class PageResultTests {

  @Test
  void totalPages_shouldReturnZero_whenThereAreNoElements() {
    assertThat(pageOf(0, 20, 0).totalPages()).isZero();
  }

  @Test
  void totalPages_shouldNotAddExtraPage_whenTotalIsExactMultipleOfSize() {
    assertThat(pageOf(0, 20, 40).totalPages()).isEqualTo(2);
  }

  @Test
  void totalPages_shouldRoundUp_whenThereIsARemainder() {
    assertThat(pageOf(0, 20, 41).totalPages()).isEqualTo(3);
  }

  @Test
  void totalPages_shouldThrow_whenPageCountExceedsIntRange() {
    PageResult<ContentItem> result = pageOf(0, 1, Integer.MAX_VALUE + 1L);

    assertThatThrownBy(result::totalPages).isInstanceOf(ArithmeticException.class);
  }

  @Test
  void hasNext_shouldBeFalse_whenThereAreNoElements() {
    assertThat(pageOf(0, 20, 0).hasNext()).isFalse();
  }

  @Test
  void hasNext_shouldBeTrue_onFirstPageOfMany() {
    assertThat(pageOf(0, 20, 41).hasNext()).isTrue();
  }

  @Test
  void hasNext_shouldBeFalse_onLastPage() {
    PageResult<ContentItem> lastPage = pageOf(2, 20, 41);

    assertThat(lastPage.content()).hasSize(1);
    assertThat(lastPage.hasNext()).isFalse();
  }

  @Test
  void hasNext_shouldBeFalse_onLastPageWhenTotalIsExactMultiple() {
    PageResult<ContentItem> lastPage = pageOf(1, 20, 40);

    assertThat(lastPage.content()).hasSize(20);
    assertThat(lastPage.hasNext()).isFalse();
  }

  @Test
  void hasPrevious_shouldBeFalse_onFirstPage() {
    assertThat(pageOf(0, 20, 41).hasPrevious()).isFalse();
  }

  @Test
  void hasPrevious_shouldBeTrue_afterFirstPage() {
    assertThat(pageOf(1, 20, 41).hasPrevious()).isTrue();
  }

  @Test
  void hasContent_shouldBeFalse_whenThereAreNoElements() {
    assertThat(pageOf(0, 20, 0).hasContent()).isFalse();
  }

  @Test
  void hasContent_shouldBeTrue_whenPageHoldsItems() {
    assertThat(pageOf(0, 20, 1).hasContent()).isTrue();
  }

  @Test
  void map_shouldConvertItemsAndKeepMetadata() {
    PageResult<String> result = pageOf(1, 2, 7).map(ContentItem::name);

    assertThat(result.content()).containsExactly("Customer 3", "Customer 4");
    assertThat(result.page()).isEqualTo(1);
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.totalElements()).isEqualTo(7);
  }

  @Test
  void constructor_shouldThrow_whenContentExceedsPageSize() {
    List<ContentItem> threeCustomers =
        List.of(new ContentItem(1, "A"), new ContentItem(2, "B"), new ContentItem(3, "C"));

    assertThatThrownBy(() -> new PageResult<>(threeCustomers, 0, 2, 3))
        .isInstanceOf(IllegalArgumentException.class);
  }

  /**
   * Builds the page a repository would return for the given paging values: the content is the slice
   * of customers 1..totalElements that falls inside the requested page.
   */
  private static PageResult<ContentItem> pageOf(int page, int size, long totalElements) {
    long offset = (long) page * size;
    int count = (int) Math.max(0, Math.min(size, totalElements - offset));

    List<ContentItem> content =
        LongStream.range(offset, offset + count)
            .mapToObj(i -> new ContentItem(i + 1, "Customer " + (i + 1)))
            .toList();

    return new PageResult<>(content, page, size, totalElements);
  }

  private record ContentItem(long id, String name) {}
}
