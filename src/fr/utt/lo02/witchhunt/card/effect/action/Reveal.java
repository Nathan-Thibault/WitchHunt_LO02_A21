package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

public final class Reveal extends Action{

    public Reveal(){
        super("Reveal your identity.\nWitch: Player to your left takes next turn.\nVillager: Choose next player.");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        if(caller.getIdentityCard().isRevealed()){
            //TODO: can't execute this action -> can't play card
            return false;
        } else {
            String callerName = PlayerManager.getInstance().getByPlayer(caller);

            caller.getIdentityCard().reveal();
            if(caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
                RoundManager.getInstance().setIndexAtPlayer(callerName);
            } else {
                PlayerManager.getInstance().eliminate(callerName);
            }
            return true;
        }
    }
}
