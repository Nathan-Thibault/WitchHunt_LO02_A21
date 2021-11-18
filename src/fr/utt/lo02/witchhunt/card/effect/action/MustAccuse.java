package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.ArrayList;
import java.util.HashMap;

public final class MustAccuse extends Action {

    public MustAccuse() {
        super("On their turn they must accuse a\nplayer other than you, if possible.");
    }

    @Override
    public void execute(String callerName, HashMap<String, Object> args) {
        PlayerManager pManager = PlayerManager.getInstance();
        RoundManager rManager = RoundManager.getInstance();

        Player caller = pManager.getByName(callerName);
        String target = caller.choosePlayerFrom(pManager.getInGamePlayers());

        ArrayList<String> targetables = pManager.getUnrevealedPlayers();
        targetables.remove(target);//target can't choose herself as its own target
        if (targetables.size() > 1)//if there is at least two unrevealed players, target can choose someone else than caller
            targetables.remove(callerName);

        rManager.setIndexAtPlayer(target);
        String targetOfTarget = pManager.getByName(target).choosePlayerFrom(targetables);
        rManager.accuse(target, targetOfTarget);
    }

    @Override
    public boolean isExecutable(String callerName, HashMap<String, Object> args) {
        return true;
    }
}
