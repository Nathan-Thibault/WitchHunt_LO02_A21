package fr.utt.lo02.witchhunt.card.effect.actions;

import fr.utt.lo02.witchhunt.managers.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.managers.PlayerManager;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

/**
 * <b>MustAccuse</b> represents the <i>"On their turn they must accuse a
 * player other than you, if possible."</i> action on rumour cards.
 */
public final class MustAccuse extends Action {

    public MustAccuse() {
        super("On their turn they must accuse a\nplayer other than you, if possible.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        RoundManager rManager = RoundManager.getInstance();

        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "MustAccuse : missing argument effect");
        String target = Objects.requireNonNull(effect.getTarget(), "MustAccuse : target can't be null");

        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(target);//target can't choose herself as its own target

        //remove the player with the card "immune" against the one playing this action if not null
        possibleTargets.remove((String) args.get("protectedPlayer"));

        if (possibleTargets.size() > 1)//if there is at least two unrevealed players, target can choose someone else than caller
            possibleTargets.remove(callerName);

        IOController.getInstance().printInfo(target + " you are forced to accuse someone else than " + callerName);
        String targetOfTarget = pManager.getByName(target).choosePlayerFrom(possibleTargets);
        rManager.accuse(target, targetOfTarget);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
