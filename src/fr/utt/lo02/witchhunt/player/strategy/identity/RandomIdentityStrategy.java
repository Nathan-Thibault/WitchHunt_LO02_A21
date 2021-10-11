package fr.utt.lo02.witchhunt.player.strategy.identity;

import fr.utt.lo02.witchhunt.Identity;

import java.util.Random;

public final class RandomIdentityStrategy implements IdentityStrategy{
    @Override
    public Identity chooseIdentity() {
        Random random = new Random();
        return random.nextBoolean() ? Identity.VILLAGER : Identity.WITCH;
    }
}
