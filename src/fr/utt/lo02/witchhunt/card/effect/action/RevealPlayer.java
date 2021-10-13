package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.player.Player;

public final class RevealPlayer extends Action{

    public RevealPlayer(){
        super("Reveal another player's identity.\nWitch: You gein 2pts. You take next turn.\nVillager: You lose 2pts. They take next turn.");
    }

    @Override
    public void execute(Player caller) {
        Player target = caller.choosePlayer(true);

        target.getIdentityCard().reveal();
        if(target.getIdentityCard().getIdentity().equals(Identity.WITCH)){
            caller.addToScore(2);
            //TODO: caller take next turn
        } else {
            caller.addToScore(-2);
            //TODO: target take next turn
        }
    }
}
