package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.player.Player;

public final class Reveal extends Action{

    public Reveal(){
        super("Reveal your identity.");
    }

    @Override
    public boolean execute(Player caller) {
        if(caller.getIdentityCard().isRevealed()){
            //TODO: can't execute this action -> can't play card
            return false;
        } else {
            caller.getIdentityCard().reveal();
            return true;
        }
    }
}
