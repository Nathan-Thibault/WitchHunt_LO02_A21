package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.List;

public interface IOInterface {
    void clear();

    void titleScreen();

    void pause();

    void displayGameInfos();

    void printInfo(String msg);

    void printError(String msg);

    int readIntBetween(int min, int max);

    Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType);

    <T> T readFromList(List<T> list);
}
