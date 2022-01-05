package fr.utt.lo02.witchhunt.player.strategy.identity;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.player.strategy.Strategy;

public interface IdentityStrategy extends Strategy {
    Identity chooseIdentity();
}
