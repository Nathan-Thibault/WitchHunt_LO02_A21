package fr.utt.lo02.witchhunt.player;

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

    public PlayerManager getInstance(){
        if (instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public void addPhysicalPlayer(String name){
        //TODO: add ay to distribute cards
        //players.put(name, new PhysicalPlayer());
    }

    public void createArtificialPlayers(int amount){
        for (int i = 0; i < amount; i++) {
            createArtificialPlayer();
        }
    }

    public void createArtificialPlayer(){
        //TODO: create ArtificialPlayer
        //players.put(artificialPlayerName.concat(String.valueOf(artificialPlayerCount)), new ArtificialPlayer());
        artificialPlayerCount++;
    }

    public Player getByName(String name){
        return players.get(name);
    }

    public Set<String> getPlayersNames(){
        return players.keySet();
    }
}
