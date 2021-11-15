package fr.utt.lo02.witchhunt;

import java.util.ArrayList;
import java.util.Random;

public final class Utils {

    public static <T> T randomFromList(ArrayList<T> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
