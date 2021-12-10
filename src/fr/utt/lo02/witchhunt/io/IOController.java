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
        interfaces.add(new CommandLineInterface());
    }

    public static IOController getInstance() {
        if (instance == null) {
            instance = new IOController();
        }
        return instance;
    }

    public void stopWaiting() {
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
            new Thread(ioInterface::titleScreen).start();
        }

        startWaiting();
        clear();
    }

    @Override
    public void pause() {
        for(IOInterface ioInterface: interfaces){
            new Thread(ioInterface::pause).start();
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
    public void printInfo(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.printInfo(msg);
        }
    }

    @Override
    public void printError(String msg) {
        for (IOInterface ioInterface : interfaces) {
            ioInterface.printError(msg);
        }
    }

    @Override
    public int readIntBetween(int min, int max) {
        for (IOInterface ioInterface : interfaces) {
            new Thread(() -> ioInterface.readIntBetween(min, max)).start();
        }

        startWaiting();
        clear();
        return (int) readValues.get("int");
    }

    @Override
    public <T> T readFromSet(Set<T> set) {
        for (IOInterface ioInterface : interfaces) {
            new Thread(() -> ioInterface.readFromSet(set)).start();
        }

        startWaiting();
        clear();

        @SuppressWarnings("unchecked") final T from_list = (T) readValues.get("from_list");
        return from_list;
    }
}
