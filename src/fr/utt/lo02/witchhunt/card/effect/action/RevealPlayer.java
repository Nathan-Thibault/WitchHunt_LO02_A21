package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class RevealPlayer extends Action {

    public RevealPlayer() {
        super("Reveal another player's identity.\nWitch: You gain 2pts. You take next turn.\nVillager: You lose 2pts. They take next turn.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        ArrayList<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(callerName);
        String targetName = caller.choosePlayerFrom(possibleTargets);
        Player target = pManager.getByName(targetName);

        target.getIdentityCard().setRevealed(true);
        if (target.getIdentityCard().getIdentity().equals(Identity.WITCH)) {
            caller.addToScore(2);
            target.revealIdentity();
            IOController.getInstance().printInfo(callerName.concat(" gains two points and takes next turn."));
            RoundManager.getInstance().setIndexAtPlayer(callerName);
        } else {
            caller.addToScore(-2);
            IOController.getInstance().printInfo(callerName.concat(" loses two points and ").concat(targetName).concat(" takes next turn."));
            RoundManager.getInstance().setIndexAtPlayer(targetName);
        }
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
