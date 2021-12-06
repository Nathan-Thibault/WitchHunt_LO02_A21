package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.Set;

public interface IOInterface {
    void clear();

    void titleScreen();

    void pause();

    void displayGameInfos();

    void printInfo(String msg);

    void printError(String msg);

    int readIntBetween(int min, int max);

    <T> T readFromSet(Set<T> list);
}
