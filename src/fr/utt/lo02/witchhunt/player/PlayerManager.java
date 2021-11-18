package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class PlayerManager {

    private static PlayerManager instance;

    private final HashMap<String, Player> players = new HashMap<>();
    private ArrayList<String> inGamePlayers = new ArrayList<>();
    private int artificialPlayerCount;

    private PlayerManager() {
        artificialPlayerCount = 0;
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public void addPhysicalPlayer(String name) {
        players.put(name, new PhysicalPlayer(name));
    }

    public void createArtificialPlayer(HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies) {
        String artificialPlayerNameBase = "AI";
        String artificialPlayerName = artificialPlayerNameBase.concat(String.valueOf(artificialPlayerCount));

        players.put(artificialPlayerName, new ArtificialPlayer(artificialPlayerName, strategies));
        artificialPlayerCount++;
    }

    public Player getByName(String name) {
        return players.get(name);
    }

    public String getByPlayer(Player player) throws NullPointerException {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().equals(player))
                return entry.getKey();
        }
        throw new NullPointerException("PlayerManager getByPlayer: couldn't find an entry that matches with the given player");
    }

    public Player getOwnerOf(String cardName) {
        for (Player player : players.values()) {
            if (player.getOwnedCards().contains(cardName))
                return player;
        }
        return null;
    }

    public ArrayList<String> getAllPlayers() {
        return new ArrayList<>(players.keySet());
    }

    public ArrayList<String> getInGamePlayers() {
        return inGamePlayers;
    }

    public ArrayList<String> getUnrevealedPlayers() {
        ArrayList<String> unrevealedPlayers = new ArrayList<>();

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (!entry.getValue().getIdentityCard().isRevealed())
                unrevealedPlayers.add(entry.getKey());
        }

        return unrevealedPlayers;
    }

    public ArrayList<String> getPlayersWithUnrevealedCards() {
        ArrayList<String> playersWithCards = new ArrayList<>();

        for (String player : getInGamePlayers()) {
            if (!getByName(player).getHand().isEmpty()) {
                playersWithCards.add(player);
            }
        }

        return playersWithCards;
    }

    public void eliminate(String playerName) {
        inGamePlayers.remove(playerName);
    }

    public void resetAll() {
        inGamePlayers = new ArrayList<>(players.keySet());

        for (Player p : players.values()) {
            p.reset();
        }
    }
}
