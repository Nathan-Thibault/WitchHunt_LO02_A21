package fr.utt.lo02.witchhunt.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class IOController implements IOInterface {

    private static IOController instance;

    private final ArrayList<IOInterface> interfaces = new ArrayList<>();

    private final HashMap<String, Object> readValues = new HashMap<>();
    private volatile boolean waiting = false;

    private IOController() {
        interfaces.add(new CLI());
        interfaces.add(new GUI());
    }

    public static IOController getInstance() {
        if (instance == null) {
            instance = new IOController();
        }
        return instance;
    }

    public void stopWaiting() {
        clear();
        waiting = false;
    }

    public void read(String key, Object value) {
        readValues.put(key, value);
        stopWaiting();
    }

    private void startWaiting() {
        waiting = true;
        readValues.clear();

        while (waiting) Thread.onSpinWait();
    }

    @Override
    public void clear() {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.clear();
        }
    }

    @Override
    public void titleScreen() {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.titleScreen();
        }

        startWaiting();
        clear();
    }

    @Override
    public void pause(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.pause(msg);
        }

        startWaiting();
        clear();
    }

    @Override
    public void displayGameInfos() {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.displayGameInfos();
        }
    }

    @Override
    public void playerInfos(String playerName, String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.playerInfos(playerName, msg);
        }
    }

    @Override
    public void printInfo(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.printInfo(msg);
        }
    }

    @Override
    public int readIntBetween(int min, int max, String message) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.readIntBetween(min, max, message);
        }

        startWaiting();
        clear();
        return (int) readValues.get("int");
    }

    @Override
    public String readName(int playerNum) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.readName(playerNum);
        }

        startWaiting();
        clear();

        return (String) readValues.get("name");
    }

    @Override
    public <T> T readFromSet(Set<T> set, String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.readFromSet(set, msg);
        }

        startWaiting();
        clear();

        @SuppressWarnings("unchecked") final T from_set = (T) readValues.get("from_set");
        return from_set;
    }

    public void startGame() {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                ((GUI) ioInterface).startGame();
        }
    }
}
