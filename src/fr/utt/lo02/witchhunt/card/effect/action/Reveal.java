package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.player.Player;

public class Reveal extends Action{

    public Reveal(){
        super("Reveal your identity.");
    }

    @Override
    public void execute(Player caller) {
        caller.getIdentityCard().reveal();
    }
}
