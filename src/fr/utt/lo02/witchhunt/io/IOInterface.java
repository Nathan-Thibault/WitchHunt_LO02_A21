package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

public interface IOInterface {
    void clear();

    void titleScreen();

    void printInfo(String msg);

    void printError(String msg);

    void readIntBetween(int min, int max);

    void readStrategy(Strategy.StrategyType strategyType);
}
