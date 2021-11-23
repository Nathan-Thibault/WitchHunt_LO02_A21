package fr.utt.lo02.witchhunt.player.strategy.turn;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

import java.util.Set;

public final class AlwaysAccuse implements TurnStrategy {
    @Override
    public void playTurn(String callerName) {
        PlayerManager pManager = PlayerManager.getInstance();
        Player caller = pManager.getByName(callerName);

        Set<String> possibleTargets = pManager.getUnrevealedPlayers();
        possibleTargets.remove(callerName);//player can't choose himself

        String target = caller.choosePlayerFrom(possibleTargets);
        RoundManager.getInstance().accuse(callerName, target);
    }
}
