package fr.utt.lo02.witchhunt.io;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * The <b>IOController</b> class controls and coordinate every user interfaces.
 */
public final class IOController implements IOInterface {
    /**
     * Unique instance of <b>IOController</b>.
     */
    private static IOController instance;

    /**
     * List of all interfaces of the game.
     */
    private final ArrayList<IOInterface> interfaces = new ArrayList<>();
    /**
     * Temporarily stores the inputted values.
     */
    private final HashMap<String, Object> readValues = new HashMap<>();
    /**
     * Tells if the game is waiting for a user input.
     */
    private volatile boolean waiting;

    /**
     * Constructor to be called only once.
     */
    private IOController() {
        waiting = true;
        SwingUtilities.invokeLater(() -> {
            interfaces.add(new CLI());
            waiting = false;
        });

        while (waiting) Thread.onSpinWait();
        interfaces.add(new GUI());
    }

    /**
     * Gets the unique instance of <b>IOController</b>.
     *
     * @return the instance
     */
    public static IOController getInstance() {
        if (instance == null) {
            instance = new IOController();
        }
        return instance;
    }

    /**
     * Stops waiting for user input.
     */
    public void stopWaiting() {
        clear();
        waiting = false;
    }

    /**
     * Reads an inputted value.
     *
     * @param key   string identifying what the value is for
     *              <ul>
     *              <li>"int" -> {@link IOInterface#readIntBetween(int, int, String)}</li>
     *              <li>"boolean" -> {@link IOInterface#yesOrNo(String, String, String)}</li>
     *              <li>"name" -> {@link IOInterface#readName(int)}</li>
     *              <li>"from_set" -> {@link IOInterface#readFromSet(Set, String)}</li>
     *              </ul>
     * @param value value inputted
     */
    public void read(String key, Object value) {
        readValues.put(key, value);
        stopWaiting();
    }

    /**
     * Starts waiting for an input.
     */
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

    /**
     * Calls the {@link GUI#startGame()} method of every GUI.
     */
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
