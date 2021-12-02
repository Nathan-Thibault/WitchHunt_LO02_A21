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

/**
 * <b>PlayerManager</b> stores all the players and provides methods to manipulate them.
 * <p>
 * Players ({@link Player}) are stored in an LinkedHashMap with their name as key.
 * This allows to manipulate only strings for most of the operations affecting the players
 * and only get the {@link Player} when needed with its name.
 * The order in which the players are in the LinkedHashMap determines the order they play.
 * <p>
 * <b>PlayerManager</b> is a singleton because players should not be duplicated.
 * Moreover, being a singleton allows access to it from anywhere in the project, which is needed.
 * <p>
 * <b>PlayerManager</b> provides methods (among others):
 * <ul>
 *     <li>to add a {@link PhysicalPlayer} or create {@link ArtificialPlayer}</li>
 *     <li>to get a {@link Player} by its name</li>
 *     <li>to get a list of players depending on certain requirements</li>
 * </ul>
 */
public final class PlayerManager {

    private static PlayerManager instance;

    private LinkedHashMap<String, Player> players = new LinkedHashMap<>();
    private LinkedHashSet<String> inGamePlayers = new LinkedHashSet<>();
    private int artificialPlayerCount;

    private PlayerManager() {
        artificialPlayerCount = 0;
    }

    /**
     * Gets the unique instance of <b>PlayerManager</b>.
     * <p>
     * If the instance is null, this method creates it.
     *
     * @return the instance of <b>PlayerManager</b>
     */
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    /**
     * Adds a {@link PhysicalPlayer} to the game.
     *
     * @param name the name of the {@link PhysicalPlayer} to add
     */
    public void addPhysicalPlayer(String name) {
        players.put(name, new PhysicalPlayer(name));
    }

    /**
     * Creates an {@link ArtificialPlayer} with the specified strategies.
     * <p>
     * The artificial players are named as "AI" plus an integer, starting at 0
     * and incremented by one for each artificial player created.
     *
     * @param strategies a {@link Map} of strategies to affect to the artificial Player
     *                   with all {@link fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType}
     *                   as {@link Map#keySet()} and the {@link Strategy} wanted as {@link Map#values()}
     */
    public void createArtificialPlayer(HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies) {
        String artificialPlayerName = "AI".concat(String.valueOf(artificialPlayerCount));

        players.put(artificialPlayerName, new ArtificialPlayer(artificialPlayerName, strategies));
        artificialPlayerCount++;
    }

    /**
     * Sets the game play order at random.
     */
    public void shufflePlayers() {
        List<String> list = new ArrayList<>(players.keySet());
        Collections.shuffle(list);

        LinkedHashMap<String, Player> shuffleMap = new LinkedHashMap<>();
        list.forEach(k -> shuffleMap.put(k, players.get(k)));
        players = shuffleMap;
    }

    /**
     * Gets a {@link Player} by its name.
     *
     * @param name the name of the player
     * @return the {@link Player} with the given name
     */
    public Player getByName(String name) {
        return players.get(name);
    }

    /**
     * Gets the owner ({@link Player} of a specific card.
     * <p>
     * This method doesn't check if a card with the given name exists,
     * if the card does not exist, it will return <code>null</code>.
     *
     * @param cardName the name of the card
     * @return the owner of the card, or <code>null</code> if none is found
     */
    public Player getOwnerOf(String cardName) {
        for (Player player : players.values()) {
            if (player.getOwnedCards().contains(cardName))
                return player;
        }
        return null;
    }

    /**
     * Gets the name of the owner of a specific card.
     * <p>
     * This method doesn't check if a card with the given name exists,
     * if the card does not exist, it will return <code>null</code>.
     *
     * @param cardName the name of the card
     * @return the name of the owner of the card, or <code>null</code> if none is found
     */
    public String getOwnerOfCard(String cardName) {
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().getOwnedCards().contains(cardName))
                return entry.getKey();
        }
        return null;
    }

    /**
     * Gets all players.
     *
     * @return an ordered set of player names
     */
    public LinkedHashSet<String> getAllPlayers() {
        return new LinkedHashSet<>(players.keySet());
    }

    /**
     * Gets all players in the list of in game players.
     *
     * @return an ordered set of player names
     * @see PlayerManager#eliminate(String)
     */
    public LinkedHashSet<String> getInGamePlayers() {
        return inGamePlayers;
    }

    /**
     * Gets all players that have their {@link fr.utt.lo02.witchhunt.card.IdentityCard} not revealed.
     *
     * @return an ordered set of player names
     */
    public HashSet<String> getUnrevealedPlayers() {
        HashSet<String> unrevealedPlayers = new HashSet<>();

        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (!entry.getValue().getIdentityCard().isRevealed())
                unrevealedPlayers.add(entry.getKey());
        }

        return unrevealedPlayers;
    }

    /**
     * Gets all players that have at least one card in hand.
     *
     * @return an ordered set of player names
     * @see Player#getHand()
     */
    public HashSet<String> getPlayersWithHand() {
        HashSet<String> playersWithCards = new HashSet<>();

        for (String player : getInGamePlayers()) {
            if (!getByName(player).getHand().isEmpty()) {
                playersWithCards.add(player);
            }
        }

        return playersWithCards;
    }

    /**
     * Removes a player from the list of in game players.
     *
     * @param playerName the name of the player to eliminate
     * @see PlayerManager#getInGamePlayers()
     */
    public void eliminate(String playerName) {
        inGamePlayers.remove(playerName);
    }

    /**
     * Resets all players and sets list of in game players to all players.
     */
    public void resetAll() {
        inGamePlayers = new LinkedHashSet<>(players.keySet());

        for (Player p : players.values()) {
            p.reset();
        }
    }
}
