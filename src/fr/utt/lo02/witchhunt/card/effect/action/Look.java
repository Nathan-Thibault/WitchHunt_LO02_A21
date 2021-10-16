package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.Identity;
import fr.utt.lo02.witchhunt.card.effect.CardEffect;
import fr.utt.lo02.witchhunt.player.Player;
import fr.utt.lo02.witchhunt.player.PlayerManager;

public final class Look extends Action{

    public Look(){
        super("Before their turn, secretly\nlook at their identity");
    }

    @Override
    public boolean execute(Player caller, CardEffect effect) {
        Identity targetIdentity = PlayerManager.getInstance().getByName(effect.getTarget()).getIdentityCard().getIdentity();
        //TODO : tell caller the identity of it's target
        return false;
    }

    @Override
    public String cantExecute() {
        return null;
    }
}
