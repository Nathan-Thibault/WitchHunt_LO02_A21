package fr.utt.lo02.witchhunt.io;

import java.util.Set;

public interface IOInterface {
    void clear();

    void titleScreen();

    void pause();

    void displayGameInfos();

    void printInfo(String msg);

    int readIntBetween(int min, int max, String message);

    <T> T readFromSet(Set<T> list);
}
