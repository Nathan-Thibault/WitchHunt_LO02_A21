package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.Random;

public final class RoundManager {

    private static RoundManager instance;

    private int index;
    private String startingPlayer;

    private RoundManager() {
    }

    public RoundManager getInstance() {
        if (instance == null)
            instance = new RoundManager();
        return instance;
    }

    public void startNewRound() {
        PlayerManager pManager = PlayerManager.getInstance();

        CardManager.getInstance().resetDealSystem();
        pManager.resetAll();
        //TODO : make players choose their identity

        if(startingPlayer == null){
            index = new Random().nextInt(pManager.getInGamePlayers().size());
        } else {
            setIndexAtPlayer(startingPlayer);
            startingPlayer = null;
        }

        next();
    }

    public void next() {
        PlayerManager pManager = PlayerManager.getInstance();

        if(pManager.getUnrevealedPlayers().size() <= 1) {
            endRound();
        } else {
            pManager.getByName(pManager.getInGamePlayers().get(index)).playTurn();
            incrementIndex();
        }
    }

    public void endRound(){
        PlayerManager pManager = PlayerManager.getInstance();

        startingPlayer = pManager.getUnrevealedPlayers().get(0);

        Player lastUnrevealed = pManager.getByName(startingPlayer);
        lastUnrevealed.revealIdentity();
        lastUnrevealed.addToScore(lastUnrevealed.getIdentityCard().getIdentity().equals(Identity.WITCH) ? 2 : 1);

        //TODO :  determine winner and display scores
    }

    public void setIndexAtPlayer(String playerName) {
        index = PlayerManager.getInstance().getInGamePlayers().lastIndexOf(playerName);
    }

    private void incrementIndex(){
        index++;
        if (index > PlayerManager.getInstance().getInGamePlayers().size())
            index = 0;
    }
}
