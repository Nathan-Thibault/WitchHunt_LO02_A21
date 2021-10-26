package fr.utt.lo02.witchhunt.player.strategy;

public interface Strategy {
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
