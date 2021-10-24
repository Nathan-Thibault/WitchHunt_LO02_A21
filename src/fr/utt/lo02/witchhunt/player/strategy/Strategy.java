package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.strategy.identity.IdentityStrategy;
import fr.utt.lo02.witchhunt.player.strategy.respond.RespondStrategy;
import fr.utt.lo02.witchhunt.player.strategy.turn.TurnStrategy;

public interface Strategy {
    enum StrategyType {
        IDENTITY(IdentityStrategy.class, "identity strategy"),
        RESPOND(RespondStrategy.class, "respond strategy"),
        TURN(TurnStrategy.class, "turn strategy");

        private final Class<? extends Strategy> typeClass;
        private final String name;

        StrategyType(Class<? extends Strategy> typeClass, String name) {
            this.typeClass = typeClass;
            this.name = name;
        }

        public Class<? extends Strategy> getTypeClass() {
            return typeClass;
        }

        public String getName() {
            return name;
        }
    }
}
