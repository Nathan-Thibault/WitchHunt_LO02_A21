package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class RevealPlayer extends Action {

    public RevealPlayer() {
        super("Reveal another player's identity.\nWitch: You gain 2pts. You take next turn.\nVillager: You lose 2pts. They take next turn.");
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        ArrayList<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(callerName);
        String targetName = caller.choosePlayerFrom(possibleTargets);
        Player target = pManager.getByName(targetName);

        target.getIdentityCard().setRevealed(true);
        if (target.getIdentityCard().getIdentity().equals(Identity.WITCH)) {
            caller.addToScore(2);
            pManager.eliminate(targetName);
            RoundManager.getInstance().setIndexAtPlayer(callerName);
        } else {
            caller.addToScore(-2);
            RoundManager.getInstance().setIndexAtPlayer(targetName);
        }
        return true;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}
