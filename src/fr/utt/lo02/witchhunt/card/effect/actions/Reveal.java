package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Set;

/**
 * <b>Reveal</b> represents the <i>"Reveal your identity"</i> action on rumour cards.
 * <p>
 * <i>Reveal your identity.
 * <b>Witch</b>: Player to your left takes next turn.
 * <b>Villager</b>: Choose next player.</i>
 */
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
            Set<String> possibleTargets = pManager.getInGamePlayers();
            possibleTargets.remove(callerName);//player can't choose himself

            //remove the player with the card "immune" against the one playing this action if not null
            possibleTargets.remove((String) args.get("protectedPlayer"));

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
