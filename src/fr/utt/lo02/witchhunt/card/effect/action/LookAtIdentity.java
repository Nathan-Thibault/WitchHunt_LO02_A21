package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.io.IOController;
import fr.utt.lo02.witchhunt.player.ArtificialPlayer;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.HashMap;
import java.util.Objects;

public final class LookAtIdentity extends Action {

    public LookAtIdentity() {
        super("Before their turn, secretly\nlook at their identity");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        CardEffect effect = (CardEffect) Objects.requireNonNull(args.get("effect"), "Look : missing argument effect");
        String target = Objects.requireNonNull(effect.getTarget(), "RandomlyTakeCardFrom : target can't be null");
        Identity targetIdentity = pManager.getByName(target).getIdentityCard().getIdentity();

        if (caller instanceof ArtificialPlayer) {
            ((ArtificialPlayer) caller).savePlayerIdentity(target, targetIdentity);
        } else {
            IOController.getInstance().printInfo(effect.getTarget().concat(" is a ").concat(targetIdentity.toString()));
        }
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
