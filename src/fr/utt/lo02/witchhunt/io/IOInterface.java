package fr.utt.lo02.witchhunt.io;

import java.util.Set;

public interface IOInterface {
    void clear();

    void titleScreen();

    void pause(String msg);

    void displayGameInfos();

    void playerInfos(String playerName, String msg);

    void printInfo(String msg);

    int readIntBetween(int min, int max, String msg);

    <T> T readFromSet(Set<T> list, String msg);
}
