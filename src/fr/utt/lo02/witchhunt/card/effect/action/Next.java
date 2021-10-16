package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.RoundManager;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

public final class Next extends Action{

    public Next(){
        super("Choose next player.");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        String target = caller.choosePlayerFrom(PlayerManager.getInstance().getInGamePlayers());

        effect.setTarget(target);
        RoundManager.getInstance().setIndexAtPlayer(target);
        return true;
    }
}
