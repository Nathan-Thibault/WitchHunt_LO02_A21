package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class MakeRevealOrDiscard extends Action {

    public MakeRevealOrDiscard() {
        super("Choose a player. They must reveal their\nidentity or discard a card from their hand."
                .concat("\nWitch: You gain 1pt. You take next turn.")
                .concat("\nVillager: You lose 1pt. They take next turn.")
                .concat("\nIf they discard: They take next turn."));
    }

    @Override
    public boolean execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        RoundManager rManager = RoundManager.getInstance();
        Player caller = pManager.getByName(callerName);

        ArrayList<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(callerName);
        String targetName = caller.choosePlayerFrom(possibleTargets);
        Player target = pManager.getByName(targetName);

        boolean b;
        if (target.getHand().isEmpty()) {//target has no cards in hand, they must reveal
            b = true;
        } else {
            b = target.chooseToRevealOrDiscard();
        }

        if (b) {
            target.revealIdentity();
            if (target.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
                caller.addToScore(-1);
                rManager.setIndexAtPlayer(targetName);
            } else {
                caller.addToScore(1);
                rManager.setIndexAtPlayer(callerName);
            }
        } else {
            String card = target.chooseCardFrom(target.getHand());
            target.getHand().remove(card);
            CardManager.getInstance().discard(card);
            rManager.setIndexAtPlayer(targetName);
        }

        return true;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}