package fr.utt.lo02.witchhunt;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.Random;

public final class RoundManager {

    private static RoundManager instance;

    private int index;
    private ArrayList<String> players;
    private String startingPlayer;

    private RoundManager() {
    }

    public RoundManager getInstance() {
        if (instance == null)
            instance = new RoundManager();
        return instance;
    }

    public void startNewRound() {
        CardManager.getInstance().resetDealSystem();
        PlayerManager.getInstance().resetAll();
        //TODO : make players choose their identity

        players = PlayerManager.getInstance().getAllPlayers();
        if(startingPlayer == null){
            index = new Random().nextInt(players.size());
        } else {
            setIndexAtPlayer(startingPlayer);
        }

        next();
    }

    public void next() {
        if(PlayerManager.getInstance().getUnrevealedPlayers().size() <= 1) {
            endRound();
        } else {
            PlayerManager.getInstance().getByName(players.get(index)).playTurn();
            incrementIndex();
        }
    }

    public void endRound(){
        startingPlayer = PlayerManager.getInstance().getUnrevealedPlayers().get(0);

        Player lastUnrevealed = PlayerManager.getInstance().getByName(startingPlayer);
        lastUnrevealed.revealIdentity();
        lastUnrevealed.addToScore(lastUnrevealed.getIdentityCard().getIdentity().equals(Identity.WITCH) ? 2 : 1);

        //TODO :  determine winner and display scores
    }

    public void setIndexAtPlayer(String playerName) {
        index = players.lastIndexOf(playerName);
    }

    public void eliminate(String playerName){
        players.remove(playerName);
    }

    private void incrementIndex(){
        index++;
        if (index > players.size())
            index = 0;
    }
}
