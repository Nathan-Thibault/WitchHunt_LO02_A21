package fr.utt.lo02.witchhunt.player.strategy.identity;

import fr.utt.lo02.witchhunt.Identity;

public final class WitchStrategy implements IdentityStrategy{
    @Override
    public Identity chooseIdentity() {
        return Identity.WITCH;
    }
}
