package fr.utt.lo02.witchhunt.io;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.ArrayList;

public class IOController {

    private static IOController instance;

    private final ArrayList<IOInterface> interfaces = new ArrayList<>();

    private volatile int readInt = -1;
    private volatile Class<? extends Strategy> readStrategy = null;
    private volatile boolean waiting = false;

    private IOController() {
        interfaces.add(new CommandLineInterface());
    }

    public static IOController getInstance() {
        if (instance == null) {
            instance = new IOController();
        }
        return instance;
    }

    public void setReadInt(int n) {
        readInt = n;
    }

    public void setReadStrategy(Class<? extends Strategy> strategy) {
        readStrategy = strategy;
    }

    public void stopWaiting() {
        waiting = false;
    }

    public void clear() {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.clear();
        }
    }

    public void titleScreen() {
        waiting = true;

        for (IOInterface ioInterface : interfaces) {
            new Thread(ioInterface::titleScreen).start();
        }

        while (waiting) {
            Thread.onSpinWait();
        }

        clear();
    }

    public void printInfo(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.printInfo(msg);
        }
    }

    public void printError(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.printError(msg);
        }
    }

    public int readIntBetween(int min, int max) {
        readInt = -1;

        for (IOInterface ioInterface : interfaces) {
            new Thread(() -> ioInterface.readIntBetween(min, max)).start();
        }

        while (readInt == -1) {
            Thread.onSpinWait();
        }

        clear();
        return readInt;
    }

    public Class<? extends Strategy> readStrategy(Strategy.StrategyType strategyType) {
        readStrategy = null;

        for (IOInterface ioInterface : interfaces) {
            new Thread(() -> ioInterface.readStrategy(strategyType)).start();
        }

        while (readStrategy == null) {
            Thread.onSpinWait();
        }

        clear();
        return readStrategy;
    }
}
