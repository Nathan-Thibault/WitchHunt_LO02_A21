package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PlayerManager {

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
        players.put(name, new PhysicalPlayer());
    }

    public void createArtificialPlayer(TurnStrategy turnStrategy, RespondStrategy respondStrategy, IdentityStrategy identityStrategy){
        players.put(artificialPlayerName.concat(String.valueOf(artificialPlayerCount)),
                new ArtificialPlayer(turnStrategy, respondStrategy, identityStrategy));
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

        for (Map.Entry<String, Player> entry: players.entrySet()) {
            if(entry.getValue().getIdentityCard().isRevealed())
                unrevealedPlayers.add(entry.getKey());
        }

        return unrevealedPlayers;
    }

    public ArrayList<String> getPlayersWithCards(){
        ArrayList<String> playersWithCards = new ArrayList<>();

        for (Map.Entry<String, Player> entry: players.entrySet()) {
            if(!entry.getValue().getHand().isEmpty())
                playersWithCards.add(entry.getKey());
        }

        return playersWithCards;
    }

    public void resetAll(){
        for (Player p : players.values()){
            p.resetHand();
            p.resetScore();
        }
    }
}
