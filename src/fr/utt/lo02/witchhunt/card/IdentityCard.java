package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.Identity;

import java.util.Objects;

/**
 * This class is a basic extension of <b>Card</b> who's whole purpose is to represent a player's {@link Identity}.
 */
public final class IdentityCard extends Card {
    /**
     * The {@link Identity} represented by the card.
     */
    private final Identity identity;

    /**
     * Constructs a new card representing an {@link Identity}.
     *
     * @param identity the {@link Identity} to be represented by the card
     */
    public IdentityCard(Identity identity) {
        super();
        this.identity = Objects.requireNonNull(identity, "IdentityCard constructor: identity can't be null");
    }

    /**
     * Gets the identity represented by the card.
     *
     * @return the {@link Identity}
     */
    public Identity getIdentity() {
        return identity;
    }
}
