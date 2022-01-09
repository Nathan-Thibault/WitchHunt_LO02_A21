package fr.utt.lo02.witchhunt.player.strategy;

/**
 * Identifies strategies for an {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} to use.
 */
public interface Strategy {
    /**
     * Types of strategies an {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} must have.
     */
    enum StrategyType {
        IDENTITY("identity strategy"),
        RESPOND("respond strategy"),
        TURN("turn strategy");

        private final String name;

        StrategyType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
