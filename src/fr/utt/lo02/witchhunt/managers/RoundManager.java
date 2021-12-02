package fr.utt.lo02.witchhunt.managers;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.WitchHunt;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <b>RoundManager</b> provides methods to manage the progress of the game.
 * <p>
 * <b>RoundManager</b> is a singleton because it has to be accessed from many parts of the project.
 */
public final class RoundManager {

    private static RoundManager instance;

    private int index;
    private String startingPlayer;
    private int roundCount = 0;

    private RoundManager() {
    }

    /**
     * Gets the unique instance of <b>RoundManager</b>.
     * <p>
     * If the instance is null, this method creates it.
     *
     * @return the instance of <b>RoundManager</b>
     */
    public static RoundManager getInstance() {
        if (instance == null)
            instance = new RoundManager();
        return instance;
    }

    /**
     * Prepares the game before making the first player play.
     * <p>
     * This method first resets the deal system and the players for a new round.
     * Then starts a new round, makes players choose their identity for the round.
     * And finally makes the first player play.
     *
     * @see CardManager#resetDealSystem()
     * @see PlayerManager#resetAll()
     * @see RoundManager#identityRound()
     */
    public void startNewRound() {
        CardManager.getInstance().resetDealSystem();
        PlayerManager.getInstance().resetAll();

        roundCount++;
        IOController.getInstance().printInfo("Le round ".concat(String.valueOf(roundCount)).concat(" commence !"));

        identityRound();

        if (startingPlayer != null) {
            setIndexAtPlayer(startingPlayer);
            startingPlayer = null;
        }

        next();
    }

    /**
     * Makes next player play or end the round.
     * <p>
     * If there's only one player remaining unrevealed then ends the round.
     * Else, makes the next player play his turn.
     */
    public void next() {
        PlayerManager pManager = PlayerManager.getInstance();

        List<String> unrevealedPlayers = pManager.getUnrevealedPlayers().stream().toList();

        if (unrevealedPlayers.size() == 1) {
            //only one player remains unrevealed, end the round
            endRound(unrevealedPlayers.get(0));
        } else {
            //makes player at index play
            String nextPlayer = pManager.getInGamePlayers().stream().toList().get(index);

            pManager.getByName(nextPlayer).playTurn();
            incrementIndex();
        }
    }

    /**
     * Makes the targeted {@link Player} respond an accusation.
     *
     * @param accuser the name of the player who accused the target
     * @param target  the name of the targeted {@link Player}
     * @see Player#respondAccusation(String)
     */
    public void accuse(String accuser, String target) {
        PlayerManager.getInstance().getByName(target).respondAccusation(accuser);
    }

    /**
     * Sets the next player to play as a specific one.
     *
     * @param playerName name of the player to be next
     * @throws IllegalArgumentException if the name of the player isn't found in the list of players in game
     * @see PlayerManager#getInGamePlayers()
     */
    public void setIndexAtPlayer(String playerName) throws IllegalArgumentException {
        index = 0;
        for (String p : PlayerManager.getInstance().getInGamePlayers()) {
            if (p.equals(playerName))
                return;
            index++;
        }
        throw new IllegalArgumentException("RoundManager setIndexAtPlayer: playerName not found.");
    }

    /**
     * Sets the next player to play.
     */
    public void incrementIndex() {
        index++;
        if (index > PlayerManager.getInstance().getInGamePlayers().size())
            index = 0;
    }

    /**
     * Reveals the last player remaining unrevealed,
     * give him points according to his identity, then check for winners.
     *
     * @param lastUnrevealedName name of the last player remaining unrevealed
     * @see RoundManager#identityRound()
     */
    private void endRound(String lastUnrevealedName) {
        PlayerManager pManager = PlayerManager.getInstance();

        startingPlayer = lastUnrevealedName;

        Player lastUnrevealed = pManager.getByName(lastUnrevealedName);
        Identity identity = lastUnrevealed.getIdentityCard().getIdentity();

        lastUnrevealed.revealIdentity();
        lastUnrevealed.addToScore(identity.equals(Identity.WITCH) ? 2 : 1);

        IOController.getInstance().printInfo(startingPlayer
                .concat(" was the last player unrevealed. He was a ")
                .concat(identity.toString())
                .concat(" so he gains ")
                .concat(identity.equals(Identity.WITCH) ? "two points." : "one point."));

        checkForWinner();
    }

    /**
     * Makes all players choose their identity for the round.
     */
    private void identityRound() {
        PlayerManager pManager = PlayerManager.getInstance();

        boolean test = WitchHunt.isTest();

        if (test && roundCount == 1) {
            pManager.getByName("Pierre").setIdentity(Identity.VILLAGER);
            pManager.getByName("Paul").setIdentity(Identity.WITCH);
            pManager.getByName("Jacques").setIdentity(Identity.VILLAGER);
        }

        for (String playerName : pManager.getInGamePlayers()) {
            Player player = pManager.getByName(playerName);
            if (player instanceof ArtificialPlayer || !(test && roundCount == 1))
                player.chooseIdentity();
        }
    }

    /**
     * Checks if a player as enough points to win.
     * If there's one or more, then display a message to indicate the winner
     * and end the game. Else, start a new round.
     */
    private void checkForWinner() {
        PlayerManager pManager = PlayerManager.getInstance();

        Iterator<String> it = pManager.getAllPlayers().iterator();

        ArrayList<String> highestScorePlayers = new ArrayList<>();
        highestScorePlayers.add(it.next());

        while (it.hasNext()) {
            String pName = it.next();
            int scoreDifference = pManager.getByName(pName).getScore() - pManager.getByName(highestScorePlayers.get(0)).getScore();

            if (scoreDifference > 0) {
                //current player is the new highest score, set the list to only him
                highestScorePlayers = new ArrayList<>();
                highestScorePlayers.add(pName);
            } else if (scoreDifference == 0) {
                //new ex-aequo player, add it to the list
                highestScorePlayers.add(pName);
            }
            //current player's score < the highest score, do nothing
        }

        if (pManager.getByName(highestScorePlayers.get(0)).getScore() >= 5) {
            // We have one or more winners
            StringBuilder sb = new StringBuilder();

            if (highestScorePlayers.size() > 1) {
                for (String player : highestScorePlayers) {
                    sb.append(player);
                    sb.append(", ");
                }
                sb.delete(sb.length() - 3, sb.length() - 1);//remove useless ", " at the end
                int lastIndexOf = sb.lastIndexOf(",");
                sb.replace(lastIndexOf, lastIndexOf + 1, " and ");//replace last comma by "and"
                sb.append(" are the winners !");
            } else {
                sb.append(highestScorePlayers.get(0));
                sb.append(" is the winner !");
            }

            IOController.getInstance().printInfo(sb.toString());
            System.exit(0);
        } else {
            // no winner yet, play a new round
            IOController.getInstance().printInfo("There is no winner yet, a new round will start.");
            startNewRound();
        }
    }
}
