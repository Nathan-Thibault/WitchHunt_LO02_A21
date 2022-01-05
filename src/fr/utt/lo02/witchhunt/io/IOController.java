package fr.utt.lo02.witchhunt.io;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public final class IOController implements IOInterface {

    private static IOController instance;

    private final ArrayList<IOInterface> interfaces = new ArrayList<>();
    private final HashMap<String, Object> readValues = new HashMap<>();

    private volatile boolean waiting;

    private IOController() {
        waiting = true;
        SwingUtilities.invokeLater(() -> {
            interfaces.add(new CLI());
            waiting = false;
        });

        while (waiting) Thread.onSpinWait();
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
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(ioInterface::clear);
            else
                ioInterface.clear();
        }
    }

    @Override
    public void titleScreen() {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(ioInterface::titleScreen);
            else
                ioInterface.titleScreen();
        }

        startWaiting();
        clear();
    }

    @Override
    public void pause(String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.pause(msg));
            else
                ioInterface.pause(msg);
        }

        startWaiting();
        clear();
    }

    @Override
    public void playerInfos(String playerName, String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.playerInfos(playerName, msg));
            else
                ioInterface.playerInfos(playerName, msg);
        }
    }

    @Override
    public void printInfo(String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.printInfo(msg));
            else
                ioInterface.printInfo(msg);
        }
    }

    @Override
    public int readIntBetween(int min, int max, String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.readIntBetween(min, max, msg));
            else
                ioInterface.readIntBetween(min, max, msg);
        }

        startWaiting();
        clear();
        return (int) readValues.get("int");
    }

    @Override
    public boolean yesOrNo(String yesMsg, String noMsg, String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.yesOrNo(yesMsg, noMsg, msg));
            else
                ioInterface.yesOrNo(yesMsg, noMsg, msg);
        }

        startWaiting();
        clear();
        return (boolean) readValues.get("boolean");
    }

    @Override
    public String readName(int playerNum) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.readName(playerNum));
            else
                ioInterface.readName(playerNum);
        }

        startWaiting();
        clear();

        return (String) readValues.get("name");
    }

    @Override
    public <T> T readFromSet(Set<T> set, String msg) {
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> ioInterface.readFromSet(set, msg));
            else
                ioInterface.readFromSet(set, msg);
        }

        startWaiting();
        clear();

        @SuppressWarnings("unchecked") final T from_set = (T) readValues.get("from_set");
        return from_set;
    }

    public void startGame() {
        waiting = true;
        for (IOInterface ioInterface : interfaces) {
            if (ioInterface instanceof GUI)
                SwingUtilities.invokeLater(() -> {
                    ((GUI) ioInterface).startGame();
                    waiting = false;
                });
        }
        while (waiting) Thread.onSpinWait();
    }
}
