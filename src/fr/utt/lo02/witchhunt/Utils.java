package fr.utt.lo02.witchhunt;

import java.util.List;
import java.util.Random;
import java.util.Set;

public final class Utils {

    public static <T> T randomFromSet(Set<T> set) {
        List<T> list = set.stream().toList();
        return randomFromList(list);
    }

    public static <T> T randomFromList(List<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
