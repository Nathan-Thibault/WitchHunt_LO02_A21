package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.strategy.identity.RandomIdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.VillagerStrategy;
import fr.utt.lo02.witchhunt.player.strategy.identity.WitchStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.AlwaysReveal;
import fr.utt.lo02.witchhunt.player.strategy.respond.NeverReveal;
import fr.utt.lo02.witchhunt.player.strategy.respond.RevealIfVillager;
import fr.utt.lo02.witchhunt.player.strategy.turn.AlwaysAccuse;

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
    ALWAYSACCUSE(AlwaysAccuse.class, Strategy.StrategyType.TURN, "The artificial player will always accuse another player.");

    private final Class<? extends Strategy> sClass;
    private int code;
    private final Strategy.StrategyType type;
    private final String description;

    StrategyEnum(Class<? extends Strategy> sClass, Strategy.StrategyType type, String description) {
        this.sClass = sClass;
        this.type = type;
        this.description = description;
    }

    public static StrategyEnum getByCode(int code) throws IllegalArgumentException {
        for (StrategyEnum s : StrategyEnum.values()) {
            if (s.getCode() == code)
                return s;
        }
        throw new IllegalArgumentException("StrategyEnum getByCode: couldn't find a strategy with this code");
    }

    public Class<? extends Strategy> getStrategyClass() {
        return sClass;
    }

    private void setCode(int c){
        this.code = c;
    }

    public int getCode() {
        return code;
    }

    public Strategy.StrategyType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    static {
        //attribute a unique code to each strategy (in increasing order)
        int codeCount = 1;
        for (StrategyEnum s: StrategyEnum.values()) {
            s.setCode(codeCount);
            codeCount++;
        }
    }
}
