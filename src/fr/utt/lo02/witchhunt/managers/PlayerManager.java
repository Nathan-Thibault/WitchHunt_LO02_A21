package fr.utt.lo02.witchhunt.managers;

import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.PhysicalPlayer;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public final class PlayerManager {

    private static PlayerManager instance;

    private LinkedHashMap<String, Player> players = new LinkedHashMap<>();
    private LinkedHashSet<String> inGamePlayers = new LinkedHashSet<>();
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

    public void shufflePlayers() {
        List<String> list = new ArrayList<>(players.keySet());
        Collections.shuffle(list);

        LinkedHashMap<String, Player> shuffleMap = new LinkedHashMap<>();
        list.forEach(k -> shuffleMap.put(k, players.get(k)));
        players = shuffleMap;
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

    public LinkedHashSet<String> getAllPlayers() {
        return new LinkedHashSet<>(players.keySet());
    }

    public LinkedHashSet<String> getInGamePlayers() {
        return inGamePlayers;
    }

    public HashSet<String> getUnrevealedPlayers() {
        HashSet<String> unrevealedPlayers = new HashSet<>();

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (!entry.getValue().getIdentityCard().isRevealed())
                unrevealedPlayers.add(entry.getKey());
        }

        return unrevealedPlayers;
    }

    public HashSet<String> getPlayersWithHand() {
        HashSet<String> playersWithCards = new HashSet<>();

        for (String player : getInGamePlayers()) {
            if (!getByName(player).getHand().isEmpty()) {
                playersWithCards.add(player);
            }
        }

        return playersWithCards;
    }

    public String getOwnerOfCard(String cardName) {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().getOwnedCards().contains(cardName))
                return entry.getKey();
        }
        return null;
    }

    public void eliminate(String playerName) {
        inGamePlayers.remove(playerName);
    }

    public void resetAll() {
        inGamePlayers = new LinkedHashSet<>(players.keySet());

        for (Player p : players.values()) {
            p.reset();
        }
    }
}
