package fr.utt.lo02.witchhunt.card.effect.action;

import fr.utt.lo02.witchhunt.card.CardManager;
import fr.utt.lo02.witchhunt.player.Player;

public final class Discard extends Action{

    public Discard(){
        super("Discard a card from your hand.");
    }

    @Override
    public boolean execute(Player caller) {
        if(caller.getHand().isEmpty()){
            //TODO: can't execute this action -> can't play card
            return false;
        } else {
            String cardName = caller.chooseCardFrom(caller.getHand());
            CardManager.getInstance().discard(cardName);
            return true;
        }
    }
}
