package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class Reveal extends Action {

    public Reveal() {
        super("Reveal your identity.\nWitch: Player to your left takes next turn.\nVillager: Choose next player.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        RoundManager rManager = RoundManager.getInstance();
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        caller.revealIdentity();

        if (caller.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
            ArrayList<String> possibleTargets = pManager.getInGamePlayers();
            possibleTargets.remove(callerName);//player can't choose himself

            String target = caller.choosePlayerFrom(possibleTargets);
            rManager.setIndexAtPlayer(target);
        } else {
            pManager.eliminate(callerName);
            rManager.incrementIndex();
        }
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return !PlayerManager.getInstance().getByName(callerName).getIdentityCard().isRevealed();
    }
}
