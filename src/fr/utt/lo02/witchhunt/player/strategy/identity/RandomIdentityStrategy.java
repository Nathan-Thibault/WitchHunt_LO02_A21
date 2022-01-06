package fr.utt.lo02.witchhunt.player.strategy.identity;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.strategy.IdentityStrategy;

import java.util.Random;

/**
 * An {@link fr.utt.lo02.witchhunt.player.ArtificialPlayer} with this strategy will choose his {@link Identity} at random.
 */
public final class RandomIdentityStrategy implements IdentityStrategy {
    @Override
    public Identity chooseIdentity() {
        Random random = new Random();
        return random.nextBoolean() ? Identity.VILLAGER : Identity.WITCH;
    }
}
