package fr.utt.lo02.witchhunt.card;

import fr.utt.lo02.witchhunt.Identity;

public final class IdentityCard extends Card{

    private final Identity identity;

    public IdentityCard(Identity identity){
        super(false);
        this.identity = identity;
    }

    @Override
    public void reveal(){
        revealed = true;
    }
}
