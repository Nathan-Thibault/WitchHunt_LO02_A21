package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.ArrayList;

public interface IOInterface {
    void clear();

    void titleScreen();

    void displayGameInfos();

    void playerTurn(String playerName);

    void printInfo(String msg);

    void printError(String msg);

    int readIntBetween(int min, int max);

    Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType);

    Identity readIdentity();

    <T> T readFromList(ArrayList<T> list);
}
