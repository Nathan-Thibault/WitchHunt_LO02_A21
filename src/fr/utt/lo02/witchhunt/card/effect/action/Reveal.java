package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;

public final class Reveal extends Action {

    public Reveal() {
        super("Reveal your identity.\nWitch: Player to your left takes next turn.\nVillager: Choose next player.");
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        if (caller.getIdentityCard().isRevealed()) {
            return false;
        } else {
            caller.getIdentityCard().setRevealed(true);

            if (caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
                RoundManager.getInstance().setIndexAtPlayer(callerName);
            } else {
                pManager.eliminate(callerName);
            }
            return true;
        }
    }

    @Override
    public String cantExecute() {
        return "Your identity is already revealed.";
    }
}
