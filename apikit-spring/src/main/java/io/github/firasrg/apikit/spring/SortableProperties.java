package io.github.firasrg.apikit.spring;

import io.github.firasrg.apikit.core.exception.InvalidSortException;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Whitelists client-facing sort names and maps them to persistence paths.
 *
 * <p>Use this mapping before passing client-requested sort properties to Spring Data so arbitrary
 * client input cannot select persistence paths.
 */
public final class SortableProperties {

  private final Map<String, String> paths;

  private SortableProperties(Map<String, String> paths) {
    this.paths = Map.copyOf(paths);
  }

  /**
   * Maps client-facing names to persistence paths, e.g. {@code "city" -> "address.city"}.
   *
   * @param clientToPath mapping from public sort names to persistence property paths
   * @return an immutable whitelist based on the supplied mappings
   */
  public static SortableProperties of(Map<String, String> clientToPath) {
    return new SortableProperties(clientToPath);
  }

  /**
   * Allows the given names, using each as its own persistence path.
   *
   * @param names client-facing names that are also persistence paths
   * @return an immutable whitelist of the supplied names
   */
  public static SortableProperties of(String... names) {
    return new SortableProperties(
        Arrays.stream(names).collect(Collectors.toMap(Function.identity(), Function.identity())));
  }

  /**
   * Returns the persistence path for a client-facing name.
   *
   * @param clientName client-facing sort name to resolve
   * @return the mapped persistence path
   * @throws InvalidSortException if the name is not sortable
   */
  public String resolve(String clientName) {
    String path = paths.get(clientName);
    if (path == null) {
      throw new InvalidSortException(clientName);
    }
    return path;
  }
}
