package fr.utt.lo02.witchhunt;

import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * The <b>Utils</b> class regroups utility methods that may be used anywhere in the project.
 */
public final class Utils {

    /**
     * Chooses an element of the specified {@link Set} at random.
     *
     * @param set the set to choose from
     * @param <T> type of objects the set contains
     * @return one element of the specified set
     */
    public static <T> T randomFromSet(Set<T> set) {
        List<T> list = set.stream().toList();
        return randomFromList(list);
    }

    /**
     * Chooses an element of the specified {@link List} at random.
     *
     * @param list the list to choose from
     * @param <T>  type of objects the list contains
     * @return one element of the specified list
     */
    public static <T> T randomFromList(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}