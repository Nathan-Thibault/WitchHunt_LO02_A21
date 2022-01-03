package fr.utt.lo02.witchhunt.io;

import java.util.Set;

public interface IOInterface {
    void clear();

    void titleScreen();

    void pause(String msg);

    void playerInfos(String playerName, String msg);

    void printInfo(String msg);

    int readIntBetween(int min, int max, String msg);

    String readName(int playerNum);

    <T> T readFromSet(Set<T> list, String msg);
}
