package fr.utt.lo02.witchhunt.io;

import java.util.Set;

/**
 *
 */
public interface IOInterface {
    /**
     * Stops any user input and clears screen if necessary.
     */
    void clear();

    /**
     * Displays a title screen at the start of the application.
     */
    void titleScreen();

    /**
     * Pauses the game until a user action is performed.
     *
     * @param msg message to display with the pause
     */
    void pause(String msg);

    /**
     * Displays the information about the specified player with the specified message.
     *
     * @param playerName name of the payer to display the information
     * @param msg        message to display with the player information
     */
    void playerInfos(String playerName, String msg);

    /**
     * Displays a message.
     *
     * @param msg message to display
     */
    void printInfo(String msg);

    /**
     * Asks an integer between the specified range to the user.
     *
     * @param min minimum input accepted
     * @param max maximum input accepted
     * @param msg message to display when asking for input
     * @return the inputted integer
     */
    int readIntBetween(int min, int max, String msg);

    /**
     * Asks the user to choose between two specified options.
     *
     * @param yesMsg one of the option
     * @param noMsg  the other option
     * @param msg    message to display when asking for input
     * @return <code>true</code> if the first option is chosen,
     * <code>false</code> if the second option is chosen
     */
    boolean yesOrNo(String yesMsg, String noMsg, String msg);

    /**
     * Asks the user to choose a name for the specified player.
     *
     * @param playerNum number of the player
     * @return inputted name for the player
     */
    String readName(int playerNum);

    /**
     * Aks the user to choose one element of the specified set.
     *
     * @param set the set to choose from
     * @param msg message to display when asking for input
     * @param <T> type of element the set contains
     * @return an element of the set
     */
    <T> T readFromSet(Set<T> set, String msg);
}
