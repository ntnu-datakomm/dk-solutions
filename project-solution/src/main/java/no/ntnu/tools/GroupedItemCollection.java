package no.ntnu.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A generic class which contains a map-of-list collection.
 * The key of the map is a "type of objects".
 * The value is then a list which contains all the items of that type.
 *
 * @param <T> The class of items being stored
 */
public class GroupedItemCollection<T> implements Iterable<T> {
  private final Map<String, List<T>> items = new HashMap<>();

  /**
   * Add an item to the collection. Duplicates are discarded.
   *
   * @param type The type of the item
   * @param item The item to add
   */
  public void add(String type, T item) {
    List<T> itemGroup = getGroup(type);
    if (!itemGroup.contains(item)) {
      itemGroup.add(item);
    }
  }

  private List<T> getGroup(String type) {
    return items.computeIfAbsent(type, k -> new ArrayList<>());
  }

  /**
   * Get the i-th item of a given type.
   *
   * @param type The type of the item
   * @param i    The index of the item in the group with the given type.
   *             Indexing starts at zero.
   * @return The desired item or null if it is not found
   */
  public T get(String type, int i) {
    T item = null;
    List<T> group = items.get(type);
    if (group != null && i >= 0 && i < group.size()) {
      item = group.get(i);
    }
    return item;
  }

  @Override
  public Iterator<T> iterator() {
    return items.values().stream().flatMap(List::stream).iterator();
  }
}

