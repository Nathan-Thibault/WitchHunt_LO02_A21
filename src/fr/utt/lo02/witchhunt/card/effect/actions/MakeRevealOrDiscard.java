package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.player.Identity;
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
            target.revealIdentity(false);

            StringBuilder sb = new StringBuilder();
            sb.append(targetName);
            sb.append(" is a ");
            sb.append(target.getIdentityCard().getIdentity());
            sb.append(".\n");
            sb.append(callerName);

            String nextPlayer;
            if (target.getIdentityCard().getIdentity().equals(Identity.VILLAGER)) {
                sb.append(" loses a point and ");
                sb.append(targetName);
                sb.append(" takes next turn.");

                caller.addToScore(-1);
                nextPlayer = targetName;
            } else {
                sb.append(" gains a point and takes next turn.");

                caller.addToScore(1);
                nextPlayer = callerName;
            }

            IOController.getInstance().pause(sb.toString());
            RoundManager.getInstance().setIndexAtPlayer(nextPlayer);
        } else {
            String cardName = target.chooseCardFrom(target.getHand());

            IOController.getInstance().pause(targetName + "  discarded " + cardName + ". He takes next turn.");

            target.removeFromOwnedCards(cardName);
            CardManager.getInstance().discard(cardName);

            RoundManager.getInstance().setIndexAtPlayer(targetName);
        }
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
