package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public final class RoundManager {

    private static RoundManager instance;

    private int index;
    private String startingPlayer;

    private RoundManager() {
    }

    public static RoundManager getInstance() {
        if (instance == null)
            instance = new RoundManager();
        return instance;
    }

    public void startNewRound() {
        PlayerManager pManager = PlayerManager.getInstance();

        CardManager.getInstance().resetDealSystem();
        pManager.resetAll();
        identityRound();

        if (startingPlayer == null) {
            index = new Random().nextInt(pManager.getInGamePlayers().size());
        } else {
            setIndexAtPlayer(startingPlayer);
            startingPlayer = null;
        }

        next();
    }

    public void next() {
        PlayerManager pManager = PlayerManager.getInstance();

        if (pManager.getUnrevealedPlayers().size() <= 1) {
            endRound();
        } else {
            String nextPlayer = pManager.getInGamePlayers().get(index);

            pManager.getByName(nextPlayer).playTurn();
            incrementIndex();
        }
    }

    public void accuse(String accuser, String target) {
        setIndexAtPlayer(target);
        PlayerManager.getInstance().getByName(target).respondAccusation(accuser);
    }

    public void endRound() {
        PlayerManager pManager = PlayerManager.getInstance();

        startingPlayer = pManager.getUnrevealedPlayers().get(0);

        Player lastUnrevealed = pManager.getByName(startingPlayer);
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

    public void setIndexAtPlayer(String playerName) {
        index = PlayerManager.getInstance().getInGamePlayers().lastIndexOf(playerName);
    }

    public void incrementIndex() {
        index++;
        if (index > PlayerManager.getInstance().getInGamePlayers().size())
            index = 0;
    }

    private void identityRound() {
        PlayerManager pManager = PlayerManager.getInstance();
        for (String player : pManager.getInGamePlayers()) {
            pManager.getByName(player).chooseIdentity();
        }
    }

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
        } else {
            // no winner yet, play a new round
            IOController.getInstance().printInfo("There is no winner yet, a new round will start.");
            startNewRound();
        }
    }
}
