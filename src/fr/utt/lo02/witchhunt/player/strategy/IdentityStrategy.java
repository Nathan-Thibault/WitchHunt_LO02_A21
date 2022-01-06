package fr.utt.lo02.witchhunt.player.strategy;

import fr.utt.lo02.witchhunt.player.Identity;

public interface IdentityStrategy extends Strategy {
    Identity chooseIdentity();
}
