package fr.utt.lo02.witchhunt.player.strategy;

public enum StrategyEnum {
    //identity
    RANDOMIDENTITY(1, Strategy.StrategyType.IDENTITY, "The artificial player will choose his identity at random."),
    VILLAGER(2, Strategy.StrategyType.IDENTITY, "The artificial player will always choose to be a villager."),
    WITCH(3, Strategy.StrategyType.IDENTITY, "The artificial player will always choose to be a witch."),
    //respond
    ALWAYSREVEAL(4, Strategy.StrategyType.RESPOND, "The artificial player will always reveal his identity."),
    NEVERREVEAL(5, Strategy.StrategyType.RESPOND, "The artificial player will always try not to never reveal his identity."),
    REVEALIFVILLAGER(6, Strategy.StrategyType.RESPOND, "The artificial player will reveal his identity if he is a villager. Otherwise he will try not to reveal it."),
    //turn
    ALWAYSACCUSE(7, Strategy.StrategyType.TURN, "The artificial player will always accuse another player.");

    private final int code;
    private final Strategy.StrategyType type;
    private final String description;

    StrategyEnum(int code, Strategy.StrategyType type, String description) {
        if (isPresent(code))
            throw new IllegalArgumentException("ConcreteRespondStrategy : code must be unique");
        this.code = code;
        this.type = type;
        this.description = description;
    }

    private boolean isPresent(int code) {
        for (StrategyEnum crs : StrategyEnum.values()) {
            if (crs.getCode() == code)
                return true;
        }
        return false;
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
}
