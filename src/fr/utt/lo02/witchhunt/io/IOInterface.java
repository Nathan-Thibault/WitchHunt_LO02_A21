package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

public interface IOInterface {
    void titleScreen();

    void printInfo(String msg);

    void printError(String msg);

    int readIntBetween(int min, int max);

    Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType);
}
