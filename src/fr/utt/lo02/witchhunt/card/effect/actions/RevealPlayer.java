package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.player.Identity;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.Set;
import java.util.HashMap;

/**
 * <b>RevealPlayer</b> represents the <i>"Reveal another player's identity."</i> action on rumour cards.
 * <p>
 * <i>Reveal another player's identity.
 * <b>Witch</b>: You gain 2pts. You take next turn.
 * <b>Villager</b>: You lose 2pts. They take next turn.</i>
 */
public final class RevealPlayer extends Action {

    public RevealPlayer() {
        super("Reveal another player's identity.\nWitch: You gain 2pts. You take next turn.\nVillager: You lose 2pts. They take next turn.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(callerName);//Player can't choose himself

        //remove the player with the card "immune" against the one playing this action if not null
        possibleTargets.remove((String) args.get("protectedPlayer"));

        String targetName = caller.choosePlayerFrom(possibleTargets);
        Player target = pManager.getByName(targetName);

        target.revealIdentity(false);

        StringBuilder sb = new StringBuilder();
        sb.append(targetName);
        sb.append(" is a ");
        sb.append(target.getIdentityCard().getIdentity());
        sb.append(".\n");
        sb.append(callerName);

        String nextPlayer;
        if (target.getIdentityCard().getIdentity().equals(Identity.WITCH)) {
            sb.append(" gains two points and takes next turn.");

            caller.addToScore(2);
            nextPlayer = callerName;
        } else {
            sb.append(" loses two points and ");
            sb.append(targetName);
            sb.append(" takes next turn.");

            caller.addToScore(-2);
            nextPlayer = targetName;
        }

        IOController.getInstance().pause(sb.toString());
        RoundManager.getInstance().setIndexAtPlayer(nextPlayer);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
