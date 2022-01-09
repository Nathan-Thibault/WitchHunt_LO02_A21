package fr.utt.lo02.witchhunt.player;

/**
 * Identities a player can have.
 */
public enum Identity {
    VILLAGER("Villager"),
    WITCH("Witch");

    /**
     * Name of the identity.
     */
    private final String name;

    /**
     * Constructor.
     *
     * @param name name of the identity
     */
    Identity(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
