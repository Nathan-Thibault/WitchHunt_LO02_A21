package fr.utt.lo02.witchhunt.player;

import fr.utt.lo02.witchhunt.Utils;
import fr.utt.lo02.witchhunt.managers.PlayerManager;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;
import fr.utt.lo02.witchhunt.player.strategy.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.TurnStrategy;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * An <b>ArtificialPlayer</b> is a {@link Player} that acts based on strategies.
 * <p>
 * This class basically only just implements abstract methods of {@link Player}.
 * Each works approximately the same way, which is, delegating to the {@link Strategy} of the corresponding
 * {@link fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType} assigned to this  <b>ArtificialPlayer</b>.
 * <p>
 * That's why an <b>ArtificialPlayer</b> must have one {@link Strategy} for each {@link fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType}.
 * These strategies determine what the <b>ArtificialPlayer</b> does in different situations.
 *
 * @see fr.utt.lo02.witchhunt.player.strategy
 */
public final class ArtificialPlayer extends Player {
    /**
     * Strategy for playing a turn.
     *
     * @see TurnStrategy
     */
    private TurnStrategy turnStrategy;
    /**
     * Strategy to respond an accusation.
     *
     * @see RespondStrategy
     */
    private RespondStrategy respondStrategy;
    /**
     * Strategy to choose an {@link Identity}.
     *
     * @see IdentityStrategy
     */
    private IdentityStrategy identityStrategy;

    /**
     * {@link Map} of know identities with the name of player as <i>key</i> and his {@link Identity} as <i>value</i>.
     */
    private final HashMap<String, Identity> knownIdentities = new HashMap<>();

    /**
     * Constructor.
     *
     * @param name       unique name representing the player
     * @param strategies {@link Map} of strategies the artificial player to create will have,
     *                   with the {@link fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType}
     *                   as <i>key</i> and the {@link Strategy} as <i>value</i>
     */
    public ArtificialPlayer(String name, HashMap<Strategy.StrategyType, Class<? extends Strategy>> strategies) {
        super(name);
        try {
            turnStrategy = (TurnStrategy) strategies.get(Strategy.StrategyType.TURN).getConstructor().newInstance();
            respondStrategy = (RespondStrategy) strategies.get(Strategy.StrategyType.RESPOND).getConstructor().newInstance();
            identityStrategy = (IdentityStrategy) strategies.get(Strategy.StrategyType.IDENTITY).getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Adds a known player identity to the list of known identities by this <b>ArtificialPlayer</b>.
     *
     * @param playerName name of the player we know the identity
     * @param identity   his identity
     */
    public void savePlayerIdentity(String playerName, Identity identity) {
        knownIdentities.put(playerName, identity);
    }

    /**
     * Makes the <b>ArtificialPlayer</b> accuse another player.
     * <p>
     * If the artificial player knows any player identity he will base his choice on it.
     * That is, if he knows an unrevealed villager he will not choose it.
     * At the opposite, if he knows an unrevealed witch, he will accuse it.
     */
    public void accuseSomeone() {
        PlayerManager pManager = PlayerManager.getInstance();

        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(name);//player can't choose himself

        String target;
        if (!knownIdentities.isEmpty()) {
            Set<String> judiciousTargets = possibleTargets;

            if (knownIdentities.containsValue(Identity.WITCH)) {
                //put all unrevealed witch players as judicious targets
                judiciousTargets = new HashSet<>();

                for (Map.Entry<String, Identity> entry : knownIdentities.entrySet()) {
                    if (entry.getValue().equals(Identity.WITCH) && possibleTargets.contains(entry.getKey()))
                        judiciousTargets.add(entry.getKey());
                }
            } else {
                //remove players known as villager from judicious targets
                for (String pName : knownIdentities.keySet())
                    judiciousTargets.remove(pName);
            }

            //choose target from judicious targets if not empty, from possible targets otherwise
            target = choosePlayerFrom(judiciousTargets.isEmpty() ? possibleTargets : judiciousTargets);
        } else {
            target = choosePlayerFrom(possibleTargets);
        }

        RoundManager.getInstance().accuse(name, target);
    }

    @Override
    public void playTurn() {
        turnStrategy.playTurn(this);
    }

    @Override
    public void respondAccusation(String accuser) {
        respondStrategy.respondAccusation(this, accuser);
    }

    @Override
    public void chooseIdentity() {
        setIdentity(identityStrategy.chooseIdentity());
    }

    @Override
    public boolean chooseToRevealOrDiscard() {
        //reveal if villager, we could add strategies for that
        return identityCard.getIdentity().equals(Identity.VILLAGER);
    }

    @Override
    public String chooseCardFrom(Set<String> listOfCardNames) {
        //choose at random, we could add strategies to choose cards
        return Utils.randomFromSet(listOfCardNames);
    }

    @Override
    public String choosePlayerFrom(Set<String> listOfPlayerNames) {
        //choose at random, we could add strategies to choose players
        return Utils.randomFromSet(listOfPlayerNames);
    }
}
