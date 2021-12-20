package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.managers.CardManager;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.Set;
import java.util.HashMap;

/**
 * <b>MakeRevealOrDiscard</b> represents the <i>"Choose a player. They must reveal their
 * identity or discard a card from their hand."</i> action on rumour cards.
 * <p>
 * <i>Choose a player. They must reveal their
 * identity or discard a card from their hand.
 * <b>Witch</b>: You gain 1pt. You take next turn.
 * <b>Villager</b>: You lose 1pt. They take next turn.
 * <b>If they discard</b>: They take next turn.</i>
 */
public final class MakeRevealOrDiscard extends Action {

    public MakeRevealOrDiscard() {
        super("""
                Choose a player. They must reveal their
                identity or discard a card from their hand.
                Witch: You gain 1pt. You take next turn.
                Villager: You lose 1pt. They take next turn.
                If they discard: They take next turn.""");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        RoundManager rManager = RoundManager.getInstance();
        IOController io = IOController.getInstance();

        Player caller = pManager.getByName(callerName);
        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
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
                io.printInfo(callerName + " loses a point and " + targetName + " takes next turn.");
                caller.addToScore(-1);
                rManager.setIndexAtPlayer(targetName);
            } else {
                io.printInfo(callerName.concat(" gains a point and takes next turn."));
                caller.addToScore(1);
                rManager.setIndexAtPlayer(callerName);
            }
        } else {
            String card = target.chooseCardFrom(target.getHand());

            io.printInfo(targetName + "  discarded " + card + ". He takes next turn.");

            target.getOwnedCards().remove(card);
            CardManager.getInstance().discard(card);

            rManager.setIndexAtPlayer(targetName);
        }
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
