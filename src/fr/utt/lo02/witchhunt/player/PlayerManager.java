package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PlayerManager {

    private static PlayerManager instance;
    private final HashMap<String, Player> players = new HashMap<>();
    private final String artificialPlayerName = "AI";
    private int artificialPlayerCount;

    private PlayerManager(){
        artificialPlayerCount = 0;
    }

    public static PlayerManager getInstance(){
        if (instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public void addPhysicalPlayer(String name){
        players.put(name, new PhysicalPlayer(CardManager.getInstance().dealHand()));
    }

    public void createArtificialPlayer(TurnStrategy turnStrategy, RespondStrategy respondStrategy, IdentityStrategy identityStrategy){
        players.put(artificialPlayerName.concat(String.valueOf(artificialPlayerCount)),
                new ArtificialPlayer(CardManager.getInstance().dealHand(), turnStrategy, respondStrategy, identityStrategy));
        artificialPlayerCount++;
    }

    public Player getByName(String name){
        return players.get(name);
    }

    public ArrayList<String> getAllPlayers(){
        return new ArrayList<>(players.keySet());
    }

    public ArrayList<String> getUnrevealedPlayers(){
        ArrayList<String> unrevealedPlayers = new ArrayList<>();

        for (String playerName: players.keySet()) {
            if(getByName(playerName).getIdentityCard().isRevealed())
                unrevealedPlayers.add(playerName);
        }

        return unrevealedPlayers;
    }
}
