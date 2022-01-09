package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.strategy.identity.RandomIdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.VillagerStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.WitchStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.AlwaysReveal;
import fr.utt.lo02.witchhunt.player.strategy.respond.NeverReveal;
import fr.utt.lo02.witchhunt.player.strategy.respond.RevealIfVillager;
import fr.utt.lo02.witchhunt.player.strategy.turn.AlwaysAccuse;
import fr.utt.lo02.witchhunt.player.strategy.turn.PlayHunt;
import fr.utt.lo02.witchhunt.player.strategy.turn.RandomTurn;

import java.util.HashSet;
import java.util.Set;

/**
 * Classifies all existing strategies by their {@link fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType}.
 */
public enum StrategyEnum {
    //identity
    RANDOMIDENTITY(RandomIdentityStrategy.class, Strategy.StrategyType.IDENTITY, "The artificial player will choose his identity at random."),
    VILLAGER(VillagerStrategy.class, Strategy.StrategyType.IDENTITY, "The artificial player will always choose to be a villager."),
    WITCH(WitchStrategy.class, Strategy.StrategyType.IDENTITY, "The artificial player will always choose to be a witch."),
    //respond
    ALWAYSREVEAL(AlwaysReveal.class, Strategy.StrategyType.RESPOND, "The artificial player will always reveal his identity."),
    NEVERREVEAL(NeverReveal.class, Strategy.StrategyType.RESPOND, "The artificial player will always try not to never reveal his identity."),
    REVEALIFVILLAGER(RevealIfVillager.class, Strategy.StrategyType.RESPOND, "The artificial player will reveal his identity if he is a villager. Otherwise he will try not to reveal it."),
    //turn
    ALWAYSACCUSE(AlwaysAccuse.class, Strategy.StrategyType.TURN, "The artificial player will always accuse another player."),
    PLAYHUNT(PlayHunt.class, Strategy.StrategyType.TURN, "The artificial player will always try to play a hunt effect."),
    RANDOMTURN(RandomTurn.class, Strategy.StrategyType.TURN, "The artificial player will choose at random to either accuse another player or try to play a hunt effect.");

    /**
     * The class of the strategy.
     */
    private final Class<? extends Strategy> sClass;
    /**
     * The type of the strategy.
     *
     * @see fr.utt.lo02.witchhunt.player.strategy.Strategy.StrategyType
     */
    private final Strategy.StrategyType type;
    /**
     * A description of the comportment an artificial player with the strategy will have.
     */
    private final String description;

    /**
     * Constructor.
     *
     * @param sClass      class of the strategy
     * @param type        type of the strategy
     * @param description description of the strategy
     */
    StrategyEnum(Class<? extends Strategy> sClass, Strategy.StrategyType type, String description) {
        this.sClass = sClass;
        this.type = type;
        this.description = description;
    }

    /**
     * Gets all the strategies of the specified type.
     *
     * @param type the type of strategies wanted
     * @return a set of strategies of the specified type
     */
    public static Set<StrategyEnum> getAllOfType(Strategy.StrategyType type) {
        Set<StrategyEnum> strategies = new HashSet<>();

        for (StrategyEnum s : StrategyEnum.values()) {
            if (s.getType() == type)
                strategies.add(s);
        }

        return strategies;
    }

    /**
     * Gets the class of the strategy.
     *
     * @return the class of the strategy
     */
    public Class<? extends Strategy> getStrategyClass() {
        return sClass;
    }

    /**
     * Gets the type of the strategy.
     *
     * @return the type of the strategy
     */
    public Strategy.StrategyType getType() {
        return type;
    }

    @Override
    public String toString() {
        return description;
    }
}
