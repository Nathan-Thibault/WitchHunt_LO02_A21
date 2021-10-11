package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.Identity;

import java.util.Objects;

public final class IdentityCard extends Card{

    private final Identity identity;

    public IdentityCard(Identity identity){
        super(false);
        this.identity = Objects.requireNonNull(identity, "IdentityCard constructor: identity can't be null");
    }

    @Override
    public void reveal(){
        revealed = true;
    }

    public Identity getIdentity(){
        return identity;
    }
}
